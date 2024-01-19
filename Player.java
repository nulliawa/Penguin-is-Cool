import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {
    // Private vs. protected?
    private int x, y;
    private final int SIZE=30,WIDTH = 1400, HEIGHT = 800, LEFT=180,RIGHT=0,UP=90,DOWN=270,HALF=45;;
    private int walkSpdX=0,walkSpdY=0,walkSpd=10;
    private int interactionDistance = 10, offsetDistance = 10; // Within interactionDistance

    public Player(int x, int y) {
        this.x = x;//x and y on screen
        this.y = y;
    }
    public double rad(int degree){//direction in degrees
//        if(degree>360){
//            double angle=degree%360.0;
//            return angle/180.0*Math.PI;
//        }
        return degree/180.0*Math.PI;
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
    public boolean isFixedX(){//in x direction, lines where background can scroll
        return x>=WIDTH/2-50&&x<=WIDTH/2+50;
    }
    public boolean isFixedY(){//in y direction, lines where background can start scrolling
        return y>=HEIGHT/2-50&&y<=HEIGHT/2+50;
    }
    public void move(boolean[] keys,boolean direction,BKG bkg) {
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        int collideX=collisionX(bkg);
        int collideY=collisionY(bkg);

        walkSpdX=0;
        walkSpdY=0;
        // Movement of penguin
        if(direction) {
            if(!(keys[W]&&keys[S])) {//no more "vibrating" with quick switch spd -10 to 10
                if (keys[W] && !edgeYT() && collideY != 2) {
                    walkSpdY = -10;
                }
                if (keys[S] && !edgeYB() && collideY != 1) {
                    walkSpdY = 10;
                }
            }
        }
        else {
            if(!(keys[A]&&keys[D])) {
                if (keys[A] && !edgeXL() && collideX != 4) {
                    walkSpdX = -10;
                }
                if (keys[D] && !edgeXR() && collideX != 3) {
                    walkSpdX = 10;
                }
            }
        }
        //enacting the movement
        this.x+=walkSpdX;
        this.y+=walkSpdY;
    }
    private void setSpeeds(int heading){
        walkSpdX=(int)Math.round(10*Math.cos(rad(heading)));
        walkSpdY=-(int)Math.round(10*Math.sin(rad(heading)));
    }
    public void move(boolean[] keys) {//movement for battle (no bkg movement considered)
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        walkSpdX=0;
        walkSpdY=0;
        // movement of penguin is uniform across all directions in battle
        if(keys[W]&&!edgeYT()){//UP [W] KEY
            if(!(keys[S]||keys[A]||keys[D])){
                setSpeeds(UP);
            }
            if(keys[D]&&!edgeXR()&&!keys[A]){
                setSpeeds((UP+RIGHT)/2);
            }
            if(keys[A]&&!edgeXL()&&!keys[D]){
                setSpeeds((UP+LEFT)/2);
            }
        }
        if(keys[S]&&!edgeYB()){//DOWN [S] KEY
            if(!(keys[W]||keys[A]||keys[D])){
                setSpeeds(DOWN);
            }
            if(keys[D]&&!edgeXR()&&!keys[A]){
                setSpeeds(-HALF);//half between a full x/y axis direction
            }
            if(keys[A]&&!edgeXL()&&!keys[D]){
                setSpeeds((DOWN+LEFT)/2);
            }
        }
        if(keys[A]&&!edgeXL()){//LEFT [A] KEY
            if(!(keys[W]||keys[S]||keys[D])){
                setSpeeds(LEFT);
            }
            if(keys[W]&&!edgeYT()&&!keys[S]){
                setSpeeds((UP+LEFT)/2);
            }
            if(keys[S]&&!edgeYB()&&!keys[W]){
                setSpeeds((DOWN+LEFT)/2);
            }
        }
        if(keys[D]&&!edgeXR()){//RIGHT [D] KEY
            if(!(keys[W]||keys[S]||keys[A])){
                setSpeeds(RIGHT);
            }
            if(keys[W]&&!edgeYT()&&!keys[S]){
                setSpeeds((UP+RIGHT)/2);
            }
            if(keys[S]&&!edgeYB()&&!keys[W]){
                setSpeeds(-HALF);
            }
        }


//        else {
//            if (!(keys[W] && keys[S])) {//no more "vibrating" with quick switch spd -10 to 10
//                if (keys[W] && !edgeYT()) {
//                    walkSpdY = -10;
//                }
//                if (keys[S] && !edgeYB()) {
//                    walkSpdY = 10;
//                }
//            }
//            if (!(keys[A] && keys[D])) {
//                if (keys[A] && !edgeXL()) {
//                    walkSpdX = -10;
//                }
//                if (keys[D] && !edgeXR()) {
//                    walkSpdX = 10;
//                }
//            }
//        }
        //enacting the movement
        this.x+=walkSpdX;
        this.y+=walkSpdY;
    }
    //returns a number telling where the player is in relation to a block
    //0=none,1=North,2=South,3=East,4=West
    public int collisionY(BKG bkg) {//with blocks
        ArrayList<ArrayList<BKG>> blocks = bkg.getBlocks();
        for (ArrayList<BKG> row : blocks) {
            for (BKG block : row) {
                int bX = block.getOffX(), bY = block.getOffY(), bW = block.getWidth(), bH = block.getHeight();
                if ((x + SIZE > bX &&x+SIZE<bX+bW)||(x < bX + bW&&x>bX)) {//player within side collisionY range
                    //top of player meets bottom of block
                    if (y <= bY+bH&&y>=bY) {
                        return 2;
                    }
                    //bottom of p intersects top of block
                    else if (y + SIZE >=bY && y+SIZE <= bY + bH) {
                        return 1;
                    }
                }
            }
        }
        return 0;//no collisionY on top/bottom
    }
    public int collisionX(BKG bkg) {
        ArrayList<ArrayList<BKG>> blocks = bkg.getBlocks();
        for (ArrayList<BKG> row : blocks) {
            for (BKG block : row) {
                int bX = block.getOffX(), bY = block.getOffY(), bW = block.getWidth(), bH = block.getHeight();
                if ((y + SIZE > bY && y < bY + bH) || (y < bY + bH && y > bY)) {//player within bounds for up/down collisionY
                    //right of player intersects left of block
                    if (x + SIZE >= bX && x + SIZE <= bX + bW) {
                        return 3;
                    }
                    //left side of player intersects right of block
                    else if (x <= bX + bW && x >= bX) {
                        return 4;
                    }
                }
            }
        }
        return 0;//no collisionY on either side
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
