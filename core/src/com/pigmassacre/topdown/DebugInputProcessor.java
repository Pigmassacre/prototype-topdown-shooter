package com.pigmassacre.topdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class DebugInputProcessor extends InputAdapter {
    private OrthographicCamera camera;

    public DebugInputProcessor(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log(toString(), Input.Keys.toString(keycode) + " pressed!");
        switch (keycode) {
            case Input.Keys.W:
                camera.translate(0, 32);
                break;
            case Input.Keys.S:
                camera.translate(0, -32);
                break;
            case Input.Keys.A:
                camera.translate(-32, 0);
                break;
            case Input.Keys.D:
                camera.translate(32, 0);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.log(toString(), Input.Keys.toString(keycode) + " released!");
        switch (keycode) {

        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom += amount / 32f;
        return false;
    }
}
