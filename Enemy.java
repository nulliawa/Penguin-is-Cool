import java.awt.*;
import java.util.ArrayList;

public class Enemy {
    private final int WIDTH = 1400, HEIGHT = 800;
    private int x, y, width, height,spdX,spdY;
    private ArrayList<Enemy> enemies=new ArrayList<>();

    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.spdX=0;
        this.spdY=0;
        this.width = width;
        this.height = height;
    }
    public void setUp(){
        enemies.add(new Enemy(1000,30,40,40));
        enemies.add(new Enemy(510,340,50,50));
        enemies.add(new Enemy(WIDTH-200,HEIGHT-200,10,10));
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
                //if player's rectangle colliding with enemy's rectangle
                return true;
            }
        }
        return false;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g){
        g.setColor(Color.red);
        for(Enemy e:enemies){
            int eX=e.getRect().x,eY=e.getRect().y,eW=e.getRect().width,eH=e.getRect().height;
            g.fillRect(eX,eY,eW,eH);
        }
    }

}
