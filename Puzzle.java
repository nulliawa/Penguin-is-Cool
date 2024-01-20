import java.awt.*;

public class Puzzle {
    private final int width = 400, height = 200;
    public void createButton() {
        for (int i = 0; i < 4; i++) {
            Button button = new Button(40 * i, 200, width, height);
        }
    }

    public void getRect() {

    }

    public void draw(Graphics g) {
        Color[] colours = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
        for (int i = 0; i < 4; i++) {
            g.setColor(colours[i]);
            System.out.println(colours);
        }
        // g.drawRect();
    }
}
