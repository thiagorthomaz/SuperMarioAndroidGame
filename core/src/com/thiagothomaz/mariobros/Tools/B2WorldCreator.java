package com.thiagothomaz.mariobros.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Scenes.Hud;
import com.thiagothomaz.mariobros.Sprites.TileObjects.Brick;
import com.thiagothomaz.mariobros.Sprites.TileObjects.Coin;
import com.thiagothomaz.mariobros.Sprites.Enemies.Goomba;
import com.thiagothomaz.mariobros.Sprites.TileObjects.Ground;
import com.thiagothomaz.mariobros.Sprites.TileObjects.Pipe;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class B2WorldCreator {

    private Array<Goomba> goombas;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        Hud hud = screen.getHud();
        AssetManager manager = screen.getManager();

        //Create ground bodies/fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Ground(screen, rect);
        }

        //create pipe bodies/fixture
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Pipe(screen, rect);
        }

        //create brick bodies/fixture
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(screen, rect);
        }

        //create coin bodies/fixture
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Coin(screen, rect);
        }

        this.goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            this.goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }

    }

    public Array<Goomba> getGoombas(){
        return this.goombas;
    }

}
