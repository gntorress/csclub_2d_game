package Main;

import Entity.*;
import World.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameState {
    public ControlHandler controller;
    public ArrayList<Entity> entityArray;
    public Player player;
    public Tile[][] map;

    //TODO: dynamic map sizing
    public int mapHeight = 8;
    public int mapWidth = 16;

    public GameState() {
        map = new Tile[mapHeight][mapWidth];

        try {
            loadMap();
        }catch(IOException e){
            Logger.log(1, "MAP LOAD FAILURE");
        }

        entityArray = new ArrayList<>();
        player = new Player(
                "Player",
                16,
                8
        );
        entityArray.add(player);
    }

    //TODO: map selection
    //TODO: dynamic map sizing (from file)
    private void loadMap() throws IOException {
        InputStream in = getClass().getResourceAsStream("/world/maps/default.txt");
        BufferedReader read = new BufferedReader(new InputStreamReader(in));

        for(int i = 0; i < mapHeight; i++){
            String line = read.readLine();
            for(int j = 0; j < mapWidth; j++){
                //hacky trick to turn character into number!
                //since chars are stored as number values,
                //subtracting the value of the char '0'
                //from a number char
                //returns that number!
                //specifically, the char '0' has the value 48
                //and '1' has the value 49,
                //and so on! (ASCII)
                map[i][j] = new Tile(line.charAt(j) - '0');
            }
        }
    }

    public void linkController(ControlHandler con) {
        controller = con;
    }

    public void update() {
        for (Entity u : entityArray) {
            u.update();
        }
        if (controller.isLeftClick) {
            player.moveTo(controller.mouseX, controller.mouseY);
        }
    }

    public Tile[][] getMap() {
        return map;
    }

    public ArrayList<Entity> getUnits() {
        return entityArray;
    }
}