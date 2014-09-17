package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class AnimationComponent extends Component implements Pool.Poolable {
    public Animation standingLeft;
    public Animation standingRight;
    public Animation movingLeft;
    public Animation movingRight;

    public AnimationState state, previousState;
    public float stateTime = 0f;

    public enum AnimationState {
        STANDING_LEFT, STANDING_RIGHT, MOVING_LEFT, MOVING_RIGHT
    }

    @Override
    public void reset() {
        standingLeft = null;
        standingRight = null;
        movingLeft = null;
        movingRight = null;
        state = null;
        previousState = null;
        stateTime = 0f;
    }
}
