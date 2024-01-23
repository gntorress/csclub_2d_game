package Entity;
import Main.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    //name: the name of the entity (used for display)
    public String name;

    //image: the sprite of the entity
    public BufferedImage image;

    //x, y: 2D coordinate location
    public float x, y;

    //targetX, targetY: the 2D coordinate to travel towards
    public float targetX, targetY;

    //size: render/collision length/width, assumed to be square TODO: COLLISION
    public int size;

    //moveSpeed: distance traveled per update() call (per frame)
    public int moveSpeed;

    //CONSTRUCTORS
    public Entity() {
        //TODO: set spawn point somewhere else
        x = 128;
        y = 128;
    }
    public Entity(String name, int size, int moveSpeed) {
        this();

        this.name = name;
        this.size = size;
        this.moveSpeed = moveSpeed;

        this.targetX = x;
        this.targetY = y;
    }

    //setImage(): takes the name of a file in the textures folder
    //and loads that into the entity's image
    public void setImage(String fileName) {
        try {
                //try to find it
            image = ImageIO.read(getClass().getResourceAsStream("/textures/" + fileName));
        }catch(IOException e){
                //if it failed, log it,
            Logger.log(1, "FAILED TO FIND IMAGE: " + fileName);
                //and then get the default image instead
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/textures/default.png"));
            } catch (IOException ex) {
                Logger.log(1, "DEFAULT IMAGE LOST, PLS FIND");
            }
        }
    }

    //moveTarget(): sets the X and Y world coordinates that the entity will move towards automatically
    public void moveTarget(float X, float Y) {
        targetX = X;
        targetY = Y;
    }
    //moveVector(): moveTarget(), but relative to the entity itself (directional/WASD controls)
    public void moveVector(float X, float Y){
        targetX = x + moveSpeed * X * 2;
        targetY = y + moveSpeed * Y * 2;
    }

    //moveDirections: turns direction booleans (from ControlHandler) into x/y for moveVector();
    public void moveDirections(boolean up, boolean left, boolean down, boolean right) {
        float x = 0, y = 0;
        if(up) y -= 1;
        if(left) x -= 1;
        if(down) y += 1;
        if(right) x += 1;
        moveVector(x,y);
    }

    //update(): called every frame, handles entity logic/movement
    public void update() {
        //deltaX, deltaY: the target to move towards, relative to the entity
        float deltaX = targetX - x;
        float deltaY = targetY - y;
        //so long as there is distance to move still,
        if (deltaX != 0 || deltaY != 0) {
            //use pythagoras to get distance to target point
            double vectorMagnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //if close enough to where one more step overshoots,
            if (vectorMagnitude < moveSpeed) {
                //snap to the target lol
                x = targetX;
                y = targetY;
            }
            //otherwise, move towards target
            //moveX, moveY: x/y values normalized (to prevent diagonal movement being ~1.4x the speed)
            double moveX = deltaX / vectorMagnitude;
            double moveY = deltaY / vectorMagnitude;

            //move according to movement speed value
            x += moveSpeed * moveX;
            y += moveSpeed * moveY;
        }
    }
}