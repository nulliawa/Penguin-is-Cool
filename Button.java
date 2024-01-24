import java.awt.*;

public class Button {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Rectangle rect;

    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new Rectangle(x, y, width, height);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean isHovered(int mx, int my) { // If the mouse hovers over the button
        return rect.contains(mx, my);
    }

    public boolean isClicked(int mx, int my, int mb) {
        return isHovered(mx, my) && mb == 1;
    }

    public void draw(Graphics g, Color colour) {
        g.setColor(colour);
        g.fillRect(x, y, width, height);
    }

    public void drawHover(Graphics g, Color colour, int mx, int my) {
        g.setColor(colour);
        if (isHovered(mx, my)) {
            g.fillRect(x, y, width, height);
        }
    }
}