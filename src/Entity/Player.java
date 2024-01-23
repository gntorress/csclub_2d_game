package Entity;

public class Player extends Entity{

    //TODO: GAMEPLAY, MORE ENTITY TYPES
    //this will hold player-exclusive stuff

    public Player() {
        super();
        setImage("player.png");
    }
    public Player(String name, int radius, int moveSpeed) {
        super(name, radius, moveSpeed);
        setImage("player.png");
    }
}
