package com.thiagothomaz.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.thiagothomaz.mariobros.Sprites.Mario;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Ground extends InteractiveTileObject {
    public Ground(PlayScreen screen, MapObject object) {
        super(screen, object);
    }

    @Override
    public void onHeadHit(Mario mario) {

    }
}
