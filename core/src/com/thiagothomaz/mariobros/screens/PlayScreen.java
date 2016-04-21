package com.thiagothomaz.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Scenes.Hud;
import com.thiagothomaz.mariobros.Sprites.Enemies.Enemy;
import com.thiagothomaz.mariobros.Sprites.Items.ItemDef;
import com.thiagothomaz.mariobros.Sprites.Items.Item;
import com.thiagothomaz.mariobros.Sprites.Items.Mushroom;
import com.thiagothomaz.mariobros.Sprites.Mario;
import com.thiagothomaz.mariobros.Tools.B2WorldCreator;
import com.thiagothomaz.mariobros.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by thiago on 03/04/16.
 */
public class PlayScreen implements Screen {

    private MarioBros game;
    private TextureAtlas atlas;

    //Playscreen
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    //Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //Sprites
    private Mario player;

    private AssetManager manager;
    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(MarioBros game, AssetManager manager){

        this.manager = manager;
        this.music = this.manager.get("audio/music/mario_music.ogg", Music.class);
        this.music.setLooping(true);
        this.music.play();


        this.atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;

        this.gamecam = new OrthographicCamera();
        this.gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM,MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);
        this.hud = new Hud(game.getBatch());


        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("level1.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1  / MarioBros.PPM);
        this.gamecam.position.set(this.gamePort.getWorldWidth() / 2, this.gamePort.getWorldHeight() / 2, 0);

        this.world = new World(new Vector2(0, -10), true);
        this.b2dr = new Box2DDebugRenderer();


        //new B2WorldCreator(this.world, this.map, this.hud, this.manager);
        this.creator = new B2WorldCreator(this);

        this.player = new Mario(this);


        this.world.setContactListener(new WorldContactListener());

        this.items = new Array<Item>();
        this.itemsToSpawn = new LinkedBlockingQueue<ItemDef>();


    }

    public void spawnItem(ItemDef idef){
        this.itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){

        if (!this.itemsToSpawn.isEmpty()) {
            ItemDef idef = this.itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                this.items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }

    }

    public TextureAtlas getAtlas(){
        return this.atlas;
    }

    @Override
    public void show() {

    }

    public boolean gameOver(){
        if (this.player.getCurrentState() == Mario.State.DEAD && player.getStateTimer() > 3){
            return  true;
        }
        return false;
    }

    public void update(float dt){

        this.handleInput(dt);
        this.handleSpawningItems();;

        this.world.step(1 / 60f, 6, 2);

        this.player.update(dt);

        for (Enemy enemy : creator.getEnemies()){
            enemy.update(dt);
            /**
             * 224 = pixels size (16) * amount of blocks until the end of the screen + 2 more after the screen.
             */
            if (enemy.getX() < this.player.getX() + 224 / MarioBros.PPM){
                enemy.getB2body().setActive(true);
            }
        }

        for(Item item : this.items){
            item.update(dt);
        }

        this.hud.update(dt);
        if (this.player.getCurrentState() != Mario.State.DEAD) {
            this.gamecam.position.x = this.player.getB2body().getPosition().x;
        }

        this.gamecam.update();
        this.renderer.setView(this.gamecam);

    }

    private void handleInput(float dt) {

        if (this.player.getCurrentState() != Mario.State.DEAD) {

            if (this.getDirection() == 1) {
                this.player.jumpSound();
                this.player.getB2body().applyLinearImpulse(new Vector2(0, 4f), player.getB2body().getWorldCenter(), true);
            }

            if (this.getDirection() == 3) {
                this.player.getB2body().applyLinearImpulse(new Vector2(0.1f, 0), player.getB2body().getWorldCenter(), true);
            }

            if (this.getDirection() == 4) {
                this.player.getB2body().applyLinearImpulse(new Vector2(-0.1f, 0), player.getB2body().getWorldCenter(), true);
            }
        }
    }

    /**
     * 0 - Nothing
     * 1 - UP
     * 2 - Down
     * 3 - Righ
     * 4 - Left
     * @return
     */
    private int getDirection(){

        if (Gdx.input.justTouched()) {

            if ((Gdx.input.getY() - MarioBros.V_HEIGHT) / MarioBros.PPM < (this.player.getB2body().getPosition().y + 100) / MarioBros.PPM) {
                return 1;
            }

        }
        if (Gdx.input.isTouched()) {

            if ((Gdx.input.getX() - MarioBros.V_WIDTH) / MarioBros.PPM > (this.player.getB2body().getPosition().x - 32) / MarioBros.PPM) {
                return 3;
            }

            if ((Gdx.input.getX() - MarioBros.V_WIDTH) / MarioBros.PPM < (this.player.getB2body().getPosition().x - 32) / MarioBros.PPM) {
                return 4;
            }


        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            return 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getB2body().getLinearVelocity().x <= 2) {
            return 3;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getB2body().getLinearVelocity().x >= -2) {
            return 4;
        }
        return 0;
    }

    @Override
    public void render(float delta) {
        this.update(delta);

        //Clear the screen
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render game map
        renderer.render();

        //Render Box2DebugLines
        this.b2dr.render(this.world, this.gamecam.combined);

        this.game.getBatch().setProjectionMatrix(this.gamecam.combined);
        this.game.getBatch().begin();
        this.player.draw(this.game.getBatch());
        for (Enemy enemy : creator.getEnemies()){
            enemy.draw(this.game.getBatch());
        }

        for (Item item : this.items){
            item.draw(this.game.getBatch());
        }


        this.game.getBatch().end();

        //
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        this.hud.getStage().draw();

        if (gameOver()){
            this.game.setScreen(new GameOverScreen(this.game, this.manager));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return this.map;
    }

    public World getWorld(){
        return this.world;
    }

    public Hud getHud(){
        return this.hud;
    }

    public AssetManager getManager(){
        return this.manager;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        this.map.dispose();
        this.renderer.dispose();
        this.world.dispose();
        this.b2dr.dispose();
        hud.dispose();

    }
}
