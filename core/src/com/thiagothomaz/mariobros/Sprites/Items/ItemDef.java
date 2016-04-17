package com.thiagothomaz.mariobros.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by thiago on 17/04/16.
 */
public class ItemDef {

    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
