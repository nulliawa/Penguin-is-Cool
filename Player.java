import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    // Private vs. protected?
    private int x, y;
    private final int SIZE=30,WIDTH=1366,HEIGHT=768;
    private int walkSpeed = 10;
    private int interactionDistance = 10, offsetDistance = 10; // Within interactionDistance
//    private boolean walkUp, walkLeft, walkDown, walkRight;

    public Player(int x, int y) {
        this.x = x;//x and y on screen
        this.y = y;
    }
//    public boolean atEdge(){
//        return x<1366/2&&y<786/2;//resolution/2
//    }

    public boolean edgeX(){
        return x<WIDTH/2-SIZE/2;
    }
    public boolean edgeY(){
        return y<HEIGHT/2-SIZE/2;
    }
    public boolean pushX(){
        //player is up against center line in x direction
        return Math.abs(x-WIDTH/2-SIZE/2)<=10;
    }
    public boolean pushY(){
        return Math.abs(y-HEIGHT/2-SIZE/2)<=10;
    }
    public boolean isFixedX(){
        return x==WIDTH/2-SIZE/2;
    }
    public boolean isFixedY(){
        return y==HEIGHT/2-SIZE/2;
    }
    public boolean isFixed(){
        return isFixedX()&&isFixedY();
    }

    public void move(int keyCode,boolean direction) {
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;

        // Movement of penguin
        if(direction) {
            if (keyCode == W) {
                y -= walkSpeed;
            }
            if (keyCode == S) {
                y += walkSpeed;
            }
        }
        else {
            if (keyCode == A) {
                x -= walkSpeed;
            }
            if (keyCode == D) {
                x += walkSpeed;
            }
        }


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

    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public Rectangle getRect() {
        return new Rectangle(x, y, SIZE,SIZE);
    }

    public void draw(Graphics g) {
        // Draw the penguin
        g.setColor(Color.BLACK);
        g.fillRect(x, y, SIZE, SIZE);
    }

}
