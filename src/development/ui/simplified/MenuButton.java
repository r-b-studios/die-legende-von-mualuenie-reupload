package development.ui.simplified;

import engine.main.Button;

// Button mit vordefinierten Eigenschaften und Konstruktor
public class MenuButton extends Button {

    private final String _text;
    private final Runnable _onClick;
    private final float _x, _y;
    private boolean centerX;

    public MenuButton(String text, Runnable onClick, float y) {
        this(text, onClick, 0, y);
        centerX = true;
    }

    public MenuButton(String text, Runnable onClick, float x, float y) {
        _text = text;
        _onClick = onClick;
        _x = x;
        _y = y;
    }

    @Override
    protected void load() {
        super.load();

        label.setText(_text);
        click.subscribe(_onClick);
        setPosition(_x, _y);

        setSize(1.5f, .5f);
        setSrc("img\\ui\\button.png");
        label.setFont(getFont("font\\pixel.ttf"));

        if (centerX) {
            setX((getCanvasSize().width - getWidth()) / 2f);
        }
    }
}