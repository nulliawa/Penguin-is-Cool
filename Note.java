import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

public class Note {
    private int x, y;
    private int approachRadius = 250;
    private final int RADIUS = 150;
    private Ellipse2D.Double circle;
    private boolean visible = true;
    private boolean game = false;
    Timer timer = new Timer(20, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (visible && game) {
                shrinkCircle();
            }
        }
    });

    public Note(int x, int y) {
        this.x = x;
        this.y = y;
        timer.start();
        circle = new Ellipse2D.Double(this.x - (double) RADIUS / 2, this.y - (double) RADIUS / 2, RADIUS, RADIUS);
    }

    public void shrinkCircle() {
        approachRadius -= 3;
    }

    public boolean isHovered(int mx, int my) {
        return circle.contains(mx, my);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        miss();
        if (visible) {
            g.fillOval(x - RADIUS / 2, y - RADIUS / 2, RADIUS, RADIUS);
            g.drawOval(x - approachRadius / 2, y - approachRadius / 2, approachRadius, approachRadius);
        }
    }

    public void miss() {
        if (approachRadius <= RADIUS) {
            visible = false;
        }
    }

    public void setGame(boolean game) {
        this.game = game;
    }
}
