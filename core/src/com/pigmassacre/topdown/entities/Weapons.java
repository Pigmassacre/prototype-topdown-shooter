package com.pigmassacre.topdown.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pigmassacre.topdown.components.*;
import com.pigmassacre.topdown.components.collision.EntityCollisionComponent;
import com.pigmassacre.topdown.components.collision.MapCollisionComponent;
import com.pigmassacre.topdown.components.collision.RectangleCollisionComponent;
import com.pigmassacre.topdown.components.movement.BobComponent;
import com.pigmassacre.topdown.components.movement.GravityComponent;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.components.weapons.WeaponComponent;

/**
 * Created by pigmassacre on 2014-09-06.
 */
public class Weapons {

    public static Entity createCrossbow(float x, float y, PooledEngine engine) {
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.init(x, y, 8f);
        entity.add(position);

        VisualComponent visualComponent = engine.createComponent(VisualComponent.class);
        visualComponent.image = new TextureRegion(new Texture(Gdx.files.internal("crossbow.png")));
        entity.add(visualComponent);

        entity.add(engine.createComponent(ShadowComponent.class));

        RectangleCollisionComponent collision = engine.createComponent(RectangleCollisionComponent.class);
        collision.init(visualComponent.image.getRegionWidth() * visualComponent.scaleX, visualComponent.image.getRegionHeight() * visualComponent.scaleX);
        entity.add(collision);

        //entity.add(engine.createComponent(GravityComponent.class));

        entity.add(engine.createComponent(MapCollisionComponent.class));

        entity.add(engine.createComponent(EntityCollisionComponent.class));

        entity.add(engine.createComponent(WeaponComponent.class));

        entity.add(engine.createComponent(BobComponent.class));

        engine.addEntity(entity);
        return entity;
    }

}
