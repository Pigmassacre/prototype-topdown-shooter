package com.pigmassacre.topdown;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.pigmassacre.topdown.screens.GameScreen;

public class Topdown extends Game {

    public static final String LOG = "Topdown";

    public PooledEngine engine;

    public InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        engine = new PooledEngine();
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(LOG, "Resizing game to: " + width + " x " + height);
        super.resize(width, height);
    }

    @Override
    public void pause() {
        Gdx.app.log(LOG, "Pausing game");
        super.pause();
    }

    @Override
    public void resume() {
        Gdx.app.log(LOG, "Resuming game");
        super.resume();
    }

    @Override
    public void dispose() {
        Gdx.app.log(LOG, "Disposing game");
        super.dispose();
    }

}
