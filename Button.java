import java.awt.*;

public class Button {
    private int x, y, width, height;
    public Button(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(x, y, width, height);
    }
}
