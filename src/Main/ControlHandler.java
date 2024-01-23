package Main;

import java.awt.event.*;

public class ControlHandler implements KeyListener, MouseListener, MouseMotionListener {
    public boolean isLeftClick, isRightClick;
    public int mouseX, mouseY;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
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

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
