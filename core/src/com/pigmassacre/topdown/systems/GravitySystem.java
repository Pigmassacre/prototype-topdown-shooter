package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.GravityComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class GravitySystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(VelocityComponent.class, GravityComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        VelocityComponent velocity;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            velocity = velocityMapper.get(entity);

            velocity.z -= 10f * deltaTime;
        }
    }
}
