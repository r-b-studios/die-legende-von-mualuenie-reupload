package engine.main;

import engine.tools.AnimationFrame;
import engine.tools.SafeList;

import java.util.List;

// ermöglicht es Aktionen in bestimmten Zeitabständen auszuführen
public class Animator extends Component {

    // alle Frames die animiert werden
    private final SafeList<AnimationFrame> frames = new SafeList<>();

    // Cooldown für die übrige Zeit bis zum nächstem Frame
    private float cooldown;

    // der index für den aktuellen Frame
    private int frameIndex;

    @Override
    void update() {
        // spielt den aktuellen Frame ab

        if (cooldown > 0) {
            cooldown -= 1f / owner.getFPS();
        } else {
            if (frameIndex < frames.size() - 1) {
                frameIndex++;
            } else {
                frameIndex = 0;
            }

            var frame = frames.get(frameIndex);
            frame.action().run();

            cooldown = frame.delay();
        }
    }

    // Frames setzen
    public void setFrames(AnimationFrame... frames) {
        this.frames.clear();
        this.frames.addAll(List.of(frames));
    }

    // Frames hinzufügen
    public void addFrames(AnimationFrame... frames) {
        this.frames.addAll(List.of(frames));
    }
}