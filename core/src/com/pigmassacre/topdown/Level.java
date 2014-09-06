package com.pigmassacre.topdown;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by pigmassacre on 2014-08-28.
 */
public class Level {
    private static TiledMap map;

    private static int mapTileSize;
    private static int mapWidth, mapHeight;

    public static TiledMap getMap() {
        return map;
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public static int getMapTileSize() {
        return mapTileSize;
    }

    public static void loadMap(String path) {
        map = new TmxMapLoader().load(path);

        int mapTileWidth = Level.map.getProperties().get("width", Integer.class);
        int mapTileHeight = Level.map.getProperties().get("height", Integer.class);
        int tilePixelWidth = Level.map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = Level.map.getProperties().get("tileheight", Integer.class);

        mapWidth = mapTileWidth * tilePixelWidth;
        mapHeight = mapTileHeight * tilePixelHeight;

        mapTileSize = Math.max(mapTileWidth, mapTileHeight);
    }
}
