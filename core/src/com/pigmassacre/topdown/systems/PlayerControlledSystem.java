package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Bits;
import com.pigmassacre.topdown.Constants;
import com.pigmassacre.topdown.components.*;
import com.pigmassacre.topdown.components.collision.EnemyCollisionComponent;
import com.pigmassacre.topdown.components.collision.MapCollisionComponent;
import com.pigmassacre.topdown.components.collision.RectangleCollisionComponent;
import com.pigmassacre.topdown.components.collision.StickToMapComponent;
import com.pigmassacre.topdown.components.movement.*;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class PlayerControlledSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private PooledEngine engine;

    private ComponentMapper<AccelerationComponent> accelerationMapper = ComponentMapper.getFor(AccelerationComponent.class);
    private ComponentMapper<DecelerationComponent> decelerationMapper = ComponentMapper.getFor(DecelerationComponent.class);
    private ComponentMapper<PlayerControlledComponent> playerControlledMapper = ComponentMapper.getFor(PlayerControlledComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RectangleCollisionComponent> collisionMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);
    private ComponentMapper<DirectionComponent> directionMapper = ComponentMapper.getFor(DirectionComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
        entities = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(PlayerControlledComponent.class), ComponentType.getBitsFor(AccelerationComponent.class, DecelerationComponent.class, PositionComponent.class, RectangleCollisionComponent.class, DirectionComponent.class), new Bits()));
    }

    @Override
    public void update(float deltaTime) {
        AccelerationComponent acceleration;
        DecelerationComponent deceleration;
        PlayerControlledComponent playerControlled;
        PositionComponent position;
        RectangleCollisionComponent collision;
        DirectionComponent direction;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            playerControlled = playerControlledMapper.get(entity);
            acceleration = accelerationMapper.get(entity);
            direction = directionMapper.get(entity);

            if (acceleration != null) {
                if (playerControlled.isMovingUp) {
                    acceleration.y = 0.5f * Constants.TARGET_FRAME_RATE;
                } else if (playerControlled.isMovingDown) {
                    acceleration.y = -0.5f * Constants.TARGET_FRAME_RATE;
                }

                if (!playerControlled.isMovingUp && !playerControlled.isMovingDown) {
                    acceleration.y = 0f;
                }

                if (playerControlled.isMovingLeft) {
                    acceleration.x = -0.5f * Constants.TARGET_FRAME_RATE;
                } else if (playerControlled.isMovingRight) {
                    acceleration.x = 0.5f * Constants.TARGET_FRAME_RATE;
                }

                if (!playerControlled.isMovingLeft && !playerControlled.isMovingRight) {
                    acceleration.x = 0f;
                }
            }

            deceleration = decelerationMapper.get(entity);

            if (deceleration != null) {
                if (!playerControlled.isMovingUp && !playerControlled.isMovingDown) {
                    deceleration.y = 1f * Constants.TARGET_FRAME_RATE;
                } else {
                    deceleration.y = 0f;
                }

                if (!playerControlled.isMovingLeft && !playerControlled.isMovingRight) {
                    deceleration.x = 1f * Constants.TARGET_FRAME_RATE;
                } else {
                    deceleration.x = 0f;
                }
            }

            if (direction != null) {
                if (playerControlled.isMovingLeft) {
                    direction.x = -1f;
                } else if (playerControlled.isMovingRight) {
                    direction.x = 1f;
                }

                if (playerControlled.isMovingLeft || playerControlled.isMovingRight) {
                    if (playerControlled.isMovingUp) {
                        direction.y = 1f;
                    } else if (playerControlled.isMovingDown) {
                        direction.y = -1f;
                    } else {
                        direction.y = 0f;
                    }
                }

                if (playerControlled.isMovingUp) {
                    direction.y = 1f;
                } else if (playerControlled.isMovingDown) {
                    direction.y = -1f;
                }

                if (playerControlled.isMovingUp || playerControlled.isMovingDown) {
                    if (playerControlled.isMovingLeft) {
                        direction.x = -1f;
                    } else if (playerControlled.isMovingRight) {
                        direction.x = 1f;
                    } else {
                        direction.x = 0f;
                    }
                }
            }

            position = positionMapper.get(entity);
            collision = collisionMapper.get(entity);

            if (position != null && collision != null) {
                if (playerControlled.isShooting) {
                    if (direction != null) createBouncyBullet(position.x, position.y, direction.x, direction.y);
                    else createBouncyBullet(position.x, position.y, 1f, 0f);
                }
            }
        }
    }

    public void createBouncyBullet(float x, float y, float directionX, float directionY) {
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.init(x, y, 0f);
        entity.add(position);

        VisualComponent visualComponent = engine.createComponent(VisualComponent.class);
        visualComponent.init(new TextureRegion(new Texture(Gdx.files.internal("arrow.png"))));
        entity.add(visualComponent);

        RectangleCollisionComponent collision = engine.createComponent(RectangleCollisionComponent.class);
        collision.init(4f, 4f);
        entity.add(collision);

        //visualComponent.offsetX = collision.rectangle.width - visualComponent.image.getRegionWidth();
        //visualComponent.offsetY = collision.rectangle.height - visualComponent.image.getRegionHeight();
        //visualComponent.originX -= visualComponent.offsetX + collision.rectangle.width / 2f;
        //visualComponent.originY -= visualComponent.offsetY + collision.rectangle.height / 2f;
        visualComponent.originX = visualComponent.image.getRegionWidth() - collision.rectangle.width / 2f;
        visualComponent.originY = visualComponent.image.getRegionHeight() / 2f;
        visualComponent.offsetX = -visualComponent.originX + collision.rectangle.width / 2f;
        visualComponent.offsetY = -visualComponent.originY + collision.rectangle.height / 2f;

        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        float angle = MathUtils.atan2(directionY, directionX);
        angle += MathUtils.random(-(MathUtils.PI / 64), MathUtils.PI / 64);
        velocity.maxX *= 2f;
        velocity.maxY *= 2f;
        velocity.x = velocity.maxX * MathUtils.cos(angle);
        velocity.y = velocity.maxY * MathUtils.sin(angle);
        entity.add(velocity);

        RotationComponent rotation = engine.createComponent(RotationComponent.class);
        rotation.angle = angle;
        entity.add(rotation);

        AccelerationComponent accelerationComponent = engine.createComponent(AccelerationComponent.class);
        entity.add(accelerationComponent);

        entity.add(engine.createComponent(MapCollisionComponent.class));

        //EntityCollisionComponent entityCollisionComponent = engine.createComponent(EntityCollisionComponent.class);
        //entity.add(entityCollisionComponent);

        //entity.add(engine.createComponent(BounceComponent.class));

        entity.add(engine.createComponent(StickToMapComponent.class));

        entity.add(engine.createComponent(EnemyCollisionComponent.class));

        engine.addEntity(entity);
    }
}
