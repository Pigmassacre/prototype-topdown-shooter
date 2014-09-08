package com.pigmassacre.topdown.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.*;

/**
 * Created by pigmassacre on 2014-09-06.
 */
public class PlayerCharacter {

    public static void createPlayerCharacter(PooledEngine engine) {
        Entity entity = engine.createEntity();

        Array<RectangleMapObject> objects = Level.getMap().getLayers().get("spawn").getObjects().getByType(RectangleMapObject.class);
        Rectangle rectangle = objects.get(MathUtils.random(objects.size - 1)).getRectangle();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.init(rectangle.x, rectangle.y, 0);
        entity.add(position);

        VisualComponent visualComponent = engine.createComponent(VisualComponent.class);
        visualComponent.init(new TextureRegion(new Texture(Gdx.files.internal("player.png"))));
        entity.add(visualComponent);

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

        engine.addEntity(entity);
    }

}
