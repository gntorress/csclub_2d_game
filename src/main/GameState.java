package main;

import main.entities.*;
import main.world.Map;
import main.world.Tile;

import java.util.ArrayList;

public class GameState {

    //controller: the input controller, same controller as the Game object
    public ControlHandler controller;

    //panel: the game screen
    public GamePanel panel;

    //cameraScale: the RENDER_SCALE from panel, used for transforming screen-space coordinates to world-space
    public int cameraScale;

    //entityArray: a list of all entities currently loaded
    public ArrayList<Entity> entityArray;

    //player: the player entity. "you"
    public Player player;

    //loadedMap: the currently loaded map, as a 2D array of Tile objects
    public Map loadedMap;

    public GameState() {
        //we cannot create the map until we read the file
        loadedMap = null;

        //load the map
        //TODO: map selection
        loadedMap = FileHandler.loadMap(Main.MAP_SELECTION);

        //create the entity array, with an initial size of 16
        //TODO: MASTER CONSTANTS CLASS
        entityArray = new ArrayList<>(16);

        //create the player
        player = new Player(
                "Player",
                16,
                4
        );

        //add the player to the array
        entityArray.add(player);

        //use the player to link all entities to this GameState object
        //so that all entities can read the state
        //this is called "global state"
        player.linkGameState(this);

        //set the player's location to the spawn point provided by the map.
        player.setLocation(loadedMap.spawnX, loadedMap.spawnY);
    }

    //update(): ran once per frame, where all the game processing happens
    public void update() {
        //update every entity loaded
        for (Entity u : entityArray) {
            u.update();
        }
    }

    //linkController(): binds the controller to the GameState object
    public void linkController(ControlHandler con) {
        controller = con;
    }

    //linkPanel(): binds the panel to the GameState object
    public void linkPanel(GamePanel pan){
        panel = pan;
        cameraScale = GamePanel.RENDER_SCALE;
        //center the camera on the player (at their spawn)
        panel.centerCamera();
    }

    //getMap(): returns the loaded map
    public Tile[][] getMapLayout() {
        return loadedMap.layout;
    }

    //getEntities(): returns the entity array
    public ArrayList<Entity> getEntities() {
        return entityArray;
    }

    //tileAt(): returns the tile at the given worldspace coordinates
    public Tile tileAt(float x, float y){
        //initialize the tile
        Tile t;

        //grab the tile coordinate by dividing by the tile size
        int tileX = (int) (x / Main.TILE_SIZE);
        int tileY = (int) (y / Main.TILE_SIZE);

        //if the pixel coordinate is negative, we have an issue with rounding,
        //which i solve by just decrementing by one.
        //this returns the correct tile
        if(x < 0) tileX--;
        if(y < 0) tileY--;

        //try to grab the tile from the 2D tile array of the map
        try{
            t = getMapLayout()[tileY][tileX];
        }
        //if out of bounds, we are trying to grab a tile outside of the map
        //so grab a null tile instead
        catch(ArrayIndexOutOfBoundsException e){
            t = null;
        }

        //return the grabbed tile
        return t;
    }
}