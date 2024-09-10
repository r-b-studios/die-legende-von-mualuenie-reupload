package development.ui.simplified;

import engine.main.Slider;

public class MenuSlider extends Slider {

    private final Runnable _onRelease;
    private final float _x, _y;
    private boolean centerX;

    public MenuSlider(Runnable onRelease, float y) {
        this(onRelease, 0, y);
        centerX = true;
    }

    public MenuSlider(Runnable onRelease, float x, float y) {
        _onRelease = onRelease;
        _x = x;
        _y = y;
    }

    @Override
    protected void load() {
        super.load();

        button.release.subscribe(_onRelease);
        setPosition(_x, _y);

        setSrc("img\\ui\\slider.png");
        button.setSrc("img\\ui\\slider-button.png");

        if (centerX) {
            setX((getCanvasSize().width - getWidth()) / 2f);
        }
    }
}