import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
//switch to battle screen when you touch an enemy
//win the battle by waiting enough time
//lose when hp goes to 0 and return to game's overworld
public class Battle {
    private class Projectile{
        private int x,y,spdX,spdY,spdScale;
        private final int width,height;
//        private int elli;
        private double heading;
        private Image cover;
        public Projectile(int x,int y,int width,int height,int spdScale,double heading,Image cover){
            this.x=x;
            this.y=y;
            this.spdX=0;
            this.spdY=0;
            this.width=width;
            this.height=height;
            this.heading=heading;
            this.spdScale=5;
            this.cover=cover;
        }
//        //ellipse version
//        public Projectile(int x,int y,int width,int height,double heading,int elli){
//            this.x=x;
//            this.y=y;
//            this.width=width;
//            this.height=height;
//        }
        public void move(){
            //sideways movement supported
            this.spdX= (int) Math.round(spdScale*Math.cos(heading));
            this.spdY= -(int) Math.round(spdScale*Math.sin(heading));
            this.x += this.spdX;
            this.y += this.spdY;
        }
        public void edgeCX(){//edge collision x direction does not let cloud out of edge
            if(this.x<=0){
                x=0;
            }
            else if(this.x+this.width>=WIDTH){
                x=WIDTH-width;
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
        //cloud "follows" x position of player
        private void followPlayerX(Player player){
            int pX=player.getRect().x, pW=player.getRect().width/2;
            int cMid=this.width/2+this.x;
            if(pX<cMid&&pX+pW>cMid){//stop moving
                this.spdScale=0;
            }
            else {
                setSpdScale(7);
            }
            if(pX<cMid){
                this.setSpeeds(LEFT,spdScale);
            }
            else if(pX+pW>cMid){
                this.setSpeeds(RIGHT,spdScale);
            }
            this.x += this.spdX;
            this.y += this.spdY;
            edgeCX();
        }
        private void setSpeeds(int heading,int spdScale){
            spdX =(int)Math.round(spdScale*Math.cos(rad(heading)));
            spdY =-(int)Math.round(spdScale*Math.sin(rad(heading)));
        }
        private void setSpdScale(int set){
            this.spdScale=set;
        }
        public Rectangle getRect(){
            return new Rectangle(this.x,this.y,this.width,this.height);
        }
        public void hit(){
            if(blinks==10){//10 blinks/1 second later
                blinks=0;
                iFrame=false;
            }
            if(this.getRect().intersects(player.getRect())&&!iFrame){
                hp-=1;
                iFrame=true;//turns invincibility frames on where player is not hurt
                if(hp<=0){
                    lose=true;
                }
            }
        }
        public void draw(Graphics g){
//            g.fillRect(x,y,width,height);
            g.drawImage(cover,x,y,null);
        }
    }
    private static Image[] covers;
    private static Image battleBack;
    private final Random random=new Random();
    private boolean iFrame=false;
    private boolean win,lose;
    private final long[] memTime=new long[10];
    private static final double PI=Math.PI;
    private static final int WIDTH = 1400, HEIGHT = 800,SIZE=30,RIGHT=0,LEFT=180,UP=90,DOWN=270;
    private static final int SNOWGEN=100,CLOUDSTALL=5000,ICESTALL=1000,GENERATE=2000;
    private static final int CLOUD=0,SNOW=1,BLINK=3;
    private int hp,blinks=0;
    private final ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private static final ArrayList<Projectile> clouds=new ArrayList<>();
    private static final ArrayList<Projectile> snow=new ArrayList<>();
    private Player player=new Player(WIDTH/2,HEIGHT/2);
    public Battle(int hp){//create new battle
        this.win=false;
        this.lose=false;
        this.hp=hp;
        //new screen with new projectiles, player set to middle
    }
    public void start(){
        betweenTimer.start();
    }
    public void stop(){
        betweenTimer.stop();
        destroyCloud();
        if(!snow.isEmpty()){
            snow.clear();
        }
    }
    public static void setUp(Image[] images,Image background){//happens once to initialize all images
        battleBack=background;
        covers=images;
    }
    public int getHP(){
        return hp;
    }
    private void nextAtk(){
        betweenTimer.stop();//stop timer for next time
        createCloud();
    }
    public boolean result(){//constantly called to see if battle has ended
        if(lose||win){
            this.stop();
            return true;
        }
        return false;
    }
    //rest period in between attacks
    private final Timer betweenTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("new atk");
            nextAtk();//start next attack (random)
        }
    });
    private final Timer cloudTimer = new Timer(CLOUDSTALL, new ActionListener() {//duration of cloud 10s
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("new cloud");
            //stops cloud by removing it
            destroyCloud();
            betweenTimer.start();//start next sequence
        }
    });
    //create main cloud that is above snow
    public void createCloud(){
        clouds.add(new Projectile(random(0,900),0,500,100,2,rad(RIGHT),covers[CLOUD]));
        cloudTimer.start();
    }
    public void destroyCloud(){
        if(!clouds.isEmpty()) {
            clouds.removeFirst();
        }
        cloudTimer.stop();
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
    public int random(int low, int high){//easier to get random number
        return random.nextInt(low,high+1);
    }
    public double rad(int degree){//direction from degree to radians
        return degree/180.0*PI;
    }
    //snow generates at a random x under a cloud
    private void createSnow(Projectile cloud){
        Rectangle cRect=cloud.getRect();
        //snow starts appearing in middle of cloud with random x along cloud's width
        snow.add(new Projectile(cRect.x+random(0,49)*10,cRect.y+50,15,15,5,rad(DOWN),covers[random(1,7)]));//snow per tick
        snow.add(new Projectile(cRect.x+random(0,49)*10,cRect.y+50,15,15,5,rad(DOWN),covers[random(1,7)]));
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
    private double distance(Projectile proj,Player player){//USE MATH.ATAN2
        double dist=0;
        int pX=player.getRect().x,pY=player.getRect().y,pW=player.getRect().width,pH=player.getRect().height;

        return dist;
    }

//    public void setUp(){//old setup tester
//        projectiles.add(new Projectile(500,500,10,10,1, rad(350)));
//        projectiles.add(new Projectile(100,100,10,10,1,rad(280)));
//    }

    public void move(boolean[] keys) {
//        runProj(projectiles);
        if (!snow.isEmpty()) {
            runProj(snow);
        }
        for (int i = 0; i < clouds.size(); i++) {Projectile cloud = clouds.get(i);
            if (!cloud.remove(clouds)) {//projectile is not off screen
                cloud.followPlayerX(player);
                cloud.hit();
                //create new snowflake every 200milliseconds with 200ms variation
                if (timeMill(SNOWGEN + random(0, 200), SNOW)) {
                    createSnow(cloud);
                }
            } else {//take out of list loop
                i--;
            }
        }
        player.move(keys);
    }
    public void draw(Graphics g){
//        g.setColor(Color.blue);
//        g.fillRect(0,0,WIDTH,HEIGHT);
        //background
        g.drawImage(battleBack,0,0,null);
        if(iFrame) {
            if (timeMill(100, BLINK)) {
                blinks++;
                player.draw(g);
            }
        }
        else{
            player.draw(g);
        }
        for(Projectile p:projectiles){
            p.draw(g);
        }
        for(Projectile flake:snow){///NEED IMAGE
//            g.setColor(Color.white);
            flake.draw(g);//section 1-7 inclusive are snowflake variations
        }
        for(Projectile nimbus:clouds){
            nimbus.draw(g);
        }
    }
}