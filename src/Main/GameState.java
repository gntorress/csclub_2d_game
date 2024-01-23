package Main;

import Entity.*;
import World.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameState {

    //controller: the input controller, same controller as the Game object
    public ControlHandler controller;

    //entityArray: a list of all entities currently loaded
    public ArrayList<Entity> entityArray;

    //player: the player entity. "you"
    public Player player;

    //map: the currently loaded map, as a 2D array of Tile objects
    public Tile[][] map;

    //TODO: dynamic map sizing
    //mapHeight, mapWidth: the dimensions of the currently loaded map
    //these values are temporary! will be replaced with the dynamic map size system
    public int mapHeight = 8, mapWidth = 16;

    public GameState() {
        //create the map
        map = new Tile[mapHeight][mapWidth];

        //load the map TODO: map selection
        try {
            loadMap();
        }catch(IOException e){
            //if loading failed, we cant play!! just crash the game
            Logger.log(1, "MAP LOAD FAILURE: " + e.getMessage(), true);
        }

        //create the entity array
        entityArray = new ArrayList<>();

        //create the player
        player = new Player(
                "Player",
                16,
                8
        );

        //add the player to the array
        entityArray.add(player);
    }

    //TODO: map selection
    //TODO: dynamic map sizing (from file)
    //loadMap(): reads text files for maps and loads them into the game
    private void loadMap() throws IOException {
        //inputStream: loads our map .txt file
        InputStream inputStream = getClass().getResourceAsStream("/world/maps/default.txt");

        //bufferedReader: reads the file loaded by inputStream
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        //for each line in the file
        for(int i = 0; i < mapHeight; i++){
            //grab the line
            String line = bufferedReader.readLine();

            //iterate through each character in the line
            //and load the appropriate tile into the 2D array
            for(int j = 0; j < mapWidth; j++){

                try {
                    //hacky trick to turn character into number!

                    int tileValue = line.charAt(j) - '0';
                    map[i][j] = new Tile(tileValue);

                    //since chars are stored as number values,
                    //subtracting the value of the char '0'
                    //from a number char
                    //returns that number!
                    //specifically, the char '0' has the value 48
                    //and '1' has the value 49,
                    //and so on! (ASCII)
                }catch(ArrayIndexOutOfBoundsException e){
                    //if we run out of space, the file is invalid!
                    throw new IOException("Invalid map size!");
                }
            }
        }
    }

    //linkController(): binds the controller to the GameState object
    public void linkController(ControlHandler con) {
        controller = con;
    }

    //update(): ran once per frame, where all the game processing happens
    public void update() {
        //take user input to move player
        if (Main.CONTROL_TYPE == 1) {
            //MOUSE CONTROLS! player follows mouse clicks/drags
            if(controller.isLeftClick) {
                player.moveTarget(controller.mouseX, controller.mouseY);
            }
        }
        else if(Main.CONTROL_TYPE == 0) {
            //WASD CONTROLS! move with WASD like arrow keys
            player.moveDirections(controller.up, controller.left, controller.down, controller.right);
        }

        //update every entity loaded
        for (Entity u : entityArray) {
            u.update();
        }
    }

    //getMap(): returns the loaded map
    public Tile[][] getMap() {
        return map;
    }

    //getEntities(): returns the entity array
    public ArrayList<Entity> getEntities() {
        return entityArray;
    }
}