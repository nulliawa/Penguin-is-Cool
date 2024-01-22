import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
//switch to battle screen when you touch an enemy
//win the battle by waiting enough time
//lose when hp goes to 0 and return to game's overworld
public class Battle {
    private class Projectile{
        private final Image[] iceImgs=new Image[4];
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
            this.spdScale=spdScale;
            this.cover=cover;
        }
//        //ellipse version
//        public Projectile(int x,int y,int width,int height,double heading,int elli){
//            this.x=x;
//            this.y=y;
//            this.width=width;
//            this.height=height;
//        }

        private void edgeCX(){//edge collision x direction does not let cloud out of edge
            if(this.x<=0){
                x=0;
            }
            else if(this.x+this.width>=WIDTH){
                x=WIDTH-width;
            }
        }
        private boolean remove(ArrayList pList){//removes off-screen projectiles
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
        //accurate sideways movement supported with cos and sin
        private void move(double heading, int spdScale){//"seeking" move (can change heading)
            this.heading=heading;
            this.spdScale=spdScale;
            this.spdX= (int) Math.round(this.spdScale*Math.cos(this.heading));
            this.spdY= -(int) Math.round(this.spdScale*Math.sin(this.heading));
            this.x += this.spdX;
            this.y += this.spdY;
        }
        private void move(){//basic move based on constructor's variables
            this.spdX= (int) Math.round(this.spdScale*Math.cos(this.heading));
            this.spdY= -(int) Math.round(this.spdScale*Math.sin(this.heading));
            this.x += this.spdX;
            this.y += this.spdY;
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
                this.move(rad(LEFT),spdScale);
            }
            else if(pX+pW>cMid){
                this.move(rad(RIGHT),spdScale);
            }
            edgeCX();
        }
        private void setSpdScale(int set){
            this.spdScale=set;
        }
        public Rectangle getRect(){
            return new Rectangle(this.x,this.y,this.width,this.height);
        }
        private void hit(){
            System.out.println(blinks);
            if(blinks>=10){//10 blinks/1 second later
                blinks=0;
                iFrame=false;
            }
            if(this.getRect().intersects(player.getRect())&&!iFrame){
                System.out.println(hp);
                hp-=1;
                iFrame=true;//turns invincibility frames on where player is not hurt
                if(hp<=0){
                    lose=true;
                }
            }
        }
        private void icicleAnimation(Graphics g,Projectile icicle){
            int iX=icicle.getRect().x,iY=icicle.getRect().y;
            int index=frame/ICEANI%iciclesBase.length;//gets a number 0 to 5
            g.drawImage(iceImgs[index],iX,iY,null);
        }
        public void draw(Graphics g){
//            g.fillRect(x,y,width,height);
            g.drawImage(cover,x,y,null);
        }
    }
    private static final Image[] iciclesBase=new Image[4];
    private static final Image[] snows=new Image[8];
    private static Image battleBack;
    private final Random random=new Random();
    private boolean iFrame=false;
    private boolean win,lose;
    private final long[] memTime=new long[10];
    private static final double PI=Math.PI;
    private static final int WIDTH = 1400, HEIGHT = 800,SIZE=30,RIGHT=0,LEFT=180,UP=90,DOWN=270;
    private static final int SNOWGEN=100,CLOUDSTALL=5000,ICESTALL=0, BETWEEN =100,ICEANI=3;
    private static final int CLOUD=0,SNOW=1,BLINK=3;
    private int frame;
    private int hp,blinks=0;
    private final ArrayList<Projectile> icicles = new ArrayList<Projectile>();
    private static final ArrayList<Projectile> clouds=new ArrayList<>();
    private static final ArrayList<Projectile> snow=new ArrayList<>();
    private Player player=new Player(WIDTH/2,HEIGHT/2);
    public Battle(int hp){//create new battle
        this.win=false;
        this.lose=false;
        this.hp=hp;
        //TEMP
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
    private static Image rotateImage(Image image, double angle) {//rotates specified image
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        //convert to buffered image for rotation
        BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        //rotate based on given angle (radians)
        transform.rotate(-angle, width / 2, height / 2);
        //center rotation at center of image
        g2d.setTransform(transform);//perform transformation

        // Draw the rotated image onto the BufferedImage
        g2d.drawImage(image, 0, 0, null);

        g2d.dispose();

        return rotatedImage;
    }
    public static void setUp(){//happens once to initialize all images
        battleBack=new ImageIcon("battleBack.png").getImage().getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
        for(int ice=1;ice<5;ice++){//icicle projectile
            iciclesBase[ice-1]=new ImageIcon("icicle"+ice+".png").getImage();//.getScaledInstance(50,30,Image.SCALE_SMOOTH);
        }
//        for(int rot=0;rot<4;rot++){
//            icicles[rot]= rotateImage(icicles[rot],rad(180)).getScaledInstance(50,30,Image.SCALE_SMOOTH);
//        }
        for(int j=1;j<8;j++){//7 snowflake variants
            snows[j]=new ImageIcon("snow/snowFlake"+j+".png").getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
        }
        snows[0]=new ImageIcon("snowCloud.png").getImage().getScaledInstance(500,100,Image.SCALE_SMOOTH);
    }

    public int getHP(){
        return hp;
    }
    private void nextAtk(){//CHANGE VALUES WIP
        betweenTimer.stop();//stop timer for next time
        int pick=random(0,1);
        System.out.println(pick);
//        if(pick==1||pick==0){
//            createCloud();
//        }
        if(pick==1||pick==0){
            createIcicles();
        }
    }
    public boolean result(){//constantly called to see if battle has ended
        if(lose||win){
            this.stop();
            return true;
        }
        return false;
    }
    //rest period in between attacks
    private final Timer betweenTimer = new Timer(BETWEEN, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("new atk");
            nextAtk();//start next attack (random)
        }
    });
    private final Timer cloudTimer = new Timer(CLOUDSTALL, new ActionListener() {//duration of cloud 10s
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("destroy cloud");
            //stops cloud by removing it
            destroyCloud();
            betweenTimer.start();//start next sequence
        }
    });
    private final Timer icicleTimer=new Timer(ICESTALL, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("destroy icicles");
            icicleTimer.stop();//stop icicles
            betweenTimer.start();//next attack
        }
    });
    //create main cloud that is above snow
    private void createCloud(){
        clouds.add(new Projectile(random(0,900),0,500,100,2,rad(RIGHT),snows[CLOUD]));
        cloudTimer.start();
    }
    //snow generates at a random x under a cloud
    private void createSnow(Projectile cloud){
        Rectangle cRect=cloud.getRect();
        //snow starts appearing in middle of cloud with random x along cloud's width
        snow.add(new Projectile(cRect.x+random(0,49)*10,cRect.y+50,15,15,5,rad(DOWN),snows[random(1,7)]));//snow per tick
        snow.add(new Projectile(cRect.x+random(0,49)*10,cRect.y+50,15,15,5,rad(DOWN),snows[random(1,7)]));
    }

    private void destroyCloud(){
        if(!clouds.isEmpty()) {
            clouds.clear();
        }
        cloudTimer.stop();
    }
    private void createIcicles(){
        int randX=random(0,1);
        if (randX == 1) {
            randX=WIDTH-50;
        }
        int randY=random(0,HEIGHT-30);
        double angle=getAngle(randX+20,randY+20,player);

        this.icicles.add(new Projectile(randX,randY,50,30,15,angle,null));
        for(Projectile ice: icicles){
            if(ice.iceImgs[0]==null){//icicles not filled yet
                for(int rot=0;rot<4;rot++){//rotates base image for creation of new icicle
                    ice.iceImgs[rot]= rotateImage(iciclesBase[rot],angle).getScaledInstance(50,30,Image.SCALE_SMOOTH);
                }
            }
        }
        icicleTimer.start();
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
    public static double rad(int degree){//direction from degree to radians
        return degree/180.0*PI;
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
    //gets the radian angle from projectile to player
    private double getAngle(int startX,int startY,Player player){
        int pX=player.getRect().x, pY=player.getRect().y,pW=player.getRect().width,pH=player.getRect().height;
        double distX=pX+pW-startX;
        double distY=startY-(pY+pH);//y is inverted
        return Math.atan2(distY,distX);//"slope" distY/distX in radians
    }

    public void move(boolean[] keys) {
//        runProj(projectiles);
        if (!snow.isEmpty()) {
            runProj(snow);
        }
        //runs at angle specified when created
        if(!icicles.isEmpty()){
            runProj(icicles);
        }
        for (int i = 0; i < clouds.size(); i++) {
            Projectile cloud = clouds.get(i);
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
        frame++;
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
        for(Projectile p: icicles){
            p.icicleAnimation(g,p);
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