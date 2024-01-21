import java.awt.*;
import java.util.ArrayList;
//enemies are golems
//collide with them to initiate battle
public class Enemy {
    private static final int WIDTH = 1400, HEIGHT = 800;
    private int x, y, width, height,spdX,spdY;
    private ArrayList<Enemy> enemies=new ArrayList<>();
    private int current;
    public static int frame=0;
    private static final int IDLETIME=5,DEATHTIME=5;
    private int[] animation=new int[]{10};
    private int idleAnimation=0;
    private long[] memTime=new long[4];
    private static Image[] covers;
    private static Image[] idles=new Image[6];
    private static Image[] deaths=new Image[6];
    private boolean dead;
    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.spdX=0;
        this.spdY=0;
        this.width = width;
        this.height = height;
        this.dead=false;
    }
    public void setUp(Image[] images){
        covers=images;
        for(int i=0;i<6;i++){//copies section of all images to idle animation array
            idles[i]=covers[i];
        }
        for(int j=6;j<12;j++){
            deaths[j-6]=covers[j];
        }
//        enemies.add(new Enemy(1000,30,50,70));
//        enemies.add(new Enemy(510,340,50,70));
//        enemies.add(new Enemy(WIDTH-200,HEIGHT-200,50,70));
        enemies.add(new Enemy(1000,30,50,55));
        enemies.add(new Enemy(510,340,50,55));
        enemies.add(new Enemy(WIDTH-200,HEIGHT-200,50,50));
    }
    public boolean dead(){
        return true;
//        enemies.remove();
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
        if(frame==2147483647){//limit on ints
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
