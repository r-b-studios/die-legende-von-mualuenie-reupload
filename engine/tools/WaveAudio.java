package engine.tools;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// Zum Abspielen von .wav-Dateien
public class WaveAudio {

    private Clip clip;

    public WaveAudio(String src) {
        try {
            var url = new File("assets\\" + src).toURI().toURL();
            var stream = AudioSystem.getAudioInputStream(url);

            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play(boolean loop) {
        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }

    public void stop() {
        clip.stop();
    }

    public void setVolume(float volume) {
        FloatControl ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue(20f * (float) Math.log10(volume * 2f));
    }

    public float getLength() {
        return (float) (clip.getMicrosecondLength() / 1_000_000);
    }
}