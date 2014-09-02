package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.pigmassacre.topdown.Constants;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class VelocityComponent extends Component implements Pool.Poolable {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public float maxX = 6f * Constants.TARGET_FRAME_RATE;
    public float maxY = 6f * Constants.TARGET_FRAME_RATE;
    public float maxZ = 6f * Constants.TARGET_FRAME_RATE;

    @Override
    public void reset() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        maxX = 6f * Constants.TARGET_FRAME_RATE;
        maxY = 6f * Constants.TARGET_FRAME_RATE;
        maxZ = 6f * Constants.TARGET_FRAME_RATE;
    }
}
