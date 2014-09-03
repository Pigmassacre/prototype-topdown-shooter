package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.RectangleCollisionComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class DebugRenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ShapeRenderer shapeRenderer;
    private Camera camera;

    private ComponentMapper<RectangleCollisionComponent> collisionMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);

    public DebugRenderSystem(Camera camera) {
        super();
        this.shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }

    public DebugRenderSystem(int priority, Camera camera) {
        super(priority);
        this.shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, RectangleCollisionComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        RectangleCollisionComponent collision;

        camera.update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);

        MapObjects objects = Level.getMap().getLayers().get(0).getObjects();
        for (RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            shapeRenderer.rect(object.getRectangle().x, object.getRectangle().y, object.getRectangle().width, object.getRectangle().height);
        }

        for (int i = 0; i < entities.size(); i++) {
            collision = collisionMapper.get(entities.get(i));
            shapeRenderer.rect(collision.rectangle.x, collision.rectangle.y, collision.rectangle.width, collision.rectangle.height);
        }

        shapeRenderer.end();
    }
}
