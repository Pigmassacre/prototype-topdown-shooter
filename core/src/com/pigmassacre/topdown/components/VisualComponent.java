package com.pigmassacre.topdown.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class VisualComponent extends Component implements Pool.Poolable {
    public TextureRegion image;

    public void init(TextureRegion image) {
        this.image = image;
    }

    @Override
    public void reset() {
        this.image = null;
    }
}
