import java.awt.*;

public class Note {
    private int x, y;
    private final int RADIUS = 150;

    public Note(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getRADIUS() {
        return RADIUS;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillOval(x, y, RADIUS, RADIUS);
    }
}
