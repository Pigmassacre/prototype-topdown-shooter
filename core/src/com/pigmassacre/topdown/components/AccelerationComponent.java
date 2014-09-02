package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class AccelerationComponent extends Component implements Pool.Poolable {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

    @Override
    public void reset() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }
}
