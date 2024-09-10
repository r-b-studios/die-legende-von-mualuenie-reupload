package development.ui;

import development.ui.simplified.MenuButton;
import development.world.PlayMode;
import engine.main.RectObject;
import java.awt.*;

// Das Menü das während dem Pausieren des Spiels angezeigt wird
public class PauseMenu extends RectObject {

    @Override
    protected void load() {
        super.load();

        setSize(getCanvasSize());
        setColor(new Color(0x7C000000, true));

        addChildren(new MenuButton("Weiter", this::continuePlaying, 1.75f));
        addChildren(new MenuButton("Einstellungen", () -> replace(new SettingsMenu(new PauseMenu())), 2.5f));
        addChildren(new MenuButton("Aufgeben", this::surrender, 3.25f));
    }

    // lässt Spiel weiter laufen
    private void continuePlaying() {
        destroy();
        PlayMode.getInstance().setActive(true);
    }

    // Aufgeben und zurück zum Hauptmenü
    private void surrender() {
        destroy();
        PlayMode.getInstance().gameOver(false, true);
    }
}