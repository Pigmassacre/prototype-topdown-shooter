package com.pigmassacre.topdown.components.weapons;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class HolsterComponent extends Component implements Pool.Poolable {
    public Entity equippedWeapon;
    public Array<Entity> availableWeapons = new Array<Entity>();

    @Override
    public void reset() {
        equippedWeapon = null;
        availableWeapons.clear();
    }
}
