package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Scenes.Hud;

/**
 * Created by thiago on 04/04/16.
 */
public abstract class InterativeTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected Hud hud;
    protected AssetManager manager;

    public InterativeTileObject(World world, TiledMap map, Rectangle bounds, Hud hud, AssetManager manager){
        this.world = world;
        this.map = map;
        this.tile = tile;
        this.bounds = bounds;
        this.hud = hud;
        this.manager = manager;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set( (bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM );

        this.body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / MarioBros.PPM, bounds.getHeight() / 2 / MarioBros.PPM);
        fdef.shape = shape;

        this.fixture = this.body.createFixture(fdef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        this.fixture.setFilterData(filter);

    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get(1);
        int x = (int)(this.body.getPosition().x * MarioBros.PPM / 16);
        int y = (int)(this.body.getPosition().y * MarioBros.PPM / 16);
        return layer.getCell(x, y);
    }



}
