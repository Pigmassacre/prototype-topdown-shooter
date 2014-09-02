package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
                    velocity.x = 0f;
                    collision.rectangle.x += xOverlap;
                } else {
                    collision.rectangle.y += yOverlap;
                    velocity.y = 0f;
                }
            }

            position.x = collision.rectangle.x;
            position.y = collision.rectangle.y;
        }
    }
}
