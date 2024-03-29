package com.pigmassacre.topdown.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.*;
import com.pigmassacre.topdown.components.collision.MapCollisionComponent;
import com.pigmassacre.topdown.components.collision.RectangleCollisionComponent;
import com.pigmassacre.topdown.components.movement.*;
import com.pigmassacre.topdown.components.weapons.HolsterComponent;

/**
 * Created by pigmassacre on 2014-09-06.
 */
public class PlayerCharacter {

    public static Entity createPlayerCharacter(PooledEngine engine) {
        Entity entity = engine.createEntity();

        Array<RectangleMapObject> objects = Level.getMap().getLayers().get("spawn").getObjects().getByType(RectangleMapObject.class);
        Rectangle rectangle = objects.get(MathUtils.random(objects.size - 1)).getRectangle();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.init(rectangle.x, rectangle.y, 0);
        entity.add(position);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);

        TextureRegion image = new TextureRegion(new Texture(Gdx.files.internal("player_walk.png")));
        TextureRegion[][] splitSheet = image.split(11, 20);
        TextureRegion[] frames = new TextureRegion[6 * 1];
        TextureRegion[] flippedFrames = new TextureRegion[6 * 1];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                frames[index++] = splitSheet[i][j];
                TextureRegion flipped = new TextureRegion(splitSheet[i][j]);
                flipped.flip(true, false);
                flippedFrames[index - 1] = flipped;
            }
        }
        Animation animation = new Animation(0.05f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animationComponent.movingRight = animation;

        animation = new Animation(0.05f, flippedFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animationComponent.movingLeft = animation;

        TextureRegion standingLeft = new TextureRegion(new Texture(Gdx.files.internal("player.png")));
        standingLeft.flip(true, false);
        animationComponent.standingLeft = new Animation(0.1f, standingLeft);
        animationComponent.standingRight = new Animation(0.1f, new TextureRegion(new Texture(Gdx.files.internal("player.png"))));

        animationComponent.state = AnimationComponent.AnimationState.STANDING_RIGHT;

        entity.add(animationComponent);

        VisualComponent visualComponent = engine.createComponent(VisualComponent.class);
        visualComponent.image = animationComponent.standingLeft.getKeyFrame(0f);
        entity.add(visualComponent);

        entity.add(engine.createComponent(ShadowComponent.class));

        RectangleCollisionComponent collision = engine.createComponent(RectangleCollisionComponent.class);
        collision.init(visualComponent.image.getRegionWidth() * visualComponent.scaleX, visualComponent.image.getRegionWidth() * visualComponent.scaleX);
        entity.add(collision);

        VelocityComponent velocity = engine.createComponent(VelocityComponent.class);
        entity.add(velocity);

        entity.add(engine.createComponent(AccelerationComponent.class));

        entity.add(engine.createComponent(DecelerationComponent.class));

        PlayerControlledComponent pc = engine.createComponent(PlayerControlledComponent.class);
        pc.init(1);
        entity.add(pc);

        DirectionComponent directionComponent = engine.createComponent(DirectionComponent.class);
        directionComponent.x = 0f;
        directionComponent.y = -1f;
        entity.add(directionComponent);

        entity.add(engine.createComponent(GravityComponent.class));

        entity.add(engine.createComponent(MapCollisionComponent.class));

        entity.add(engine.createComponent(CameraFocusComponent.class));

        HolsterComponent holsterComponent =  engine.createComponent(HolsterComponent.class);
        entity.add(holsterComponent);

        engine.addEntity(entity);
        return entity;
    }

}
