import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game extends BaseFrame {
//    private final int WIDTH = 1920, HEIGHT = 1080;
    private final int WIDTH=1366,HEIGHT=768;
    public final int MENU = 0, GAME = 1;
    int screen = GAME; // Change to MENU when done gameplay
    private Player player;
    private BKG bkg;
    private boolean sideMove=false;
    private int offsetDistance = 300; // How far from the edge of the offset
    // private ArrayList<Button> menuButtons = new ArrayList<>();

    public Game(String title, int width, int height) {
        super(title, width, height);

        // bkg = new BKG(width / 2, height / 2);
        bkg = new BKG(0,0,3000, 1000);
        bkg.setUp();
        player = new Player(width/2, height/2);
        player.setX(width/2 - player.getRect().width);
        player.setY(height/2 - player.getRect().height);

    }

    public void move(KeyEvent e) {
        // If player gets to the edge of the background, stop moving the background, instead move the player

        System.out.println(bkg.getOffX());
        System.out.println(bkg.getOffY());


        
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
//        System.out.println(sideMove);
//        if (player.getRect().x < Math.abs(bkg.getOffX() + offsetDistance) || player.getRect().y < Math.abs(bkg.getRect().y - offsetDistance)) { // Check other 3 conditions
//            player.move(e.getKeyCode());
//        } else {
//            bkg.move(e.getKeyCode());
//        }
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

        g.setFont(new Font("Times New Roman", Font.PLAIN, 40)); // PLACEHOLDER FONT
        g.setColor(Color.CYAN);
        g.drawString("Penguin is Cool", WIDTH/2 - 125, 200);

        Button play = new Button(WIDTH/2 - 150, 200, 300, 200);
        play.draw(g);
    }

    public void drawGame(Graphics g) {
        bkg.draw(g, null);
        g.setColor(Color.red);
        player.draw(g);
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
