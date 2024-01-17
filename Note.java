import java.awt.*;

public class Note {
    private int x, y;
    private int RADIUS = 150;
    private long time = System.currentTimeMillis();

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
        if (time == System.currentTimeMillis() + 10) {
            RADIUS = RADIUS - 1;
            g.fillOval(x, y, RADIUS, RADIUS);
            time = System.currentTimeMillis();
        }
        System.out.println(RADIUS);
    }
}
