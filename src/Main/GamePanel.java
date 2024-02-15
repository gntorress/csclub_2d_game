package Main;

import Entity.*;
import World.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    //TILE_SIZE: the size (in pixels) of every game tile.
    //currently, tiles are 32x32 pixels
    public static final int TILE_SIZE = 32;

    //SCREEN_WIDTH_IN_TILES:
    //SCREEN_HEIGHT_IN_TILES:
    //how wide/high the screen is, measured in tiles,
    //to have a static screen size despite differing map sizes
    public static final int SCREEN_WIDTH_IN_TILES = 16;
    public static final int SCREEN_HEIGHT_IN_TILES = 9;

    //RENDER_SCALE: a multiplier to all pixel values for rendering
    //at TILE_SIZE = 32, the final tiles will be rendered as 64x64 pixels on the screen
    public static final int RENDER_SCALE = 2;

    //DEFAULT_TILE: the tile that the game falls back on
    //if it encounters an error accessing a tile (i.e. out of bounds)
    //TODO: better empty space background, or restrict camera to only inbounds?
    public static final Tile DEFAULT_TILE = new Tile(null, -1, -1);

    //state: the GameState object. necessary for communication between state and panel
    private GameState state;

    //displayFPS: the current FPS value to display on the screen
    public double displayFPS;

    //fps: an array that stores 15 frames of fps values, for averaging
    public double[] fps;

    //cameraX, cameraY, cameraMoveSpeed:
    //these variables handle the camera location
    //and movement, in the world.
    //TODO: camera object?
    public int cameraX;
    public int cameraY;
    public int cameraMoveSpeed = 8;

    //CONSTRUCTOR
    public GamePanel(GameState gameState){
        //super() calls the JPanel constructor
        super();

        //link with GameState
        state = gameState;

        //initialize fps
        fps = new double[16];
        //initialize camera
        cameraX = 0;
        cameraY = 0;

        //calculate width/height of the screen, based on the tiles and render scale
        int width = (SCREEN_WIDTH_IN_TILES * TILE_SIZE) * RENDER_SCALE;
        int height = (SCREEN_HEIGHT_IN_TILES * TILE_SIZE) * RENDER_SCALE;

        //set the size of the game window to this width/height
        //this is the size that the window object in Game will pack itself to
        this.setPreferredSize(new Dimension(width,height));

        //the GamePanel is what accepts user input, and to do this,
        //it needs to be focused (when clicked on)
        this.setFocusable(true);

        //background color (just in case)
        this.setBackground(Color.RED);

        //DoubleBuffering lets the panel render invisibly,
        //and then swap the current image for the new one
        //(only updates the picture once its fully rendered)
        this.setDoubleBuffered(true);
    }

    //update(): called once per frame
    public void update(){
        //move the camera, based on user input
        this.handleCameraMovement(state.controller);

        //repaint: handles the actual drawing to the screen
        this.repaint();
    }

    //paintComponent(): this is what gets called every frame
    //all visual rendering happens here.
    public void paintComponent(Graphics g){
        //without this line, nothing updates:
        super.paintComponent(g);

        //we use Graphics2D instead of Graphics, it has some better 2D tools
        Graphics2D g2 = (Graphics2D)g;

        //draw game stuff, in order from background -> foreground
        drawMap(g2);
        drawEntities(g2);
        drawUI(g2);

        //dispose just cleans up memory
        g2.dispose();
    }

    //drawMap(): draws the background tiles
    private void drawMap(Graphics2D g2D){
        //grab the map's 2D tile array
        Tile[][] map = state.getMapLayout();

        int width = map[0].length;
        int height = map.length;

        //startingX, startingY: the top left corner tile of the camera's visible space
        //does not include the tile that is partially cut off
        int startingX = cameraX / (TILE_SIZE*RENDER_SCALE);
        int startingY = cameraY / (TILE_SIZE*RENDER_SCALE);

        //nested for loop: iterates through all tiles
        //we start at the starting coordinates minus 1, to account for the partially cut off tiles
        //we iterate through the screen size plus one, to account for the partially cut off tiles
        for(int i = startingY - 1; i < startingY + SCREEN_HEIGHT_IN_TILES + 1; i++){
            for(int j = startingX - 1; j < startingX + SCREEN_WIDTH_IN_TILES + 1 ; j++){

                //image: the tile's image
                BufferedImage image;

                //try/catch tries to grab the tile at the coordinate j,i
                //if out of bounds, it grabs the default tile image instead
                try {
                    image = map[i][j].image;
                }catch(ArrayIndexOutOfBoundsException e){
                    image = DEFAULT_TILE.image;
                }

                //transform: an AffineTransform object
                //that handles scaling and movement of the tile texture
                AffineTransform transform = new AffineTransform();

                //we scale the tile by RENDER_SCALE
                transform.scale(RENDER_SCALE,RENDER_SCALE);

                //move it into place, based on the camera pos and RENDER_SCALE
                int imageY = (TILE_SIZE*i - (cameraY/RENDER_SCALE));
                int imageX = (TILE_SIZE*j - (cameraX/RENDER_SCALE));
                transform.translate(imageX, imageY);

                //draw the tile.
                //TODO MINOR: stitch textures together? to reduce draw call to one?
                g2D.drawImage(image, transform, null);

                //draw hitboxes if desired
                //TODO: scaling of debug hitboxes
                if(Main.DEBUG_HITBOXES) {
                    try {
                        g2D.setColor(map[i][j].hasCollision ? Color.RED : Color.GREEN);
                        Rectangle2D box = map[i][j].collider;
                        g2D.drawRect((int)box.getX() + RENDER_SCALE, (int)box.getY() + RENDER_SCALE, (int)box.getWidth() - RENDER_SCALE, (int)box.getHeight() - RENDER_SCALE);
                        g2D.draw(map[i][j].collider);
                    }catch(ArrayIndexOutOfBoundsException e){
                        //do nothing
                    }
                }
            }
        }
    }

    //drawEntities(): draws all entities loaded in the GameState object
    private void drawEntities(Graphics2D g2D){
        for(Entity u : state.getEntities()){
            BufferedImage image = u.image;

            AffineTransform transform = new AffineTransform();

            transform.scale(RENDER_SCALE,RENDER_SCALE);

            int x = ((int)(u.x));
            int y = ((int)(u.y));
            transform.translate(x - cameraX / RENDER_SCALE, y - cameraY / RENDER_SCALE);

            g2D.drawImage(image, transform, null);

            if(Main.DEBUG_HITBOXES) {
                g2D.setColor(Color.GREEN);
                g2D.fillOval(x-4,y-4,8,8);
                g2D.draw(u.collider);
                g2D.drawString(u.x + "," + u.y, 128, 20);
            }
        }
    }

    //drawUI(): draw game information, currently only FPS display
    private void drawUI(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.setFont(g2D.getFont().deriveFont(Font.PLAIN, g2D.getFont().getSize() * RENDER_SCALE));
        g2D.drawString("fps:"+(double)((int)(displayFPS * 100))/100.0, 10, 12*RENDER_SCALE);
    }

    //updateFPS: takes the time of the previous frame (in milliseconds)
    //and calculates the FPS from it
    public void updateFPS(double frameTimeMilli) {
        for(int i = 1; i < fps.length; i++){
            fps[i] = fps[i - 1];
        }
        fps[0] = 1000.0/frameTimeMilli;

        displayFPS = 0;
        for(double d : fps){
            displayFPS += d;
        }
        displayFPS = displayFPS / fps.length;
    }

    //handleCameraMovement: moves camera around, based on controller
    public void handleCameraMovement(ControlHandler controller) {
        if(controller.resetCam){
            cameraX = ((int) state.player.x * RENDER_SCALE - (RENDER_SCALE * SCREEN_WIDTH_IN_TILES * TILE_SIZE)/2 + RENDER_SCALE*state.player.size/2);
            cameraY = ((int) state.player.y * RENDER_SCALE - (RENDER_SCALE * SCREEN_HEIGHT_IN_TILES * TILE_SIZE)/2 + RENDER_SCALE*state.player.size/2);
        }else {
            if (controller.upCam) cameraY -= cameraMoveSpeed;
            if (controller.leftCam) cameraX -= cameraMoveSpeed;
            if (controller.downCam) cameraY += cameraMoveSpeed;
            if (controller.rightCam) cameraX += cameraMoveSpeed;
        }
    }
}