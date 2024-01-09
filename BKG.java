import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BKG {
    //background
    private ArrayList<BKG> squares=new ArrayList<>();
    private int offX, offY,width,height;
    private final int off = 10;
    private boolean sideMove;//is the character at the edge
    //character at center, move background instead of player
    public BKG(int offX, int offY, int width,int height){
        this.offX=offX;
        this.offY=offY;
        this.width=width;
        this.height=height;
        this.sideMove=false;
    }
    public void setUp(){
        for(int i=0;i<10;i++){
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
        return offX<0;
    }
    public boolean edgeY(){
        return offY<0;
    }
    public void move(int keyCode,boolean direction){
        //direction: true=up/down, false=left/right
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;

//        if(!sideMove){//switch for moving at side, turns back on with player x,y
//            this.sideMove=this.atEdge();
//        }
        for(BKG b:squares){
            b.move(keyCode,direction);
        }
        // Movement of offset
        if(direction) {
            if (keyCode == W) {
                offY += off;
            }
            if (keyCode == S) {
                offY -= off;
            }
        }
        else{
            if (keyCode == A) {
                offX += off;
            }
            if (keyCode == D) {
                offX -= off;
            }
        }
        //will need to stop offset at edge of screen
        //if not at edge, player at center, move the background
//        this.offX=p.getRect().x;
//        this.offY=p.getRect().y;
    }

    public void moveDown(int keyCode) {
        if (keyCode == KeyEvent.VK_S) {
            offY -= off;
        }
    }

    public void moveRight(int keyCode) {
        if (keyCode == KeyEvent.VK_D) {
            offX += off;
        }
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
        g.setColor(Color.cyan);//placeholder
        g.fillRect(offX,offY,width,height);
        g.setColor(Color.blue);

        for(int s=0;s<squares.size()-1;s++){
            BKG b=squares.get(s);
                g.fillRect(b.offX, b.offY, b.width, b.height);
        }
    }
}