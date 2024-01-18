import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class Battle {
    private final double PI=Math.PI;
    private final int WIDTH = 1400, HEIGHT = 800,SIZE=30;
    private BKG batBKG;
    private int hp;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private Player player=new Player(WIDTH/2,HEIGHT/2);
    Timer bTimer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    });
    public Battle(){
        //new screen with new projectiles, player set to middle
        this.hp=100;
    }
    private class Projectile{

        private int x,y,spdX,spdY,width,height,spdScale;
        private int elli;
        private double heading;
        public Projectile(int x,int y,int width,int height,double heading){
            this.x=x;
            this.y=y;
            this.spdX=0;
            this.spdY=0;
            this.width=width;
            this.height=height;
            this.heading=heading;
            this.spdScale=10;
        }
        //ellipse version
        public Projectile(int x,int y,int width,int height,double heading,int elli){
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
        }
        public void move(){
            //sideways movement
            this.spdX= (int) Math.round(5*Math.cos(heading));
            this.spdY= -(int) Math.round(5*Math.sin(heading));
            this.x+=this.spdX;
            this.y+=this.spdY;
        }
        public boolean remove(){//removes off-screen projectiles
            if(this.x+width<0){
                projectiles.remove(this);
                return true;
            }
            else if(this.x>WIDTH){
                projectiles.remove(this);
                return true;
            }
            else if(this.y<0){
                projectiles.remove(this);
                return true;
            }
            else if(this.y+height>HEIGHT){
                projectiles.remove(this);
                return true;
            }
            return false;
        }
        public Rectangle getRect(){
            return new Rectangle(this.x,this.y,this.width,this.height);
        }
        public void hit(){
            if(this.getRect().intersects(player.getRect())){
                hp-=1;
                System.out.println(hp);
            }
        }
        public void draw(Graphics g){
            g.setColor(Color.pink);
            g.fillRect(x,y,width,height);
        }
    }
    public void setUp(){
        projectiles.add(new Projectile(500,500,10,10,7*PI/4));
        projectiles.add(new Projectile(100,100,10,10,5*PI/3));
    }
    public void move(boolean[] keys){
        for(int i=0;i<projectiles.size();i++){
            Projectile proj=projectiles.get(i);
            if(!proj.remove()){//projectile is not off screen
                proj.move();
                proj.hit();
            }
            else{//take out of list loop
                i--;
            }
        }
        player.move(keys);
    }
    public void draw(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(0,0,WIDTH,HEIGHT);
        player.draw(g);
        for(Projectile p:projectiles){
            p.draw(g);
        }
    }

}