package com.pigmassacre.topdown.components.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class RotationComponent extends Component implements Pool.Poolable {
    public float angle = 0f;
    public boolean rotateToVelocity = true;

    @Override
    public void reset() {
        angle = 0f;
        rotateToVelocity = true;
    }
}
