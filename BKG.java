import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
//background includes walls
//water tiles and ice wall tiles cannot be passed through by player
//made of many tiles so can be easily customizable
//has scrolling based on player's position in the overworld vs on the screen - offset
//https://graphicriver.net/item/winterland-top-down-tileset/17565368?ref=evtheme
public class BKG {
    //background
    private static final int WIDTH = 1400, HEIGHT = 800;
    private static final char DEFAULT='0',TOP='1',BOT='2',LEFT='3',RIGHT='4',STAIR='5',TOPL='6',TOPR='7',BOTL='8',BOTR='9';
    private static final char WALL='A',WATER='W';
    private static final String[] blockNames =new String[]{"iceTileBasic","iceTileWall","iceTileWater","iceTileTop","iceTileBot",
            "iceTileLeft","iceTileRight","iceTileStair",
            "iceTileTopLeft","iceTileTopRight","iceTileBotLeft","iceTileBotRight",
    };
    private static final Image[] blockImgs=new Image[blockNames.length];
    private static final ArrayList<ArrayList<BKG>> blocks=new ArrayList<>();
    private static final ArrayList<ArrayList<BKG>> walls=new ArrayList<>();
    private int offX, offY,width,height;
    private static int offSpdX,offSpdY;
    private final int offSpd=10;
    private final Image cover;
    public BKG(int offX, int offY, int width,int height,Image cover){
        this.offX=offX;
        this.offY=offY;
        this.width=width;
        this.height=height;
        this.cover=cover;
    }
    public static void setup(){
        for(int i = 0; i< blockNames.length; i++){//make image list
            blockImgs[i]= new ImageIcon("iceTile/"+blockNames[i]+".png").getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        }
        templateWriter();
    }
    private static void templateWriter(){
        //creation of grid array
        for(int ad=0;ad<30;ad++) {
            blocks.add(new ArrayList<>());
            walls.add(new ArrayList<>());
        }
        //coords based on 100x100 sized squares
        //30x  15y
        //player starts at 700,400
        //DEFAULT=0,TOP=1,BOT=2,LEFT=3,RIGHT=4,STAIR=5,TOPL=6,TOPR=7,BOTL=8,BOTR=9;
        String template="6 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 7\n" +
                        "3 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 2 2 0 0 0 0 0 0 0 2 2 0 4\n" +
                        "3 0 0 0 0 0 A A A A A 0 A 0 0 0 4 W W 3 0 0 0 0 2 9 W W 3 4\n" +
                        "3 0 0 0 0 0 A A A A A A A 0 0 0 0 7 W 3 0 0 0 9 W W W W 3 4\n" +
                        "A A 5 A A A A 0 0 0 0 0 A A 0 0 0 0 1 0 0 0 4 W W W W 6 0 4\n" +
                        "3 0 0 0 0 0 A A A 0 0 0 0 A 5 A A A A 0 0 0 0 7 W W W 3 0 4\n" +
                        "3 0 0 0 0 0 0 0 A 0 0 0 0 A 5 A A A A A 0 0 0 0 7 W W 3 0 4\n" +
                        "3 0 0 0 0 0 0 0 A A 0 2 2 0 0 0 2 2 0 A 0 0 0 0 0 1 1 0 0 4\n" +
                        "3 0 0 0 0 0 0 0 0 A 4 W W 3 0 4 W W 3 A 0 0 0 0 0 0 0 0 0 4\n" +
                        "3 0 0 0 0 0 0 0 0 A 0 1 1 0 0 4 W W 3 A 0 0 0 0 0 0 2 2 0 4\n" +
                        "3 0 0 0 0 0 0 0 0 A A A A A 0 0 1 1 0 A A 5 A 0 0 9 W W 8 2\n" +
                        "8 2 2 2 2 2 2 2 2 2 2 2 0 A A 0 0 0 0 0 0 0 A 0 4 W W W W W\n" +
                        "W W W W W W W W W W W W 3 0 A 0 0 0 0 0 0 0 A 0 0 7 W W W W\n" +
                        "W W W W W W W W W W W W 3 0 A A 0 0 0 0 0 0 A 0 0 0 1 1 1 1\n" +
                        "W W W W W W W W W W W W 8 2 2 A 2 2 2 2 2 2 A 2 2 2 2 2 2 9";
        int row=0,col=0;
        for(int loc=0;loc<template.length();loc++){
            char type=template.charAt(loc);
            if(type=='\n'){//next line/row
                row++;
                col=0;
            }
            else if(type==DEFAULT){//blank snow tile
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[0]));
            }
            else if(type==WALL){//default wall pattern
                walls.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[1]));
            }
            else if(type==WATER){//water acts as wall
                walls.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[2]));

            }
            else if(type==TOP){//pattern at top
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[3]));
            }
            else if(type==BOT){//patern at bottom
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[4]));
            }
            else if(type==LEFT){//pattern on left
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[5]));
            }
            else if(type==RIGHT){//pattern on right
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[6]));
            }
            else if(type==STAIR){//stair (needs walls on both sides)
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[7]));
            }
            else if(type==TOPL){//pattern on top left
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[8]));
            }
            else if(type==TOPR){//pattern on top right
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[9]));
            }
            else if(type==BOTL){//pattern on bottom left
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[10]));
            }
            else if(type==BOTR){//pattern on bottom right
                blocks.get(row).add(new BKG(col*100,row*100,100,100,blockImgs[11]));
            }
            if(loc%2==0){//every odd is a space character
                col++;
            }
        }
    }
    public void reset(){
        BKG.stopMove();
        offX=0;
        offY=0;
        blocks.clear();
        walls.clear();
        templateWriter();
    }
    //0=none,1=North,2=South,3=East,4=West
    public int collisionY(Player p){//called on the blocks that cannot be passed through
        //returns a number telling where the player is in relation to the block
        int pX=p.getRect().x,pY=p.getRect().y,pW=p.getRect().width,pH=p.getRect().height;

        for(ArrayList<BKG> row:walls) {
            for (BKG wall : row) {
                int bX = wall.getOffX(), bY = wall.getOffY(), bW = wall.getWidth(), bH = wall.getHeight();
                if ((pX + pW > bX && pX+pW<bX+bW)||(pX < bX +bW && pX>bX)) {//player within colliding range based on squares
                    //top of player meets bottom of block
                    if (pY <= bY+bH&&pY>= bY) {
                        return 2;
                    }
                    //bottom of p intersects top of block
                    else if (pY + pH >=bY && pY+pH <= bY + bH) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    public int collisionX(Player p) {
        int pX = p.getRect().x, pY = p.getRect().y, pW = p.getRect().width, pH = p.getRect().height;
        for (ArrayList<BKG> row : walls) {
            for (BKG wall : row) {
                int bX = wall.getOffX(), bY = wall.getOffY(), bW = wall.getWidth(), bH = wall.getHeight();
                if ((pY+ pH > bY && pY < bY + bH) || (pY < bY + bH && pY > bY)) {//player within bounds for up/down collision
                    //right of player intersects left of block
                    if (pX + pW >= bX && pX + pW <= bX + bW) {
                        return 3;
                    }
                    //left side of player intersects right of block
                    else if (pX <= bX + bW && pX >= bX) {
                        return 4;
                    }
                }
            }
        }
        return 0;
    }
    public ArrayList<ArrayList<BKG>> getWalls(){
        return walls;
    }
    public boolean edgeXL(){
        return offX>=0;
    }
    public boolean edgeXR(){
        return offX+width<=WIDTH;
    }
    public boolean edgeX(){//check if background top left corner is at starting position
        //background is either too far right or too far left
        return edgeXL()||edgeXR();
    }
    public boolean edgeYT(){
        return offY>=0;
    }
    public boolean edgeYB(){
        return offY+height<=HEIGHT;
    }
    public boolean edgeY(){//background y is on one of the edges
        return edgeYT()||edgeYB();
    }
    private void moveHelp(){//all other background elements follow movement of main background image
        this.offX+=offSpdX;
        this.offY+=offSpdY;
    }
    public void move(boolean[] keys,Player player){
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        //0=none,1=North,2=South,3=East,4=West
        int collideY=collisionY(player);
        int collideX=collisionX(player);

        //when no keys pressed, speed 0, no movement
        offSpdX=0;
        offSpdY=0;
        // Movement of offset
        if(player.isFixedY()) {
            if(!(keys[W]&&keys[S])) {//no more "vibrating" with quick switch of spd from +/-
                if (keys[W] && !edgeYT() && collideY != 2) {
                    offSpdY = offSpd;
                }
                if (keys[S] && !edgeYB() && collideY != 1) {
                    offSpdY = -offSpd;
                }

            }
        }
        if(player.isFixedX()){//player within area where background can be moved, player will
            // stop moving across screen and background will move instead
            if(!(keys[A]&&keys[D])) {
                if (keys[A] && !edgeXL() && collideX != 4) {
                    offSpdX = offSpd;
                    Player.setLastDirection(true);//left
                    //last direction for player animation
                }
                if (keys[D] && !edgeXR() && collideX != 3) {
                    offSpdX = -offSpd;
                    Player.setLastDirection(false);//right
                }
            }
        }
        //updating offX and offY
        offX+=offSpdX;
        offY+=offSpdY;
        for(ArrayList<BKG> row:blocks){//blocks and walls will also move according to speed
            for(BKG block:row){
                block.moveHelp();
            }
        }
        for(ArrayList<BKG> row:walls){
            for(BKG wall:row){
                wall.moveHelp();
            }
        }
        //will need to stop offset at edge of screen
        //if not at edge, player at center, move the background
    }

    public int getOffX() {
        return offX;
    }
    public int getOffY() {
        return offY;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public static int getOffSpdX(){
        return offSpdX;
    }
    public static int getOffSpdY(){
        return offSpdY;
    }
    public static void stopMove(){
        offSpdX=0;
        offSpdY=0;
    }
    public void draw(Graphics g){
        for(ArrayList<BKG> row:blocks){
            for(BKG block:row){
                g.drawImage(block.cover,block.offX,block.offY,null);
            }
        }
        for(ArrayList<BKG> row:walls){
            for(BKG wall:row){
                g.drawImage(wall.cover,wall.offX,wall.offY,null);
            }
        }
    }
}