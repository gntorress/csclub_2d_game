package Main;

import World.Tile;
import World.TileType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHandler {
    //TEXTURES_FOLDER: what folder to look into for textures, in /src/
    public static final String TEXTURES_FOLDER = "/textures/";

    //TEXTURE_FILE_EXTENSION: self explanatory lol
    public static final String TEXTURE_FILE_EXTENSION = ".png";

    //MAP_FILE_EXTENSION: self explanatory lol
    public static final String MAP_FILE_EXTENSION = ".txt";

    //DEFAULT_TEXTURE: the name of the default fallback texture file, if another texture is missing
    public static final String DEFAULT_TEXTURE = "default";

    //PLAYER_TEXTURE: the name of the player texture file
    public static final String PLAYER_TEXTURE = "player";

    //loadImage(): takes in a file name (without extension)
    //returns a BufferedImage object of that file, for use in rendering
    public static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(FileHandler.class.getResourceAsStream(TEXTURES_FOLDER + fileName + TEXTURE_FILE_EXTENSION));
        } catch (IOException | IllegalArgumentException e) {
            Logger.log(1, "IMAGE: " + fileName + " NOT FOUND");
            try {
                image = ImageIO.read(FileHandler.class.getResourceAsStream(TEXTURES_FOLDER + DEFAULT_TEXTURE + TEXTURE_FILE_EXTENSION));
            } catch (IOException | IllegalArgumentException f) {
                Logger.log(2, "DEFAULT IMAGE MISSING", true);
            }
        }
        return image;
    }

    //loadMap(): takes in the name of a map file (without extension)
    //returns a 2D array of tiles, to be stored in GameState
    public static Tile[][] loadMap(String mapName) {
        //initialize to invalid values
        Tile[][] outputMap = null;
        TileType[] loadedTiles = null;
        int mapWidth = -1;
        int mapHeight = -1;

        //inputStream: loads our map .txt file
        InputStream inputStream = FileHandler.class.getResourceAsStream("/world/maps/" + mapName + MAP_FILE_EXTENSION);

        //bufferedReader: reads the file loaded by inputStream
        //if the map is not found, inputStream is null, so the program crashes
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        }catch(NullPointerException e){
            Logger.log(2, "MAP NOT FOUND: " + mapName, true);
        }

        //lineNumber: keeps track of what file line we are on
        int lineNumber = 0;

        //TODO: REPLACE BIG TRY/CATCH WITH SMALLER ONES THROUGHOUT
        try {
            //while there is file left to read,
            while (bufferedReader.ready()) {
                //grab the next line
                String line = bufferedReader.readLine();
                lineNumber++;

                //this if-else block checks for the different data prefixes
                if (line.startsWith("//") || line.isBlank()) {
                    //this enables comments and ignores empty lines
                    continue;
                }
                //size: holds the size of the map
                //with the format: [width]x[height] (lowercase x)
                else if (line.startsWith("size:")) {
                    String[] widthAndHeight = line.substring(line.indexOf(':') + 1).trim().split("x");
                    mapWidth = Integer.parseInt(widthAndHeight[0].trim());
                    mapHeight = Integer.parseInt(widthAndHeight[1].trim());
                }
                //tiles: holds the individual data
                //with the format:
                //tiles: [number of tiles]
                //[tile code] - [tile name/file name] - [boolean for collision value]
                else if (line.startsWith("tiles:")) {
                    int numberOfTiles = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                    loadedTiles = new TileType[numberOfTiles];
                    for (int i = 0; i < numberOfTiles; i++) {
                        line = bufferedReader.readLine();
                        lineNumber++;
                        String[] tileData = line.split("-");
                        TileType tileToLoad = new TileType(tileData[1].trim(), Boolean.parseBoolean(tileData[2].trim()));
                        loadedTiles[Integer.parseInt(tileData[0].trim())] = tileToLoad;
                    }
                }
                //layout: holds the 2D tile array data
                else if (line.startsWith("layout:")) {
                    //initialize the 2D array with the width and height
                    try {
                        outputMap = new Tile[mapHeight][mapWidth];
                    } catch (NegativeArraySizeException e) {
                        //if the size wasnt gathered, the values are still negative one,
                        //so a NegativeArraySizeException is thrown, and there is
                        //cannot load without this, so crash
                        Logger.log(2, "MAP SIZE MISSING BEFORE LAYOUT", true);
                    }

                    //loop through the tile data lines
                    for (int i = 0; i < mapHeight; i++) {

                        //grab the line
                        line = bufferedReader.readLine();
                        lineNumber++;

                        //split it by spaces
                        String[] row = line.split(" ");

                        //iterate through each integer in the line
                        //and load the appropriate tile into the 2D array
                        for (int j = 0; j < mapWidth; j++) {
                            //initialize tile value to -1 for default tile
                            int tileValue = -1;

                            //try to grab the next tile value
                            try {
                                tileValue = Integer.parseInt(row[j]);
                            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                                //if invalid data, log it and continue
                                Logger.log(2, "MAP DATA INVALID AT POSITION " + j + "," + i);
                            }

                            //calculate the X and Y coordinates of the tile
                            int x = j * GamePanel.TILE_SIZE;
                            int y = i * GamePanel.TILE_SIZE;

                            //if loadedTiles is null, the tile information wasnt found
                            //cannot load without this, so crash
                            if(loadedTiles == null) Logger.log(2,"TILE INFO MISSING BEFORE LAYOUT", true);

                            //if tile value and position is valid, we create and store the tile object
                            assert loadedTiles != null;
                            outputMap[i][j] = new Tile(loadedTiles[tileValue], x, y);
                        }
                    }
                }
                //if no other command is met, we have a line that we do not understand
                //log this
                else{
                    Logger.log(1,"INVALID MAP LINE AT LINE " + lineNumber);
                }
            }
        }
        //GENERIC IOException CATCH, WILL REPLACE
        catch(IOException e){
            Logger.log(2, "ERROR IN MAP FILE", true);
        }

        //return the loaded map :)
        return outputMap;
    }
}