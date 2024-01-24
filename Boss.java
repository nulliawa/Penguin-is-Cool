import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Boss{
    private static final int WIDTH = 1400, HEIGHT = 800;
    private int x,y,width,height,selfSpdX,selfSpdY,rectX,rectY;
    private final Rectangle bounds, triggerBox,hitbox;
    private static final Image[] swimRightImgs =new Image[7];
    private static final Image[] swimLeftImgs=new Image[7];
    private static int frame;
    private static boolean seek;
    public Boss(int x, int y){
        this.x=x;
        this.y=y;
        this.width=300;
        this.height=100;
        this.selfSpdX=10;
        for(int i=1;i<8;i++){
            Image file=new ImageIcon("orca/orcaSwimR"+i+".png").getImage();
            BufferedImage bufferedImage = new BufferedImage(file.getWidth(null),file.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(file, 0, 0, null);
            swimRightImgs[i-1]=bufferedImage.getScaledInstance(width,height,Image.SCALE_SMOOTH);

            Image file2=new ImageIcon("orca/orcaSwimL"+i+".png").getImage();
            BufferedImage bufferedImage2 = new BufferedImage(file2.getWidth(null),file2.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage2.getGraphics().drawImage(file2, 0, 0, null);
            swimLeftImgs[i-1]=bufferedImage2.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        }
        this.bounds=new Rectangle(0,y-height,1200-width,0);
        this.triggerBox =new Rectangle(0,y-300+height,width,300);
        this.hitbox=new Rectangle(x,y,width,height);
    }
    private void boundedX(){//moves left/right itself according to rectangle boundary
        rectX+=selfSpdX;
        if((selfSpdX>0&&rectX>=bounds.x+bounds.width)||(selfSpdX<0&&rectX<=bounds.x)){
            selfSpdX*=-1;
        }
    }
    public Rectangle getRect(){
        return new Rectangle(x,y,width,height);
    }
    public Rectangle getTriggerBox(){
        return triggerBox;
    }
    public void move(Player player){//moves according to background offset
        int pX=player.getRect().x,pY=player.getRect().y,pW=player.getRect().width,pH=player.getRect().height;

        boundedX();
        if(player.getRect().intersects(triggerBox)){//player is within detection and initiates orca's battle
            seek=true;
        }
        x+=BKG.getOffSpdX()+selfSpdX;//adds offset speed as well as own speed
        y+=BKG.getOffSpdY()+selfSpdY;
        triggerBox.x+=BKG.getOffSpdX()+selfSpdX;//applies to other boxes
        triggerBox.y+=BKG.getOffSpdY()+selfSpdY;
        hitbox.x+=BKG.getOffSpdX()+selfSpdX;
        hitbox.y+=BKG.getOffSpdY()+selfSpdY;
        if(seek){
            if(hitbox.x+hitbox.width/2<pX+pW/2){
                selfSpdX=15;
            }
            else if(hitbox.x+hitbox.width/2>pX+pW/2){
                selfSpdX=-15;
            }
            if(hitbox.y+hitbox.height/2<pY+pH/2){
                selfSpdY=15;
            }
            else if(hitbox.y+hitbox.height/2>pY+pH/2){
                selfSpdY=-15;
            }
        }
    }
    public boolean collides(Player player){
        return hitbox.intersects(player.getRect());
    }
    public void draw(Graphics g){
        frame++;
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        g.setColor(Color.RED);
        g.fillRect(triggerBox.x, triggerBox.y, triggerBox.width, triggerBox.height);
        g.setColor(Color.green);
        g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        int index=frame/5% swimRightImgs.length;
        if(selfSpdX>0) {
            g.drawImage(swimRightImgs[index], x, y, null);
        }
        if(selfSpdX<0){
            g.drawImage(swimLeftImgs[index],x,y,null);
        }
    }
}
