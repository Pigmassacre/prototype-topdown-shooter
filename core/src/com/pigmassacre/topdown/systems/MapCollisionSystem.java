package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pigmassacre.topdown.components.MapCollisionComponent;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.RectangleCollisionComponent;
import com.pigmassacre.topdown.components.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class MapCollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<RectangleCollisionComponent> collisionMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);

    private Signal<MapObjectCollisionSignal> collisionSignal = new Signal<MapObjectCollisionSignal>();

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
                handleCollision(object);
            }
        });
    }

    private void handleCollision(CollisionSystem.MapCollisionSignal object) {
        Entity entity = object.entity;
        MapObject mapObject = object.object;

        if (entities.contains(entity, false)) {
            PositionComponent position = positionMapper.get(entity);
            VelocityComponent velocity = velocityMapper.get(entity);
            RectangleCollisionComponent collision = collisionMapper.get(entity);

            boolean left = false, right = false, up = false, down = false;

            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                Rectangle rectangle = rectangleMapObject.getRectangle();
                Vector2 center = rectangle.getCenter(new Vector2());

                float xOverlap, yOverlap;

                float wx = collision.rectangle.x + (collision.rectangle.width / 2f) - center.x;

                if (wx > 0) {
                    xOverlap = (rectangle.x + rectangle.width) - collision.rectangle.x;
                } else {
                    xOverlap = rectangle.x - (collision.rectangle.x + collision.rectangle.width);
                }

                float hy = collision.rectangle.y + (collision.rectangle.height / 2f) - center.y;

                if (hy > 0) {
                    yOverlap = (rectangle.y + rectangle.height) - collision.rectangle.y;
                } else {
                    yOverlap = rectangle.y - (collision.rectangle.y + collision.rectangle.height);
                }

                if (Math.abs(xOverlap) < Math.abs(yOverlap)) {
                    if (xOverlap > 0) left = true;
                    else right = true;
                    //velocity.x = 0f;
                    collision.rectangle.x += xOverlap;
                } else {
                    if (yOverlap > 0) up = true;
                    else down = true;
                    collision.rectangle.y += yOverlap;
                    //velocity.y = 0f;
                }
            }

            position.x = collision.rectangle.x;
            position.y = collision.rectangle.y;

            if (up) collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.UP));
            else if (down) collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.DOWN));
            else if (left) collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.LEFT));
            else if (right) collisionSignal.dispatch(new MapObjectCollisionSignal(entity, mapObject, MapObjectSide.RIGHT));
        }
    }
}
