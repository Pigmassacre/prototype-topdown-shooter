package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.collision.StickToMapComponent;
import com.pigmassacre.topdown.components.movement.AccelerationComponent;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.components.movement.RotationComponent;
import com.pigmassacre.topdown.components.movement.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class StickToMapSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<AccelerationComponent> accelerationMapper = ComponentMapper.getFor(AccelerationComponent.class);
    private ComponentMapper<RotationComponent> rotationMapper = ComponentMapper.getFor(RotationComponent.class);

    private MapCollisionSystem collisionSystem;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(StickToMapComponent.class, VelocityComponent.class));
        collisionSystem = engine.getSystem(MapCollisionSystem.class);
        collisionSystem.registerMapCollisionListener(new Listener<MapCollisionSystem.MapObjectCollisionSignal>() {
            @Override
            public void receive(Signal<MapCollisionSystem.MapObjectCollisionSignal> signal, MapCollisionSystem.MapObjectCollisionSignal object) {
                stickTo(object);
            }
        });
    }

    private void stickTo(MapCollisionSystem.MapObjectCollisionSignal object) {
        VelocityComponent velocity;
        AccelerationComponent acceleration;
        RotationComponent rotation;

        if (entities.contains(object.entity, false)) {
            velocity = velocityMapper.get(object.entity);

            if (velocity != null) {
                velocity.reset();
            }

            acceleration = accelerationMapper.get(object.entity);

            if (acceleration != null) {
                acceleration.reset();
            }

            rotation = rotationMapper.get(object.entity);

            if (rotation != null) {
                rotation.rotateToVelocity = false;
            }
        }
    }
}
