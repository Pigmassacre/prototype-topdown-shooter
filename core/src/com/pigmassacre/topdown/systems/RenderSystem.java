package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.VisualComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private SpriteBatch batch;
    private MapRenderer mapRenderer;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);

    public RenderSystem(OrthographicCamera camera) {
        super();
        this.batch = new SpriteBatch();
        this.mapRenderer = new OrthogonalTiledMapRenderer(Level.getMap(), batch);
        this.camera = camera;
    }

    public RenderSystem(int priority, OrthographicCamera camera) {
        super(priority);
        this.batch = new SpriteBatch();
        this.mapRenderer = new OrthogonalTiledMapRenderer(Level.getMap(), batch);
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VisualComponent.class));
    }


    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        VisualComponent visual;

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            position = positionMapper.get(entity);
            visual = visualMapper.get(entity);

            batch.draw(visual.image, position.x, position.y + position.z);
        }

        batch.end();
    }
}
