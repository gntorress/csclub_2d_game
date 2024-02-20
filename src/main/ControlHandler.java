package main;

import java.awt.event.*;

public class ControlHandler implements KeyListener, MouseListener, MouseMotionListener {
    //ControlHandler handles all mouse/keyboard input from the player.
    //Method names should be self-explanatory?

    //MOUSE CONTROLS
    public boolean isLeftClick, isRightClick;
    public int mouseX, mouseY;

    //DIRECTIONAL (WASD) CONTROLS
    public boolean up, left, down, right;

    //CAMERA CONTROLS
    public boolean upCam, leftCam, downCam, rightCam, resetCam;

    @Override
    public void keyTyped(KeyEvent e) {
        //invoked when a key is fully pressed *and* released
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //invoked when a key is pressed down, or when it's pressed repeatedly when held down
        //repeats are because of Windows! I cant fix it without replacing this controller
        //with something else. this is why i use booleans here, they cant add up

        if(Main.DEBUG_PRINT_KEYCODES) System.out.println(e.getKeyCode());

        switch(e.getKeyCode()){
            case KeyEvent.VK_W:    //W
                up = true;
                break;
            case KeyEvent.VK_A:    //A
                left = true;
                break;
            case KeyEvent.VK_S:    //S
                down = true;
                break;
            case KeyEvent.VK_D:    //D
                right = true;
                break;
            case KeyEvent.VK_I:    //I
                upCam = true;
                break;
            case KeyEvent.VK_J:    //J
                leftCam = true;
                break;
            case KeyEvent.VK_K:    //K
                downCam = true;
                break;
            case KeyEvent.VK_L:    //L
                rightCam = true;
                break;
            case KeyEvent.VK_SPACE://SPACE
                resetCam = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //invoked when a key is released
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:    //W
                up = false;
                break;
            case KeyEvent.VK_A:    //A
                left = false;
                break;
            case KeyEvent.VK_S:    //S
                down = false;
                break;
            case KeyEvent.VK_D:    //D
                right = false;
                break;
            case KeyEvent.VK_I:    //I
                upCam = false;
                break;
            case KeyEvent.VK_J:    //J
                leftCam = false;
                break;
            case KeyEvent.VK_K:    //K
                downCam = false;
                break;
            case KeyEvent.VK_L:    //L
                rightCam = false;
                break;
            case KeyEvent.VK_SPACE://SPACE
                resetCam = false;
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
