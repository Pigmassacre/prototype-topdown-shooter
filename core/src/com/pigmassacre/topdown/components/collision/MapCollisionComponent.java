package com.pigmassacre.topdown.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class MapCollisionComponent extends Component implements Pool.Poolable {
    public boolean destroyOnCollision = false;

    @Override
    public void reset() {
        destroyOnCollision = false;
    }
}
