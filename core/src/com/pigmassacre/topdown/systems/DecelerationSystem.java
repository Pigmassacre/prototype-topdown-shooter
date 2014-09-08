package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.movement.DecelerationComponent;
import com.pigmassacre.topdown.components.movement.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class DecelerationSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<DecelerationComponent> decelerationMapper = ComponentMapper.getFor(DecelerationComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(DecelerationComponent.class, VelocityComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        DecelerationComponent deceleration;
        VelocityComponent velocity;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            deceleration = decelerationMapper.get(entity);
            velocity = velocityMapper.get(entity);

            float oldX = velocity.x, oldY = velocity.y, oldZ = velocity.z;

            if (velocity.x > 0) {
                velocity.x -= deceleration.x;// * deltaTime;
            } else if (velocity.x < 0) {
                velocity.x += deceleration.x;// * deltaTime;
            }

            if (velocity.y > 0) {
                velocity.y -= deceleration.y;// * deltaTime;
            } else if (velocity.y < 0) {
                velocity.y += deceleration.y;// * deltaTime;
            }

            if (velocity.z > 0) {
                velocity.z -= deceleration.z;// * deltaTime;
            } else if (velocity.z < 0) {
                velocity.z += deceleration.z;// * deltaTime;
            }

            if (oldX * velocity.x < 0) velocity.x = 0;
            if (oldY * velocity.y < 0) velocity.y = 0;
            if (oldZ * velocity.z < 0) velocity.z = 0;
        }
    }
}
