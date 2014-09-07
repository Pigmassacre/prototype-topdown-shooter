package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.BounceComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class BounceSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(BounceComponent.class, VelocityComponent.class));
        MapCollisionSystem mapCollisionSystem = engine.getSystem(MapCollisionSystem.class);

        mapCollisionSystem.registerMapCollisionListener(new Listener<MapCollisionSystem.MapObjectCollisionSignal>() {
            @Override
            public void receive(Signal<MapCollisionSystem.MapObjectCollisionSignal> signal, MapCollisionSystem.MapObjectCollisionSignal object) {
                handleCollision(object);
            }
        });

        EntityCollisionSystem entityCollisionSystem = engine.getSystem(EntityCollisionSystem.class);

        entityCollisionSystem.registerEntityCollisionListener(new Listener<EntityCollisionSystem.EntityObjectCollisionSignal>() {
            @Override
            public void receive(Signal<EntityCollisionSystem.EntityObjectCollisionSignal> signal, EntityCollisionSystem.EntityObjectCollisionSignal object) {
                handleCollision(object);
            }
        });
    }

    private void handleCollision(MapCollisionSystem.MapObjectCollisionSignal object) {
        Entity entity = object.entity;
        MapCollisionSystem.MapObjectSide side = object.side;

        if (entities.contains(entity, false)) {
            VelocityComponent velocity = velocityMapper.get(entity);

            switch (side) {
                case UP:
                case DOWN:
                    velocity.y = -velocity.y;
                    break;
                case LEFT:
                case RIGHT:
                    velocity.x = -velocity.x;
                    break;
                case TOP:
                case BOTTOM:
                    velocity.z = -velocity.z;
                    break;
            }
        }
    }

    private void handleCollision(EntityCollisionSystem.EntityObjectCollisionSignal object) {
        Entity entity = object.entity1;
        EntityCollisionSystem.EntitySide side = object.side;

        if (entities.contains(entity, false)) {
            VelocityComponent velocity = velocityMapper.get(entity);

            switch (side) {
                case UP:
                case DOWN:
                    velocity.y = -velocity.y;
                    break;
                case LEFT:
                case RIGHT:
                    velocity.x = -velocity.x;
                    break;
            }
        }
    }
}
