import java.awt.*;

public class Enemy {
    private int x, y, width, height;

    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

}
