package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Records which movement keys are held (WASD or arrow keys).
 * Runs on Swing's thread; the game loop reads the flags each tick. It never changes the game itself.
 */
public class KeyboardInput extends KeyAdapter {
    private volatile boolean up;
    private volatile boolean down;
    private volatile boolean left;
    private volatile boolean right;

    @Override
    public void keyPressed(KeyEvent e) {
        set(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        set(e.getKeyCode(), false);
    }

    private void set(int keyCode, boolean held) {
        switch (keyCode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                up = held;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                down = held;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = held;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = held;
                break;
            default:
                break;
        }
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }
}
