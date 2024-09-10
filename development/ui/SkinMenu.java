package development.ui;

import development.data.DataFile;
import development.enums.Skin;
import development.ui.simplified.MenuButton;
import development.ui.simplified.MenuLabel;
import engine.main.Button;
import engine.main.GameObject;
import engine.main.ImageObject;
import engine.main.Label;
import java.awt.*;
import java.util.List;

public class SkinMenu extends GameObject {

    private final int SKIN_PRICE = 1;
    private Skin skin;
    private Label skinNameLabel;
    private Button selectBtn;

    // Aktuelle Skin-Anzeige
    private final ImageObject skinDisplay = new ImageObject();

    @Override
    protected void load() {
        super.load();

        skin = DataFile.getMuaSkin();
        setCanvasBackground(new Color(0x17C255));

        // Button, der zum Hauptmenü zurückführt
        addChildren(new MenuButton("Zurück", () -> replace(new MainMenu()), .5f, .5f));

        // Label, der den Name des Skins Anzeigt
        addChildren(skinNameLabel = new MenuLabel("", 1.25f));

        // Button, der Skin auswählt
        addChildren(selectBtn = new MenuButton("Auswählen", this::selectSkin, 4f));

        // Button zum Wechseln des aktuellen anzuzeigenden Skins
        Button leftBtn = new Button();
        addChildren(leftBtn);
        leftBtn.setSize(0.5f, 0.5f);
        leftBtn.setPosition((getCanvasSize().width - leftBtn.getWidth()) / 2 - 1.5f, 4);
        leftBtn.setSrc("img\\ui\\arrow-left.png");
        leftBtn.click.subscribe(this::switchLeft);

        // Button zum Wechseln des aktuellen anzuzeigenden Skins
        Button rightBtn = new Button();
        addChildren(rightBtn);
        rightBtn.setSize(0.5f, 0.5f);
        rightBtn.setPosition((getCanvasSize().width - rightBtn.getWidth()) / 2 + 1.5f, 4);
        rightBtn.setSrc("img\\ui\\arrow-right.png");
        rightBtn.click.subscribe(this::switchRight);

        addChildren(skinDisplay);
        skinDisplay.setSize(2, 2);
        skinDisplay.setPosition((getCanvasSize().width - skinDisplay.getWidth()) / 2f, 2);

        loadSkin();
    }

    // Wechseln des aktuellen anzuzeigenden Skins
    private void switchLeft() {
        int index = List.of(Skin.values()).indexOf(skin);
        index--;

        if (index < 0) {
            index = Skin.values().length - 1;
        }

        skin = Skin.values()[index];
        loadSkin();
    }

    // Wechseln des aktuellen anzuzeigenden Skins
    private void switchRight() {
        int index = List.of(Skin.values()).indexOf(skin);
        index++;

        if (index >= Skin.values().length) {
            index = 0;
        }

        skin = Skin.values()[index];
        loadSkin();
    }

    // Wählt Skin aus oder kauft ihn
    private void selectSkin() {
        if (DataFile.getUnlockedSkins().contains(skin)) {
            DataFile.setMuaSkin(skin);
            replace(new MainMenu());
        } else if (DataFile.getCoins() >= getPrice()) {
            DataFile.addCoins(-getPrice());
            DataFile.unlockSkin(skin);
            loadSkin();
        }
    }

    // lädt den Skin in skinDisplay
    private void loadSkin() {
        String fileName = skin.name().toLowerCase();
        String src = "img\\obj\\mua\\" + fileName + "\\run-6.png";

        String btnText = DataFile.getUnlockedSkins().contains(skin) ? "Auswählen" : (getPrice() + " Coins");
        selectBtn.label.setText(btnText);

        skinDisplay.setSrc(src);

        String name = "Müaluenie";

        switch (skin) {
            case KNIGHT -> name = "Ritterluenie";
            case PIRATE -> name = "Piratenluenie";
            case MARTIAN -> name = "Marsluenie";
            case SNAIL -> name = "Schneckenluenie";
        }

        skinNameLabel.setText(name);
    }

    // berechnte Preis
    private int getPrice() {
        return SKIN_PRICE * List.of(Skin.values()).indexOf(skin);
    }
}