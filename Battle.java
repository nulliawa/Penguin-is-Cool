import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
//switch to battle screen when you touch an enemy
//win the battle by waiting enough time (survive 3 randomly generated attacks) and get 3 fish
//attacks are:
//-Icicles that seek to player's path in straight line
//-Snow cloud that drops snow randomly from above
//-Seekers that continuously pathfind to the player's location
//-Spikes that appear on the sides or top/bottom accompanying another of the 3 mentioned atks
//lose when hp goes to 0 and return to game's overworld to receive lose animation
//Icicles/Spikes - https://pimen.itch.io/ice-spell-effect-01
//Seekers - https://itch.io/t/2995590/ice-spells-pixel-vfx-for-your-game
//Background - https://assetstore.unity.com/packages/tools/level-design/2d-parallax-background-parallax-2d-parallax-effect-254988
//Snowflakes - https://opengameart.org/content/pixel-art-snowflakes
//Cloud - https://www.reddit.com/r/PixelArt/comments/dhdgab/storm_clouds_practice/
public class Battle {
    private class Projectile{
        private final Image[] iceImgs=new Image[10];
        private final Image[] spikesImgs=new Image[8];
        private final Image[] seekerImgs=new Image[4];
        private int x,y,spdX,spdY,spdScale;
        private final int width,height;
        private double heading;
        private final Image cover;
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
        private void edgeCX(){//edge collision x direction does not let cloud out of edge
            if(this.x<=0){
                x=0;
            }
            else if(this.x+this.width>=WIDTH){
                x=WIDTH-width;
            }
        }
        private boolean clean(ArrayList<Projectile> pList){//removes off-screen projectiles
            if(this.x+width<0){
                pList.remove(this);
                return true;
            }
            else if(this.x>WIDTH){
                pList.remove(this);
                return true;
            }
            else if(this.y+height<0){
                pList.remove(this);
                return true;
            }
            else if(this.y>HEIGHT){
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
            int pX=player.getRect().x, pM=player.getRect().width/2;
            int cMid=this.width/2+this.x;
            if(pX<cMid&&pX+pM>cMid){//stop moving
                this.spdScale=0;
            }
            else {
                setSpdScale(7);
            }
            if(pX<cMid){
                this.move(rad(LEFT),spdScale);
            }
            else if(pX+pM>cMid){
                this.move(rad(RIGHT),spdScale);
            }
            edgeCX();
        }
        private void seek(Player player){
            this.move(getAngle(x+width/2,y+width/2,player),5);//finds angle from center to player
            for(Projectile ice:seekers){
                for (int rota = 0; rota < 4; rota++) {//rotates base image according to which heading it was created with
                    ice.seekerImgs[rota]=rotateImage(seekersBase[rota],ice.width,ice.height,ice.heading);
                }
            }
        }
        private void setSpdScale(int set){
            this.spdScale=set;
        }
        public Rectangle getRect(){
            return new Rectangle(this.x,this.y,this.width,this.height);
        }
        private void icicleAnimation(Graphics g){
            int index=frame/ICEANI%iciclesBase.length;//the frame based on animation time
            g.drawImage(iceImgs[index],x,y,null);
        }
        private void spikeAnimation(Graphics g){
            int index=frame/SPIKEANI%spikesBase.length;//gets to next index when frame/ani allows
            g.drawImage(spikesImgs[index],x,y,null);
        }
        private void seekerAnimation(Graphics g){
            int index=frame/SEEKERANI%seekersBase.length;
            g.drawImage(seekerImgs[index],x,y,null);
        }
        public void draw(Graphics g){
            g.drawImage(cover,x,y,null);
        }
    }
    private static final Image[] iciclesBase=new Image[10];
    private static final Image[] spikesBase=new Image[8];
    private static final Image[] snows=new Image[8];
    private static final Image[] seekersBase=new Image[4];
    private static Image battleBack;
    private final Random random=new Random();
    private boolean iFrame=false;
    private boolean win,lose;
    private final long[] memTime=new long[10];
    private static final double PI=Math.PI;
    private static final int WIDTH = 1400, HEIGHT = 800,RIGHT=0,UP=90,LEFT=180,DOWN=270;
    private static final int SNOWGEN=100,CLOUDSTALL=10000,ICICLESTALL=10000,SEEKERSTALL=12000,BETWEEN =4000,ICEANI=5,SPIKEANI=3,SEEKERANI=9;
    //time stats for easy adjustment
    private static final int CLOUD=0,SNOW=1,ICICLE=2,BLINK=3,SEEKER=4;
    //index of memTime for repeatable actions timer timeMill()
    private static int frame;
    //animation frames
    private static int totalAtks;
    private int hp,blinks=0;
    private final ArrayList<Projectile> icicles = new ArrayList<>();
    private static final ArrayList<Projectile> clouds=new ArrayList<>();
    private static final ArrayList<Projectile> snow=new ArrayList<>();
    private static final ArrayList<Projectile> spikes=new ArrayList<>();
    private static final ArrayList<Projectile> seekers=new ArrayList<>();
    private final Player player=new Player(WIDTH/2,HEIGHT/2);
    public Battle(int hp){//create new battle
        this.win=false;
        this.lose=false;
        this.hp=hp;
        //new screen with new projectiles, player set to middle
    }
    //one orientation only rotation
    private static Image rotateImage(Image image, double angle) {//rotates specified image to angle
        //help from ChatGPT3.5
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        //convert to buffered image for rotation
        BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        //rotate based on given angle (radians)
        transform.rotate(-angle,width/2.0,height/2.0);
        //center rotation at center of image
        g2d.setTransform(transform);//perform transformation

        // Draw the rotated image onto the BufferedImage
        g2d.drawImage(image, 0, 0, null);

        g2d.dispose();

        return rotatedImage;
    }
    //help from ChatGPT3.5
    //other rotation for seekers that does not cut off on original image size
    private static Image rotateImage(Image image, int scaleX,int scaleY, double angle){
        //input image must be scaled to correct size first
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        //new
        //get the translation distance of where the image should be when it is rotated
        int transX=Math.abs(width-scaleX)/2;
        int transY=Math.abs(height-scaleY)/2;

        //convert to buffered image for rotation
        BufferedImage rotatedImage = new BufferedImage(scaleX,scaleY, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        //new
        //moves the image over to the center
        transform.translate(transX,transY);
        //rotate based on given angle (radians, flipped y axis)
        transform.rotate(-angle, width/2.0,height/2.0);
        //center rotation at center of original image
        g2d.setTransform(transform);//perform transformation

        // Draw the rotated image onto the BufferedImage
        g2d.drawImage(image, 0, 0,null);

        g2d.dispose();

        return rotatedImage;
    }
    public static void setUp(){//happens once to initialize all images
        battleBack=new ImageIcon("battleBack.png").getImage().getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
        for(int ice=1;ice<11;ice++){//icicle projectile 10 frames
            iciclesBase[ice-1]=new ImageIcon(("icicle/VFX 1 Repeatable"+ice+".png")).getImage();
        }
        for(int j=1;j<8;j++){//7 snowflake variants
            snows[j]=new ImageIcon("snow/snowFlake"+j+".png").getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
        }
        snows[0]=new ImageIcon("snowCloud.png").getImage().getScaledInstance(500,100,Image.SCALE_SMOOTH);
        for(int s=1;s<9;s++) {//spike projectile 8 frames
            spikesBase[s-1] = new ImageIcon("icicle/Ice VFX 2 Active"+s+".png").getImage();
        }
        for(int q=1;q<5;q++){//seeker projectile 4 frames
            seekersBase[q-1]=new ImageIcon("icicle/icicle"+q+".png").getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        }
    }
    public void start(){
        betweenTimer.start();
        totalAtks=-1;//3 attacks survived to win
    }
    public void stopAll(){
        frame=0;
        betweenTimer.stop();
        destroyCloud();
        icicleTimer.stop();
        seekerTimer.stop();
        snow.clear();
        icicles.clear();
        spikes.clear();
        seekers.clear();
    }
    public int getHP(){
        return hp;
    }
    public void setHP(int setTo){
        hp=setTo;
    }
    private void hit(Projectile projectile){//projectile hits player
        if(projectile.getRect().intersects(player.getRect())&&!iFrame){
            hp-=1;
            iFrame=true;//turns invincibility frames on where player is not hurt
            if(hp<=0){
                lose=true;
            }
        }
    }
    public boolean result(){//constantly called to see if battle has ended
        if(lose||win){
            this.stopAll();
            return true;
        }
        return false;
    }
    public boolean getWin(){
        return win;
    }
    public boolean getLose(){
        return lose;
    }
    public void setWin(boolean won){
        win=won;
    }
    public void setLose(boolean setTo){
        lose=setTo;
    }
    private void nextAtk(){
        betweenTimer.stop();//stop timer for next time
        int pick=random(0,2);
        if(pick==0){
            createSpikes(false,true);
            createCloud();//creates cloud, creates snow
        }
        else if(pick==1){
            createSpikes(true,false);//spikes accompanying for icicle atk duration
            icicleTimer.start();//starts creating icicles
        }
        else if(pick==2){
            createSpikes(true,true);
            seekerTimer.start();//start (creates 1 seeker automatically)
        }
    }
    //rest period in between attacks
    private final Timer betweenTimer = new Timer(BETWEEN, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            totalAtks++;
            if(totalAtks<3) {
                nextAtk();//start next attack (random)
            }
            else{//win when 3 attacks have passed
                win=true;
            }
        }
    });
    private final Timer cloudTimer = new Timer(CLOUDSTALL, new ActionListener() {//duration of cloud 10s
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //stops cloud by removing it
            destroyCloud();
            spikes.clear();
            betweenTimer.start();//start next sequence
        }
    });
    private final Timer icicleTimer=new Timer(ICICLESTALL, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            icicleTimer.stop();//stop icicles
            spikes.clear();
            betweenTimer.start();//next attack
        }
    });
    private final Timer seekerTimer=new Timer(SEEKERSTALL, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            seekerTimer.stop();
            spikes.clear();
            betweenTimer.start();
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
    //icicles go in a straight line towards player once they are created
    private void createIcicle(){
        int randX=random(0,1);
        if (randX == 1) {//create on either left or right of screen not in middle
            randX=WIDTH-150;
        }
        int randY=random(0,HEIGHT-75);//random y
        double angle=getAngle(randX,randY,player);
        this.icicles.add(new Projectile(randX, randY, 150, 75, 15, angle, null));

        for(Projectile ice: icicles){
            if(ice.iceImgs[0]==null) {//icicles not filled yet
                for(int rota=0;rota<10;rota++){//rotates base image for creation of new icicle image facing towards player
                    ice.iceImgs[rota] = rotateImage(iciclesBase[rota], angle).getScaledInstance(150, 75, Image.SCALE_SMOOTH);
                }
            }
        }
    }
private void createSpikes(boolean topBot,boolean leftRight){
    if(topBot) {//while normal icicles happening
        for (int i = 1; i < 13; i++) {
            //bottom and top spikes
            spikes.add(new Projectile(i * 100, HEIGHT - 100, 100, 100, 0, 0, null));
            spikes.add(new Projectile(i * 100, 0, 100, 100, 0, rad(180), null));
            //width/height reduced due to image scaling
        }
    }
    if(leftRight){//while cloud/snow atk is performed
        for (int j = 0; j < 8; j++) {
            //left and right spikes
            spikes.add(new Projectile(0, j * 100, 100, 100, 0, rad(-90), null));
            spikes.add(new Projectile(WIDTH - 100, j * 100, 100, 100, 0, rad(90), null));
        }
    }
    for(Projectile ice:spikes){
        if(ice.spikesImgs[0]==null) {//no images yet
            for (int rota = 0; rota < 8; rota++) {//rotates base image according to which heading it was created with
                ice.spikesImgs[rota]=rotateImage(spikesBase[rota],ice.heading).getScaledInstance(100,100,Image.SCALE_SMOOTH);
            }
        }
    }
}
    //seekers follow the player
    private void createSeeker(){
        int randX=random(0,1);
        int randY=random(0,1);
        if(randX==1){
            randX=WIDTH-100;
        }
        if(randY==1){
            randY=HEIGHT-100;
        }
        seekers.add(new Projectile(randX,randY,100,100,10,getAngle(0,0,player),null));
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
            if (!proj.clean(pList)) {//projectile is not off screen
                proj.move();
                this.hit(proj);
            }
            else {//take out of list loop
                i--;
            }
        }
    }
    //gets the radian angle from projectile to player
    private double getAngle(int startX,int startY,Player player){
        int pX=player.getRect().x, pY=player.getRect().y,pW=player.getRect().width,pH=player.getRect().height;
        double distX=pX+pW/2-startX;
        double distY=startY-(pY+pH/2);//y is inverted
        return Math.atan2(distY,distX);//"slope" distY/distX in radians
    }

    public void move(boolean[] keys) {
        if (!snow.isEmpty()) {
            runProj(snow);
        }
        if(icicleTimer.isRunning()){
            if(timeMill(2000,ICICLE)){
                for(int icy=0;icy<6;icy++){
                    createIcicle();
                }
            }
        }
        if(seekerTimer.isRunning()){
            if(timeMill(2500,SEEKER)){
                createSeeker();
            }
        }
        //runs at angle specified when created
        if(!icicles.isEmpty()){
            runProj(icicles);
        }
        if(!spikes.isEmpty()){
            runProj(spikes);
        }
        if(!seekers.isEmpty()){
            if(seekerTimer.isRunning()){
                for (int i = 0; i < seekers.size(); i++) {
                    Projectile proj = seekers.get(i);
                    if (!proj.clean(seekers)) {//projectile is not off screen or hit player
                        proj.seek(player);
                        this.hit(proj);
                    } else {//list loop index set back
                        i--;
                    }
                }
            }
            else{//seekers will "coast" unless atk sequence is seekers
                runProj(seekers);
            }
        }
        for (int i = 0; i < clouds.size(); i++) {
            Projectile cloud = clouds.get(i);
            if (!cloud.clean(clouds)) {//projectile is not off screen
                cloud.followPlayerX(player);
                this.hit(cloud);
                //create new snowflake every 100milliseconds with 200ms variation
                if (timeMill(SNOWGEN + random(0, 200), SNOW)) {
                    createSnow(cloud);
                }
            } else {//take out of list loop
                i--;
            }
        }
        player.move(keys);
    }
    public void drawBack(Graphics g){
        //background
        g.drawImage(battleBack,0,0,null);
    }
    public void draw(Graphics g){
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        frame++;
        drawBack(g);

        if(blinks>=10){//10 blinks/1 second later
            blinks=0;
            iFrame=false;//turns invicibility frames off
        }
        if(iFrame) {
            if (timeMill(100, BLINK)) {
                blinks++;
                player.draw(g);
            }
        }
        else{
            player.draw(g);
        }

        for(Projectile flake:snow){
            flake.draw(g);//snows[1-7] inclusive are snowflake variations
        }
        for(Projectile poke:spikes){
            poke.spikeAnimation(g);
        }
        for(Projectile iceType1: icicles){
            iceType1.icicleAnimation(g);
        }
        for(Projectile iceType2: seekers){
            iceType2.seekerAnimation(g);
        }
        for(Projectile nimbus:clouds){
            nimbus.draw(g);
        }
    }
}