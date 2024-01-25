package Main;

import Entity.*;
import World.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    //TILE_SIZE: the size (in pixels) of every game tile.
    //currently, tiles are 32x32 pixels
    public static final int TILE_SIZE = 32;

    //EDGE_BORDER_THICKNESS: the thickness (in pixels) of the black border
    //around the edge of the map
    public static final int EDGE_BORDER_THICKNESS = 0;

    //RENDER_SCALE: a multiplier to all pixel values for rendering
    //at TILE_SIZE = 32, the final tiles will be rendered as 64x64 pixels on the screen
    public static final int RENDER_SCALE = 2;

    //state: the GameState object. necessary for communication between state and panel
    private GameState state;

    //fps: the current FPS value to display on the screen
    public double fps;

    public GamePanel(GameState gameState){
        super();
        //link with GameState
        state = gameState;
        //initialize fps
        fps = -1;

        //calculate width/height with map size, TILE_SIZE, EDGE_BORDER_THICKNESS, and RENDER_SCALE
        int width = (state.mapWidth * TILE_SIZE + EDGE_BORDER_THICKNESS * 2) * RENDER_SCALE;
        int height = (state.mapHeight * TILE_SIZE + EDGE_BORDER_THICKNESS * 2) * RENDER_SCALE;
        //set the size of the game window to this width/height
        //this is the size that the window object in Game will pack itself to
        this.setPreferredSize(new Dimension(width,height));
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
        Tile[][] map = state.getMap();
        int width = map[0].length;
        int height = map.length;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                BufferedImage image = map[i][j].image;
                AffineTransform transform = new AffineTransform();
                transform.scale(RENDER_SCALE,RENDER_SCALE);
                int imageX = (TILE_SIZE*j + EDGE_BORDER_THICKNESS);
                int imageY = (TILE_SIZE*i + EDGE_BORDER_THICKNESS);
                transform.translate(imageX, imageY);
                g2D.drawImage(image, transform, null);
                g2D.setColor(map[i][j].hasCollision ? Color.RED : Color.GREEN);
                if(Main.DEBUG_HITBOXES) g2D.draw(map[i][j].collider);
                //g2D.setColor(map[i][j].color);
                //g2D.fillRect(TILE_SIZE*j + EDGE_BORDER_THICKNESS, TILE_SIZE*i + EDGE_BORDER_THICKNESS, TILE_SIZE,TILE_SIZE);
            }
        }
    }

    //drawEntities(): draws all entities loaded in the GameState object
    private void drawEntities(Graphics2D g2D){
        for(Entity u : state.getEntities()){
            BufferedImage image = u.image;

            AffineTransform transform = new AffineTransform();

            transform.scale(RENDER_SCALE,RENDER_SCALE);

            int x = ((int)(u.x)) / RENDER_SCALE;
            int y = ((int)(u.y)) / RENDER_SCALE;
            transform.translate(x, y);

            g2D.drawImage(image, transform, null);

            g2D.setColor(Color.GREEN);
            if(Main.DEBUG_HITBOXES) {
                g2D.draw(u.collider);
                g2D.drawString(u.x + "," + u.y, 10, 20);
            }
        }
    }

    //drawUI(): draw game information, currently only FPS display
    private void drawUI(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.drawString("fps:"+fps, 10, 10);
    }

    //updateFPS: takes the time of the previous frame (in milliseconds)
    //and calculates the FPS from it
    //TODO: running average, instead of instantaneous
    public void updateFPS(double frameTimeMilli) {
        fps = 1000.0/frameTimeMilli;
        fps = fps * 100;
        fps = (int)fps;
        fps = fps/100;
    }
}
