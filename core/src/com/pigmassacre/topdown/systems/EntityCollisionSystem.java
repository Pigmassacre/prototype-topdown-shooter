package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pigmassacre.topdown.components.*;
import com.pigmassacre.topdown.components.collision.EntityCollisionComponent;
import com.pigmassacre.topdown.components.collision.RectangleCollisionComponent;
import com.pigmassacre.topdown.components.movement.PositionComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class EntityCollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private PooledEngine engine;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RectangleCollisionComponent> collisionMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);
    private ComponentMapper<EntityCollisionComponent> entityCollisionMapper = ComponentMapper.getFor(EntityCollisionComponent.class);

    private Signal<EntityObjectCollisionSignal> collisionSignal = new Signal<EntityObjectCollisionSignal>();

    public void registerEntityCollisionListener(Listener<EntityObjectCollisionSignal> listener) {
        collisionSignal.add(listener);
    }

    public void unregisterEntityCollisionListener(Listener<EntityObjectCollisionSignal> listener) {
        collisionSignal.remove(listener);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
        entities = engine.getEntitiesFor(Family.getFor(RectangleCollisionComponent.class, PositionComponent.class, EntityCollisionComponent.class));

        CollisionSystem collisionSystem = engine.getSystem(CollisionSystem.class);
        collisionSystem.registerEntityCollisionListener(new Listener<CollisionSystem.EntityCollisionSignal>() {
            @Override
            public void receive(Signal<CollisionSystem.EntityCollisionSignal> signal, CollisionSystem.EntityCollisionSignal object) {
                switch (object.axis) {
                    case X:
                        handleCollisionX(object);
                        break;
                    case Y:
                        handleCollisionY(object);
                        break;
                    case Z:
                        break;
                }
            }
        });
    }

    public void handleCollisionX(CollisionSystem.EntityCollisionSignal signal) {
        Entity entity1 = signal.entity1;
        Entity entity2 = signal.entity2;

        if (entities.contains(entity1, false) && entities.contains(entity2, false)) {
            PositionComponent position = positionMapper.get(entity1);
            RectangleCollisionComponent collision = collisionMapper.get(entity1);

            boolean left = false, right = false;

            Rectangle rectangle = collisionMapper.get(entity2).rectangle;
            Vector2 center = rectangle.getCenter(new Vector2());

            if (Intersector.overlaps(collision.rectangle, rectangle)) {
                float xOverlap;

                float wx = collision.rectangle.x + (collision.rectangle.width / 2f) - center.x;

                if (wx > 0) {
                    xOverlap = (rectangle.x + rectangle.width) - collision.rectangle.x;
                } else {
                    xOverlap = rectangle.x - (collision.rectangle.x + collision.rectangle.width);
                }

                if (xOverlap > 0) left = true;
                else right = true;
                collision.rectangle.x += xOverlap;

                position.x = collision.rectangle.x;

                if (left)
                    collisionSignal.dispatch(new EntityObjectCollisionSignal(entity1, entity2, EntitySide.LEFT));
                else if (right)
                    collisionSignal.dispatch(new EntityObjectCollisionSignal(entity1, entity2, EntitySide.RIGHT));

                if (entityCollisionMapper.get(entity1).destroyOnCollision) entity1.add(engine.createComponent(RemovalComponent.class));
                if (entityCollisionMapper.get(entity2).destroyOnCollision) entity2.add(engine.createComponent(RemovalComponent.class));
            }
        }
    }

    public void handleCollisionY(CollisionSystem.EntityCollisionSignal signal) {
        Entity entity1 = signal.entity1;
        Entity entity2 = signal.entity2;

        if (entities.contains(entity1, false) && entities.contains(entity2, false)) {
            PositionComponent position = positionMapper.get(entity1);
            RectangleCollisionComponent collision = collisionMapper.get(entity1);

            boolean up = false, down = false;

                Rectangle rectangle = collisionMapper.get(entity2).rectangle;
                Vector2 center = rectangle.getCenter(new Vector2());

                if (Intersector.overlaps(collision.rectangle, rectangle)) {
                    float yOverlap;

                    float hy = collision.rectangle.y + (collision.rectangle.height / 2f) - center.y;

                    if (hy > 0) {
                        yOverlap = (rectangle.y + rectangle.height) - collision.rectangle.y;
                    } else {
                        yOverlap = rectangle.y - (collision.rectangle.y + collision.rectangle.height);
                    }

                    if (yOverlap > 0) up = true;
                    else down = true;
                    collision.rectangle.y += yOverlap;

                    position.y = collision.rectangle.y;

                    if (up) collisionSignal.dispatch(new EntityObjectCollisionSignal(entity1, entity2, EntitySide.UP));
                    else if (down)
                        collisionSignal.dispatch(new EntityObjectCollisionSignal(entity1, entity2, EntitySide.DOWN));

                    if (entityCollisionMapper.get(entity1).destroyOnCollision) entity1.add(engine.createComponent(RemovalComponent.class));
                    if (entityCollisionMapper.get(entity2).destroyOnCollision) entity2.add(engine.createComponent(RemovalComponent.class));
                }
        }
    }

    public enum EntitySide {
        UP, DOWN, LEFT, RIGHT
    }

    public class EntityObjectCollisionSignal {
        public Entity entity1;
        public Entity entity2;
        public EntitySide side;

        public EntityObjectCollisionSignal(Entity entity1, Entity entity2, EntitySide side) {
            this.entity1 = entity1;
            this.entity2 = entity2;
            this.side = side;
        }
    }
}
