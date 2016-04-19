package com.thiagothomaz.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Pipe extends InteractiveTileObject {

    public Pipe(PlayScreen screen, MapObject object) {
        super(screen, object);
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.hud = screen.getHud();
        this.manager = screen.getManager();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set( (bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM );

        this.body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / MarioBros.PPM, bounds.getHeight() / 2 / MarioBros.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
        this.fixture = this.body.createFixture(fdef);
    }

    @Override
    public void onHeadHit() {

    }
}
