package com.thiagothomaz.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Ground extends com.thiagothomaz.mariobros.Sprites.TileObjects.InterativeTileObject {
    public Ground(PlayScreen screen, MapObject object) {
        super(screen, object);
    }

    @Override
    public void onHeadHit() {

    }
}
