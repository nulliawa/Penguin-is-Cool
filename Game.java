import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
//main game, start at menu

public class Game extends BaseFrame {
    private static final int WIDTH = 1400, HEIGHT = 800;
    public final int MENU = 0, GAME = 1, TUTORIAL = 2, MUSIC = 3, BATTLE = 4, PUZZLE = 5;
    int screen = MENU;
    private final Player player;
    private final BKG bkg;
    private ArrayList<Integer> noteX = new ArrayList<>(Arrays.asList(300, 700, 800, 900, 100));
    private ArrayList<Integer> noteY = new ArrayList<>(Arrays.asList(400, 200, 300, 320, 100));
    private ArrayList<Note> notes = new ArrayList<>();
    private final int[] times = {500, 200, 50, 50, 50};
    private int timeCounter = 0;
    private static ImageInit imgInit;
    private static Image[] blocks;
    private static Image[] projectiles;
    private static Image[] golems;
    Timer timer = new Timer(times[timeCounter], new ActionListener() { // Timer goes off at different intervals listed in array times
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // System.out.println(times[timeCounter]);
            if (timeCounter < times.length - 1) {
                createNotes();
                timeCounter++;
            }
        }
    });

    private boolean playingSong = false;
    private final Enemy enemy;
    private Battle battle;

    private Puzzle puzzle = new Puzzle();

    public Game(String title, int width, int height) {
        super(title, width, height);
        ImageInit.setUp();
        blocks=ImageInit.getBlocks();
        projectiles=ImageInit.getProjectiles();
        golems=ImageInit.getGolems();

        bkg = new BKG(0, 0, 3000, 1500,null);//wip
        BKG.setup(blocks);

        player = new Player(width / 2, height / 2);
        enemy = new Enemy(0, 0, 0, 0);
        enemy.setUp(golems);
        battle = new Battle(1);
        Battle.setUp(projectiles,ImageInit.getBacks()[0]);

        puzzle.createButton();
    }

    public void move() {
        // If player gets to the edge of the background, stop moving the background, instead move the player

        if (player.isFixedX()) {
            //player is fixed to middle line, can move background left/right
            bkg.move(keys, false, player);
            enemy.move(bkg);

        }
        if (player.isFixedY()) {
            bkg.move(keys, true, player);
            enemy.move(bkg);

        }

        if (bkg.edgeX()) {//arrive at edge of background left/right direction
            player.move(keys, false, bkg);
            //player itself can move left/right and background stops
            // left/right movement as it will no longer be at a midline
        }
        if (bkg.edgeY()) {
            player.move(keys, true, bkg);
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
        int[][] textCoordinates = {{WIDTH / 2 - 45, 365}, {WIDTH / 2 - 95, 620}};
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
        player.draw(g);
        enemy.draw(g);
    }

    public void drawTutorial(Graphics g) {
        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // Controls
        g.setColor(Color.CYAN); // PLACEHOLDER
        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 70));
        g.drawString("CONTROLS", 20, 100);

        String[] instructions = {"W - UP", "A - LEFT", "S - DOWN", "D - RIGHT"};
        // Rectangle creation
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                g.drawRect(350 * i + 25, 250 * j + 200, 300, 200);
            }
        }

        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
        g.drawString(instructions[0], 20, 100);
        g.drawString(instructions[1], 50, 200);
        // WHAT TO DO
        /*
        WASD - up left down right
        SPACE - interact
        */

        Button next = new Button(100, 100, 200, 200);
    }

    public void drawBattle(Graphics g){
        battle.draw(g);
    }

    public void drawTest(Graphics g) {
        if (!playingSong) {
            playMusic();
            playingSong = true;
        }
        timer.start();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        drawNotes(g);
    }

    public void createNotes() {
        // System.out.println(timeCounter);
        Note newNote = new Note(noteX.get(timeCounter), noteY.get(timeCounter));
        notes.add(newNote);
    }

    public void drawNotes(Graphics g) {
        for (Note note : notes) {
            note.draw(g);
            note.setGame(true);
        }

        for (int i = notes.size() - 1; i >= 0; i--) {
            if (notes.get(i).isHovered(mx, my) && mb == 1) {
                notes.remove(i);
            }
        }
    }

    public void playMusic() {
        try {
            // Create an AudioInputStream from the file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("The Barber.wav"));

            // Get a Clip to play the audio
            Clip clip = AudioSystem.getClip();

            // Open the audioInputStream to the clip
            clip.open(audioInputStream);

            // Add a listener to handle the end of the audio playback
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        playingSong = false;
                    }
                }
            });

            // Start playing the audio
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPuzzle(Graphics g) {
        puzzle.draw(g);
    }

    public void draw(Graphics g) {//test
        if (screen == MENU) {
            drawMenu(g);
        } else if (screen == GAME) {
            drawGame(g);
        } else if (screen == TUTORIAL) {
            drawTutorial(g);
        } else if (screen == MUSIC) {
            drawTest(g);
        } else if (screen == BATTLE) {
            drawBattle(g);
        } else if (screen == PUZZLE) {

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
    public void actionPerformed(ActionEvent e) {
        if (screen == GAME) {
            move();
            if (enemy.pCollision(player)) {
                screen = BATTLE;
                battle=new Battle(battle.getHP());//new battle carries over hp
                battle.start();
            }
        }
        if(screen==BATTLE){
            if(battle.result()){
                screen=GAME;
            }
            else{
                battle.move(keys);
            }
        }
        repaint();
    }


    public static void main(String[] args) {
        Game game = new Game("Penguin Is Cool", 1400, 800);
    }
}
