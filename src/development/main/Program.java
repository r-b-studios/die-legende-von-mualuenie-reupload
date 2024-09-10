package development.main;

import development.data.DataFile;
import development.data.Database;
import development.enums.Scene;
import development.enums.Skin;
import engine.tools.Game;
import engine.tools.Settings;
import engine.tools.WaveAudio;

public class Program {

    // Musik
    public static final WaveAudio music = new WaveAudio("audio\\music.wav");

    // Spiel
    public static Game game;

    public static void main(String[] args) {

        // Lädt Datenanwendungen
        DataFile.load();
        Database.load();

        // spielt Musik ab
        music.play(true);
        music.setVolume(DataFile.getMusicVolume());

        Settings settings = new Settings(10, 6);
        Environment environment = new Environment();

        // Starten des Spiels
        game = new Game(settings, environment, "img\\ui\\icon.png");
        game.run();
    }
}