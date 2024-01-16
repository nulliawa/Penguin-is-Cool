import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BKG {
    //background
    private final int WIDTH=1366,HEIGHT=768;
    private ArrayList<BKG> squares=new ArrayList<>();
    private ArrayList<ArrayList<BKG>> blocks=new ArrayList<>();
    private int offX, offY,width,height;
    private int offSpdX=10,offSpdY=10;
    private final int offSpd=10;
    private boolean sideMove;//is the character at the edge
    //character at center, move background instead of player
    public BKG(int offX, int offY, int width,int height){
        this.offX=offX;
        this.offY=offY;
        this.width=width;
        this.height=height;
        this.sideMove=false;
    }
    public void setup(){
        for(int i=0;i<15;i++){//temporary grid for easier visuals
            for(int j=0;j<10;j++){
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
                "0 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 1 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 1 0 1 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 1 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
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
                System.out.println(row+", "+col);
            }
            if(loc%2==0){//every odd is a space character
                col++;
            }
        }
    }
    public void collision(){//called on the blocks that cannot be passed through
//        int pX=p.getRect().x,pY=p.getRect().y,pW=p.getRect().width,pH=p.getRect().height;

        if(edgeXL()||edgeXR()){
            offSpdX=0;
        }
//        else if(edgeXR()){
//            offX=WIDTH-width;
//        }
        if(edgeYT()||edgeYB()){
            offSpdY=0;
        }
//        else if(edgeYB()){
//            offY=HEIGHT-height;
//        }

//        if(pX+pW>this.offX&&pX<this.offX+this.width) {//player within collision range based on squares
//            //top of player intersect bottom of block
//            if (pY + pH >= this.offY) {
//                p.setY(this.offY);
//            }
//            //bottom of p intersect
//            else if (pY < this.offY + this.height) {
//                p.setY(this.offY + this.height);
//            }
//        }
    }
    public boolean edgeXL(){
        return offX>=0;
    }
    public boolean edgeXR(){
        return offX+width<=WIDTH+offSpd;
    }
    public boolean edgeX(){//check if background top left corner is at starting position
        //background is either too far right or too far left
//        return offX>=offSpd||offX+width<=WIDTH+offSpd;
        return edgeXL()||edgeXR();
        //in terms of offSpd because the next action will move everything by 1 spd unit
    }
    public boolean edgeYT(){
        return offY>=0;
    }
    public boolean edgeYB(){
        return offY+height<=HEIGHT+offSpd;
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
    public void move(boolean[] keys,boolean direction){
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;

//        for(BKG b:squares){//temporary background grid
//            b.move(keys,direction);
//        }
//        for(ArrayList<BKG> row:blocks){
//            for(BKG block:row){
//                block.move(keys,direction);
//            }
//        }
        offSpdX=0;
        offSpdY=0;
        // Movement of offset
        if(direction) {//true=up/down
            if (keys[W]&&!edgeYT()) {
                offSpdY=10;
            }
            if (keys[S]&&!edgeYB()) {
                offSpdY=-10;
            }
        }
        else{//false=left/right
            if (keys[A]&&!edgeXL()) {
                offSpdX=10;
            }
            if (keys[D]&&!edgeXR()) {
                offSpdX=-10;
            }
        }
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
        // Movement of offset
//        if(direction) {//true=up/down
//            if (keys[W]) {
//                offY += offSpdY;
//            }
//            if (keys[S]) {
//                offY -= offSpdY;
//            }
//        }
//        else{//false=left/right
//            if (keys[A]) {
//                offX += offSpdX;
//            }
//            if (keys[D]) {
//                offX -= offSpdX;
//            }
//        }
        //will need to stop offset at edge of screen
        //if not at edge, player at center, move the background
    }

    public int getOffX() {
        return offX;
    }

    public int getOffY() {
        return offY;
    }

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public void setOffY(int offY) {
        this.offY = offY;
    }
    private void setOffSpd(int xSpd,int ySpd){
        offSpdX=xSpd;
        offSpdY=ySpd;
    }
    private void setOffSpdX(int speed){
        offSpdX=speed;
    }
    private void setOffSpdY(int speed){
        offSpdY=speed;
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