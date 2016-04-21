package com.thiagothomaz.mariobros.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Sprites.Mario;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 12/04/16.
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        this.setPosition(x, y);
        defineEnemy(this.getRestitution());
        this.velocity = new Vector2(1, 0);
        this.b2body.setActive(false);

    }

    protected abstract float getRestitution();

    protected void defineEnemy(float restitution) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        this.b2body = this.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.MARIO_BIT;

        fdef.shape = shape;
        this.b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
        vertice[3] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);

        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = restitution;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public abstract void update(float dt);
    public abstract void hitOnHead(Mario mario);

    public Body getB2body(){
        return this.b2body;
    }

    public void reverseVelocity(boolean x, boolean y){
        if (x) {
            this.velocity.x = -this.velocity.x;
        }
        if (x){
            this.velocity.y = -this.velocity.y;
        }
    }

}
