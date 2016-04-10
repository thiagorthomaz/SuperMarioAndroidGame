package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by thiago on 04/04/16.
 */
public class Brick extends InterativeTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        this.fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
    }
}
