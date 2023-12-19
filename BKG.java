import java.awt.*;
public class BKG {
    //background
    private int offX=300, offY=150,width,height;
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

    }
    public void move(Player p){

        //will need to stop offset at edge of screen
        //if not at edge, player at center, move the background
        this.offX=p.getRect().x;
        this.offY=p.getRect().y;
    }
    public Rectangle getRect(){
        return backRect;
    }
    public void draw(Graphics g,Image img){
        g.setColor(Color.cyan);//placeholder
        g.fillRect(offX,offY,width,height);
    }
}