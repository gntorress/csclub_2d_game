package Main;

public class Main {
    //TITLE: the name of the game, shown in the bar at the top of the window
    public static final String TITLE = "GRID_GAME_TEST INDEV";

    //TARGET_FPS: the fps to try to render at
    public static final int TARGET_FPS = 60;

    /*CONTROL_TYPE: the control scheme.
        0: WASD movement
        1: MOUSE CLICK movement
    */
    public static final int CONTROL_TYPE = 0;
    public static final boolean DEBUG_HITBOXES = false;
    public static final boolean DEBUG_NOCOLLIDE = false;
    public static final boolean DEBUG_PRINT_KEYCODES = false;

    public static void main(String[] args) {
        //create game object
        Game game = new Game();
        //start it
        game.start();
    }
}
