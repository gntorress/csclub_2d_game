package Main;

import Entity.*;
import World.Tile;
import World.TileType;

import java.io.IOException;
import java.util.ArrayList;

public class GameState {

    //controller: the input controller, same controller as the Game object
    public ControlHandler controller;

    //panel: the game screen
    public GamePanel panel;

    //entityArray: a list of all entities currently loaded
    public ArrayList<Entity> entityArray;

    //player: the player entity. "you"
    public Player player;

    //loadedMap: the currently loaded map, as a 2D array of Tile objects
    public Tile[][] loadedMap;

    //loadedTiles: the currently loaded tile information for the map, used when loading
    public TileType[] loadedTiles;

    //TODO: dynamic map sizing
    //mapHeight, mapWidth: the dimensions of the currently loaded map
    //these values are temporary! will be replaced with the dynamic map size system
    public int mapHeight = -1, mapWidth = -1;

    public GameState() {
        //we cannot create the map until we read the file
        loadedMap = null;

        //load the map TODO: map selection
        loadedMap = FileHandler.loadMap("default");

        //create the entity array
        entityArray = new ArrayList<>();

        //create the player
        player = new Player(
                "Player",
                16,
                4
        );
        player.linkGameState(this);

        //add the player to the array
        entityArray.add(player);
    }

    //TODO: map selection
    //loadMap(): reads text files for maps and loads them into the game

    private void invalidMap(){
        Logger.log(2, "MAP FILE INVALID", true);
    }
    private void validateMap(){
        for(Tile[] row : loadedMap){
            for(Tile tile : row){
                if(tile == null) invalidMap();
            }
        }
    }

    //linkController(): binds the controller to the GameState object
    public void linkController(ControlHandler con) {
        controller = con;
    }

    //linkPanel(): binds the panel to the GameState object
    public void linkPanel(GamePanel pan){
        panel = pan;
    }

    //update(): ran once per frame, where all the game processing happens
    public void update() {
        //update every entity loaded
        for (Entity u : entityArray) {
            u.update();
        }
    }

    //getMap(): returns the loaded map
    public Tile[][] getMap() {
        return loadedMap;
    }

    //getEntities(): returns the entity array
    public ArrayList<Entity> getEntities() {
        return entityArray;
    }

    //tileAt(): returns the tile at the given worldspace coordinates
    public Tile tileAt(float x, float y){
        Tile t;
        int tileX = (int) ((x) / (GamePanel.TILE_SIZE));
        int tileY = (int) ((y) / (GamePanel.TILE_SIZE));
        try{
            t = loadedMap[tileY][tileX];
        }catch(ArrayIndexOutOfBoundsException e){
            t = null;
        }

        return t;
    }
}