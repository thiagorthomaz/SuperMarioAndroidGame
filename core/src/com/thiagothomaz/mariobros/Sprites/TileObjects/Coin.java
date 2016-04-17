package com.thiagothomaz.mariobros.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Sprites.Items.ItemDef;
import com.thiagothomaz.mariobros.Sprites.Items.Mushroom;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Coin extends InterativeTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
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

            this.hud.addScore(400);
            if (this.object.getProperties().containsKey("mushroom")){
                this.screen.spawnItem(new ItemDef(new Vector2(this.body.getPosition().x, this.body.getPosition().y + 16 / MarioBros.PPM), Mushroom.class));
                this.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }else {
                this.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }

            getCell().setTile(tileSet.getTile(BLANK_COIN));
        }


    }
}
