package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class EntityCollisionComponent extends Component implements Pool.Poolable {
    public boolean destroyOnCollision = false;

    @Override
    public void reset() {
        destroyOnCollision = false;
    }
}
