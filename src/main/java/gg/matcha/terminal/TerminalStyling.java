package gg.matcha.terminal;

public class TerminalStyling {
    public TerminalStyling(String font, int fontSize, String bgColor, String fgColor) {
        this._font = font;
        this._fontSize = fontSize;
        this._backgroundColor = bgColor;
        this._foregroundColor = fgColor;
    }

    public String getFont() { return _font; }
    public int getFontSize() { return _fontSize; }
    public String getBackgroundColor() { return _backgroundColor; }
    public String getForegroundColor() { return _foregroundColor; }

    public void setFont(String newFont) { _font = newFont; }
    public void setFontSize(int newFontSize) { _fontSize = newFontSize; }
    public void setBackgroundColor(String newBgColor) { _backgroundColor = newBgColor; }
    public void setForegroundColor(String newFgColor) { _foregroundColor = newFgColor; }

    private String _font;
    private int _fontSize;
    private String _backgroundColor;
    private String _foregroundColor;
}
