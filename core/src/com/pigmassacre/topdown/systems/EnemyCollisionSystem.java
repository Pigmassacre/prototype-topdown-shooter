package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.BounceComponent;
import com.pigmassacre.topdown.components.EnemyCollisionComponent;
import com.pigmassacre.topdown.components.RemovalComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class EnemyCollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private PooledEngine engine;

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;

        entities = engine.getEntitiesFor(Family.getFor(EnemyCollisionComponent.class));
        EntityCollisionSystem collisionSystem = engine.getSystem(EntityCollisionSystem.class);

        collisionSystem.registerEntityCollisionListener(new Listener<EntityCollisionSystem.EntityObjectCollisionSignal>() {
            @Override
            public void receive(Signal<EntityCollisionSystem.EntityObjectCollisionSignal> signal, EntityCollisionSystem.EntityObjectCollisionSignal object) {
                handleCollision(object);
            }
        });
    }

    private void handleCollision(EntityCollisionSystem.EntityObjectCollisionSignal object) {
        Entity entity1 = object.entity1;
        //Entity entity2 = object.entity2;

        if (entities.contains(entity1, false)) entity1.add(engine.createComponent(RemovalComponent.class));
        //if (entities.contains(entity2, false)) entity2.add(engine.createComponent(RemovalComponent.class));
    }
}
