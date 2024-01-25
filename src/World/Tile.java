package World;

import Main.GamePanel;
import Main.Logger;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile {
    //image: the texture of the tile
    public BufferedImage image;

    //hasCollision: whether the tile blocks entities or not
    public boolean hasCollision;

    //collider: used for collision TODO: IMPLEMENT
    public Rectangle2D collider;

    /*
    TILES:
    0 - Grass
    1 - Rock Wall
    2 - Water
    3 - Sand
    TODO: MORE TYPES
     */
    public Tile(int type, int x, int y) throws IOException {
        collider = new Rectangle2D.Float(x,y, GamePanel.TILE_SIZE * GamePanel.RENDER_SCALE, GamePanel.TILE_SIZE * GamePanel.RENDER_SCALE);
        switch (type) {
            case 0:
                image = getImage("grass.png");
                hasCollision = false;
                break;
            case 1:
                image = getImage("rock.png");
                hasCollision = true;
                break;
            case 2:
                image = getImage("water.png");
                hasCollision = true;
                break;
            case 3:
                image = getImage("sand.png");
                hasCollision = false;
                break;
            default:
                Logger.log(1, "INVALID MAP TILE! VALUE: " + type);
                image = getImage("rock.png");
                hasCollision = true;
        }
    }
    private BufferedImage getImage(String fileName) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream("/textures/" + fileName));
    }
}