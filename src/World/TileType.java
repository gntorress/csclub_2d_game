package World;

import Main.FileHandler;

public class TileType {
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
