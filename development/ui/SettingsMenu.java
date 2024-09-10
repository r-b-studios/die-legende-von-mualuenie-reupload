package development.ui;

import development.data.DataFile;
import development.main.Program;
import development.ui.simplified.MenuButton;
import development.ui.simplified.MenuLabel;
import development.ui.simplified.MenuSlider;
import engine.main.*;
import engine.main.Button;
import engine.main.Label;

import java.awt.*;

// Einstellungsmenü
public class SettingsMenu extends RectObject {
    private Label fpsLabel, musicLabel, sfxLabel;
    private Slider fpsSlider, musicSlider, sfxSlider;
    private Button fullscreenBtn;

    // GameObject, welches nach Verlassen dieser Anzeige angezeigt wird
    private final GameObject objToReturn;

    public SettingsMenu(GameObject objToReturn) {
        this.objToReturn = objToReturn;
    }

    @Override
    protected void load() {
        super.load();

        setSize(getCanvasSize());
        setColor(objToReturn instanceof PauseMenu ? new Color(0x7C000000, true) : new Color(0x567BB4));

        // zurück
        addChildren(new MenuButton("Zurück", () -> replace(objToReturn), .5f, .5f));

        // Vollbildmodus ein/aus
        addChildren(fullscreenBtn = new MenuButton("Vollbild " + (hasFullscreen() ? "an" : "aus"), this::changeWindowState, .8f));

        Color labelColor = objToReturn instanceof PauseMenu ? Color.white : Color.black;

        addChildren(musicLabel = new MenuLabel("Musiklautstärke " + (int) (DataFile.getMusicVolume() * 100f), 1.5f));
        musicLabel.setColor(labelColor);

        // Slider zum Anpassen der Musiklautstärke
        addChildren(musicSlider = new MenuSlider(this::switchMusicSettings, 1.9f));
        musicSlider.setValue(DataFile.getMusicVolume());

        addChildren(sfxLabel = new MenuLabel("SFX-Lautstärke " + (int) (DataFile.getSFXVolume() * 100f), 2.2f));
        sfxLabel.setColor(labelColor);

        // Slider zum Anpassen der Musiklautstärke
        addChildren(sfxSlider = new MenuSlider(this::switchSFXSettings, 2.6f));
        sfxSlider.setValue(DataFile.getSFXVolume());

        addChildren(fpsLabel = new MenuLabel(DataFile.getMaxFPS() + " max. FPS", 2.9f));
        fpsLabel.setColor(labelColor);

        // Slider zum Anpassen der Maximalen FPS
        addChildren(fpsSlider = new MenuSlider(this::switchMaxFPS, 3.3f));
        fpsSlider.setValue((DataFile.getMaxFPS() - 10) / 990f);

        // nur im Hauptmenü möglich
        if (objToReturn instanceof MainMenu) {
            // Zeigt den aktuellen Username an
            Label nameLabel = new MenuLabel("Name: " + DataFile.getName(), 3.6f);
            addChildren(nameLabel);
            nameLabel.setColor(labelColor);

            // Username ändern
            addChildren(new MenuButton("Ändern", () -> replace(new NameInput(new SettingsMenu(objToReturn))), 4f));
        }
    }

    @Override
    protected void update() {
        super.update();
        fpsLabel.setText(getSliderFPSValue() + " max. FPS");
    }

    // ändert max. FPS
    private void switchMaxFPS() {
        DataFile.setMaxFPS(getSliderFPSValue());
    }

    // berechnet FPS-Wert des Sliders
    private int getSliderFPSValue() {
        return 10 + (int) (fpsSlider.getValue() * 990f);
    }

    // ändert Musiklautstärke
    private void switchMusicSettings() {
        DataFile.setMusicVolume(musicSlider.getValue());
        Program.music.setVolume(musicSlider.getValue());
        musicLabel.setText("Musiklautstärke " + (int) (musicSlider.getValue() * 100f));
    }

    // ändert SFX-Lautstärke
    private void switchSFXSettings() {
        DataFile.setSFXVolume(sfxSlider.getValue());
        sfxLabel.setText("SFX-Lautstärke " + (int) (sfxSlider.getValue() * 100f));
    }

    @Override
    protected void changeWindowState() {
        super.changeWindowState();
        fullscreenBtn.label.setText("Vollbild " + (hasFullscreen() ? "an" : "aus"));
    }
}