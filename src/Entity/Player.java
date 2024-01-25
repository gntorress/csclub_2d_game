package Entity;

import Main.GameState;
import Main.Main;

public class Player extends Entity{

    //TODO: GAMEPLAY, MORE ENTITY TYPES
    //this will hold player-exclusive stuff

    public Player(GameState game) {
        super(game);
        setImage("player.png");
    }
    public Player(GameState game, String name, int size, int moveSpeed) {
        super(game, name, size, moveSpeed);
        setImage("player.png");
    }

    public void update(){
        //CONTROL_TYPE == 0 : WASD controls
        if(Main.CONTROL_TYPE == 0){
            float moveX = 0;
            float moveY = 0;
            float diagFactor = (float) (Math.sqrt(2)/2);

            if(game.controller.up) moveY -= moveSpeed;
            if(game.controller.left) moveX -= moveSpeed;
            if(game.controller.down) moveY += moveSpeed;
            if(game.controller.right) moveX += moveSpeed;

            if(moveX != 0 && moveY != 0){
                moveX *= diagFactor;
                moveY *= diagFactor;
            }
            //TODO: sub-movespeed movement close to walls
            if(canMoveHorizontal(moveX)) x += moveX;
            if(canMoveVertical(moveY)) y += moveY;
        }
        //CONTROL_TYPE == 1 : mouse controls
        else if(Main.CONTROL_TYPE == 1) {

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
    }
}
