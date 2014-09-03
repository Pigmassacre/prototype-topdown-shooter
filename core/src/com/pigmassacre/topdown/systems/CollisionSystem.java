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
import com.pigmassacre.topdown.components.CircleCollisionComponent;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.RectangleCollisionComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<CircleCollisionComponent> circleMapper = ComponentMapper.getFor(CircleCollisionComponent.class);
    private ComponentMapper<RectangleCollisionComponent> rectangleMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);

    private Signal<EntityCollisionSignal> entityCollisionSignal = new Signal<EntityCollisionSignal>();

    public class EntityCollisionSignal {
        public Entity entity1, entity2;

        public EntityCollisionSignal(Entity entity1, Entity entity2) {
            this.entity1 = entity1;
            this.entity2 = entity2;
        }
    }

    public void registerEntityCollisionListener(Listener<EntityCollisionSignal> listener) {
        entityCollisionSignal.add(listener);
    }

    public void unregisterEntityCollisionListener(Listener<EntityCollisionSignal> listener) {
        entityCollisionSignal.remove(listener);
    }

    private Signal<MapCollisionSignal> mapCollisionSignal = new Signal<MapCollisionSignal>();

    public class MapCollisionSignal {
        public Entity entity;
        public MapObject object;

        public MapCollisionSignal(Entity entity, MapObject object) {
            this.entity = entity;
            this.object = object;
        }
    }

    public void registerMapCollisionListener(Listener<MapCollisionSignal> listener) {
        mapCollisionSignal.add(listener);
    }

    public void unregisterMapCollisionListener(Listener<MapCollisionSignal> listener) {
        mapCollisionSignal.remove(listener);
    }

    @Override
    public void addedToEngine(Engine engine) {
        //entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, CircleCollisionComponent.class));
        entities = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(PositionComponent.class), ComponentType.getBitsFor(RectangleCollisionComponent.class, CircleCollisionComponent.class), new Bits()));
    }

    @Override
    public void update(float deltaTime) {
        CircleCollisionComponent circleCollision1, circleCollision2;
        RectangleCollisionComponent rectangleCollision1, rectangleCollision2;
        PositionComponent position1, position2;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity1 = entities.get(i);

            position1 = positionMapper.get(entity1);

            circleCollision1 = circleMapper.get(entity1);

            if (circleCollision1 != null) {
                circleCollision1.circle.x = position1.x + circleCollision1.circle.radius;
                circleCollision1.circle.y = position1.y + circleCollision1.circle.radius;

                MapObjects objects = Level.getMap().getLayers().get(0).getObjects();
                for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                    Rectangle rectangle = rectangleObject.getRectangle();
                    if (Intersector.overlaps(circleCollision1.circle, rectangle)) {
                        mapCollisionSignal.dispatch(new MapCollisionSignal(entity1, rectangleObject));
                    }
                }

                for (int j = 0; j < entities.size(); j++) {
                    Entity entity2 = entities.get(j);

                    position2 = positionMapper.get(entity2);

                    circleCollision2 = circleMapper.get(entity2);

                    if (circleCollision2 != null) {
                        circleCollision2.circle.x = position2.x + circleCollision2.circle.radius;
                        circleCollision2.circle.y = position2.y + circleCollision2.circle.radius;

                        if (i != j) {
                            if (circleCollision1.circle.overlaps(circleCollision2.circle)) {
                                entityCollisionSignal.dispatch(new EntityCollisionSignal(entity1, entity2));
                            }
                        }
                    } else {
                        rectangleCollision2 = rectangleMapper.get(entity1);

                        rectangleCollision2.rectangle.x = position2.x;
                        rectangleCollision2.rectangle.y = position2.y;

                        if (i != j) {
                            if (Intersector.overlaps(circleCollision1.circle, rectangleCollision2.rectangle)) {
                                entityCollisionSignal.dispatch(new EntityCollisionSignal(entity1, entity2));
                            }
                        }
                    }
                }
            } else {
                rectangleCollision1 = rectangleMapper.get(entity1);

                rectangleCollision1.rectangle.x = position1.x;
                rectangleCollision1.rectangle.y = position1.y;

                MapObjects objects = Level.getMap().getLayers().get(0).getObjects();
                for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                    Rectangle rectangle = rectangleObject.getRectangle();
                    if (Intersector.overlaps(rectangleCollision1.rectangle, rectangle)) {
                        mapCollisionSignal.dispatch(new MapCollisionSignal(entity1, rectangleObject));
                    }
                }

                for (int j = 0; j < entities.size(); j++) {
                    Entity entity2 = entities.get(j);

                    position2 = positionMapper.get(entity2);

                    circleCollision2 = circleMapper.get(entity2);

                    if (circleCollision2 != null) {
                        circleCollision2.circle.x = position2.x + circleCollision2.circle.radius;
                        circleCollision2.circle.y = position2.y + circleCollision2.circle.radius;

                        if (i != j) {
                            if (Intersector.overlaps(circleCollision2.circle, rectangleCollision1.rectangle)) {
                                entityCollisionSignal.dispatch(new EntityCollisionSignal(entity1, entity2));
                            }
                        }
                    } else {
                        rectangleCollision2 = rectangleMapper.get(entity1);

                        rectangleCollision2.rectangle.x = position2.x;
                        rectangleCollision2.rectangle.y = position2.y;

                        if (i != j) {
                            if (Intersector.overlaps(rectangleCollision1.rectangle, rectangleCollision2.rectangle)) {
                                entityCollisionSignal.dispatch(new EntityCollisionSignal(entity1, entity2));
                            }
                        }
                    }
                }
            }
        }
    }
}