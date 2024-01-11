import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BKG {
    //background
    private final int WIDTH=1366,HEIGHT=768;
    private ArrayList<BKG> squares=new ArrayList<>();
    private int offX, offY,width,height;
    private final int offSpd = 10;
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
        for(int i=0;i<10;i++){//temporary grid for easier visuals
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
    }
    public void collision(Player p){//called on the blocks that cannot be passed through

        //top intersect
        int pX=p.getRect().x,pY=p.getRect().y,pW=p.getRect().width,pH=p.getRect().height;
        if(pY+pH>=this.offY&&pX+pW<this.offX||pX>this.offX+this.width){
            p.setY(this.offY+pH);
        }
        //bottom intersect
        else if(pY<this.offY+this.height){

        }
    }
//    public boolean atEdge(){//check if background top left corner is at starting position
//        return offX<=0&&offY<=0;
//    }
    public boolean edgeX(){
        //background is either too far right or too far left
        return offX>=offSpd||offX+width-WIDTH<=offSpd;
        //in terms of offSpd because the next action will move everything by 1 spd unit
    }
    public boolean edgeY(){//background y is on one of the edges
        return offY+height-HEIGHT<=offSpd||offY>=offSpd;
    }
    public void move(boolean[] keys,boolean direction){
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;

//        if(!sideMove){//switch for moving at side, turns back on with player x,y
//            this.sideMove=this.atEdge();
//        }
        for(BKG b:squares){//temporary background grid
            b.move(keys,direction);
        }
        // Movement of offset
        if(direction) {//true=up/down
            if (keys[W]) {
                offY += offSpd;
            }
            if (keys[S]) {
                offY -= offSpd;
            }
        }
        else{//false=left/right
            if (keys[A]) {
                offX += offSpd;
            }
            if (keys[D]) {
                offX -= offSpd;
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

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public void setOffY(int offY) {
        this.offY = offY;
    }

    public void draw(Graphics g,Image img){
        g.setColor(Color.cyan);//placeholder large main rect
        g.fillRect(offX,offY,width,height);
        g.setColor(Color.blue);

        for(int s=0;s<squares.size()-1;s++){//grid
            BKG b=squares.get(s);
                g.fillRect(b.offX, b.offY, b.width, b.height);
        }
    }
}