package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
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
        defineEnemy();
        this.velocity = new Vector2(1, 0);
        this.b2body.setActive(false);

    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead();

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
