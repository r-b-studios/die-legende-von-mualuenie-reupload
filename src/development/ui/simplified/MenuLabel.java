package development.ui.simplified;

import engine.main.Label;

public class MenuLabel extends Label {
    private final String _text;
    private final float _x, _y;
    private boolean centerX;

    public MenuLabel(String text, float y) {
        this(text, 0, y);
        centerX = true;
    }

    public MenuLabel(String text,  float x, float y) {
        _text = text;
        _x = x;
        _y = y;
    }

    @Override
    protected void load() {
        super.load();

        setText(_text);
        setPosition(_x, _y);

        setSize(2f, .5f);
        setFont(getFont("font\\pixel.ttf"));

        if (centerX) {
            setX((getCanvasSize().width - getWidth()) / 2f);
        }
    }
}