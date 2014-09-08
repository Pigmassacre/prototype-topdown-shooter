package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class PlayerControlledComponent extends Component implements Pool.Poolable {
    public int number = -1;
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;
    public boolean isMovingUp = false;
    public boolean isMovingDown = false;
    public boolean isJumping = false;
    public boolean isInAir = false;
    public boolean isShooting = false;

    public void init(int number) {
        this.number = number;
    }

    @Override
    public void reset() {
        number = -1;
        isMovingLeft = false;
        isMovingRight = false;
        isMovingUp = false;
        isMovingDown = false;
        isJumping = false;
        isInAir = false;
        isShooting = false;
    }
}
