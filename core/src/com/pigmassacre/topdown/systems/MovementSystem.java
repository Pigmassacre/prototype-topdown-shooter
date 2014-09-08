package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.components.movement.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    private CollisionSystem collisionSystem;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VelocityComponent.class));
        collisionSystem = engine.getSystem(CollisionSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        VelocityComponent velocity;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            position = positionMapper.get(entity);
            velocity = velocityMapper.get(entity);

            if (velocity.x > velocity.maxX) velocity.x = velocity.maxX;
            if (velocity.x < -velocity.maxX) velocity.x = -velocity.maxX;
            if (velocity.y > velocity.maxY) velocity.y = velocity.maxY;
            if (velocity.y < -velocity.maxY) velocity.y = -velocity.maxY;
            if (velocity.z > velocity.maxZ) velocity.z = velocity.maxZ;
            if (velocity.z < -velocity.maxZ) velocity.z = -velocity.maxZ;

            position.x += velocity.x * deltaTime;
            collisionSystem.checkCollision(entity, CollisionSystem.CollisionAxis.X);

            position.y += velocity.y * deltaTime;
            collisionSystem.checkCollision(entity, CollisionSystem.CollisionAxis.Y);

            position.z += velocity.z * deltaTime;
            collisionSystem.checkCollision(entity, CollisionSystem.CollisionAxis.Z);
        }
    }
}
