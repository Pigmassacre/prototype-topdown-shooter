package com.pigmassacre.topdown;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pigmassacre.topdown.components.*;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class PlayerInputProcessor extends InputAdapter {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<PlayerControlledComponent> playerControlledMapper = ComponentMapper.getFor(PlayerControlledComponent.class);

    private PooledEngine engine;

    public PlayerInputProcessor(PooledEngine engine) {
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VelocityComponent.class, PlayerControlledComponent.class));
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log(toString(), Input.Keys.toString(keycode) + " pressed!");
        switch (keycode) {
            case Input.Keys.Z:
                for (int i = 0; i < entities.size(); i++) {
                    VelocityComponent velocity = velocityMapper.get(entities.get(i));
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    if (!playerControlled.isInAir) {
                        velocity.z = 2f * Constants.TARGET_FRAME_RATE;
                    }
                }
                break;
            case Input.Keys.X:
                for (int i = 0; i < entities.size(); i++) {
                    PositionComponent position = positionMapper.get(entities.get(i));
                    VelocityComponent velocity = velocityMapper.get(entities.get(i));
                    createBouncyBullet(position.x, position.y, velocity.x, velocity.y);
                }
                break;
            case Input.Keys.C:
                break;
            case Input.Keys.UP:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingUp = true;
                    playerControlled.isMovingDown = false;
                }
                break;
            case Input.Keys.DOWN:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingUp = false;
                    playerControlled.isMovingDown = true;
                }
                break;
            case Input.Keys.LEFT:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingLeft = true;
                    playerControlled.isMovingRight = false;
                }
                break;
            case Input.Keys.RIGHT:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingLeft = false;
                    playerControlled.isMovingRight = true;
                }
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.log(toString(), Input.Keys.toString(keycode) + " released!");
        switch (keycode) {
            case Input.Keys.Z:
                break;
            case Input.Keys.X:
                break;
            case Input.Keys.C:
                break;
            case Input.Keys.UP:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingUp = false;
                }
                break;
            case Input.Keys.DOWN:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingDown = false;
                }
                break;
            case Input.Keys.LEFT:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingLeft = false;
                }
                break;
            case Input.Keys.RIGHT:
                for (int i = 0; i < entities.size(); i++) {
                    PlayerControlledComponent playerControlled = playerControlledMapper.get(entities.get(i));
                    playerControlled.isMovingRight = false;
                }
                break;
        }
        return false;
    }

    public void createBouncyBullet(float x, float y, float velocityX, float velocityY) {
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.init(x, y, 0);
        entity.add(position);

        VisualComponent visualComponent = engine.createComponent(VisualComponent.class);
        visualComponent.init(new TextureRegion(new Texture(Gdx.files.internal("bullet.png"))));
        entity.add(visualComponent);

        RectangleCollisionComponent collision = engine.createComponent(RectangleCollisionComponent.class);
        collision.init(visualComponent.image.getRegionWidth() * visualComponent.scaleX, visualComponent.image.getRegionWidth() * visualComponent.scaleX);
        entity.add(collision);

        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        velocity.maxX *= 2f;
        velocity.maxY *= 2f;
        velocity.x = velocityX * 2f;
        velocity.y = velocityY * 2f;
        entity.add(velocity);

        AccelerationComponent accelerationComponent = engine.createComponent(AccelerationComponent.class);
        entity.add(accelerationComponent);

        entity.add(engine.createComponent(GravityComponent.class));

        entity.add(engine.createComponent(MapCollisionComponent.class));

        entity.add(engine.createComponent(BounceComponent.class));

        engine.addEntity(entity);
    }
}
