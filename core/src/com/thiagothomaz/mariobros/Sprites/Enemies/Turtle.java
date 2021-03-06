package com.thiagothomaz.mariobros.Sprites.Enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Sprites.Mario;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 21/04/16.
 */
public class Turtle extends Enemy {


    public enum State {WALKING, STANDING_SHELL, MOVING_SHELL, DEAD};
    private State currentState;
    private State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private AssetManager manager;
    private TextureRegion shell;
    private int kick_left_speed = -2;
    private int kick_right_speed = 2;
    private float deadRotationDegrees;



    public Turtle(PlayScreen screen, float x, float y) {

        super(screen, x, y);

        this.frames = new Array<TextureRegion>();
        this.frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0,0, 16, 24));
        this.frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16,0, 16, 24));
        this.shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64,0, 16, 24);
        this.walkAnimation = new Animation(0.2f, this.frames);

        this.currentState = this.previousState = State.WALKING;
        this.deadRotationDegrees = 0;

        setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);

    }

    @Override
    protected float getRestitution() {
        if (this.currentState == State.MOVING_SHELL){
            return 1.8f;
        } else {
            return 0.5f;
        }
    }


    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if (this.currentState == State.STANDING_SHELL && this.stateTime > 5){
            this.currentState = State.WALKING;
            this.velocity.x = 1;
        }

        setPosition(this.b2body.getPosition().x - getWidth() / 2, this.b2body.getPosition().y - 8 / MarioBros.PPM);

        if (this.currentState == State.DEAD) {
            this.deadRotationDegrees += 3;
            rotate(this.deadRotationDegrees);
            if (this.stateTime > 5){
                this.world.destroyBody(this.b2body);
                this.destroyed = true;
            }
        } else {
            this.b2body.setLinearVelocity(this.velocity);
        }
    }

    private TextureRegion getFrame(float dt){
        TextureRegion region;

        switch (this.currentState){
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = this.shell;
                break;
            case WALKING:
            default:
                region = this.walkAnimation.getKeyFrame(dt, true);
                break;
        }

        if (this.velocity.x > 0 && !region.isFlipX()){
            region.flip(true, false);
        }

        if (this.velocity.x < 0 && region.isFlipX()){
            region.flip(true, false);
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTime = currentState == previousState ? stateTime + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame

        return region;
    }

    @Override
    public void hitOnHead(Mario mario) {
        if (this.currentState != State.STANDING_SHELL){
            currentState = State.STANDING_SHELL;
            this.velocity.x = 0;
        } else {
            if (mario.getX() <= this.getX()){
                kick(this.kick_right_speed);
            }else {
                kick(kick_left_speed);
            }

        }
    }

    @Override
    public void onEnemyHit(Enemy enemy) {
        if (enemy instanceof Turtle){
            if (((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
                killed();
            } else if (currentState == State.MOVING_SHELL && ((Turtle) enemy).currentState == State.WALKING ){
                return;
            } else {
                reverseVelocity(true, false);
            }


        }else if (this.currentState != State.MOVING_SHELL){
            reverseVelocity(true, false);
        }
    }

    public void kick(int speed){
        this.velocity.x = speed;
        this.currentState = State.MOVING_SHELL;

    }

    public State getCurrentState(){
        return this.currentState;
    }

    public int getKick_left_speed(){
        return this.kick_left_speed;
    }

    public int getKick_right_speed(){
        return this.kick_right_speed;
    }

    public void killed(){
        this.currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarioBros.NOTHING_BIT;

        for(Fixture fixture : this.b2body.getFixtureList()){
            fixture.setFilterData(filter);
        }

        this.b2body.applyLinearImpulse(new Vector2(0, 5f), this.b2body.getWorldCenter(), true);

    }

    public void draw(Batch batch){
        if (!this.destroyed){
            super.draw(batch);
        }
    }

}
