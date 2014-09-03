package com.pigmassacre.topdown.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.pigmassacre.topdown.Level;
import com.pigmassacre.topdown.components.CameraFocusComponent;
import com.pigmassacre.topdown.components.CircleCollisionComponent;
import com.pigmassacre.topdown.components.PositionComponent;
import com.pigmassacre.topdown.components.RectangleCollisionComponent;

/**
 * Created by pigmassacre on 2014-08-27.
 */
public class CameraSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RectangleCollisionComponent> collisionMapper = ComponentMapper.getFor(RectangleCollisionComponent.class);

    public CameraSystem(OrthographicCamera camera) {
        super();
        this.camera = camera;
    }

    public CameraSystem(int priority, OrthographicCamera camera) {
        super(priority);
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(CameraFocusComponent.class, PositionComponent.class), ComponentType.getBitsFor(RectangleCollisionComponent.class, CircleCollisionComponent.class), new Bits()));
    }

    private static final float minimumXSpace = 64f;
    private static final float minimumYSpace = 64f;

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        RectangleCollisionComponent collision;

        Array<Entity> focusedEntities = new Array<Entity>();

        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            position = positionMapper.get(entity);
            collision = collisionMapper.get(entity);

            if (position.x < minX) minX = position.x;
            if (position.y < minY) minY = position.y;
            if (position.x + collision.rectangle.width > maxX) maxX = position.x + collision.rectangle.width;
            if (position.y + collision.rectangle.height > maxY) maxY = position.y + collision.rectangle.height;
        }

        minX -= minimumXSpace;
        minY -= minimumYSpace;
        maxX += minimumXSpace;
        maxY += minimumYSpace;

        if (minX < 0) minX = 0;
        if (minY < 0) minY = 0;
        if (maxX > Level.getMapWidth()) maxX = Level.getMapWidth();
        if (maxY > Level.getMapHeight()) maxY = Level.getMapHeight();

        float width = maxX - minX;
        float height = maxY - minY;

        camera.position.set(minX + (width / 2f), minY + (height / 2f), 0);

        float xZoom = width / camera.viewportWidth;
        float yZoom = height / camera.viewportHeight;

        camera.zoom = MathUtils.clamp(Math.max(xZoom, yZoom), 0.5f, Level.getMapWidth() / camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, Level.getMapWidth() - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, Level.getMapHeight() - effectiveViewportHeight / 2f);

        camera.update();
    }
}
