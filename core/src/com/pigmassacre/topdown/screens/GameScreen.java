package com.pigmassacre.topdown.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.pigmassacre.topdown.DebugInputProcessor;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.PlayerInputProcessor;
import com.pigmassacre.topdown.Topdown;
import com.pigmassacre.topdown.components.*;
import com.pigmassacre.topdown.systems.*;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class GameScreen extends AbstractScreen {

    public GameScreen(Topdown game) {
        super(game);

        getGame().inputMultiplexer = new InputMultiplexer();
        getGame().inputMultiplexer.addProcessor(new DebugInputProcessor(getCamera()));
        getGame().inputMultiplexer.addProcessor(new PlayerInputProcessor(getEngine()));
        Gdx.input.setInputProcessor(getGame().inputMultiplexer);

        Level.map = new TmxMapLoader().load("maps/test.tmx");

        getEngine().addSystem(new PlayerControlledSystem());
        getEngine().addSystem(new AccelerationSystem());
        getEngine().addSystem(new DecelerationSystem());
        getEngine().addSystem(new MovementSystem());
        getEngine().addSystem(new GravitySystem());
        getEngine().addSystem(new CollisionSystem());
        getEngine().addSystem(new MapCollisionSystem());
        getEngine().addSystem(new RenderSystem(getCamera()));
        getEngine().addSystem(new DebugRenderSystem(getCamera()));

        createTestEntity();
    }

    private void createTestEntity() {
        Entity entity = getEngine().createEntity();

        Array<RectangleMapObject> objects = Level.map.getLayers().get("Spawn Positions").getObjects().getByType(RectangleMapObject.class);
        Rectangle rectangle = objects.get(MathUtils.random(objects.size - 1)).getRectangle();

        PositionComponent position = getEngine().createComponent(PositionComponent.class);
        position.init(rectangle.x, rectangle.y, 0);
        entity.add(position);

        RectangleCollisionComponent collision = getEngine().createComponent(RectangleCollisionComponent.class);
        collision.init(32f, 32f);
        entity.add(collision);

        getCamera().position.set(rectangle.x, rectangle.y, 0);

        VelocityComponent velocity = getEngine().createComponent(VelocityComponent.class);
        entity.add(velocity);

        entity.add(getEngine().createComponent(AccelerationComponent.class));

        entity.add(getEngine().createComponent(DecelerationComponent.class));

        PlayerControlledComponent pc = getEngine().createComponent(PlayerControlledComponent.class);
        pc.init(1);
        entity.add(pc);

        entity.add(getEngine().createComponent(GravityComponent.class));

        entity.add(getEngine().createComponent(MapCollisionComponent.class));

        getEngine().addEntity(entity);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getEngine().update(delta);
    }
}
