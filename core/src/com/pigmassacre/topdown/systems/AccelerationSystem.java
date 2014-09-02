package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.AccelerationComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class AccelerationSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<AccelerationComponent> accelerationMapper = ComponentMapper.getFor(AccelerationComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(AccelerationComponent.class, VelocityComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        AccelerationComponent acceleration;
        VelocityComponent velocity;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            acceleration = accelerationMapper.get(entity);
            velocity = velocityMapper.get(entity);

            velocity.x += acceleration.x;// * deltaTime;
            velocity.y += acceleration.y;// * deltaTime;
            velocity.z += acceleration.z;// * deltaTime;
        }
    }
}
