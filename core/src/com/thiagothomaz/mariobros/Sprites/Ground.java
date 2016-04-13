package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.thiagothomaz.mariobros.Scenes.Hud;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Ground extends InterativeTileObject {
    public Ground(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
    }

    @Override
    public void onHeadHit() {

    }
}
