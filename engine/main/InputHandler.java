package engine.main;

import engine.tools.SafeList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Überprüft Eingabe-Ereignisse
public class InputHandler implements KeyListener, MouseListener {

    // keyDown: erster Frame des Tastendrucks
    // keyPressed: wenn Taste gedrückt ist
    // keyUp: letzter Frame des Tastendrucks
    private boolean keyDown, keyPressed, keyUp;

    // mouseDown: erster Frame des Mausklicks
    // mousePressed: wenn Maustaste gedrückt ist
    // mouseUp: letzter Frame des Mausklicks
    private boolean mouseDown, mousePressed, mouseUp;

    // alle aktuell gedrückten Tastencodes
    private final SafeList<Integer> pressedKeys = new SafeList<>();

    // setzt ...down und ...up false, damit sie nur in einem Frame hintereinander true sind
    void update() {
        if (keyDown) keyDown = false;
        if (keyUp) keyUp = false;
        if (mouseDown) mouseDown = false;
        if (mouseUp) mouseUp = false;
    }

    // überprüft ob einer der angegebenen Tasten runtergedrückt wird
    public boolean keyDown(int... keyCodes) {
        return checkKey(keyCodes, keyDown);
    }

    public boolean anyKeyDown() {
        return keyDown;
    }

    // überprüft ob einer der angegebenen Tasten gedrückt wird
    public boolean keyPressed(int... keyCodes) {
        return checkKey(keyCodes, keyPressed);
    }

    public boolean anyKeyPressed() {
        return keyPressed;
    }

    // überprüft ob einer der angegebenen Tasten losgelassen wird
    public boolean keyUp(int... keyCodes) {
        return checkKey(keyCodes, keyUp);
    }

    public boolean anyKeyUp() {
        return keyUp;
    }

    // überprüft ob einer der angegebenen Tastencodes aktuelle gedrückt wird
    private boolean checkKey(int[] keyCodes, boolean condition) {
        if (condition) {
            for (int code : keyCodes) {
                if (pressedKeys.contains(code)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean mouseDown() {
        return mouseDown;
    }

    public boolean mousePressed() {
        return mousePressed;
    }

    public boolean mouseUp() {
        return mouseUp;
    }

    public SafeList<Integer> getPressedKeys() {
        return (SafeList<Integer>) pressedKeys.clone();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode())) {
            keyDown = keyPressed = true;
            // fügt gedrückte Tastencodes hinzu
            pressedKeys.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = false;
        keyUp = true;

        // entfernt gedrückte Tastencodes
        if (pressedKeys.contains(e.getKeyCode())) {
            pressedKeys.remove((Object) e.getKeyCode());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (!mousePressed) {
            mouseDown = true;
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
        mouseUp = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}