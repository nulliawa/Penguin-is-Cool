import java.awt.*;

public class Button {
    private int x, y, width, height;
    private Rectangle rect;
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

    public boolean isHover(int mx, int my) { // If the mouse hovers over the button
        return rect.contains(mx, my);
    }

    public boolean isClicked(int mx, int my, int mb) {
        return isHover(mx, my) && mb == 1;
    }
}