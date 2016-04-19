package com.thiagothomaz.mariobros.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Sprites.Mario;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 17/04/16.
 */
public abstract class Item extends Sprite {

    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.setPosition(x, y);
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        defineItem();
        this.toDestroy = false;
        this.destroyed = false;

    }

    public abstract void defineItem();

    public abstract void use(Mario mario);

    public void update(float dt){

        if (this.toDestroy && !this.destroyed){
            this.world.destroyBody(this.body);
            this.destroyed = true;
        }

    }

    @Override
    public void draw(Batch batch){
        if (!this.destroyed){
            super.draw(batch);
        }
    }

    public void destroy(){
        this.toDestroy = true;
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
