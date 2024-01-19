import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Battle {
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
            this.spdScale=5;
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
            this.spdX= (int) Math.round(spdScale*Math.cos(heading));
            this.spdY= -(int) Math.round(spdScale*Math.sin(heading));
            this.x+=this.spdX;
            this.y+=this.spdY;
        }
        public boolean remove(ArrayList pList){//removes off-screen projectiles
            if(this.x+width<0){
                pList.remove(this);
                return true;
            }
            else if(this.x>WIDTH){
                pList.remove(this);
                return true;
            }
            else if(this.y<0){
                pList.remove(this);
                return true;
            }
            else if(this.y+height>HEIGHT){
                pList.remove(this);
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
            }
        }
        public void draw(Graphics g){
            g.setColor(Color.pink);
            g.fillRect(x,y,width,height);
        }
    }
    Random random=new Random();
    private long memTime=0;
    private final double PI=Math.PI;
    private final int WIDTH = 1400, HEIGHT = 800,SIZE=30,RIGHT=0,LEFT=180,UP=90,DOWN=270;
    private BKG batBKG;
    private int hp;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private ArrayList<Projectile> snow=new ArrayList<>();
    private Player player=new Player(WIDTH/2,HEIGHT/2);

//    Timer snowTimer = new Timer(200, new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent actionEvent) {
//            createSnow(500,0);
//        }
//    });
    public void timerStart(Timer timer){
        timer.start();
    }
    public void timerStop(Timer timer){
        timer.start();
    }
    public Battle(){
        //new screen with new projectiles, player set to middle
        this.hp=100;
    }

    public int random(int low, int high){//easier to get random number
        return random.nextInt(low,high+1);
    }
    public double rad(int degree){//direction in degrees
        if(degree>360){
            double angle=degree%360.0;
            return angle/180.0*PI;
        }
        return degree/180.0*PI;
    }
    private void createSnow(int x, int y){
        snow.add(new Projectile(500+random(0,5)*10,y,10,10, rad(DOWN)));
    }

    public void setUp(){
        projectiles.add(new Projectile(500,500,10,10, rad(350)));
        projectiles.add(new Projectile(100,100,10,10, rad(280)));
    }
    private void runProj(ArrayList<Projectile> pList) {
        for (int i = 0; i < pList.size(); i++) {
            Projectile proj = pList.get(i);
            if (!proj.remove(pList)) {//projectile is not off screen
                proj.move();
                proj.hit();
            }
            else {//take out of list loop
                i--;
            }
        }
    }
    public boolean timeMill(int delay){
        if(System.currentTimeMillis()-memTime>500+random(0,200)){
            memTime=System.currentTimeMillis();
            return true;
        }
        return false;
    }
    public void move(boolean[] keys){
//        runProj(projectiles);
        if(!snow.isEmpty()) {
            runProj(snow);
        }
        if(timeMill(500+random(0,200))){
            memTime=System.currentTimeMillis();
            createSnow(500,0);
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
        for(Projectile s:snow){
            g.setColor(Color.white);
            s.draw(g);
        }
    }

}