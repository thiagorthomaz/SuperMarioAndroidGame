package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by thiago on 04/04/16.
 */
public class Pipe extends InterativeTileObject {

    public Pipe(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

    @Override
    public void onHeadHit() {

    }
}
