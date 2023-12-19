import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    // Private vs. protected?
    private int x, y;
    private final int width = 30, height = 40;
    private int walkSpeed = 5;
    private int interactionDistance = 10; // Within interactionDistance

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int keyCode) {
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        // Movement of penguin, NOT offset
        if (keyCode == W) {
            y -= walkSpeed;
        }
        if (keyCode == A) {
            x -= walkSpeed;
        }
        if (keyCode == S) {
            y += walkSpeed;
        }
        if (keyCode == D) {
            x += walkSpeed;
        }
    }

    public void keyPress(int keyCode) {
        move(keyCode);
        // interact();
    }

    public void collision() {

    }

//    public void interact(int keyCode, Puzzle puzzle) {
//        final int SPACE = KeyEvent.VK_SPACE;
//        // Check if penguin is close enough to an object
//        if (Math.hypot(puzzle.getX() - playerX, puzzle.getY()) <= interactionDistance) {
//            // If getting close enough to an object, it changes
//        }
//    }

    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }
    
    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        // Draw the penguin
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 30, 30);
    }

    //    public void draw(Graphics g) {
//        // Draw the penguin
//    }

}
