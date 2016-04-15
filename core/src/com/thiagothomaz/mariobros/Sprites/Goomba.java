package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 12/04/16.
 */
public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            this.frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }

        this.walkAnimation = new Animation(0.4f, this.frames);
        this.stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PPM,16 / MarioBros.PPM);
        setToDestroy = false;
        destroyed = false;


    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            this.world.destroyBody(this.b2body);
            this.destroyed = true;
            setRegion(new TextureRegion(this.screen.getAtlas().findRegion("goomba"), 32, 0, 16,16));
            this.stateTime = 0;

        } else if (!this.destroyed){
            this.b2body.setLinearVelocity(this.velocity);
            setPosition(this.b2body.getPosition().x -getWidth() / 2 , this.b2body.getPosition().y - getHeight() /2);
            setRegion(walkAnimation.getKeyFrame(this.stateTime, true));
        }

    }

    @Override
    protected void defineEnemy() {
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
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if (!this.destroyed || this.stateTime < 1){
            super.draw(batch);
        }

    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;

    }
}
