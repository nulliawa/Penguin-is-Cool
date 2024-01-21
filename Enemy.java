import java.awt.*;
import java.util.ArrayList;
//enemies are golems or the boss
public class Enemy {
    private static final int WIDTH = 1400, HEIGHT = 800;
    private int x, y, width, height,spdX,spdY;
    private ArrayList<Enemy> enemies=new ArrayList<>();
    private int current;
    private static int frame=0;
    private static final int FRAME=100;
    private static final int IDLE=0;
    private int[] animation=new int[]{10};
    private int idleAnimation=0;
    private long[] memTime=new long[4];
    private static Image[] covers;
    private static Image[] idles=new Image[6];
    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.spdX=0;
        this.spdY=0;
        this.width = width;
        this.height = height;
    }
    public void setUp(Image[] images){
        covers=images;
//        System.arraycopy(covers, 1, idles, 0, 6);//copies section of all images to idle animation array
        for(int i=0;i<6;i++){
            idles[i]=covers[i];
        }
        enemies.add(new Enemy(1000,30,50,70));
        enemies.add(new Enemy(510,340,50,70));
        enemies.add(new Enemy(WIDTH-200,HEIGHT-200,50,70));
    }
    public void end(Enemy enemy){
        enemies.remove(enemy);
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
            if(p.getRect().intersects(e.getRect())){
                enemies.remove(e);
//                this.current=enemies.indexOf(e);
                //if player's rectangle colliding with enemy's rectangle
                return true;
            }
        }
        return false;
    }
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
    public boolean timeMill(int delay,int index){
        //difference between current time and last recorded time is greater than delay, proceed with action (true)
        //always performs action on first use
        if(System.currentTimeMillis()-memTime[index]>delay){
            memTime[index]=System.currentTimeMillis();
            return true;
        }
        return false;
    }
    private void idle(Graphics g,Enemy e){
        int eX=e.getRect().x,eY=e.getRect().y,eW=e.getRect().width,eH=e.getRect().height;

        int index=frame/10%idles.length;//gets a number 0 to 5
        g.drawImage(idles[index],eX,eY,null);
    }
    public void draw(Graphics g){
//        g.setColor(Color.red);
        frame++;//which frame needs to be put up
        for(Enemy e:enemies){
//            g.fillRect(eX,eY,eW,eH);
            idle(g,e);
        }
    }

}
