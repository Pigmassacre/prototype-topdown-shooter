package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.pigmassacre.topdown.components.AnimationComponent;
import com.pigmassacre.topdown.components.VisualComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class AnimationSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(AnimationComponent.class, VisualComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        VisualComponent visual;
        AnimationComponent animation;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            visual = visualMapper.get(entity);
            animation = animationMapper.get(entity);

            animation.stateTime += deltaTime;

            switch (animation.state) {
                case STANDING_LEFT:
                    if (animation.previousState != AnimationComponent.AnimationState.STANDING_LEFT) animation.stateTime = 0f;
                    visual.image = animation.standingLeft.getKeyFrame(animation.stateTime);
                    break;
                case STANDING_RIGHT:
                    if (animation.previousState != AnimationComponent.AnimationState.STANDING_RIGHT) animation.stateTime = 0f;
                    visual.image = animation.standingRight.getKeyFrame(animation.stateTime);
                    break;
                case MOVING_LEFT:
                    if (animation.previousState != AnimationComponent.AnimationState.MOVING_LEFT) animation.stateTime = 0f;
                    visual.image = animation.movingLeft.getKeyFrame(animation.stateTime);
                    break;
                case MOVING_RIGHT:
                    if (animation.previousState != AnimationComponent.AnimationState.MOVING_RIGHT) animation.stateTime = 0f;
                    visual.image = animation.movingRight.getKeyFrame(animation.stateTime);
                    break;
            }

            animation.previousState = animation.state;
        }
    }
}
