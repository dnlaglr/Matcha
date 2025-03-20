package gg.matcha.config;

public class Theme {
    public Theme(String font, int fontSize, String backgroundColor, String foregroundColor) {
        this._font = font;
        this._fontSize = fontSize;
        this._backgroundColor = backgroundColor;
        this._foregroundColor = foregroundColor;
    }

    public Theme() {
        setDefaultTheme();
    }

    public int getWidth() { return this._width; }
    public int getHeight() { return this._height; }
    public String getFont() { return this._font; }
    public int getFontSize() { return this._fontSize; }
    public String getBackgroundColor() { return this._backgroundColor; }
    public String getForegroundColor() { return this._foregroundColor; }

    public void setWidth(int newWidth) { this._width = newWidth; }
    public void setHeight(int newHeight) { this._height = newHeight; }
    public void setFont(String newFont) { this._font = newFont; }
    public void setFontSize(int newFontSize) { this._fontSize = newFontSize; }
    public void setBackgroundColor(String newColor) { this._backgroundColor = newColor; }
    public void setForegroundColor(String newColor) { this._foregroundColor = newColor; }

    public void setDefaultTheme() {
        this._font = "JetBrains Mono";
        this._fontSize = 12;
        this._backgroundColor = "#000000";
        this._foregroundColor = "#FFFFFF";
    }

    public static Theme getInstance() {
        if (_instance == null) {
            synchronized (Theme.class) {
                if (_instance == null) {
                    _instance = new Theme();
                }
            }
        }
        return _instance;
    }

    private int _width;
    private int _height;
    private String _font;
    private int _fontSize;
    private String _backgroundColor;
    private String _foregroundColor;
    public static Theme _instance;
}
