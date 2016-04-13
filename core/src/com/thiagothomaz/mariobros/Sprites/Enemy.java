package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
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


    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        this.setPosition(x, y);
        defineEnemy();
    }

    protected abstract void defineEnemy();

    public Body getB2body(){
        return this.b2body;
    }

}
