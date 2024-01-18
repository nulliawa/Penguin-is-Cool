import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Note {
    private int x, y;
    private int ax, ay;
    private int approachRadius = 200;
    private int RADIUS = 150;
    Timer timer = new Timer(20, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            shrinkCircle();
        }
    });

    public Note(int x, int y) {
        this.x = x;
        this.y = y;
        ax = x;
        ay = y;
        timer.start();
    }

    public void shrinkCircle() {
        approachRadius -= 2;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillOval(x - RADIUS / 2, y - RADIUS / 2, RADIUS, RADIUS);
        g.drawOval(ax - approachRadius / 2, ay - approachRadius / 2, approachRadius, approachRadius);
    }
}
