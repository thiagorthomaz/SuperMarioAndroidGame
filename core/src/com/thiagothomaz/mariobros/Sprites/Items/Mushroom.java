package com.thiagothomaz.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 17/04/16.
 */
public class Mushroom extends Item {

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        this.velocity = new Vector2(0,0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        this.body = this.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);


        fdef.shape = shape;
        this.body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use() {
        this.destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(this.body.getPosition().x - getWidth() / 2, this.body.getPosition().y - getWidth() / 2);
        this.body.setLinearVelocity(this.velocity);
    }
}
