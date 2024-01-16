import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {
    // Private vs. protected?
    private int x, y;
    private final int SIZE=30,WIDTH=1366,HEIGHT=768;
    private int walkSpdX=0,walkSpdY=0,walkSpd=10;
    private int interactionDistance = 10, offsetDistance = 10; // Within interactionDistance

    public Player(int x, int y) {
        this.x = x;//x and y on screen
        this.y = y;
    }
    private boolean edgeXL(){
        return x<=0;
    }
    private boolean edgeXR(){
        return x+SIZE>=WIDTH;
    }
    private boolean edgeYT(){
        return y<=0;
    }
    private boolean edgeYB(){
        return y+SIZE>=HEIGHT;
    }
    public boolean isFixedX(){//in x direction, the midline
        return x+SIZE/2==WIDTH/2;
    }
    public boolean isFixedY(){//at midline in y direction
        return y+SIZE/2==HEIGHT/2;
    }
    public void move(boolean[] keys,boolean direction,BKG bkg) {
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;

        walkSpdX=0;
        walkSpdY=0;
        // Movement of penguin
        if(direction) {
            if (keys[W]&&!edgeYT()&&collision(bkg)!=2) {
                walkSpdY=-10;
            }
            if (keys[S]&&!edgeYB()&&collision(bkg)!=1) {
                walkSpdY=10;
            }
        }
        else {
            if (keys[A]&&!edgeXL()) {
                walkSpdX=-10;
            }
            if (keys[D]&&!edgeXR()) {
                walkSpdX=10;
            }
        }
        //enacting the movement
        this.x+=walkSpdX;
        this.y+=walkSpdY;
    }
    //returns a number telling where the player is in relation to a block
    //0=none,1=North,2=South,3=East,4=West
    public int collision(BKG bkg) {//with blocks
        ArrayList<ArrayList<BKG>> blocks = bkg.getBlocks();
        for (ArrayList<BKG> row : blocks) {
            for (BKG block : row) {
                int bX = block.getOffX(), bY = block.getOffY(), bW = block.getWidth(), bH = block.getHeight();
                if ((x + SIZE > bX &&x+SIZE<bX+bH)||(x < bX + bW&&x>bX)) {//player within sideCollide range based on squares
                    //top of player meets bottom of block
                    if (y > bY+bH && y + SIZE > bY) {
                        return 2;
                    }
                    //bottom of p intersects top of block
                    else if (y + SIZE < bY + bH && y > bY + bH) {
                        return 1;
                    }
                }
            }
        }
        return 0;
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
