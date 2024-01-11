import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    // Private vs. protected?
    private int x, y;
    private final int SIZE=30,WIDTH=1366,HEIGHT=768;
    private int walkSpd = 10;
    private int interactionDistance = 10, offsetDistance = 10; // Within interactionDistance
//    private boolean walkUp, walkLeft, walkDown, walkRight;

    public Player(int x, int y) {
        this.x = x;//x and y on screen
        this.y = y;
    }
    public boolean isFixedX(){//in x direction, the midline
        return x==WIDTH/2-SIZE/2;
    }
    public boolean isFixedY(){//at midline in y direction
        return y==HEIGHT/2-SIZE/2;
    }
    public boolean isFixed(){//both x y
        return isFixedX()&&isFixedY();
    }

    public void move(boolean[] keys,boolean direction) {
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
//        System.out.println(keys[W]+", "+keys[A]+", "+keys[S]+", "+keys[D]);

        // Movement of penguin
        if(direction) {
            if (keys[W]) {
                y -= walkSpd;
            }
            if (keys[S]) {
                y += walkSpd;
            }
        }
        else {
            if (keys[A]) {
                x -= walkSpd;
            }
            if (keys[D]) {
                x += walkSpd;
            }
        }
    }

    public void collision(Rectangle block) {//with walls and border
        if(x>=WIDTH-SIZE){
            x=WIDTH-SIZE;
        }
        else if(x<=0){
            x=0;
        }
        if(y>=HEIGHT+SIZE){
            y=HEIGHT+SIZE;
        }
        else if(y<=0){
            y=0;
        }
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
        g.setColor(Color.BLACK);//temp black square
        g.fillRect(x, y, SIZE, SIZE);
    }

}
