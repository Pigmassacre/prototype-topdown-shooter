package com.pigmassacre.topdown.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class PolygonCollisionComponent extends Component implements Pool.Poolable {
    public Polygon polygon;

    public void init(float[] vertices) {
        this.polygon = new Polygon(vertices);
    }

    @Override
    public void reset() {
        this.polygon = null;
    }
}
