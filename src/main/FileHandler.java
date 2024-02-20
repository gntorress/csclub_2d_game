package main;

import main.world.Map;
import main.world.Tile;
import main.world.TileType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileHandler {
    //TEXTURES_FOLDER: what folder to look into for textures, in /src/
    public static final String TEXTURES_FOLDER = "textures/";

    //MAPS_FOLDER: what folder to look into for maps, in /src/
    public static final String MAPS_FOLDER = "maps/";

    //TEXTURE_FILE_EXTENSION: file extension used for texture files
    public static final String TEXTURE_FILE_EXTENSION = ".png";

    //MAP_FILE_EXTENSION: file extension used for map data files
    public static final String MAP_FILE_EXTENSION = ".txt";

    //DEFAULT_TEXTURE: the name of the default fallback texture file, if another texture is missing
    public static final String DEFAULT_TEXTURE = "default";

    //PLAYER_TEXTURE: the name of the player texture file
    public static final String PLAYER_TEXTURE = "player";

    //loadImage(): takes in a file name (without extension)
    //returns a BufferedImage object of that file, for use in rendering
    public static BufferedImage loadImage(String fileName) {
        //init image and file input variables
        BufferedImage image = null;
        InputStream inputStream = null;

        //get the path to the file
        String filePath = TEXTURES_FOLDER + fileName + TEXTURE_FILE_EXTENSION;

        //try to read the image
        try {
            //get the input stream
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);

            //if its null, it failed to load
            if (inputStream != null) {
                //if it's not null, read the image
                image = ImageIO.read(inputStream);
            }else{
                Logger.log(2, "FAILED TO FIND IMAGE AT " + filePath);
            }
        }
        //if it fails, the image isnt found
        catch (IOException e) {
            //log this
            Logger.log(1, "IMAGE: " + fileName + " NOT FOUND");

            //then try to get the default image instead
            try {
                //get the file
                filePath = TEXTURES_FOLDER + DEFAULT_TEXTURE + TEXTURE_FILE_EXTENSION;
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);

                //check if it loaded
                assert inputStream != null;
                image = ImageIO.read(inputStream);
            }
            //if *that* fails, the default image isnt there, which is really annoying. so i crash
            catch (IOException | AssertionError f) {
                Logger.log(2, "DEFAULT IMAGE MISSING, PLS FIX", true);
            }
        }

        //return the loaded image
        return image;
    }

    //loadMap(): takes in the name of a map file (without extension)
    //returns a 2D array of tiles, to be stored in GameState
    public static Map loadMap(String fileName) {
        //initialize to invalid values, so if something is out of order, we know
        Map outputMap = new Map();
        int mapWidth = -1;
        int mapHeight = -1;

        //get the path to the map file
        String filePath = MAPS_FOLDER + fileName + MAP_FILE_EXTENSION;

        //inputStream: loads our map .txt file
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);

        //bufferedReader: reads the file
        BufferedReader bufferedReader = null;
        if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        }else{
            //if its null, we failed to find the file
            Logger.log(2, "FAILED TO FIND MAP AT " + filePath, true);
        }
        assert bufferedReader != null;

        //lines: an array of all the lines in the file
        String[] lines = bufferedReader.lines().toArray(String[]::new);

        //line: the String value read for the current line
        String line;

        //lineNumber: keeps track of what file line we are on
        int lineNumber = 0;

        int lineCount = lines.length;

        //loop until out lines in file:
        while(lineNumber < lineCount) {

            //reset line to null
            line = null;

            //grab the next current line (and increment the count)
            line = lines[lineNumber++];

            //this if-else block checks for the different data prefixes
            //if the
            if (line.startsWith("//") || line.isBlank()) {
                //this enables comments and ignores empty lines
                continue;
            }

            //size: holds the size of the map
            //with the format: [width]x[height] (lowercase x)
            else if (line.startsWith("size:")) {
                //grab the data (stored after the colon) and split it by the 'x' in the middle
                String[] widthAndHeight = line.substring(line.indexOf(':') + 1).trim().split("x");

                //store the values
                mapWidth = Integer.parseInt(widthAndHeight[0].trim());
                mapHeight = Integer.parseInt(widthAndHeight[1].trim());
            }

            //tiles: holds the individual data
            //with the format:
            //tiles: [number of tiles]
            //[tile code] - [tile name/file name] - [boolean for collision value]
            else if (line.startsWith("tiles:")) {

                //grab the number of tiles (integer after the colon)
                int numberOfTiles = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());

                //create a new array in the map object, to hold the tile types
                outputMap.tileTypes = new TileType[numberOfTiles];

                //for however many tiles we expect to see,
                for (int i = 0; i < numberOfTiles; i++) {
                    //grab the next current line (and increment the count)
                    line = lines[lineNumber++];

                    //if we find a blank line, assume that the tile data ended
                    if(line.isBlank()){
                        Logger.log(1, "TOO FEW TILES FOUND!! CHECK TILE COUNT");
                        break;
                    }

                    //split the line up using the dashes
                    String[] tileData = line.split("-");

                    //grab the tile information from the split line
                    int tileIndex = Integer.parseInt(tileData[0].trim());
                    String tileName = tileData[1].trim();
                    boolean tileHasCollision = Boolean.parseBoolean(tileData[2].trim());

                    //create the object
                    TileType tileToLoad = new TileType(tileName, tileHasCollision);

                    //store it
                    outputMap.tileTypes[tileIndex] = tileToLoad;
                }
            }

            //layout: holds the 2D tile array data
            //with the format:
            //each line of text = a row of tiles
            //each tile separated by whitespace (" ")
            else if (line.startsWith("layout:")) {
                //initialize the 2D array with the given width and height
                try {
                    outputMap.layout = new Tile[mapHeight][mapWidth];
                } catch (NegativeArraySizeException e) {
                    //if the size wasnt gathered, the values are still negative one,
                    //so a NegativeArraySizeException is thrown, and there is
                    //cannot load without this, so crash
                    Logger.log(2, "MAP SIZE MISSING BEFORE LAYOUT", true);
                }

                //loop through the tile data lines
                for (int i = 0; i < mapHeight; i++) {

                    //grab the next current line (and increment the count)
                    line = lines[lineNumber++];

                    //split it by spaces
                    String[] row = null;
                    try {
                        row = line.split(" ");
                    }catch(NullPointerException e){
                        Logger.log(2,"END OF FILE REACHED, MISSING MAP DATA, CANNOT LOAD", true);
                    }

                    //iterate through each integer in the line
                    //and load the appropriate tile into the 2D array
                    for (int j = 0; j < mapWidth; j++) {
                        //initialize tile value to -1 for default tile
                        int tileValue = -1;

                        //try to grab the next tile value
                        try {
                            assert row != null;
                            tileValue = Integer.parseInt(row[j]);
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            //if invalid data, log it and skip the rest of the loop iteration
                            Logger.log(2, "MAP DATA INVALID AT POSITION " + j + "," + i);
                            continue;
                        }

                        //calculate the X and Y coordinates of the tile
                        int x = j * GamePanel.TILE_SIZE;
                        int y = i * GamePanel.TILE_SIZE;

                        //if loadedTiles is null, the tile information wasn't found
                        //cannot load without this, so crash
                        if (outputMap.tileTypes == null) Logger.log(2, "TILE INFO MISSING BEFORE LAYOUT", true);

                        //if tile value and position is valid, we create and store the tile object
                        try {
                            outputMap.layout[i][j] = new Tile(outputMap.tileTypes[tileValue], x, y);
                        }catch(ArrayIndexOutOfBoundsException e){
                            Logger.log(2, "LINE MISSING AT LINE " + lineNumber);
                        }
                    }
                }
            }

            //if no other command is met, we have a line that we do not understand
            //log this
            else{
                Logger.log(1,"INVALID MAP LINE AT LINE " + lineNumber);
            }
        }

        //throw error if the map is invalid
        if(!outputMap.isValid()) Logger.log(2, "ERROR IN MAP LAYOUT");
        return outputMap;
    }
}