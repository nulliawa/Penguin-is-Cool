import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BKG {
    //background
    private final int WIDTH = 1400, HEIGHT = 800;

    private ArrayList<BKG> squares=new ArrayList<>();
    private ArrayList<ArrayList<BKG>> blocks=new ArrayList<>();
    private int offX, offY,width,height;
    private int offSpdX=10,offSpdY=10;
    private final int offSpd=10;
    public BKG(int offX, int offY, int width,int height){
        this.offX=offX;
        this.offY=offY;
        this.width=width;
        this.height=height;
    }
    public void setup(){
        for(int i=0;i<20;i++){//temporary grid for easier visuals
            for(int j=0;j<15;j++){
                if(i%2==0) {
                    if (j % 2 == 0) {
                        squares.add(new BKG(i * 100, j * 100, 100, 100));
                    }
                }
                else{
                    if(j%2==1){
                        squares.add(new BKG(i * 100, j * 100, 100, 100));
                    }
                }
            }
        }
        //creation of grid array
        for(int ad=0;ad<30;ad++) {
            blocks.add(new ArrayList<BKG>());
        }
        //coords based on 100x100 sized squares
        //30x  15y
        String template=
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 1 1 1 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 1 0 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
        int row=0,col=0;
        for(int loc=0;loc<template.length();loc++){
            char type=template.charAt(loc);
            if(type=='\n'){//next line/row
                row++;
                col=0;
            }
            else if(type=='1'){//default wall pattern
                blocks.get(row).add(new BKG(col*100,row*100,100,100));
            }
            if(loc%2==0){//every odd is a space character
                col++;
            }
        }
    }
    //0=none,1=North,2=South,3=East,4=West
    public int collisionY(Player p){//called on the blocks that cannot be passed through
        //returns a number telling where the player is in relation to the block
        int pX=p.getRect().x,pY=p.getRect().y,pW=p.getRect().width,pH=p.getRect().height;

        for(ArrayList<BKG> row:blocks) {
            for (BKG block : row) {
                int bX = block.getOffX(), bY = block.getOffY(), bW = block.getWidth(), bH = block.getHeight();
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
        for (ArrayList<BKG> row : blocks) {
            for (BKG block : row) {
                int bX = block.getOffX(), bY = block.getOffY(), bW = block.getWidth(), bH = block.getHeight();
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
    public ArrayList<ArrayList<BKG>> getBlocks(){
        return blocks;
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
//        return offY+height<=HEIGHT+offSpd||offY>=0;
        return edgeYT()||edgeYB();
    }
    private void moveHelp(BKG leader){//all other background elements follow "leader" main background image
        this.offSpdX=leader.offSpdX;
        this.offSpdY= leader.offSpdY;
        this.offX+=leader.offSpdX;
        this.offY+=leader.offSpdY;
    }
    public void move(boolean[] keys,boolean direction,Player player){
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;
        //when no keys pressed, speed 0, no movement
        int collideY=collisionY(player);
        int collideX=collisionX(player);
        offSpdX=0;
        offSpdY=0;
        // Movement of offset
        if(direction) {//true=up/down
            if(!(keys[W]&&keys[S])) {
                if (keys[W] && !edgeYT() && collideY != 2) {
                    offSpdY = 10;
                }
                if (keys[S] && !edgeYB() && collideY != 1) {
                    offSpdY = -10;
                }
            }
        }
        else{//false=left/right
            if(!(keys[A]&&keys[D])) {
                if (keys[A] && !edgeXL() && collideX != 4) {
                    offSpdX = 10;
                }
                if (keys[D] && !edgeXR() && collideX != 3) {
                    offSpdX = -10;
                }
            }
        }
        //updating offX and offY
        offX+=offSpdX;
        offY+=offSpdY;

        for(BKG b:squares){//temporary background grid
            b.moveHelp(this);
        }
        for(ArrayList<BKG> row:blocks){
            for(BKG block:row){
                block.moveHelp(this);
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
    public int getOffSpdX(){
        return offSpdX;
    }
    public int getOffSpdY(){
        return offSpdY;
    }

    public void draw(Graphics g,Image img){
        g.setColor(Color.cyan);//placeholder large main rect
        g.fillRect(offX,offY,width,height);
        g.setColor(Color.blue);

        for (BKG b : squares) {//grid
            g.fillRect(b.offX, b.offY, b.width, b.height);
        }
        g.setColor(Color.orange);
        for(ArrayList<BKG> row:blocks){
            for(BKG block:row){
                g.fillRect(block.offX,block.offY,block.width,block.height);
            }
        }
    }
}