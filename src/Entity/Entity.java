package Entity;
import Main.*;

import java.awt.image.BufferedImage;

public class Entity {
    //name: the name of the entity (used for display)
    public String name;

    public BufferedImage image;

    //x, y: 2D coordinate location
    public float x, y;

    //targetX, targetY: the 2D coordinate to travel towards
    public float targetX, targetY;

    //radius: render/collision radius TODO: COLLISION
    public int radius;

    //moveSpeed: distance traveled per update() call (per frame)
    public int moveSpeed;

    public Entity() {
        x = 100;
        y = 100;
    }
    public Entity(String name, int radius, int moveSpeed) {
        this();

        this.name = name;
        this.radius = radius;
        this.moveSpeed = moveSpeed;

        this.targetX = x;
        this.targetY = y;
    }

    public void setImage(){

    }

    public void moveTo(int X, int Y) {
        targetX = X;
        targetY = Y;
    }

    public void update() {
        float deltaX = targetX - x;
        float deltaY = targetY - y;
        if (deltaX != 0 || deltaY != 0) {

            double vectorMagnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //if close enough to where one more step overshoots,
            if (vectorMagnitude < moveSpeed) {
                //snap to the target lol
                x = targetX;
                y = targetY;
            } else {
                double moveX = deltaX / vectorMagnitude;
                double moveY = deltaY / vectorMagnitude;

                x += moveSpeed * moveX;
                y += moveSpeed * moveY;
            }
        }
    }
}