import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//enemies are golems
//collide with them to initiate battle
public class Enemy {
    private static final int WIDTH = 1400, HEIGHT = 800;
    private int x, y, width, height,spdX,spdY;
    private static final ArrayList<Enemy> enemies=new ArrayList<>();
    public static int frame=0;
    private static final int IDLETIME=5,DEATHTIME=5;
    private static final Image[] idles=new Image[6];
    private static final Image[] deaths=new Image[6];
    private boolean dead;
    public Enemy(){

    }
    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.spdX=0;
        this.spdY=0;
        this.width = width;
        this.height = height;
        this.dead=false;
    }
    public static void setUp(){
        //buffered image so first load does is not chunky
        for(int i=1;i<7;i++){//idle animation pics
            Image file=new ImageIcon("spirit/spiritIdle"+i+".png").getImage();//getScaledInstance(50,50,Image.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(file.getWidth(null),file.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(file, 0, 0, null);
            idles[i-1]=bufferedImage.getScaledInstance(50,50,Image.SCALE_SMOOTH);;
        }
        for(int j=1;j<7;j++){//death animation pics
            Image file=new ImageIcon("spirit/spiritDeath"+j+".png").getImage();
            BufferedImage bufferedImage = new BufferedImage(file.getWidth(null),file.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(file, 0, 0, null);
            deaths[j-1]=bufferedImage.getScaledInstance(50,50,Image.SCALE_SMOOTH);;
        }
        enemies.add(new Enemy(1000,30,50,55));
        enemies.add(new Enemy(510,340,50,55));
        enemies.add(new Enemy(WIDTH-200,HEIGHT-200,50,50));
    }
    public void destroy(){//destroys once death animation is over
        for(int i=0;i<enemies.size();i++){
            if(frame/DEATHTIME%deaths.length==5&&enemies.get(i).dead){
                enemies.remove(enemies.get(i));
                i--;
            }
        }
    }

    public void move(BKG bkg){//moves according to background offset
        for(Enemy e:enemies){
            e.spdX=bkg.getOffSpdX();
            e.spdY=bkg.getOffSpdY();
            e.x+=e.spdX;
            e.y+=e.spdY;
        }
    }
    public boolean pCollision(Player p){
        for(Enemy e:enemies){
            if(p.getRect().intersects(e.getRect())&&!e.dead){
                frame=0;
                e.dead=true;//stops checking collisions once enemy initiates battle with player
                //if player's rectangle colliding with enemy's rectangle
                return true;
            }
        }
        return false;
    }
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
    private void idle(Graphics g,Enemy e){
        int eX=e.getRect().x,eY=e.getRect().y;
        int index=frame/IDLETIME%idles.length;//gets a number 0 to 5
        g.drawImage(idles[index],eX,eY,null);
    }
    private void death(Graphics g, Enemy e){
        int eX=e.getRect().x,eY=e.getRect().y;
        int index=frame/DEATHTIME%deaths.length;
        g.drawImage(deaths[index],eX,eY,null);
    }
    public void draw(Graphics g){
        frame++;//which frame needs to be put up, constantly increasing
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        for(Enemy e:enemies){
            if(e.dead){
                death(g,e);
            }
            else{
                idle(g,e);
            }
        }
    }
}