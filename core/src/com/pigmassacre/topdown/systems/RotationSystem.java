package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.RotationComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class RotationSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<RotationComponent> rotationMapper = ComponentMapper.getFor(RotationComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(RotationComponent.class, VelocityComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        RotationComponent rotation;
        VelocityComponent velocity;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            rotation = rotationMapper.get(entity);
            velocity = velocityMapper.get(entity);

            if (rotation.rotateToVelocity) {
                rotation.angle = MathUtils.atan2(velocity.y, velocity.x);
            }
        }
    }
}
