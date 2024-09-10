package development.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import development.enums.Scene;
import development.enums.Skin;
import engine.tools.SafeList;
import org.json.*;

public final class DataFile {

    private static final int SEED = 2;
    private static final Path PATH = Path.of("player.dat");
    private static JSONObject jo;

    public static void load() {
        try {
            boolean loaded;
            String json = "{\"uid\":\"" + generateUID() + "\",\"highscore\":0,\"date\":\"01.01.2023\",\"skin\":\"DEFAULT\",\"music\":0.5,\"sfx\":0.5,\"coins\":0,\"skins\":[\"DEFAULT\"],\"scenes\":[\"OVERWORLD\"],\"name\":\"\",\"fps\":100}";

            do {
                // Erstellt JSON-Datei, falls nicht vorhanden
                if (!Files.exists(PATH)) {
                    Files.writeString(PATH, decode(json, SEED));
                    loaded = false;
                } else if (jo == null) {
                    try {
                        // lädt das JSON Objekt
                        jo = new JSONObject(decode(Files.readString(PATH), -SEED));

                        // Überprüft im folgenden ob alle JSON-Objekte existieren und den richtigen Datentype haben

                        for (String s : new String[] { "uid", "date", "skin", "name" }) jo.getString(s);
                        for (String s : new String[] { "highscore", "coins", "fps" }) jo.getInt(s);
                        for (String s : new String[] { "skins", "scenes" }) jo.getJSONArray(s);
                        for (String s : new String[] { "music", "sfx" }) jo.getFloat(s);

                    } catch (JSONException e) {
                        jo = null;
                        // für den Fall eines Fehlers wird die Datei neu geschrieben
                        Files.writeString(PATH, decode(json, SEED));
                    }

                    loaded = false;
                } else {
                    loaded = true;
                }
            } while (!loaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // verschlüsselt Text
    private static String decode(String text, int seed) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            result.append((char) ((byte) c + seed));
        }

        return result.toString();
    }

    private static void write() {
        try {
            Files.writeString(PATH, decode(jo.toString(), SEED));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    // gibt die UUID des Nutzers zurück
    public static String getUID() {
        try {
            return jo.getString("uid");
        } catch (IllegalArgumentException e1) {
            jo.put("uuid", generateUID());
            write();

            return jo.getString("uid");
        }
    }

    public static int getHighscore() {
        return jo.getInt("highscore");
    }

    public static void setHighscore(int value) {
        jo.put("highscore", value);
        write();
    }

    public static String getDate() {
        return jo.getString("date");
    }

    public static void setDate(String date) {
        jo.put("date", date);
        write();
    }

    public static Skin getMuaSkin() {
        return Skin.valueOf(jo.get("skin").toString());
    }

    public static void setMuaSkin(Skin skin) {
        jo.put("skin", skin);
        write();
    }

    public static float getMusicVolume() {
        return jo.getFloat("music");
    }

    public static void setMusicVolume(float volume) {
        jo.put("music", volume);
        write();
    }

    public static float getSFXVolume() {
        return jo.getFloat("sfx");
    }

    public static void setSFXVolume(float volume) {
        jo.put("sfx", volume);
        write();
    }

    public static int getCoins() {
        return jo.getInt("coins");
    }

    public static void addCoins(int coins) {
        jo.put("coins", getCoins() + coins);
        write();
    }

    public static SafeList<Skin> getUnlockedSkins() {
        SafeList<Skin> result = new SafeList<>();

        jo.getJSONArray("skins").forEach(s -> {
            result.add(Skin.valueOf(s.toString()));
        });

        return result;
    }

    public static void unlockSkin(Skin skin) {
        jo.getJSONArray("skins").put(skin.name());
    }

    public static SafeList<Scene> getUnlockedScenes() {
        SafeList<Scene> result = new SafeList<>();

        jo.getJSONArray("scenes").forEach(s -> {
            result.add(Scene.valueOf(s.toString()));
        });

        return result;
    }

    public static void unlockScene(Scene scene) {
        jo.getJSONArray("scenes").put(scene.name());
    }
    public static String getName() {
        return jo.getString("name");
    }

    public static void setName(String name) {
        jo.put("name", name);
        write();
    }

    private static String generateUID() {
        String result = String.valueOf(System.nanoTime());
        String ran = String.valueOf((int) (Math.random() * 100000));

        for (int i = 0; i < 5 - ran.length(); i++) {
            result += '0';
        }

        return result + ran;
    }

    public static int getMaxFPS() {
        return jo.getInt("fps");
    }

    public static void setMaxFPS(int fps) {
        jo.put("fps", fps);
        write();
    }
}