package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 03/04/16.
 */
public class Mario extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING }
    private State currentState;
    private State previousState;
    private World world;
    private Body b2body;
    private TextureRegion marioStand;
    private Animation marioRun;
    private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;
    private AssetManager manager;

    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineDefineBigMario;

    public Mario(PlayScreen screen){

        this.world = screen.getWorld();
        this.manager = screen.getManager();

        this.currentState = State.STANDING;
        this.previousState = State.STANDING;
        this.stateTimer = 0;
        this.runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0,16,16));
        }
        this.marioRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0,16,32));
        }
        this.bigMarioRun = new Animation(0.1f, frames);

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));

        this.growMario = new Animation(0.2f, frames);


        this.marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80,0,16,16);
        this.bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80,0,16,32);

        frames.clear();

        this.marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0,0,16,16);
        this.bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32);

        defineMario();

        setBounds(0,0,16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(this.marioStand);
    }

    public void update(float dt){
        if (this.marioIsBig){
            setPosition(this.b2body.getPosition().x - getWidth() / 2, this.b2body.getPosition().y - getHeight() / 2 - 6 / MarioBros.PPM);
        } else {
            setPosition(this.b2body.getPosition().x - getWidth() / 2, this.b2body.getPosition().y - getHeight() / 2);
        }

        setRegion(getFrame(dt));

        if (this.timeToDefineBigMario){
            defineBigMario();
        }

        if (this.timeToRedefineDefineBigMario){
            redefineMario();
        }
    }

    private TextureRegion getFrame(float dt) {

        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                if (this.marioIsBig){
                    region = this.bigMarioJump;
                } else {
                    region = this.marioJump;
                }

                break;
            case RUNNING:
                if (this.marioIsBig){
                    region = bigMarioRun.getKeyFrame(stateTimer, true);
                }else {
                    region = marioRun.getKeyFrame(stateTimer, true);
                }
                break;
            case FALLING:
            case STANDING:
                default:
                    if (this.marioIsBig) {
                        region = this.bigMarioStand;
                    }else {
                        region = this.marioStand;
                    }

                    break;
        }


        //if mario is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    private State getState() {

        if (this.runGrowAnimation){
            return State.GROWING;
        } else if (this.b2body.getLinearVelocity().y > 0 || (this.b2body.getLinearVelocity().y < 0 && this.previousState == State.JUMPING)){
            return State.JUMPING;
        }else if (this.b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        } else if (this.b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }

    }

    public void redefineMario(){

        Vector2 currentPosition = this.b2body.getPosition();
        this.world.destroyBody(this.b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;

        this.b2body = this.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        this.b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;

        fdef.shape = head;
        fdef.isSensor = true;

        this.b2body.createFixture(fdef).setUserData(this);

        timeToRedefineDefineBigMario = false;
    }

    public void defineBigMario(){

        Vector2 currentPosition = this.b2body.getPosition();
        this.world.destroyBody(this.b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;

        this.b2body = this.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        this.b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        this.b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        this.b2body.createFixture(fdef).setUserData(this);
        this.timeToDefineBigMario = false;
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        this.b2body = this.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        this.b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;

        fdef.shape = head;
        fdef.isSensor = true;

        this.b2body.createFixture(fdef).setUserData(this);

    }

    public void hit(){
        if (this.isBig()){
            this.marioIsBig = false;
            timeToRedefineDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() /2);
            this.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
        }else {
            this.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
        }
    }

    public void jumpSound(){
        this.manager.get("audio/sounds/jump_small.wav", Sound.class).play();
    }

    public World getWorld() {
        return world;
    }

    public Body getB2body() {
        return b2body;
    }

    public void grow(){
        this.runGrowAnimation = true;
        this.marioIsBig = true;
        this.timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        this.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public boolean isBig() {
        return marioIsBig;
    }
}
