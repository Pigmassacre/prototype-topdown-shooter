package com.pigmassacre.topdown.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pigmassacre.topdown.Topdown;

public class AbstractScreen implements Screen {
    private final Topdown game;
    private final FPSLogger fpsLogger;

    private Viewport viewport;
    private OrthographicCamera camera;

    public AbstractScreen(Topdown game) {
        this.game = game;
        fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(16 * 16, 16 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()) * 16, camera);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Topdown getGame() {
        return game;
    }

    public PooledEngine getEngine() {
        return game.engine;
    }

    @Override
    public void render(float delta) {
        fpsLogger.log();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        Gdx.app.log(Topdown.LOG, "Showing screen: " + toString());
    }

    @Override
    public void hide() {
        Gdx.app.log(Topdown.LOG, "Hiding screen: " + toString());
    }

    @Override
    public void pause() {
        Gdx.app.log(Topdown.LOG, "Pausing screen: " + toString());
    }

    @Override
    public void resume() {
        Gdx.app.log(Topdown.LOG, "Resuming screen: " + toString());
    }

    @Override
    public void dispose() {
        Gdx.app.log(Topdown.LOG, "Disposing screen: " + toString());
    }
}
