package com.pigmassacre.topdown.screens;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.pigmassacre.topdown.DebugInputProcessor;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.PlayerInputProcessor;
import com.pigmassacre.topdown.Topdown;
import com.pigmassacre.topdown.components.weapons.HolsterComponent;
import com.pigmassacre.topdown.entities.PlayerCharacter;
import com.pigmassacre.topdown.entities.Weapons;
import com.pigmassacre.topdown.systems.*;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class GameScreen extends AbstractScreen {

    public GameScreen(Topdown game) {
        super(game);

        getGame().inputMultiplexer = new InputMultiplexer();
        getGame().inputMultiplexer.addProcessor(new DebugInputProcessor(getEngine()));
        getGame().inputMultiplexer.addProcessor(new PlayerInputProcessor(getEngine()));
        Gdx.input.setInputProcessor(getGame().inputMultiplexer);

        Level.loadMap("maps/test2.tmx");

        getEngine().addSystem(new PlayerControlledSystem());
        getEngine().addSystem(new AccelerationSystem());
        getEngine().addSystem(new DecelerationSystem());
        getEngine().addSystem(new CollisionSystem());
        getEngine().addSystem(new MovementSystem());
        getEngine().addSystem(new GravitySystem());
        getEngine().addSystem(new MapCollisionSystem());
        getEngine().addSystem(new EntityCollisionSystem());
        getEngine().addSystem(new StickToMapSystem());
        getEngine().addSystem(new BounceSystem());
        getEngine().addSystem(new RotationSystem());
        getEngine().addSystem(new RemovalSystem());
        getEngine().addSystem(new CameraSystem(getCamera()));
        getEngine().addSystem(new RenderSystem(getCamera()));
        //getEngine().addSystem(new DebugRenderSystem(getCamera()));

        for (int i = 0; i < 1; i++) {
            PlayerCharacter.createPlayerCharacter(getEngine());
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getEngine().update(delta);
    }
}
