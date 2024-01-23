package World;

import Main.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tile {
    //TODO: REPLACE COLOR WITH IMAGE
    public BufferedImage image;

    public boolean collision;

    /*
    TILES:
    0 - Grass
    1 - Rock Wall
    TODO: MORE TYPES
     */
    public Tile(int type) throws IOException {
        collision = false;
        switch (type) {
            case 0:
                image = getImage("grass.png");
                collision = false;
                break;
            case 1:
                image = getImage("rock.png");
                collision = true;
                break;
            default:
                Logger.log(1, "INVALID MAP TILE! VALUE: " + type);
                image = getImage("rock.png");
                collision = true;
        }
    }
    private BufferedImage getImage(String fileName) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream("/textures/" + fileName));
    }
}