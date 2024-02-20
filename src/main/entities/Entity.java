package main.entities;
import main.*;
import main.world.Tile;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Entity {
    //game: the GameState object, so the entity can interact with other things
    protected static GameState game;

    //name: the name of the entity (used for display)
    public String name;

    //image: the sprite of the entity
    public BufferedImage image;

    //x, y: 2D coordinate location
    public float x, y;

    //targetX, targetY: the 2D coordinate to travel towards
    public float targetX, targetY;

    //size: render/collision length/width, assumed to be square
    public int size;

    //moveSpeed: distance traveled per update() call (per frame)
    public float moveSpeed;

    //collider: used for checking collision (the "hitbox") TODO: IMPLEMENT, USE FOR ENTITY/FIELD COLLISION
    public Rectangle2D collider;

    //hasCollision: if false, ignores collision
    public boolean hasCollision;

    //CONSTRUCTORS
    public Entity(){
        x = 0;
        y = 0;
        targetX = x;
        targetY = y;
        hasCollision = true;
    }
    public Entity(String name, int size, int moveSpeed) {
        this();

        this.name = name;
        this.size = size;
        this.moveSpeed = moveSpeed;

        collider = new Rectangle2D.Float(x, y, size, size);
    }

    //linkGameState(): links every entity globally to the GameState object
    public void linkGameState(GameState game){
        Entity.game = game;
    }

    //setImage(): takes the name of a file in the textures folder
    //and loads that into the entity's image
    public void setImage(String fileName) {
        image = FileHandler.loadImage(fileName);
    }

    //moveTarget(): sets the X and Y main.world coordinates that the entity will move towards automatically
    public void moveTarget(float X, float Y) {
        targetX = X;
        targetY = Y;
    }

    //moveVector(): similar to moveTarget(), but relative to the entity itself (directional/WASD controls)
    public void moveVector(float X, float Y){
        targetX = this.x + X;
        targetY = this.y + Y;
    }

    //update(): called every frame, handles entity logic/movement
    public void update() {
        //move
        updatePosition();

        //update collider position
        collider.setRect(x, y, this.size, this.size);
    }

    //updatePosition(): handles movement, towards targetX & targetY
    protected void updatePosition(){
        //deltaX, deltaY: the target to move towards, relative to the entity
        float deltaX = targetX - x;
        float deltaY = targetY - y;

        //so long as there is distance to move still, we move.
        if (deltaX != 0 || deltaY != 0) {
            //use pythagoras to get distance to target point
            float vectorMagnitude = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //if close enough to where one more step overshoots,
            if (vectorMagnitude < moveSpeed) {
                //snap to the target lol
                //only really used for mouse movement!
                //prevents glitchy rapid back-and-forth,
                //when at the target location but each step overshoots
                //TODO: fix clipping through walls with this
                if(canMoveHorizontal(x - targetX)) x = targetX;
                if(canMoveVertical(y - targetY)) y = targetY;
            } else {
                //otherwise, move towards target

                //moveX, moveY: x/y values normalized (to prevent diagonal movement being ~1.4x the speed)
                float moveX = (deltaX / vectorMagnitude) * moveSpeed;
                float moveY = (deltaY / vectorMagnitude) * moveSpeed;

                float distanceTraveled = 0;
                float remainingDistance = moveX;
                while(remainingDistance != 0 && distanceTraveled - moveX != 0){
                    if(canMoveHorizontal(remainingDistance)){
                        x += remainingDistance;
                        distanceTraveled += remainingDistance;
                    }
                    remainingDistance = remainingDistance / 2;
                }

                distanceTraveled = 0;
                remainingDistance = moveY;
                while(remainingDistance != 0 && distanceTraveled - moveY != 0){
                    if(canMoveVertical(remainingDistance) ){
                        y += remainingDistance;
                        distanceTraveled += remainingDistance;
                    }
                    remainingDistance = remainingDistance / 2;
                }
            }
        }

        //update collider to new position
        collider.setRect(x, y, collider.getWidth(), collider.getHeight());
    }

    //canMoveHorizontal: checks for collision and returns if we can move or not
    //parameter moveX: the X component of movement to check for
    protected boolean canMoveHorizontal(float moveX) {
        //if we have no collision, we can always move!
        if(!hasCollision) return true;

        //need to check two tiles, one for each "shoulder" of the player.
        Tile t1;
        Tile t2;

        float x = this.x;
        float y1 = this.y;
        float y2 = this.y + this.size;

        //if moveX < 0, we are moving left
        //if we are moving right, we check from the right edge
        //(this.x is the left edge)
        if (!(moveX < 0)) {
            x = x + this.size;

        }
        t1 = game.tileAt(x + moveX, y1);
        t2 = game.tileAt(x + moveX, y2);

        //we can move so long as neither "shoulder" is colliding with a tile
        //if the tiles are null, we treat them as having solid collision
        if(t1 == null || t2 == null) return false;
        boolean collide = t1.hasCollision || t2.hasCollision;
        return !collide;
    }

    //canMoveVertical: checks for collision and returns if we can move or not
    //parameter moveY: the Y component of movement to check for
    protected boolean canMoveVertical(float moveY) {
        //if we have no collision, we can always move!
        if(!hasCollision) return true;

        //need to check two tiles, one for each "shoulder" of the player.
        Tile t1;
        Tile t2;

        float y = this.y;
        float x1 = this.x;
        float x2 = this.x + this.size;

        //if moveY < 0, we are moving up
        //if we are moving down, we check from the bottom edge
        //(this.y is the top edge)
        if (!(moveY < 0)) {
            y = y + this.size;
        }
        t1 = game.tileAt(x1, y + moveY);
        t2 = game.tileAt(x2, y + moveY);

        //we can move so long as neither "shoulder" is colliding with a tile
        //if the tiles are null, we treat them as having solid collision
        if(t1 == null || t2 == null) return false;
        boolean collide = t1.hasCollision || t2.hasCollision;
        return !collide;
    }
}