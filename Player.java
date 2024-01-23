import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//player movement controlled by wasd in both battle and overworld
//penguin is cool :)
public class Player {
    // Private vs. protected?
    private int x, y;
    private static final int SIZE=30,WIDTH = 1400, HEIGHT = 800, LEFT=180,RIGHT=0,UP=90,DOWN=270,HALF=45;;
    private int spdX =0, spdY =0,walkSpd=10;
    private static int frame;
    public static boolean lastDirection;//true=left false=right
    private int interactionDistance = 10, offsetDistance = 10; // Within interactionDistance
    private static final Image[] walkLeft=new Image[4];
    private static final Image[] walkRight=new Image[4];
    private static final Image[] defeatImgs=new Image[5];
    private static final Image[] idleLeft =new Image[4];
    private static final Image[] idleRight=new Image[4];
    public Player(int x, int y) {
        this.x = x;//x and y on screen
        this.y = y;
    }
    public static void setUp(){
        for(int w=1;w<5;w++){
            Image img=new ImageIcon("penguin/penguinWalkL"+w+".png").getImage();
            BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(img, 0, 0, null);
            walkLeft[w-1]=bufferedImage.getScaledInstance(SIZE,SIZE,Image.SCALE_SMOOTH);

            Image img2=new ImageIcon("penguin/penguinWalkR"+w+".png").getImage();
            BufferedImage bufferedImage2 = new BufferedImage(img2.getWidth(null),img2.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage2.getGraphics().drawImage(img2, 0, 0, null);
            walkRight[w-1]=bufferedImage2.getScaledInstance(SIZE,SIZE,Image.SCALE_SMOOTH);

            Image idle=new ImageIcon("penguin/penguinIdleL"+w+".png").getImage();
            BufferedImage buffi=new BufferedImage(idle.getWidth(null),idle.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            buffi.getGraphics().drawImage(idle,0,0,null);
            idleLeft[w-1]=buffi.getScaledInstance(SIZE,SIZE,Image.SCALE_SMOOTH);

            Image idle2=new ImageIcon("penguin/penguinIdleR"+w+".png").getImage();
            BufferedImage buffi2=new BufferedImage(idle2.getWidth(null),idle2.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            buffi2.getGraphics().drawImage(idle2,0,0,null);
            idleRight[w-1]=buffi2.getScaledInstance(SIZE,SIZE,Image.SCALE_SMOOTH);
        }

    }
    public double rad(int degree){//direction in degrees
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
    public void move(boolean[] keys,BKG bkg) {
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        int collideX=collisionX(bkg);
        int collideY=collisionY(bkg);
        spdX=0;
        spdY=0;
        // Movement of penguin in overworld (scrolling/side movement)
        if(bkg.edgeY()) {
            if(!(keys[W]&&keys[S])) {//no more "vibrating" with quick switch of spd from +/-
                if (keys[W] && collideY != 2) {
                    spdY = -walkSpd;
                }
                if (keys[S] && collideY != 1) {
                    spdY = walkSpd;
                }
            }
        }
        if(bkg.edgeX()){
            if(!(keys[A]&&keys[D])) {
                if (keys[A] && collideX != 4) {
                    spdX = -walkSpd;
                    lastDirection=true;//left
                }
                if (keys[D] && collideX != 3) {
                    spdX = walkSpd;
                    lastDirection=false;//right
                }
            }
        }
        //enacting the movement
        this.x+= spdX;
        this.y+= spdY;
        border();//player cannot go off screen
    }
    private void border(){
        if(edgeYT()){
            y=0;
        }
        if(edgeYB()){
            y=HEIGHT-SIZE;
        }
        if(edgeXL()){
            x=0;
        }
        if(edgeXR()){
            x=WIDTH-SIZE;
        }
    }
    private void setSpeeds(int heading){
        spdX =(int)Math.round(walkSpd*Math.cos(rad(heading)));
        spdY =-(int)Math.round(walkSpd*Math.sin(rad(heading)));
    }
    public void move(boolean[] keys) {//movement for battle (no bkg movement considered)
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        spdX =0;
        spdY =0;
        // movement of penguin is uniform across all directions in battle
        if(keys[W]){//UP [W] KEY
            if(!(keys[S]||keys[A]||keys[D])){
                setSpeeds(UP);
            }
            if(keys[D]&&!keys[A]){
                setSpeeds((UP+RIGHT)/2);
                lastDirection=false;
            }
            if(keys[A]&&!keys[D]){
                setSpeeds((UP+LEFT)/2);
                lastDirection=true;
            }
        }
        if(keys[S]){//DOWN [S] KEY
            if(!(keys[W]||keys[A]||keys[D])){
                setSpeeds(DOWN);
            }
            if(keys[D]&&!keys[A]){
                setSpeeds(-HALF);//half between a full x/y axis direction
                lastDirection=false;
            }
            if(keys[A]&&!keys[D]){
                setSpeeds((DOWN+LEFT)/2);
                lastDirection=true;
            }
        }
        if(keys[A]){//LEFT [A] KEY
            if(!(keys[W]||keys[S]||keys[D])){
                setSpeeds(LEFT);
            }
            if(keys[W]&&!keys[S]){
                setSpeeds((UP+LEFT)/2);
            }
            if(keys[S]&&!keys[W]){
                setSpeeds((DOWN+LEFT)/2);
            }
            lastDirection=true;
        }
        if(keys[D]){//RIGHT [D] KEY
            if(!(keys[W]||keys[S]||keys[A])){
                setSpeeds(RIGHT);
            }
            if(keys[W]&&!keys[S]){
                setSpeeds((UP+RIGHT)/2);
            }
            if(keys[S]&&!keys[W]){
                setSpeeds(-HALF);
            }
            lastDirection=false;
        }
        //enacting the movement
        this.x+= spdX;
        this.y+= spdY;
        border();//resets player to border if past it
    }
    //returns a number telling where the player is in relation to a block
    //0=none,1=North,2=South,3=East,4=West
    public int collisionY(BKG bkg) {//with blocks
        ArrayList<ArrayList<BKG>> blocks = bkg.getWalls();
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
        ArrayList<ArrayList<BKG>> blocks = bkg.getWalls();
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
    private void walkAnimation(Graphics g,boolean direction){
        int index=frame/5%walkLeft.length;//get the image index to display
        if(direction) {
            g.drawImage(walkLeft[index], x, y, null);
        }
        else{
            g.drawImage(walkRight[index],x,y,null);
        }
    }
    private void idleAnimation(Graphics g,boolean direction){
        int index=frame/20% idleLeft.length;
        if(direction) {
            g.drawImage(idleLeft[index], x, y, null);
        }
        else{
            g.drawImage(idleRight[index],x,y,null);
        }
    }
    //main game drawing
    public void draw(Graphics g, BKG bkg) {
        frame++;
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        // Draw the penguin
        int bspdX=BKG.getOffSpdX();
        int bspdY=BKG.getOffSpdY();

        if(spdX==0&&spdY==0&&bspdX==0&&bspdY==0){
            idleAnimation(g,lastDirection);
        }
        else if(spdX<0||spdY!=0||bspdX>0||bspdY!=0){//left walking
            walkAnimation(g,lastDirection);
        }
        else if(spdX>0||spdY!=0||bspdX<0||bspdY!=0){//right walking
            walkAnimation(g,lastDirection);
        }
    }
    //battle drawing
    public void draw(Graphics g) {//no offset in battle
        frame++;
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        // Draw the penguin
        //lastDirection
        //true=left, false=right
        if(spdX==0&&spdY==0){//no movement idle
            idleAnimation(g,lastDirection);
        }
        else if(spdX<0||spdY!=0){//left walking
            walkAnimation(g,lastDirection);
        }
        else if(spdX>0||spdY!=0){//right walking
            walkAnimation(g,lastDirection);
        }
    }
    //for drawing in menus
    public void draw(Graphics g, int type,int posX, int posY){
        x=posX;
        y=posY;
        frame++;
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        if(type==0){//idle
            idleAnimation(g,lastDirection);
        }
        else if(type==1){//left walking
            walkAnimation(g,lastDirection);
        }
        else if(type==2){//right walking
            walkAnimation(g,lastDirection);
        }
    }

}
