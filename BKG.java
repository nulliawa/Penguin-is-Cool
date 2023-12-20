import java.awt.*;
import java.awt.event.KeyEvent;

public class BKG {
    //background
    private int offX=300, offY=150,width,height;
    private final int off = 10;
    //character at center, move background instead of player
    Rectangle backRect;
    public BKG(int width,int height){
        this.offX=offX;
        this.offY=offY;
        this.width=width;
        this.height=height;
        this.backRect=new Rectangle(offX,offY,width,height);
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
    public void move(int keyCode){
        final int W = KeyEvent.VK_W, A = KeyEvent.VK_A, S = KeyEvent.VK_S, D = KeyEvent.VK_D;

        // Movement of offset
        if (keyCode == W) {
            offY += off;
        }
        if (keyCode == A) {
            offX += off;
        }
        if (keyCode == S) {
            offY -= off;
        }
        if (keyCode == D) {
            offX -= off;
        }

        //will need to stop offset at edge of screen
        //if not at edge, player at center, move the background
//        this.offX=p.getRect().x;
//        this.offY=p.getRect().y;
    }

    public int getOffX() {
        return offX;
    }

    public Rectangle getRect(){
        return backRect;
    }
    public void draw(Graphics g,Image img){
        g.setColor(Color.cyan);//placeholder
        g.fillRect(offX,offY,width,height);
    }
}