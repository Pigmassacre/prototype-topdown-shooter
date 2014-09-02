package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class RectangleCollisionComponent extends Component implements Pool.Poolable {
    public Rectangle rectangle;

    public void init(float width, float height) {
        this.rectangle = new Rectangle(0f, 0f, width, height);
    }

    @Override
    public void reset() {
        this.rectangle = null;
    }
}
