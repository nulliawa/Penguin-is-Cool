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
        public Projectile(int x,int y,int width,int height,int spdScale,double heading){
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
        public void move(boolean mv){
            //sideways movement
            this.spdX= (int) Math.round(spdScale*Math.cos(heading));
            this.spdY= -(int) Math.round(spdScale*Math.sin(heading));
            if(mv) {
                this.x += this.spdX;
                this.y += this.spdY;
            }
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
            g.fillRect(x,y,width,height);
        }
    }
    Random random=new Random();
    private long[] memTime=new long[10];
    private static final double PI=Math.PI;
    private static final int WIDTH = 1400, HEIGHT = 800,SIZE=30,RIGHT=0,LEFT=180,UP=90,DOWN=270;
    private static final int SNOWGEN=200,CLOUDSTALL=5000,ICESTALL=1000,GENERATE=2000;
    private BKG batBKG;
    private int hp;
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private static ArrayList<Projectile> clouds=new ArrayList<>();
    private static ArrayList<Projectile> snow=new ArrayList<>();
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
    public double rad(int degree){//direction from degree to radians
        return degree/180.0*PI;
    }
    //snow generates at a random x under a cloud
    private void createSnow(Projectile cloud){
        Rectangle cRect=cloud.getRect();
        snow.add(new Projectile(cRect.x+random(0,19)*10,cRect.y,10,10,5,rad(DOWN)));
    }
    //create main cloud that is above snow
    public void createCloud(){
        clouds.add(new Projectile(random(0,1200),0,200,50,1,rad(RIGHT)));
    }
    public void setUp(){
        projectiles.add(new Projectile(500,500,10,10,1, rad(350)));
        projectiles.add(new Projectile(100,100,10,10,1,rad(280)));
        createCloud();
    }
    private void runProj(ArrayList<Projectile> pList) {
        for (int i = 0; i < pList.size(); i++) {
            Projectile proj = pList.get(i);
            if (!proj.remove(pList)) {//projectile is not off screen
                proj.move(true);
                proj.hit();
            }
            else {//take out of list loop
                i--;
            }
        }
    }
    public boolean timeMill(int delay,int index){
        if(System.currentTimeMillis()-memTime[index]>delay+random(0,200)){
            memTime[index]=System.currentTimeMillis();
            return true;
        }
        return false;
    }
    private double distance(Projectile proj,Player player){
        double dist=0;
        int pX=player.getRect().x,pY=player.getRect().y,pW=player.getRect().width,pH=player.getRect().height;

        return dist;
    }
    public void move(boolean[] keys) {

//        Math.sqrt((double)projectiles.get(0).x+(double)player.getRect().x)

//        runProj(projectiles);
        if (!snow.isEmpty()) {
            runProj(snow);
        }
            for (int i = 0; i < clouds.size(); i++) {
                Projectile cloud = clouds.get(i);
                if (!cloud.remove(clouds)) {//projectile is not off screen
                    cloud.move(true);
                    cloud.hit();
                    if (timeMill(SNOWGEN + random(0, 200), 0)) {
                        createSnow(cloud);
                    }
                } else {//take out of list loop
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
        for(Projectile c:clouds){
            g.setColor(Color.gray);
            c.draw(g);
        }
        for(Projectile s:snow){
            g.setColor(Color.white);
            s.draw(g);
        }
    }

}