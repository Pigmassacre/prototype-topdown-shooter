package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class VisualComponent extends Component implements Pool.Poolable {
    public TextureRegion image;
    public float offsetX = 0f;
    public float offsetY = 0f;
    public float originX = 0f;
    public float originY = 0f;
    public float scaleX = 1f;
    public float scaleY = 1f;

    @Override
    public void reset() {
        image = null;
        offsetX = 0f;
        offsetY = 0f;
        originX = 0f;
        originY = 0f;
        scaleX = 1f;
        scaleY = 1f;
    }
}
