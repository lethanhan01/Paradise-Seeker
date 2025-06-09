package com.paradise_seeker.game.map;

public class GameMap1 extends GameMap {
    public GameMap1() {
        super();
        this.mapName = "Forgotten Ruins";
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map1.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map1.png";
    }
}
