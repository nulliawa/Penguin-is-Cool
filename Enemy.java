import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//enemies are spirits
//collide with them to initiate battle screen
//https://www.spriters-resource.com/fullview/118500/
public class Enemy {
    private int x, y, width, height,spdX,spdY;
    private static final ArrayList<Enemy> enemies=new ArrayList<>();
    public static int frame=0;
    private static final int IDLETIME=5,DEATHTIME=5;
    private static final Image[] idles=new Image[6];
    private static final Image[] deaths=new Image[6];
    private boolean dead;
    public Enemy(){

    }
    public Enemy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.spdX=0;
        this.spdY=0;
        this.width = width;
        this.height = height;
        this.dead=false;
    }
    public static void setUp(){
        //buffered image so first load does is not chunky
        for(int i=1;i<7;i++){//idle animation pics
            Image file=new ImageIcon("spirit/spiritIdle"+i+".png").getImage();
            BufferedImage bufferedImage = new BufferedImage(file.getWidth(null),file.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(file, 0, 0, null);
            idles[i-1]=bufferedImage.getScaledInstance(50,50,Image.SCALE_SMOOTH);
        }
        for(int j=1;j<7;j++){//death animation pics
            Image file=new ImageIcon("spirit/spiritDeath"+j+".png").getImage();
            BufferedImage bufferedImage = new BufferedImage(file.getWidth(null),file.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            bufferedImage.getGraphics().drawImage(file, 0, 0, null);
            deaths[j-1]=bufferedImage.getScaledInstance(60,60,Image.SCALE_SMOOTH);;
        }
        templateWriter();
    }
    private static void templateWriter(){
        //adds enemy at $
        String template="6 1 1 1 1 1 1 1 1 $ 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 7\n" +
                        "3 0 0 0 $ 0 0 0 $ 0 0 0 0 0 0 $ 0 2 2 0 0 0 0 0 0 $ 2 2 0 4\n" +
                        "3 0 0 0 0 0 A A A A A $ A 0 0 0 4 W W 3 0 0 0 0 2 9 W W 3 4\n" +
                        "3 0 $ 0 0 0 A A A A A A A 0 0 0 0 7 W 3 0 0 0 9 W W W W 3 4\n" +
                        "A A 5 A A A A # 0 0 0 0 A A $ 0 0 0 $ 0 0 0 4 W W W W $ 0 4\n" +
                        "3 0 0 0 0 0 A A A 0 0 0 0 A 5 A A A A 0 $ 0 0 7 W W W 3 0 4\n" +
                        "3 0 0 0 0 0 0 0 A 0 0 0 0 A 5 A A A A A 0 0 0 0 7 W W 3 0 4\n" +
                        "3 0 0 0 $ 0 0 0 A A 0 2 2 0 $ 0 2 2 0 A 0 0 0 0 0 1 1 0 0 4\n" +
                        "3 0 0 0 0 0 0 0 0 A 4 W W 3 0 4 W W 3 A 0 0 0 0 0 0 0 0 0 4\n" +
                        "3 0 $ 0 0 0 0 0 0 A 0 1 $ 0 0 4 W W 3 A 0 0 0 0 0 0 $ 2 0 4\n" +
                        "3 0 0 0 0 0 0 0 $ A A A A A 0 $ 1 1 0 A A 5 A 0 0 9 W W 8 2\n" +
                        "8 2 2 2 2 2 2 2 2 2 2 2 0 A A 0 0 0 0 0 0 $ A 0 4 W W W W W\n" +
                        "W W W W W W W W W W W W 3 0 A 0 0 0 0 0 0 0 A 0 0 7 W W W W\n" +
                        "W W W W W W W W W W W W 3 0 A A 0 0 $ 0 0 0 A 0 0 0 1 1 1 1\n" +
                        "W W W W W W W W W W W W 8 2 $ A 2 2 2 2 2 2 A 2 2 2 2 2 $ 9";
        int row=0,col=0;
        for(int loc=0;loc<template.length();loc++) {
            char type = template.charAt(loc);
            if (type == '\n') {//next line/row
                row++;
                col = 0;
            }
            else if(type=='$'){
                enemies.add(new Enemy(100*col+25,100*row+25,50,50));
            }
            if(loc%2==0){//every odd is a space character
                col++;
            }
        }
    }
    public void reset(){
        frame=0;
        enemies.clear();
        templateWriter();
    }
    public void destroy(){//destroys once death animation is over
        for(int i=0;i<enemies.size();i++){
            if(frame/DEATHTIME%deaths.length==5&&enemies.get(i).dead){
                enemies.remove(enemies.get(i));
                i--;
            }
        }
    }

    public void move(){//moves according to background offset
        for(Enemy e:enemies){
            e.spdX= BKG.getOffSpdX();
            e.spdY= BKG.getOffSpdY();
            e.x+=e.spdX;
            e.y+=e.spdY;
        }
    }
    public boolean pCollision(Player p){
        for(Enemy e:enemies){
            if(p.getRect().intersects(e.getRect())&&!e.dead){
                frame=0;
                e.dead=true;//stops checking collisions once enemy initiates battle with player
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
        int eX=e.getRect().x,eY=e.getRect().y;
        int index=frame/IDLETIME%idles.length;//gets a number 0 to 5
        g.drawImage(idles[index],eX,eY,null);
    }
    private void death(Graphics g, Enemy e){
        int eX=e.getRect().x,eY=e.getRect().y;
        int index=frame/DEATHTIME%deaths.length;
        g.drawImage(deaths[index],eX,eY,null);
    }
    public void draw(Graphics g){
        frame++;//which frame needs to be put up, constantly increasing
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        for(Enemy e:enemies){
            if(e.dead){
                death(g,e);
            }
            else{
                idle(g,e);
            }
        }
    }
    public void draw(Graphics g, int type){//type0=idle, type1=death
        frame++;//which frame needs to be put up, constantly increasing
        if(frame>=2147483647){//limit on ints
            frame=0;
        }
        int index=frame/IDLETIME%idles.length;//gets a number 0 to 5
        if(type==0){
            g.drawImage(idles[index],x,y,null);
        }
        else if(type==1){
            g.drawImage(deaths[index],x,y,null);
        }
    }
}