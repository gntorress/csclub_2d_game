package Entity;
import Main.*;
import World.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    //size: render/collision length/width, assumed to be square TODO: COLLISION
    public int size;

    //moveSpeed: distance traveled per update() call (per frame)
    public int moveSpeed;

    //collider: used for checking collision (the "hitbox") TODO: IMPLEMENT
    public Rectangle2D collider;

    //hasCollision: if false, ignores collision
    public boolean hasCollision;

    //CONSTRUCTORS
    public Entity(GameState _game) {
        game = _game;

        //TODO: set spawn point somewhere else
        x = 128;
        y = 128;
        targetX = x;
        targetY = y;
        hasCollision = true;
    }
    public Entity(GameState game, String name, int size, int moveSpeed) {
        this(game);

        this.name = name;
        this.size = size;
        this.moveSpeed = moveSpeed * GamePanel.RENDER_SCALE;

        collider = new Rectangle2D.Float(x, y, size * GamePanel.RENDER_SCALE, size * GamePanel.RENDER_SCALE);
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
        //MOVEMENT:

        //deltaX, deltaY: the target to move towards, relative to the entity
        float deltaX = targetX - x;
        float deltaY = targetY - y;
        //so long as there is distance to move still,
        if (deltaX != 0 || deltaY != 0) {
            //use pythagoras to get distance to target point
            float vectorMagnitude = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //if close enough to where one more step overshoots,
            if (vectorMagnitude < moveSpeed) {
                //snap to the target lol
                //only really used for mouse movement!
                //prevents glitchy rapid back-and-forth,
                //when at the target location but each step overshoots
                x = targetX;
                y = targetY;
            } else {
                //otherwise, move towards target

                //moveX, moveY: x/y values normalized (to prevent diagonal movement being ~1.4x the speed)
                float moveX = (deltaX / vectorMagnitude) * moveSpeed;
                float moveY = (deltaY / vectorMagnitude) * moveSpeed;

                //TODO: COLLISION, MIGHT NEED TO REWORK MOVEMENT ENTIRELY LOL

                //if(this.canMove(moveX, moveY)) {
                //move according to movement speed value
                x += moveX;
                y += moveY;
                //}
            }
            collider.setRect(x, y, collider.getWidth(), collider.getHeight());
        }
    }

    protected boolean canMoveHorizontal(float moveX) {
        if(Main.DEBUG_NOCOLLIDE) return true;

        Tile t1;
        Tile t2;

        float x1 = this.x;
        float x2 = this.x + (size-1) * GamePanel.RENDER_SCALE;
        float y1 = this.y;
        float y2 = this.y + (size-1) * GamePanel.RENDER_SCALE;

        if(moveX < 0) {
            t1 = game.tileAt(x1 - moveSpeed, y1);
            t2 = game.tileAt(x1 - moveSpeed, y2);
        }else{
            t1 = game.tileAt(x2 + moveSpeed, y1);
            t2 = game.tileAt(x2 + moveSpeed, y2);
        }
        return (!t1.hasCollision && !t2.hasCollision) || !this.hasCollision;
    }

    protected boolean canMoveVertical(float moveY) {
        if(Main.DEBUG_NOCOLLIDE) return true;

        Tile t1;
        Tile t2;
        float x1 = this.x;
        float x2 = this.x + (size-1) * GamePanel.RENDER_SCALE;
        float y1 = this.y;
        float y2 = this.y + (size-1) * GamePanel.RENDER_SCALE;
        if(moveY < 0) {
            t1 = game.tileAt(x1, y1 - moveSpeed);
            t2 = game.tileAt(x2, y1 - moveSpeed);
        }else{
            t1 = game.tileAt(x1, y2 + moveSpeed);
            t2 = game.tileAt(x2, y2 + moveSpeed);
        }
        return (!t1.hasCollision && !t2.hasCollision) || !this.hasCollision;
    }

    protected boolean canMove(float X, float Y){
        //this does NOT WORK, IM STRUGGLING OK

        //get the tile that the entity is moving into
        float X2 = X + this.size;
        float Y2 = Y + this.size;
        Tile t = game.tileAt(this.x + X, this.y + Y);
        Tile t2 = game.tileAt(this.x + X2, this.y + Y2);

        if(t == null || t2 == null) return false;

        /*
        //move the collision box
        this.collider.setRect(this.x + x, this.y + y, this.collider.getWidth(), this.collider.getHeight());

        //check for intersection
        boolean intersection = this.collider.intersects(t.collider) || t.hasCollision;
        Logger.log(0, "INTERSECTION?: " + intersection);

        //if intersection, move the collision box back (box stays in place if movement successful
        if(intersection) this.collider.setRect(this.x, this.y, this.collider.getWidth(), this.collider.getHeight());

        //return true if no intersection
        return !intersection;
         */
        return (!t.hasCollision && !t2.hasCollision) || !this.hasCollision;
    }
    protected boolean canMove(String direction){
        Tile t1;
        Tile t2;
        float x1 = this.x;
        float x2 = this.x + (size-1) * GamePanel.RENDER_SCALE;
        float y1 = this.y;
        float y2 = this.y + (size-1) * GamePanel.RENDER_SCALE;
        switch(direction){
            case "up":
                t1 = game.tileAt(x1, y1 - moveSpeed);
                t2 = game.tileAt(x2, y1 - moveSpeed);
                break;
            case "left":
                t1 = game.tileAt(x1 - moveSpeed, y1);
                t2 = game.tileAt(x1 - moveSpeed, y2);
                break;
            case "down":
                t1 = game.tileAt(x1, y2 + moveSpeed);
                t2 = game.tileAt(x2, y2 + moveSpeed);
                break;
            case "right":
                t1 = game.tileAt(x2 + moveSpeed, y1);
                t2 = game.tileAt(x2 + moveSpeed, y2);
                break;
            default:
                return false;
        }
        if(Main.DEBUG_NOCOLLIDE) return true;
        return (!t1.hasCollision && !t2.hasCollision) || !this.hasCollision;
    }
}