package Main;

import java.awt.event.*;

public class ControlHandler implements KeyListener, MouseListener, MouseMotionListener {
    //ControlHandler handles all mouse/keyboard input from the player.
    //Method names should be self-explanatory?

    //MOUSE CONTROLS
    public boolean isLeftClick, isRightClick;
    public int mouseX, mouseY;

    //DIRECTIONAL (WASD) CONTROLS
    public boolean up, left, down, right;

    @Override
    public void keyTyped(KeyEvent e) {
        //invoked when a key is fully pressed *and* released
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //invoked when a key is pressed down, or when it's pressed repeatedly when held down
        //repeats are because of Windows! I cant fix it without replacing this controller
        //with something else. this is why i use booleans here, they cant add up
        switch(e.getKeyChar()){
            case 'w':
                up = true;
                break;
            case 'a':
                left = true;
                break;
            case 's':
                down = true;
                break;
            case 'd':
                right = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //invoked when a key is released
        switch(e.getKeyChar()){
            case 'w':
                up = false;
                break;
            case 'a':
                left = false;
                break;
            case 's':
                down = false;
                break;
            case 'd':
                right = false;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //invoked when a mouse is fully clicked *and* released
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //invoked when a mouse button is pressed/clicked down
        switch(e.getButton()){
            case 1:
                isLeftClick = true;
                break;
            case 2:
                isRightClick = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //invoked when a mouse button is released/unclicked
        switch(e.getButton()) {
            case 1:
                isLeftClick = false;
                break;
            case 2:
                isRightClick = false;
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //invoked when the mouse enters the bounds of the object that is "listening"
        //in this case, the listener is the panel that the game is drawn to,
        //so this is invoked when the mouse enters the bounds of the game window
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //invoked when the mouse exits the bounds of the object that is "listening"
        //similar to mouseEntered, but for exiting instead of entering
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //invoked when the mouse is moved while clicking
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //invoked when the mouse is moved (with no buttons held down)
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
