package com.thiagothomaz.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.thiagothomaz.mariobros.MarioBros;
import com.thiagothomaz.mariobros.Scenes.Hud;
import com.thiagothomaz.mariobros.screens.PlayScreen;

/**
 * Created by thiago on 04/04/16.
 */
public class Brick extends InterativeTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.fixture.setUserData(this);
        this.setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        this.getCell().setTile(null);
        this.hud.addScore(200);
        this.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
