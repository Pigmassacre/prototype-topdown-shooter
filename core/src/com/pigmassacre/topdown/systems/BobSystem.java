package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.pigmassacre.topdown.components.movement.BobComponent;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.components.movement.VelocityComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class BobSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<BobComponent> bobMapper = ComponentMapper.getFor(BobComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, BobComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        BobComponent bob;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            position = positionMapper.get(entity);
            bob = bobMapper.get(entity);

            bob.stateTime += deltaTime * 4f;

            float offset = MathUtils.sin(bob.stateTime);

            position.z += offset - bob.previousOffset;

            bob.previousOffset = offset;
        }
    }
}
