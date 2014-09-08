package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Bits;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.collision.CircleCollisionComponent;
import com.pigmassacre.topdown.components.collision.PolygonCollisionComponent;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.components.collision.RectangleCollisionComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<PolygonCollisionComponent> polygonMapper = ComponentMapper.getFor(PolygonCollisionComponent.class);
    private ComponentMapper<CircleCollisionComponent> circleMapper = ComponentMapper.getFor(CircleCollisionComponent.class);
    private ComponentMapper<RectangleCollisionComponent> rectangleMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);

    private Signal<EntityCollisionSignal> entityCollisionSignal = new Signal<EntityCollisionSignal>();
    private Signal<MapCollisionSignal> mapCollisionSignal = new Signal<MapCollisionSignal>();

    public void registerEntityCollisionListener(Listener<EntityCollisionSignal> listener) {
        entityCollisionSignal.add(listener);
    }

    public void unregisterEntityCollisionListener(Listener<EntityCollisionSignal> listener) {
        entityCollisionSignal.remove(listener);
    }

    public void registerMapCollisionListener(Listener<MapCollisionSignal> listener) {
        mapCollisionSignal.add(listener);
    }

    public void unregisterMapCollisionListener(Listener<MapCollisionSignal> listener) {
        mapCollisionSignal.remove(listener);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(PositionComponent.class), ComponentType.getBitsFor(RectangleCollisionComponent.class, CircleCollisionComponent.class), new Bits()));
    }

    public void checkCollision(Entity entity, CollisionAxis axis) {
        CircleCollisionComponent circleCollision1, circleCollision2;
        RectangleCollisionComponent rectangleCollision1, rectangleCollision2;
        PositionComponent position1, position2;

        position1 = positionMapper.get(entity);

        circleCollision1 = circleMapper.get(entity);

        if (circleCollision1 != null) {
            circleCollision1.circle.x = position1.x + circleCollision1.circle.radius;
            circleCollision1.circle.y = position1.y + circleCollision1.circle.radius;

            MapObjects objects = Level.getMap().getLayers().get("collision").getObjects();
            for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                Rectangle rectangle = rectangleObject.getRectangle();
                if (Intersector.overlaps(circleCollision1.circle, rectangle)) {
                    mapCollisionSignal.dispatch(new MapCollisionSignal(entity, rectangleObject, axis));
                }
            }

            for (int j = 0; j < entities.size(); j++) {
                Entity entity2 = entities.get(j);

                if (entity != entity2) {
                    position2 = positionMapper.get(entity2);
                    circleCollision2 = circleMapper.get(entity2);

                    if (circleCollision2 != null) {
                        circleCollision2.circle.x = position2.x + circleCollision2.circle.radius;
                        circleCollision2.circle.y = position2.y + circleCollision2.circle.radius;

                        if (circleCollision1.circle.overlaps(circleCollision2.circle)) {
                            entityCollisionSignal.dispatch(new EntityCollisionSignal(entity, entity2, axis));
                        }
                    } else {
                        rectangleCollision2 = rectangleMapper.get(entity2);

                        rectangleCollision2.rectangle.x = position2.x;
                        rectangleCollision2.rectangle.y = position2.y;

                        if (Intersector.overlaps(circleCollision1.circle, rectangleCollision2.rectangle)) {
                            entityCollisionSignal.dispatch(new EntityCollisionSignal(entity, entity2, axis));
                        }
                    }
                }
            }
        } else {
            rectangleCollision1 = rectangleMapper.get(entity);

            rectangleCollision1.rectangle.x = position1.x;
            rectangleCollision1.rectangle.y = position1.y;

            MapObjects objects = Level.getMap().getLayers().get("collision").getObjects();
            for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                Rectangle rectangle = rectangleObject.getRectangle();
                if (Intersector.overlaps(rectangleCollision1.rectangle, rectangle)) {
                    mapCollisionSignal.dispatch(new MapCollisionSignal(entity, rectangleObject, axis));
                }
            }

            for (int j = 0; j < entities.size(); j++) {
                Entity entity2 = entities.get(j);

                if (entity != entity2) {
                    position2 = positionMapper.get(entity2);

                    circleCollision2 = circleMapper.get(entity2);

                    if (circleCollision2 != null) {
                        circleCollision2.circle.x = position2.x + circleCollision2.circle.radius;
                        circleCollision2.circle.y = position2.y + circleCollision2.circle.radius;

                        if (Intersector.overlaps(circleCollision2.circle, rectangleCollision1.rectangle)) {
                            entityCollisionSignal.dispatch(new EntityCollisionSignal(entity, entity2, axis));
                        }
                    } else {
                        rectangleCollision2 = rectangleMapper.get(entity2);

                        rectangleCollision2.rectangle.x = position2.x;
                        rectangleCollision2.rectangle.y = position2.y;

                        if (Intersector.overlaps(rectangleCollision1.rectangle, rectangleCollision2.rectangle)) {
                            entityCollisionSignal.dispatch(new EntityCollisionSignal(entity, entity2, axis));
                        }
                    }
                }
            }
        }

        if (position1.z < 0) {
            mapCollisionSignal.dispatch(new MapCollisionSignal(entity, null, axis));
        }
    }

    public enum CollisionAxis {
        X, Y, Z
    }

    public class EntityCollisionSignal {
        public Entity entity1, entity2;
        public CollisionAxis axis;

        public EntityCollisionSignal(Entity entity1, Entity entity2, CollisionAxis axis) {
            this.entity1 = entity1;
            this.entity2 = entity2;
            this.axis = axis;
        }
    }

    public class MapCollisionSignal {
        public Entity entity;
        public MapObject object;
        public CollisionAxis axis;

        public MapCollisionSignal(Entity entity, MapObject object, CollisionAxis axis) {
            this.entity = entity;
            this.object = object;
            this.axis = axis;
        }
    }
}
