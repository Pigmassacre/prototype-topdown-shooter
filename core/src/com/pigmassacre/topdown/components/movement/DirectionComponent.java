package com.pigmassacre.topdown.components.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class DirectionComponent extends Component implements Pool.Poolable {
    public float x = 0f;
    public float y = 0f;

    @Override
    public void reset() {
        x = 0f;
        y = 0f;
    }
}
