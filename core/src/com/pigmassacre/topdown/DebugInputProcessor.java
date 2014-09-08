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
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.pigmassacre.topdown.components.PlayerControlledComponent;
import com.pigmassacre.topdown.entities.Weapons;

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
            case Input.Keys.W:
                spawnWeapon();
                break;
        }
        return false;
    }

    private void spawnWeapon() {
        Array<RectangleMapObject> objects = Level.getMap().getLayers().get("spawn").getObjects().getByType(RectangleMapObject.class);
        Rectangle rectangle = objects.get(MathUtils.random(objects.size - 1)).getRectangle();

        Weapons.createCrossbow(rectangle.x, rectangle.y, engine);
    }
}
