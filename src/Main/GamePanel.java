package Main;

import Entity.*;
import World.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 32;
    private static final int EDGE_BORDER_THICKNESS = 16;
    public static final int RENDER_SCALE = 2;
    private GameState state;
    public double fps;

    public GamePanel(GameState gameState){
        super();
        state = gameState;
        fps = -1;
        int width = (state.mapWidth * TILE_SIZE + EDGE_BORDER_THICKNESS * 2) * RENDER_SCALE;
        int height = (state.mapHeight * TILE_SIZE + EDGE_BORDER_THICKNESS * 2) * RENDER_SCALE;
        this.setPreferredSize(new Dimension(width,height));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        drawMap(g2);
        drawUnits(g2);
        drawUI(g2);

        g2.dispose();
    }

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
                //g2D.setColor(map[i][j].color);
                //g2D.fillRect(TILE_SIZE*j + EDGE_BORDER_THICKNESS, TILE_SIZE*i + EDGE_BORDER_THICKNESS, TILE_SIZE,TILE_SIZE);
            }
        }
    }

    private void drawUnits(Graphics2D g2D){
        for(Entity u : state.getUnits()){
            BufferedImage image = u.image;
            AffineTransform transform = new AffineTransform();
            transform.scale(RENDER_SCALE,RENDER_SCALE);
            int x = (int)(u.x - (u.radius/2) + 0.5) / RENDER_SCALE;
            int y = (int)(u.y - (u.radius/2) + 0.5) / RENDER_SCALE;
            transform.translate(x, y);
            g2D.drawImage(image, transform, null);
        }
    }
    private void drawUI(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.drawString("fps:"+fps, 10, 10);
    }

    public void updateFPS(double frameTimeMilli) {
        fps = 1000.0/frameTimeMilli;
        fps = fps * 100;
        fps = (int)fps;
        fps = fps/100;
    }
}
