import java.awt.*;
import java.util.ArrayList;
//enemies are golems or the boss
public class Enemy {
    private final int WIDTH = 1400, HEIGHT = 800;
    private int x, y, width, height,spdX,spdY;
    private ArrayList<Enemy> enemies=new ArrayList<>();
    private int current;
    private static Image[] covers;
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
        enemies.add(new Enemy(1000,30,40,60));
        enemies.add(new Enemy(510,340,40,60));
        enemies.add(new Enemy(WIDTH-200,HEIGHT-200,40,60));
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
    private void idle(Graphics g,Enemy e){
        int eX=e.getRect().x,eY=e.getRect().y,eW=e.getRect().width,eH=e.getRect().height;
        for(int i=1;i<7;i++) {
            g.drawImage(covers[i],eX, eY, null);
        }
    }
    public void draw(Graphics g){
        g.setColor(Color.red);
        for(Enemy e:enemies){
            int eX=e.getRect().x,eY=e.getRect().y,eW=e.getRect().width,eH=e.getRect().height;
            g.fillRect(eX,eY,eW,eH);
            g.drawImage(covers[1],eX,eY,null);
            g.drawImage(covers[2],eX,eY,null);
        }
    }

}
