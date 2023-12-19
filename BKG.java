import java.awt.*;
public class BKG {
    //background
    int offX=300, offY=150,width,height;
    //character at center, move background instead of player

    public BKG(int width,int height){
        this.offX=offX;
        this.offY=offY;
        this.width=width;
        this.height=height;
    }
    public void collision(Player p){//called on the blocks that cannot be passed through

    }
    public void move(Player p){

        //will need to stop offset at edge of screen

        this.offX=p.getRect().x;
        this.offY=p.getRect().y;
    }
    public void draw(Graphics g,Image img){
        g.setColor(Color.cyan);//placeholder
        g.fillRect(offX,offY,width,height);
    }
}