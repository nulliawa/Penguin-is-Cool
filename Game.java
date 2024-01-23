import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;
//main game extending baseframe, start at menu
//ESC TO PAUSE (any screen)

public class Game extends BaseFrame implements MouseListener{
    public GamePane pane;
    private static final int WIDTH = 1400, HEIGHT = 800;
    public final int MENU = 0, GAME = 1, TUTORIAL = 2, MUSIC = 3, BATTLE = 4, PUZZLE = 5,PAUSE=6;
    public int resume;
    int screen = MENU;
    private final Player player;
    private final BKG bkg;
    private boolean direction;
    private ArrayList<Integer> noteX = new ArrayList<>();
    private ArrayList<Integer> noteY = new ArrayList<>();
    private ArrayList<Note> notes = new ArrayList<>();

    private final int offset = 100;
    private int[] times = {2300, 700, 1200, 700, -1};
    private int timeCounter = 0;
    Timer firstTimer = new Timer(times[0], new ActionListener() { // Timer goes off at different intervals listed in array times
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            createNotes();
            timeCounter++;
            firstTimer.stop();
            updateTimer();
        }
    });
    Timer beatTimer;
    private boolean first = true;
    private boolean playingSong = false;
    private final Enemy enemy;
    private Battle battle;
    private Puzzle puzzle = new Puzzle();

    public Game(String title, int width, int height) {
        super(title, width, height);

        bkg = new BKG(0, 0, 3000, 1500, null);//wip
        BKG.setup();

        player = new Player(width / 2, height / 2);
        Player.setUp();
        enemy = new Enemy();
        Enemy.setUp();
        battle = new Battle(10000);
        Battle.setUp();

        puzzle.createButton();
        generateNotes();
        addOffset();
    }

    public void move() {
        // If player gets to the edge of the background, stop moving the background, instead move the player
        bkg.move(keys,player);
        player.move(keys,bkg);
        enemy.move();
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
        bkg.draw(g);
        enemy.draw(g);
        player.draw(g,bkg);
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

    public void drawBattle(Graphics g) {
        battle.draw(g);
    }

    public void addOffset() {
        for (int i = 0; i < times.length; i++) {
            times[i] += offset;
        }
    }

    public void drawMusicGame(Graphics g) {
        if (!playingSong) {
            playMusic();
            playingSong = true;
        }
        if (first) {
            firstTimer.start();
            first = false;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        drawNotes(g);
    }

    public void updateTimer() {
        beatTimer = new Timer(times[timeCounter], new ActionListener() { // Timer goes off at different intervals listed in array times
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (timeCounter < times.length - 1) { // If not out of bounds
                    createNotes();
                    timeCounter++;
                } else { // Stop the timer after going through the list
                    beatTimer.stop();
                }
            }
        });
        beatTimer.start();
    }

    public void generateNotes() {
        for (int i = 0; i < 100; i++) {
            if (i == 0) {
                noteX.add((int) (Math.random() * 1000 + 200));
                noteY.add((int) (Math.random() * 500 + 200));
            } else {
                int tmpX = (int) (Math.random() * 1000 + 200);
                int tmpY = (int) (Math.random() * 500 + 200);

                if (Math.sqrt(Math.pow((tmpX - noteX.getLast()), 2) + Math.pow((tmpY - noteY.getLast()), 2)) >= 150) {
                    noteX.add(tmpX);
                    noteY.add(tmpY);
                }
            }
        }
    }

    public void createNotes() {
        notes.add(new Note(noteX.get(timeCounter), noteY.get(timeCounter)));
        System.out.println(times[timeCounter]); //PLACEHOLDER
    }

    public void drawNotes(Graphics g) {
        for (Note note : notes) {
            note.draw(g);
            note.setGame(true);

            // Clicked on note
            if (note.isHovered(mx, my) && mb == 1) {
                note.setVisible(false);
            }
        }
    }

    public void playMusic() {
        // help from ChatGPT 3.5 (Java API documentation was tried first)
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
    public void drawPause(Graphics g){
        //layer underneath (before pause)
        if(resume==GAME){
            drawGame(g);
        }
        else if(resume==BATTLE){
            drawBattle(g);
        }
        g.setColor(new Color(10,10,10,100));//semi transparent
        g.fillRect(0,0,WIDTH,HEIGHT);//whole screen
    }

    public void draw(Graphics g) {//test
        if (screen == MENU) {
            drawMenu(g);
        } else if (screen == GAME) {
            drawGame(g);
        } else if (screen == TUTORIAL) {
            drawMusicGame(g);
            // drawTutorial(g);
        } else if (screen == MUSIC) {
            drawMusicGame(g);
        } else if (screen == BATTLE) {
            drawBattle(g);
        } else if (screen == PUZZLE) {

        }
        else if(screen==PAUSE){
            drawPause(g);
            super.timer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {//constant new checking for keys[], based on actionPerformed()
        super.keyPressed(e);
        keys[e.getKeyCode()] = true;
        if(screen!=PAUSE&&screen!=MENU&&screen!=PUZZLE) {
            if(keys[ESC]) {
                resume = screen;
                screen = PAUSE;
            }
        }
        else if(screen==PAUSE){
            if(keys[ESC]) {
                screen = resume;
                super.timer.start();
            }
            else if(keys[SPACE]){
                screen=MENU;
                super.timer.start();
            }
            else if(keys[KeyEvent.VK_ENTER]&&resume==BATTLE){
                screen=GAME;
                super.timer.start();
            }//hi :)
        }
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
            enemy.destroy();
            if (enemy.pCollision(player)) {
                screen = BATTLE;
                battle = new Battle(battle.getHP());//new battle carries over hp
                battle.start();
            }
        }
        if (screen == BATTLE) {
            if (battle.result()) {
                screen = GAME;
            } else {
                battle.move(keys);
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        Game game = new Game("Penguin Is Cool", 1400, 800);
    }
}
