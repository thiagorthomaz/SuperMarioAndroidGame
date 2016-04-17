package com.thiagothomaz.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Coin extends InterativeTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        this.fixture.setUserData(this);
        this.setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");

        if (getCell().getTile().getId() == BLANK_COIN) {
            this.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            this.manager.get("audio/sounds/coin.wav", Sound.class).play();
            this.hud.addScore(400);
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));

    }
}
