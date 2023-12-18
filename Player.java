import java.awt.event.KeyEvent;

public class Player {
    // Private vs. protected?
    private int x, y;
    private int playerX, playerY;
    private int walkDistance = 5;
    private int interactionDistance = 10; // Within interactionDistance
    public Player (int x, int y) {
        this.x = x;
        this.y = y;
        playerX = 0;
        playerY = 0;
    }

    public void playerKeyPress(int keyCode) {
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        // Movement of penguin
        if (keyCode == W) {
            playerY -= walkDistance;
        }
        if (keyCode == A) {
            playerX -= walkDistance;
        }
        if (keyCode == S) {
            playerY += walkDistance;
        }
        if (keyCode == D) {
            playerX += walkDistance;
        }
    }

    public void playerCollision() {

    }

//    public void interact(Puzzle puzzle) {
//        final int SPACE = KeyEvent.VK_SPACE;
//        // Check if penguin is close enough to an object
//        if (Math.hypot(puzzle.getX() - playerX, puzzle.getY()) <= interactionDistance) {
//            // If getting close enough to an object, it changes
//        }
//    }

//    public void draw(Graphics g) {
//        // Draw the penguin
//    }

}
