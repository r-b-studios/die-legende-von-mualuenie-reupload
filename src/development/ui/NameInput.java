package development.ui;

import development.data.DataFile;
import development.data.Database;
import development.ui.simplified.MenuButton;
import development.ui.simplified.MenuLabel;
import engine.main.GameObject;
import engine.main.Label;
import java.awt.*;
import java.awt.event.KeyEvent;

// Eingabeformular für den Benutzername
public class NameInput extends GameObject {

    private Label nameLabel;

    // GameObject, welches nach Verlassen dieser Anzeige angezeigt wird
    private final GameObject objToReturn;

    public NameInput(GameObject objToReturn) {
        this.objToReturn = objToReturn;
    }

    @Override
    protected void load() {
        super.load();

        setCanvasBackground(new Color(0x7996BE));

        addChildren(new MenuLabel("Gib deinen Namen ein.", .5f));

        addChildren(nameLabel = new MenuLabel(DataFile.getName(), 2));
        nameLabel.setFont(nameLabel.getFont().deriveFont(30f));

        addChildren(new MenuButton("Weiter", this::exit, 4));
    }

    @Override
    protected void update() {
        super.update();

        // Überprüft Eingabe der einzelnen Tasten (nur Buchstaben und Zahlen)
        if (getInput().anyKeyDown()) {
            for (int key : getInput().getPressedKeys()) {
                if (Character.isAlphabetic((char) key) || Character.isDigit((char) key)) {
                    char c = (char) key;

                    if (!getInput().getPressedKeys().contains(KeyEvent.VK_SHIFT)) {
                        c = Character.toLowerCase(c);
                    }

                    nameLabel.setText(nameLabel.getText() + c);
                } else if (key == KeyEvent.VK_BACK_SPACE && nameLabel.getText().length() > 0) {
                    nameLabel.setText(nameLabel.getText().substring(0, nameLabel.getText().length() - 1));
                }
            }
        }
    }

    // verlässt dieses Formular
    private void exit() {
        DataFile.setName(nameLabel.getText());
        Database.setName(nameLabel.getText());
        replace(objToReturn);
    }
}