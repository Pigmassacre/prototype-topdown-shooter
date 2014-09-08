package com.pigmassacre.topdown;

import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Bits;
import com.pigmassacre.topdown.components.PlayerControlledComponent;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class DebugInputProcessor extends InputAdapter {
    private PooledEngine engine;

    public DebugInputProcessor(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log(toString(), Input.Keys.toString(keycode) + " pressed!");
        switch (keycode) {
            case Input.Keys.R:
                ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.getFor(new Bits(), new Bits(), ComponentType.getBitsFor(PlayerControlledComponent.class)));
                for (int i = 0; i < entities.size(); i++) {
                    engine.removeEntity(entities.get(i));
                }
                break;
        }
        return false;
    }
}
