package World;

import Main.FileHandler;
import Main.GamePanel;
import Main.Logger;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Tile {
    //type: the integer type, to keep track of what tiles are what
    public TileType type;

    //x, y: 2D coordinates in world space
    public int x;
    public int y;

    //image: the texture of the tile
    public BufferedImage image;

    //hasCollision: whether the tile blocks entities or not
    public boolean hasCollision;

    //collider: used for collision TODO: IMPLEMENT? unused for current iteration of collision code
    public Rectangle2D collider;

    /*
    TILES:
    -1: default tile, do not use!
    0 - Grass
    1 - Rock Wall
    2 - Water
    3 - Sand
    TODO: MORE TYPES
     */

    //CONSTRUCTOR
    public Tile(TileType type, int x, int y) {
        //store values
        this.type = type;
        this.x = x;
        this.y = y;

        //init collider
        collider = new Rectangle2D.Float(x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);

        //try to get tile information based on type
        try {
            //i use type + 1 so that -1 results in a default tile,
            //and then the rest start at 0
            image = FileHandler.loadImage(type.fileName);
            hasCollision = type.hasCollision;
        }
        //if array out of bounds, it means an invalid tile type was read
        catch (ArrayIndexOutOfBoundsException e) {
            //log this
            Logger.log(1, "INVALID MAP TILE! TILE TYPE: " + type);

            //then load default values
            image = FileHandler.loadImage(TileType.DEFAULT_TILE_FILENAME);
            hasCollision = TileType.DEFAULT_COLLISION;
        }
        catch (NullPointerException e){
            //if type is null, assume a default tile is requested
            image = FileHandler.loadImage(TileType.DEFAULT_TILE_FILENAME);
            hasCollision = TileType.DEFAULT_COLLISION;
        }
    }
}