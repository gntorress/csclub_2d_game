package Entity;

import Main.GameState;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Player extends Entity{

    //TODO: GAMEPLAY

    public Player() throws IOException {
        super();
        image = ImageIO.read(getClass().getResourceAsStream("/textures/entities/player.png"));
    }
    public Player(String name, int radius, int moveSpeed) {
        super(name, radius, moveSpeed);
    }
}
