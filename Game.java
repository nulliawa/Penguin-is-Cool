import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Game extends BaseFrame {
//    private final int WIDTH = 1920, HEIGHT = 1080;
    private final int WIDTH=1366,HEIGHT=768;
    public final int MENU = 0, GAME = 1;
    int screen = MENU; // Change to MENU when done gameplay
    private Player player;
    private BKG bkg;
    private boolean sideMove=false;
    private int offsetDistance = 300; // How far from the edge of the offset
    // private ArrayList<Button> menuButtons = new ArrayList<>();

    public Game(String title, int width, int height) {
        super(title, width, height);

        // bkg = new BKG(width / 2, height / 2);
        bkg = new BKG(0,0,1500, 1000);//wip
        bkg.setUp();
        player = new Player(width/2, height/2);
        player.setX(width/2 - player.getRect().width/2);
        player.setY(height/2 - player.getRect().height/2);
    }
    private void pFixX(){
        player.setX(WIDTH/2-player.getRect().width/2);
    }
    private void pFixY(){
        player.setY(HEIGHT/2-player.getRect().height/2);
    }
    public void move(KeyEvent e) {
        // If player gets to the edge of the background, stop moving the background, instead move the player
        int bkgX=bkg.getOffX();
        int bkgY=bkg.getOffY();
        int pX=player.getRect().x;
        int pY=player.getRect().y;
        int[] coord= new int[]{bkgX, bkgY, pX, pY};

//        System.out.println(Arrays.toString(coord));
//        System.out.print(bkg.edgeX());
//        System.out.print(player.pushX());
//        System.out.print(" ");
//        System.out.print(bkg.edgeY());
//        System.out.print(player.pushY());
//        System.out.print(" ");
//        System.out.print(player.isFixed());
//        System.out.println();

//        if(Math.abs(pX-WIDTH/2-player.getRect().width/2)<=10){
//            pFixX();
//        }
//        if(Math.abs(pY-HEIGHT/2-player.getRect().height/2)<=10){
//            pFixY();
//        }

        if(player.isFixedX()){
            //player is fixed to middle line so not at an edge, can move background left/right
            bkg.move(e.getKeyCode(),false);
        }
        if(player.isFixedY()){
            bkg.move(e.getKeyCode(),true);
        }

//        if(!player.edgeX()){
//            bkg.move(e.getKeyCode(),true);
//            pFixX();
//        }
//        if(!player.edgeY()){
//            bkg.move(e.getKeyCode(),false);
//            pFixY();
//        }
        if(bkg.edgeX()){//arrive at edge of background left/right direction
            System.out.println(Arrays.toString(coord));//note
            player.move(e.getKeyCode(),false);
            //player itself can move left/right and background stops left/right movement
        }
        if(bkg.edgeY()){
            player.move(e.getKeyCode(),true);
        }


//        if(bkg.edgeX()){
//            player.move(e.getKeyCode(),true);
//        }

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


//        if (bkg.getOffX() <= 0 && bkg.getOffY() <= 0) {
//            System.out.println(player.getRect().x);
//            System.out.println(WIDTH/2);
//            if (player.getRect().x == WIDTH/2 - 30 && player.isWalkRight()) {
//                bkg.move(e.getKeyCode());
//            }
//
//        }
//        if (bkg.getOffX() > -10) {
//            player.move(e.getKeyCode());
//            if (player.getRect().x == WIDTH/2) {
//                bkg.moveRight(e.getKeyCode());
//            }
//        } else {
//            bkg.moveRight(e.getKeyCode());
//        }
//
//        if (bkg.getOffY() > -10) {
//            player.move(e.getKeyCode());
//            if (player.getRect().y == HEIGHT/2) {
//                bkg.move(e.getKeyCode());
//            }
//        } else {
//            bkg.moveDown(e.getKeyCode());
//        }
//        System.out.println(bkg.getOffX());
//        System.out.println(bkg.getOffY());
//
//        if (bkg.getOffX() <= 0 || bkg.getOffY() <= 0) {
//            bkg.move(e.getKeyCode());
//            if (player.isWalkRight() || player.isWalkDown()) {
//                player.move(e.getKeyCode());
//            }
//        } else {
//            player.move(e.getKeyCode());
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
        Rectangle bg = new Rectangle(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.fillRect(bg.x, bg.y, bg.width, bg.height);

        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 150));
        g.setColor(Color.CYAN);
        g.drawString("Penguin is Cool",  WIDTH/2 - 550, 200);

        // ARRAYS BELOW ?
        int[][] buttons = {{WIDTH/2 - 200, 250, 400, 200}, {WIDTH/2 - 200, 500, 400, 200}};
        String[] buttonText = {"Play", "Tutorial"};
        int[][] textCoordinates = {{640, 365}, {600, 600}};


        for (int i = 0; i < buttons.length; i++) {
            g.drawRect(buttons[i][0], buttons[i][1], buttons[i][2], buttons[i][3]);

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
            g.drawString(buttonText[i], textCoordinates[i][0], textCoordinates[i][1]);
        }

//        for (int[] button : buttons) {
//            g.drawRect(button[0], button[1], button[2], button[3]);
//            g.drawString(, 100, 100);
//        }
    }

    public void drawGame(Graphics g) {
        bkg.draw(g, null);
        g.setColor(Color.red);
        player.draw(g);
        g.setColor(Color.green);
    }

    public void draw(Graphics g) {//test
        if (screen == MENU) {
            drawMenu(g);
        } else if (screen == GAME) {
            drawGame(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        move(e);
        // player.keyPress(e.getKeyCode());
    }


    public static void main(String[] args) {
        Game game = new Game("Penguin Is Cool", 1366, 768);
    }
}
