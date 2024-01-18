import java.awt.*;
import java.util.ArrayList;
public class Battle {
    private final int WIDTH = 1400, HEIGHT = 800;
    private BKG batBKG;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public Battle(){

    }
    private class Projectile{
//                this.x+=Math.round(val*Math.cos(Math.toRadians(this.heading)));
//        this.y+=Math.round(val*Math.sin(Math.toRadians(this.heading))); "inspiration"
        private int x,y,spdX,spdY,width,height;
        private int radiusX,radiusY,elli;
        private double heading;
        public Projectile(int x,int y,int width,int height,double heading){
            this.x=x;
            this.y=y;
            this.spdX=0;
            this.spdY=0;
            this.width=width;
            this.height=height;
            this.heading=heading;
        }
//        public Projectile(int x,int y,int radiusX,int radiusY,double heading,int elli){
//            this.x=x;
//            this.y=y;
//            this.radiusX=radiusX;
//            this.radiusY=radiusY;
//        }
        public void move(){
            //sideways movement
            this.spdX= (int) Math.round(10*Math.cos(heading));
            this.spdY= -(int) Math.round(10*Math.sin(heading));
            this.x+=this.spdX;
            this.y+=this.spdY;
        }
        public void draw(Graphics g){
            g.setColor(Color.pink);
            g.fillRect(x,y,width,height);
        }
    }
    public void setUp(){
        projectiles.add(new Projectile(500,500,10,10,7*Math.PI/4));

    }
    public void move(){
        for(Projectile p:projectiles){
            p.move();
        }
    }
    public void draw(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(0,0,WIDTH,HEIGHT);
        for(Projectile p:projectiles){
            p.draw(g);
        }
    }

}