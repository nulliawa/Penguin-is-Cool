import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Game extends BaseFrame {
    private final int WIDTH = 1366, HEIGHT = 768;
    public final int MENU = 0, GAME = 1, TUTORIAL = 2;
    int screen = MENU; // Change to MENU when done gameplay
    private Player player;
    private BKG bkg;
    private boolean sideMoveX = true, sideMoveY = true;
    public Game(String title, int width, int height) {
        super(title, width, height);

        bkg = new BKG(0, 0, 1500, 1000);//wip
        bkg.setup();
        player = new Player(width / 2, height / 2);
        player.setX(width / 2 - player.getRect().width / 2);
        player.setY(height / 2 - player.getRect().height / 2);
    }
    public void move() {
        player.collision(new Rectangle());//WIP
        // If player gets to the edge of the background, stop moving the background, instead move the player
        int bkgX = bkg.getOffX();
        int bkgY = bkg.getOffY();
        int pX = player.getRect().x;
        int pY = player.getRect().y;
        int[] coord = new int[]{bkgX, bkgY, pX, pY};

        System.out.println(Arrays.toString(coord));

        bkg.fix();

        if (player.isFixedX()) {
            //player is fixed to middle line so not at an edge, can move background left/right
            bkg.move(keys, false);
        }
        if (player.isFixedY()) {
            bkg.move(keys, true);
        }

        if (bkg.edgeX()) {//arrive at edge of background left/right direction
            player.move(keys, false);
            //player itself can move left/right and background stops left/right movement
        }
        if (bkg.edgeY()) {
            player.move(keys, true);
        }


    }

    public void drawMenu(Graphics g) {
        final int X = 0, Y = 1, W = 2, H = 3; // Variables for button creation

        // Background screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Title
        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 150));
        g.setColor(Color.CYAN);
        g.drawString("Penguin is Cool", WIDTH / 2 - 550, 200);

        // Arrays for buttons
        int[][] buttonCoordinates = {{WIDTH / 2 - 200, 275, 400, 150}, {WIDTH / 2 - 200, 525, 400, 150}};
        String[] buttonText = {"Play", "Tutorial"};
        int[][] textCoordinates = {{640, 365}, {590, 620}};
        ArrayList<Button> buttons = new ArrayList<>();


        // Button creation
        for (int i = 0; i < buttonCoordinates.length; i++) {
            g.setColor(Color.BLUE); // PLACEHOLDER

            // Creating new button
            Button temp = new Button(buttonCoordinates[i][X], buttonCoordinates[i][Y], buttonCoordinates[i][W], buttonCoordinates[i][H]);
            buttons.add(temp);

            g.fillRect(temp.getRect().x, temp.getRect().y, temp.getRect().width, temp.getRect().height); // Drawing buttons

            // Drawing buttons when hovered
            if (temp.isHover(mx, my)) {
                g.setColor(Color.DARK_GRAY); // PLACEHOLDER
                g.fillRect(temp.getRect().x, temp.getRect().y, temp.getRect().width, temp.getRect().height);
            }

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString(buttonText[i], textCoordinates[i][X], textCoordinates[i][Y]);
        }

        // Button function
        if (buttons.get(0).isClicked(mx, my, mb)) {
            screen = GAME;
        } else if (buttons.get(1).isClicked(mx, my, mb)) {
            screen = TUTORIAL;
        }
    }

    public void drawGame(Graphics g) {
        bkg.draw(g, null);
        g.setColor(Color.red);
        player.draw(g);
        g.setColor(Color.green);
    }

    public void drawTutorial(Graphics g) {
        // Background
        g.setColor(Color.WHITE); // PLACEHOLDER


        g.fillRect(0, 0, WIDTH, HEIGHT);
        // Controls

        // WHAT TO DO
    }

    public void draw(Graphics g) {//test
        if (screen == MENU) {
            drawMenu(g);
        } else if (screen == GAME) {
            drawGame(g);
        } else if (screen == TUTORIAL) {
            drawTutorial(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {//constant new checking for keys[], based on actionPerformed()
        super.keyPressed(e);
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyPressed(e);
        keys[e.getKeyCode()] = false;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (screen == GAME) {
            move();
        }
        repaint();
    }


    public static void main(String[] args) {
        Game game = new Game("Penguin Is Cool", 1366, 768);
    }
}
