package main;

public class Main {
    //TITLE: the name of the game, shown in the bar at the top of the window
    public static final String TITLE = "GRID_GAME_TEST INDEV";

    //TARGET_FPS: the fps to try to render at
    public static final int TARGET_FPS = 60;

    /*
    CONTROL_TYPE: the control scheme.
        0: WASD movement
        1: MOUSE CLICK movement

    NOTE: MOUSE CLICK movement has less development priority right now. its only kept just because
    */
    public static final int CONTROL_TYPE = 0;

    //DEBUG_SHOW_HITBOXES: decides if hitboxes are rendered.
    //GREEN boxes have collision
    //RED boxes do not
    public static final boolean DEBUG_SHOW_HITBOXES = false;

    //DEBUG_NOCOLLIDE: decides if the player has noclip or not
    public static final boolean DEBUG_NOCOLLIDE = false;

    public static void main(String[] args) {
        //create game object
        Game game = new Game();

        //start it
        game.start();
    }
}
