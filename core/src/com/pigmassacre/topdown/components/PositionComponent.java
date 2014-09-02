package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class PositionComponent extends Component implements Pool.Poolable {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    public void init(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void reset() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }
}
