package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    public boolean upPressed,downPressed,rightPressed,leftPressed,shiftPressed;
    public boolean escapePressed=false;

    @Override
    public void keyTyped(KeyEvent e) {
        int key=e.getKeyChar();
        if (key == KeyEvent.VK_ESCAPE){
            escapePressed = !escapePressed;
        }
    }

    @Override
    public void keyPressed (KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_SHIFT){
            shiftPressed = true;
        }

    }
    @Override
    public void keyReleased (KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }
    }
}
