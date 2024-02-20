package main.world;

import main.FileHandler;

public class TileType {
    //TileType objects hold information about currently loaded tiles.
    //these objects are exclusively used when loading maps
    public static final String DEFAULT_TILE_FILENAME = FileHandler.DEFAULT_TEXTURE;
    public static final boolean DEFAULT_COLLISION = true;
    public String fileName;
    public boolean hasCollision;

    public TileType(){
        fileName = DEFAULT_TILE_FILENAME;
        hasCollision = DEFAULT_COLLISION;
    }

    public TileType(String fileName, boolean hasCollision){
        this.fileName = fileName;
        this.hasCollision = hasCollision;
    }

}
