import com.sun.source.doctree.AttributeTree;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.ArrayList;

public class Game extends BaseFrame {
    //    private final int WIDTH = 1920, HEIGHT = 1080;
    private final int WIDTH=1366,HEIGHT=768;
    public final int MENU = 0, GAME = 1;
    int screen = MENU; // Change to MENU when done gameplay
    private Player player;
    private BKG bkg;
    private boolean sideMoveX=true,sideMoveY=true;
    private int offsetDistance = 300; // How far from the edge of the offset
    // private ArrayList<Button> menuButtons = new ArrayList<>();

    public Game(String title, int width, int height) {
        super(title, width, height);

        // bkg = new BKG(width / 2, height / 2);
        bkg = new BKG(0,0,1500, 1000);//wip
        bkg.setup();
        player = new Player(width/2, height/2);
        player.setX(width/2 - player.getRect().width/2);
        player.setY(height/2 - player.getRect().height/2);
    }
    //    private void pFixX(){
//        player.setX(WIDTH/2-player.getRect().width/2);
//    }
//    private void pFixY(){
//        player.setY(HEIGHT/2-player.getRect().height/2);
//    }
    public void move() {
        player.collision(new Rectangle());//WIP
        // If player gets to the edge of the background, stop moving the background, instead move the player
        int bkgX=bkg.getOffX();
        int bkgY=bkg.getOffY();
        int pX=player.getRect().x;
        int pY=player.getRect().y;
        int[] coord= new int[]{bkgX, bkgY, pX, pY};

        System.out.println(Arrays.toString(coord));
//        System.out.print(bkg.edgeX());
//        System.out.print(player.pushX());
//        System.out.println(sideMoveX);
//        System.out.print(" ");
//        System.out.println(sideMoveY);
//        System.out.print(bkg.edgeY());
//        System.out.print(player.pushY());
//        System.out.print(" ");
//        System.out.print(player.isFixed());
//        System.out.println();

//        if(bkg.edgeX()&&player.isFixedX()&&sideMoveX){//switch between true/false
//            sideMoveX=false;
//        }
//        else if(bkg.edgeX()&&player.isFixedX()&&!sideMoveX){
//            sideMoveX=true;
//        }
//        if(bkg.edgeY()&&player.isFixedY()&&sideMoveY){//switch between true/false
//            sideMoveY=false;
//        }
//        else if(bkg.edgeY()&&player.isFixedY()&&!sideMoveY){
//            sideMoveY=true;
//        }
        if(bkg.edgeX()){//arrive at edge of background left/right direction
            player.move(keys,false);
            //player itself can move left/right and background stops left/right movement
        }
        if(bkg.edgeY()){
            player.move(keys,true);
        }

        if(player.isFixedX()){
            //player is fixed to middle line so not at an edge, can move background left/right
            bkg.move(keys,false);
        }
        if(player.isFixedY()){
            bkg.move(keys,true);
        }

//        else{
////            player.setX(WIDTH/2-player.getRect().width/2);
////            player.setY(HEIGHT/2-player.getRect().height/2);
//            bkg.move(e.getKeyCode(),true);//up/down movement possible
//            //left/right movement not possible
//        }

//        if(bkg.edgeY()){
//            player.move(e.getKeyCode(),false);
//        }
//        else{
//            bkg.move(e.getKeyCode(),false);//left/right movement only
//        }


        /*
        if (x < Math.abs(background.x - offsetDistance) || y < Math.abs(background.y - offsetDistance)) { // If the penguin is within the offset range
        if player distance is near the edge, then player.move(), else bkg.move()
         */
        //old 2
//        if(!sideMove){//at middle
//            sideMove=bkg.atEdge();
//        }
//        else{
//            sideMove=!player.atEdge();
//        }
//        if(!sideMove) {//not at edge, then check if background is at edge and player is at edge
//            sideMove = bkg.atEdge();
//        }
//        else{//once both are true, then background will be fixed so check for when player not at edge
//            sideMove=!player.atEdge();
//        }

        //old code
//        if (player.getRect().x < Math.abs(bkg.getOffX() + offsetDistance) || player.getRect().y < Math.abs(bkg.getRect().y - offsetDistance)) { // Check other 3 conditions
//            player.move(e.getKeyCode());
//        } else {
//            bkg.move(e.getKeyCode());
//        }
        //old 2
//        if(sideMove){
//            player.move(e.getKeyCode());
//        }
//        else{
//            bkg.move(e.getKeyCode());
//        }
    }

    public void drawMenu(Graphics g) {
        final int X = 0, Y = 1, W = 2, H = 3; // Variables for button creation

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 150));
        g.setColor(Color.CYAN);
        g.drawString("Penguin is Cool",  WIDTH/2 - 550, 200);

        // Arrays for buttons
        int[][] buttonCoordinates = {{WIDTH/2 - 200, 275, 400, 150}, {WIDTH/2 - 200, 525, 400, 150}};
        String[] buttonText = {"Play", "Tutorial"};
        int[][] textCoordinates = {{640, 365}, {590, 620}};

        // ArrayList<Button> buttons = new ArrayList<Button>();


        // Button creation
        for (int i = 0; i < buttonCoordinates.length; i++) {
            g.setColor(Color.BLUE); //TEMP COLOUR
            Button temp = new Button(buttonCoordinates[i][X], buttonCoordinates[i][Y], buttonCoordinates[i][W], buttonCoordinates[i][H]); // Creates a new button object with params

            // buttons.add(temp);
            g.fillRect(temp.getRect().x, temp.getRect().y, temp.getRect().width, temp.getRect().height); // Making new buttons

            if (temp.isHover(mx, my)) {
                // System.out.println("HELP");

                g.setColor(Color.DARK_GRAY);
                g.fillRect(temp.getRect().x, temp.getRect().y, temp.getRect().width, temp.getRect().height);

            }

            if (temp.isClicked(mx, my, mb)) {
                screen = GAME;
            }

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString(buttonText[i], textCoordinates[i][0], textCoordinates[i][1]);
        }

        // WIP: NEED TO DIFFERENTIATE CLICKS
    }

    public void drawGame(Graphics g) {
        bkg.draw(g, null);
        g.setColor(Color.red);
        player.draw(g);
        g.setColor(Color.green);
    }
    @Override
    public void draw(Graphics g) {
        if (screen == MENU) {
            drawMenu(g);
        } else if (screen == GAME) {
            drawGame(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {//constant new checking for keys[], based on actionPerformed()
        super.keyPressed(e);
        keys[e.getKeyCode()]=true;
    }
    @Override
    public void keyReleased(KeyEvent e){
        super.keyPressed(e);
        keys[e.getKeyCode()]=false;
    }

//        System.out.println(keys[KeyEvent.VK_W]);

//    @Override
//    public void mousePressed(MouseEvent e) {
//        super.mousePressed(e);
//
//    }


    public static void main(String[] args) {
        Game game = new Game("Penguin Is Cool", 1366, 768);
    }
}
