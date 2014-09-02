package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class CircleCollisionComponent extends Component implements Pool.Poolable {
    public Circle circle;

    public void init(float radius) {
        this.circle = new Circle(0f, 0f, radius);
    }

    @Override
    public void reset() {
        this.circle = null;
    }
}
