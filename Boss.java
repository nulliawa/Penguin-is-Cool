import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
//boss seeks player after they are in range
//otherwise moves back and forth in set bounds
//hard coded boss hitboxes
//Orca - https://opengameart.org/content/swimming-whale
public class Boss{
    private int x,y,width,height,selfSpdX,selfSpdY,rectX,rectY;
    private final Rectangle bounds, triggerBox,hitbox;
    private static final Image[] swimRightImgs =new Image[7];
    private static final Image[] swimLeftImgs=new Image[7];
    private static int frame;
    private boolean seek;
    public Boss(){
        this.x=0;
        this.y=1300;
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
    public void move(Player player){//moves according to background offset
        int pX=player.getRect().x,pY=player.getRect().y,pW=player.getRect().width,pH=player.getRect().height;
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
        else{
            boundedX();
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
        int index=frame/5% swimRightImgs.length;
        if(selfSpdX>0) {
            g.drawImage(swimRightImgs[index], x, y, null);
        }
        if(selfSpdX<0){
            g.drawImage(swimLeftImgs[index],x,y,null);
        }
    }
    public void draw(Graphics g,int type,int x, int y){//type0=left,type1=right
        frame++;
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        int index=frame/5% swimRightImgs.length;
        if(type==0){
            g.drawImage(swimLeftImgs[index],x,y,null);
        }
        else if(type==1){
            g.drawImage(swimRightImgs[index],x,y,null);
        }
    }
}
