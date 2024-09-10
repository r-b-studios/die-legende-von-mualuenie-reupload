package engine.tools;

import engine.main.GameObject;
import engine.main.Window;

// Klasse mit Grundeinstellungen und Umgebung
// settings: Einstellungen des Fensters
// environment: GameObject, dass alle anderen GameObjects enthält
public record Game(Settings settings, GameObject environment, String iconSrc) {

    // öffnet das Fenster
    // Damit wird das Spiel gestartet
    public void run() {
        new Window(settings, environment, iconSrc);
    }
}