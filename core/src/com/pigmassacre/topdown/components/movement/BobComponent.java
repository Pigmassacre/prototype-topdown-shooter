package com.pigmassacre.topdown.components.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class BobComponent extends Component implements Pool.Poolable {
    public float stateTime = 0f;
    public float previousOffset = 0f;

    @Override
    public void reset() {
        stateTime = 0f;
        previousOffset = 0f;
    }
}
