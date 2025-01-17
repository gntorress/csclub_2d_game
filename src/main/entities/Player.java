package main.entities;

import main.*;

public class Player extends Entity{

    //TODO: GAMEPLAY, MORE ENTITY TYPES
    //this will hold player-exclusive stuff

    public Player() {
        super();
        this.initDefaults();
    }
    public Player(String name, int size, int moveSpeed) {
        super(name, size, moveSpeed);
        this.initDefaults();
    }
    protected void initDefaults(){
        if(Main.DEBUG_NOCOLLIDE) this.hasCollision = false;
        setDirectionalImages(FileHandler.PLAYER_TEXTURE);
    }

    public void update(){
        handleControls(game.controller);
        super.update();
    }

    private void handleControls(ControlHandler controller){
        //take user input to move player
        if (Main.CONTROL_TYPE == 1) {
            //MOUSE CONTROLS! player follows mouse clicks/drags
            if(controller.isLeftClick) {
                int targetX = (controller.mouseX + game.panel.cameraX) / game.cameraScale - this.size/2;
                int targetY = (controller.mouseY + game.panel.cameraY) / game.cameraScale - this.size/2;
                this.moveTarget(targetX, targetY);
            }
        }
        else {
            float moveX = 0;
            float moveY = 0;

            if (controller.up) moveY -= moveSpeed;
            if (controller.left) moveX -= moveSpeed;
            if (controller.down) moveY += moveSpeed;
            if (controller.right) moveX += moveSpeed;

            //TODO: sub-movespeed movement close to walls
            this.moveVector(moveX, moveY);
        }
    }
}
