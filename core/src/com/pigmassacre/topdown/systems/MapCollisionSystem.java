package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pigmassacre.topdown.components.MapCollisionComponent;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.RectangleCollisionComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class MapCollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    //private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<RectangleCollisionComponent> collisionMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);

    private Signal<MapObjectCollisionSignal> collisionSignal = new Signal<MapObjectCollisionSignal>();

    public void registerMapCollisionListener(Listener<MapObjectCollisionSignal> listener) {
        collisionSignal.add(listener);
    }

    public void unregisterMapCollisionListener(Listener<MapObjectCollisionSignal> listener) {
        collisionSignal.remove(listener);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(RectangleCollisionComponent.class, PositionComponent.class, MapCollisionComponent.class));

        CollisionSystem collisionSystem = engine.getSystem(CollisionSystem.class);
        collisionSystem.registerMapCollisionListener(new Listener<CollisionSystem.MapCollisionSignal>() {
            @Override
            public void receive(Signal<CollisionSystem.MapCollisionSignal> signal, CollisionSystem.MapCollisionSignal object) {
                if (object.handleX) handleCollisionX(object);
                else handleCollisionY(object);
            }
        });
    }

    public void handleCollisionX(CollisionSystem.MapCollisionSignal signal) {
        Entity entity = signal.entity;
        MapObject mapObject = signal.object;

        if (entities.contains(entity, false)) {
            PositionComponent position = positionMapper.get(entity);
            RectangleCollisionComponent collision = collisionMapper.get(entity);

            boolean left = false, right = false;

            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                Rectangle rectangle = rectangleMapObject.getRectangle();
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
                        collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.LEFT));
                    else if (right)
                        collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.RIGHT));
                }
            }
        }
    }

    public void handleCollisionY(CollisionSystem.MapCollisionSignal signal) {
        Entity entity = signal.entity;
        MapObject mapObject = signal.object;

        if (entities.contains(entity, false)) {
            PositionComponent position = positionMapper.get(entity);
            RectangleCollisionComponent collision = collisionMapper.get(entity);

            boolean up = false, down = false;

            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                Rectangle rectangle = rectangleMapObject.getRectangle();
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

                    if (up) collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.UP));
                    else if (down)
                        collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.DOWN));
                }
            }
        }
    }

    public enum MapObjectSide {
        UP, DOWN, LEFT, RIGHT
    }

    public class MapObjectCollisionSignal {
        public Entity entity;
        public MapObject object;
        public MapObjectSide side;

        public MapObjectCollisionSignal(Entity entity, MapObject object, MapObjectSide side) {
            this.entity = entity;
            this.object = object;
            this.side = side;
        }
    }
}
