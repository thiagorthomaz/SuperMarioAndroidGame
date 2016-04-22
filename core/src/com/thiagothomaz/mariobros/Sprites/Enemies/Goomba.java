package com.thiagothomaz.mariobros.Sprites.Enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Sprites.Mario;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 12/04/16.
 */
public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private AssetManager manager;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.frames = new Array<TextureRegion>();
        this.manager = screen.getManager();

        for (int i = 0; i < 2; i++) {
            this.frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }

        this.walkAnimation = new Animation(0.4f, this.frames);
        this.stateTime = 0;
        setBounds(getX(), getY(), 16 / MarioBros.PPM,16 / MarioBros.PPM);
        setToDestroy = false;
        destroyed = false;


    }

    @Override
    protected float getRestitution() {
        return 0.5f;
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


    public void draw(Batch batch){
        if (!this.destroyed || this.stateTime < 1){
            super.draw(batch);
        }

    }

    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
        this.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void onEnemyHit(Enemy enemy) {
        if (enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.MOVING_SHELL){
            setToDestroy = true;
            this.manager.get("audio/sounds/stomp.wav", Sound.class).play();
        } else {
          reverseVelocity(true, false);
        }
    }
}
