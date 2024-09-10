package development.ui;

import development.data.DataFile;
import development.data.Database;
import development.data.User;
import development.ui.simplified.MenuButton;
import development.ui.simplified.MenuLabel;
import engine.main.GameObject;
import engine.main.Label;
import engine.tools.SafeList;
import engine.main.Button;
import java.awt.*;
import java.sql.SQLException;

// Zeigt die Highscores aller Spieler sortiert an
public class HighscoreMenu extends GameObject {

    // index der aktuell anzuzeigenden Highscores
    private int index;
    private SafeList<User> highscores;
    private final SafeList<Label> labels = new SafeList<>();

    @Override
    protected void load() {
        super.load();

        // Button um zum MainMenu zurückzukehren
        addChildren(new MenuButton("Zurück", () -> replace(new MainMenu()), .5f, .5f));

        try {
            highscores = Database.getSortedByHighscore();
            reload();

            // Button um die vorherigen Highscores anzuzeigen
            Button upBtn = new Button();
            addChildren(upBtn);
            upBtn.setSrc("img\\ui\\arrow-up.png");
            upBtn.click.subscribe(this::switchUp);
            upBtn.setSize(.5f, .5f);
            upBtn.setPosition(getCanvasSize().width - 1f, .5f);

            // Button um die nächsten 10 Highscores anzuzeugen
            Button downBtn = new Button();
            addChildren(downBtn);
            downBtn.setSrc("img\\ui\\arrow-down.png");
            downBtn.click.subscribe(this::switchDown);
            downBtn.setSize(.5f, .5f);
            downBtn.setPosition(getCanvasSize().width - 1f, 1.25f);

        } catch (SQLException e) {
            Label errorLabel = new MenuLabel("Connection Error", 2.5f);
            addChildren(errorLabel);
            errorLabel.setColor(new Color(0x873434));
        }
    }

    private void reload() {
        // löscht alle Highscores-Label
        labels.forEach(GameObject::destroy);
        labels.clear();

        int max = Math.min(index + 10, highscores.size());

        // lädt neue Highscores
        for (int i = index; i < max; i++) {
            User user = highscores.get(i);

            boolean isThisUser = DataFile.getUID().equals(user.id());
            String name = isThisUser ? "dir" : user.name();

            float x = getCanvasSize().width / 2 + ((i - index) < 5 ? -2f : 2f) - 1f;
            float y = 1.25f + (i - index - ((i - index) < 5 ? 0 : 5)) * .6f;
            Label scoreLabel = new MenuLabel((i + 1) + ". " + user.highscore(), x, y);
            addChildren(scoreLabel);

            if (isThisUser) {
                scoreLabel.setColor(new Color(0x9200A7));
            }

            Label infoLabel = new MenuLabel("von " + name + " am " + user.date(), x, .25f + y);
            addChildren(infoLabel);
            infoLabel.setFont(infoLabel.getFont().deriveFont(10f));
            infoLabel.setColor(isThisUser ? new Color(0xAE00C5) : new Color(0x232323));

            labels.add(scoreLabel);
            labels.add(infoLabel);
        }
    }

    // veringert den index um 10 falls index > 0 oder geht sonst ans Ende
    private void switchUp() {
        if (index <= 0) {
            index = (highscores.size() / 10) * 10;
        } else {
            index -= 10;
        }

        reload();
    }

    // erhöht den index um 10 falls index >= Anzahl der Highscores oder geht sonst an den Anfang
    private void switchDown() {
        index += 10;

        if (index >= highscores.size()) {
            index = 0;
        }

        reload();
    }
}