package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.PerspectiveTiledMapRenderer;
import com.pigmassacre.topdown.components.ShadowComponent;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.components.movement.RotationComponent;
import com.pigmassacre.topdown.components.VisualComponent;

import java.util.Comparator;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> shadowEntities;
    private ImmutableArray<Entity> entities;

    private SpriteBatch batch;
    private PerspectiveTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RotationComponent> rotationMapper = ComponentMapper.getFor(RotationComponent.class);
    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<ShadowComponent> shadowMapper = ComponentMapper.getFor(ShadowComponent.class);

    public RenderSystem(OrthographicCamera camera) {
        super();
        this.batch = new SpriteBatch();
        this.mapRenderer = new PerspectiveTiledMapRenderer("walls", Level.getMap(), batch);
        this.camera = camera;
    }

    public RenderSystem(int priority, OrthographicCamera camera) {
        super(priority);
        this.batch = new SpriteBatch();
        this.mapRenderer = new PerspectiveTiledMapRenderer("walls", Level.getMap(), batch);
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        shadowEntities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VisualComponent.class, ShadowComponent.class));
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VisualComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        RotationComponent rotation;
        VisualComponent visual;
        ShadowComponent shadow;

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            position = positionMapper.get(entity);
            rotation = rotationMapper.get(entity);
            visual = visualMapper.get(entity);
            shadow = shadowMapper.get(entity);

            if (shadow != null) {
                Color temp = batch.getColor();
                batch.setColor(0f, 0f, 0f, 0.5f);
                drawShadow(position, visual, rotation);
                batch.setColor(temp);
            }
        }

        Array<Entity> sorted = sortForDrawing(entities);
        /*for (int i = 0; i < sorted.size; i++) {
            Entity entity = sorted.get(i);

            position = positionMapper.get(entity);
            rotation = rotationMapper.get(entity);
            visual = visualMapper.get(entity);

            draw(position, visual, rotation);
        }

        batch.end();*/

        mapRenderer.renderPerspective(sorted, new RenderMethod() {
            @Override
            public void render(Entity entity) {
                draw(positionMapper.get(entity), visualMapper.get(entity), rotationMapper.get(entity));
            }
        });

        batch.end();
    }

    private void drawShadow(PositionComponent position, VisualComponent visual, RotationComponent rotation) {
        if (rotation != null) {
            batch.draw(visual.image, position.x + visual.offsetX, position.y + visual.offsetY, visual.originX, visual.originY, visual.image.getRegionWidth(), visual.image.getRegionHeight(), visual.scaleX, visual.scaleY, rotation.angle * MathUtils.radiansToDegrees);
        } else {
            batch.draw(visual.image, position.x + visual.offsetX, position.y + visual.offsetY, visual.originX, visual.originY, visual.image.getRegionWidth(), visual.image.getRegionHeight(), visual.scaleX, visual.scaleY, 0f);
        }
    }

    private void draw(PositionComponent position, VisualComponent visual, RotationComponent rotation) {
        if (rotation != null) {
            batch.draw(visual.image, position.x + visual.offsetX, position.y + visual.offsetY + position.z, visual.originX, visual.originY, visual.image.getRegionWidth(), visual.image.getRegionHeight(), visual.scaleX, visual.scaleY, rotation.angle * MathUtils.radiansToDegrees);
        } else {
            batch.draw(visual.image, position.x + visual.offsetX, position.y + visual.offsetY + position.z, visual.originX, visual.originY, visual.image.getRegionWidth(), visual.image.getRegionHeight(), visual.scaleX, visual.scaleY, 0f);
        }
    }

    private Array<Entity> sortForDrawing(ImmutableArray<Entity> entitiesToSort) {
        Array<Entity> sortedEntities = new Array<Entity>(entitiesToSort.toArray());
        sortedEntities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                PositionComponent position1, position2;

                position1 = positionMapper.get(o1);
                position2 = positionMapper.get(o2);

                if (position1.y > position2.y) return -1;
                else if (position1.y < position2.y) return 1;
                return 0;
            }
        });
        return sortedEntities;
    }

    public interface RenderMethod {
        public void render(Entity entity);
    }
}
