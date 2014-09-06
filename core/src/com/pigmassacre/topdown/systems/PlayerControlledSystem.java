package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Bits;
import com.pigmassacre.topdown.Constants;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.AccelerationComponent;
import com.pigmassacre.topdown.components.DecelerationComponent;
import com.pigmassacre.topdown.components.PlayerControlledComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class PlayerControlledSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<AccelerationComponent> accelerationMapper = ComponentMapper.getFor(AccelerationComponent.class);
    private ComponentMapper<DecelerationComponent> decelerationMapper = ComponentMapper.getFor(DecelerationComponent.class);
    private ComponentMapper<PlayerControlledComponent> playerControlledMapper = ComponentMapper.getFor(PlayerControlledComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(PlayerControlledComponent.class), ComponentType.getBitsFor(AccelerationComponent.class, DecelerationComponent.class), new Bits()));
    }

    @Override
    public void update(float deltaTime) {
        AccelerationComponent acceleration;
        DecelerationComponent deceleration;
        PlayerControlledComponent playerControlled;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            acceleration = accelerationMapper.get(entity);
            deceleration = decelerationMapper.get(entity);
            playerControlled = playerControlledMapper.get(entity);

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
        }
    }
}
