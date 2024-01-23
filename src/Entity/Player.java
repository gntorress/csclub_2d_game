package Entity;

import Main.GameState;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Player extends Entity{

    //TODO: GAMEPLAY

    public Player() {
        super();
        setImage("player.png");
    }
    public Player(String name, int radius, int moveSpeed) {
        super(name, radius, moveSpeed);
        setImage("player.png");
    }
}
