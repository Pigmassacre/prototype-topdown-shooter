package com.pigmassacre.topdown;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.pigmassacre.topdown.components.movement.PositionComponent;
import com.pigmassacre.topdown.systems.RenderSystem;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

/**
 * This tiled map renderer can be used to produce a perspective effect. For this we another method called renderPerspective
 * should be used after {@link #render()} and avatar renderer to draw perspective tiles.
 */
public class PerspectiveTiledMapRenderer extends OrthogonalTiledMapRenderer {
    private final MapLayer perspectiveLayer;

    public PerspectiveTiledMapRenderer(String perspectiveLayerName, TiledMap map, SpriteBatch batch) {
        super(map, batch);
        this.perspectiveLayer = map.getLayers().get(perspectiveLayerName);
    }

    public PerspectiveTiledMapRenderer(String perspectiveLayerName, TiledMap map, float unitScale, SpriteBatch batch) {
        super(map, unitScale, batch);
        this.perspectiveLayer = map.getLayers().get(perspectiveLayerName);
    }

    @Override
    public void render() {
        beginRender();
        for (MapLayer layer : map.getLayers()) {
            if (layer!=perspectiveLayer && layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                } else {
                    for (MapObject object : layer.getObjects()) {
                        renderObject(object);
                    }
                }
            }
        }
        endRender();
    }

    public void renderPerspective(Array<Entity> entities, RenderSystem.RenderMethod renderMethod) {
        if (perspectiveLayer instanceof TiledMapTileLayer) {
            renderPerspectiveTileLayer((TiledMapTileLayer) perspectiveLayer, entities, renderMethod);
        } else {
            for (MapObject object : perspectiveLayer.getObjects()) {
                renderObject(object);
            }
        }
    }

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);

    public void renderPerspectiveTileLayer(TiledMapTileLayer layer, Array<Entity> entities, RenderSystem.RenderMethod renderMethod) {
        final Color batchColor = spriteBatch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * unitScale;
        final float layerTileHeight = layer.getTileHeight() * unitScale;

        final int col1 = Math.max(0, (int)(viewBounds.x / layerTileWidth));
        final int col2 = Math.min(layerWidth, (int)((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth));

        final int row1 = Math.max(0, (int)(viewBounds.y / layerTileHeight));
        final int row2 = Math.min(layerHeight, (int)((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight));

        float y = row2 * layerTileHeight;
        float xStart = col1 * layerTileWidth;
        final float[] vertices = this.vertices;

        for (int row = row2; row >= row1; row--) {
            float x = xStart;
            for (int col = col1; col < col2; col++) {
                final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) {
                    x += layerTileWidth;
                    continue;
                }
                final TiledMapTile tile = cell.getTile();

                if (tile != null) {
                    final boolean flipX = cell.getFlipHorizontally();
                    final boolean flipY = cell.getFlipVertically();
                    final int rotations = cell.getRotation();

                    TextureRegion region = tile.getTextureRegion();

                    float x1 = x + tile.getOffsetX() * unitScale;
                    float y1 = y + tile.getOffsetY() * unitScale;
                    float x2 = x1 + region.getRegionWidth() * unitScale;
                    float y2 = y1 + region.getRegionHeight() * unitScale;

                    float u1 = region.getU();
                    float v1 = region.getV2();
                    float u2 = region.getU2();
                    float v2 = region.getV();

                    vertices[X1] = x1;
                    vertices[Y1] = y1;
                    vertices[C1] = color;
                    vertices[U1] = u1;
                    vertices[V1] = v1;

                    vertices[X2] = x1;
                    vertices[Y2] = y2;
                    vertices[C2] = color;
                    vertices[U2] = u1;
                    vertices[V2] = v2;

                    vertices[X3] = x2;
                    vertices[Y3] = y2;
                    vertices[C3] = color;
                    vertices[U3] = u2;
                    vertices[V3] = v2;

                    vertices[X4] = x2;
                    vertices[Y4] = y1;
                    vertices[C4] = color;
                    vertices[U4] = u2;
                    vertices[V4] = v1;

                    if (flipX) {
                        float temp = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = temp;
                        temp = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = temp;
                    }
                    if (flipY) {
                        float temp = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = temp;
                        temp = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = temp;
                    }
                    if (rotations != 0) {
                        switch (rotations) {
                            case TiledMapTileLayer.Cell.ROTATE_90: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V2];
                                vertices[V2] = vertices[V3];
                                vertices[V3] = vertices[V4];
                                vertices[V4] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U2];
                                vertices[U2] = vertices[U3];
                                vertices[U3] = vertices[U4];
                                vertices[U4] = tempU;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_180: {
                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U3];
                                vertices[U3] = tempU;
                                tempU = vertices[U2];
                                vertices[U2] = vertices[U4];
                                vertices[U4] = tempU;
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V3];
                                vertices[V3] = tempV;
                                tempV = vertices[V2];
                                vertices[V2] = vertices[V4];
                                vertices[V4] = tempV;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_270: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V4];
                                vertices[V4] = vertices[V3];
                                vertices[V3] = vertices[V2];
                                vertices[V2] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U4];
                                vertices[U4] = vertices[U3];
                                vertices[U3] = vertices[U2];
                                vertices[U2] = tempU;
                                break;
                            }
                        }
                    }
                    spriteBatch.draw(region.getTexture(), vertices, 0, 20);
                }
                x += layerTileWidth;
            }

            float nextY = y - layerTileHeight;
            for (int i = 0; i < entities.size; i++) {
                Entity entity = entities.get(i);

                PositionComponent position = positionMapper.get(entity);

                if (position.y < y && position.y >= nextY) {
                    renderMethod.render(entity);
                }
            }
            y -= layerTileHeight;
        }
    }
}
