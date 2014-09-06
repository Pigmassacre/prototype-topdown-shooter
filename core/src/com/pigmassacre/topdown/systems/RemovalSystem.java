package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.Constants;
import com.pigmassacre.topdown.components.GravityComponent;
import com.pigmassacre.topdown.components.RemovalComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class RemovalSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private PooledEngine engine;

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
        entities = engine.getEntitiesFor(Family.getFor(RemovalComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            engine.removeEntity(entity);
        }
    }
}
