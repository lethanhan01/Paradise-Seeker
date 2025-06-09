package com.paradise_seeker.game.map;

public class GameMap4 extends GameMap {
    public GameMap4() {
        super();
        this.mapName = "Old Castle";
    }

    @Override
    protected String getMapTmxPath() {
        return "tilemaps/TileMaps/maps/map4.tmx";
    }

    @Override
    protected String getMapBackgroundPath() {
        return "tilemaps/TileMaps/maps/map4.png";
    }
}