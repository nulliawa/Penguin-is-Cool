import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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
    public final int MENU = 0, GAME = 1, TUTORIAL = 2, MUSIC = 3, BATTLE = 4, PUZZLE = 5, PAUSE = 6;
    public int resume;
    private int screen = MENU;
    private int codAmount=99;
    private static final Image codFishBase =new ImageIcon("codFish.png").getImage();
    private static final Image codUI=codFishBase.getScaledInstance(100,100,Image.SCALE_SMOOTH);
    private double codAngle=0;
    private static final Image heart=new ImageIcon("heart.png").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH);
    private final Player player;
    private final BKG bkg;
    private long[] memTime=new long[2];
    private ArrayList<Integer> noteX = new ArrayList<>();
    private ArrayList<Integer> noteY = new ArrayList<>();
    private ArrayList<Note> notes = new ArrayList<>();
    private int offset = 250; // CHANGE LATENCY BASED OFF OF YOUR MACHINE
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

    private int tutorialSlides = 0;

    private final Color MAIN = new Color(151, 181, 219);
    private final Color SECONDARY = new Color(150, 167, 176);

    public Game(String title, int width, int height) {
        super(title, width, height);

        bkg = new BKG(0, 0, 3000, 1500, null);//wip
        BKG.setup();

        player = new Player(width / 2, height / 2);
        Player.setUp();
        enemy = new Enemy();
        Enemy.setUp();
        battle = new Battle(10);
        Battle.setUp();

        puzzle.createButton();
        generateNotes();
        addOffset();
    }

    public void move() {
        // If player gets to the edge of the background, stop moving the background, instead move the player
        if(!battle.getWin()) {
            bkg.move(keys, player);
            player.move(keys, bkg);
            enemy.move();
        }
    }

    public void drawMenu(Graphics g) {
        final int X = 0, Y = 1, W = 2, H = 3; // Variables for button creation

        // Background screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Title
        g.setColor(MAIN);
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
            // Creating new button
            g.setColor(MAIN);
            Button temp = new Button(buttonCoordinates[i][X], buttonCoordinates[i][Y], buttonCoordinates[i][W], buttonCoordinates[i][H]);
            buttons.add(temp);

            g.fillRect(temp.getRect().x, temp.getRect().y, temp.getRect().width, temp.getRect().height); // Drawing buttons

            // Drawing buttons when hovered
            temp.drawHover(g, SECONDARY, mx, my);

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString(buttonText[i], textCoordinates[i][X], textCoordinates[i][Y]);
        }
        //temp player draw
        Player menuPlayer1=new Player(200,200);
        menuPlayer1.draw(g,0);
//        Battle menuDraw1=new Battle(-1);
//        Battle.setUp();
//        menuDraw1.drawIcicle(g,200,200);


        g.drawImage(codFishBase,WIDTH- codFishBase.getWidth(null),HEIGHT- codFishBase.getHeight(null),null);
        // Button function
        if (buttons.get(0).isClicked(mx, my, mb)) {
            screen = GAME;
            //reset player to middle of screen
            player.setPos(WIDTH/2,HEIGHT/2);
        } else if (buttons.get(1).isClicked(mx, my, mb)) {
            screen = TUTORIAL;
        }
    }

    public void drawGame(Graphics g) {
        bkg.draw(g);
        enemy.draw(g);
        player.draw(g,bkg);
        g.drawImage(codUI,WIDTH-codUI.getWidth(null),0,null);
        Font UI=new Font("Comic Sans MS", Font.PLAIN, 40);
        g.setFont(UI);
        String codText=String.valueOf(codAmount);
        g.drawString(codText,WIDTH-codUI.getWidth(null)-codText.length()*20,60);
        g.setColor(Color.RED);//health bar
        g.drawRect(20,20,400,50);
        g.fillRect(20,20,400*battle.getHP()/10,50);
        g.drawImage(heart,440,20,null);
    }

    public void drawTutorial(Graphics g) {
        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // BACK AND NEXT (CONVENTIONS?)
        Button back = new Button(20, HEIGHT - 120, 200, 100);
        Button next = new Button(WIDTH - 220, HEIGHT - 120, 200, 100);
        back.draw(g, MAIN);
        next.draw(g, MAIN);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 70));
        g.drawString("BACK", 40, HEIGHT - 45);
        g.drawString("NEXT", WIDTH - 203, HEIGHT - 45);

        // Controls
        g.setColor(MAIN); // PLACEHOLDER
        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 70));
        g.drawString("CONTROLS", 20, 100);
        g.drawString("SETTINGS", 850, 100);

        String[] instructions = {"W - UP", "A - LEFT", "S - DOWN", "D - RIGHT"};
        // Rectangle creation
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                g.drawRect(350 * i + 25, 250 * j + 125, 300, 200);
            }
        }

        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
        g.drawString(instructions[0], 20, 250);
        g.drawString(instructions[1], 50, 400);
        g.drawString("Offset: ", 800, 250);
        // WHAT TO DO
        /*
        WASD - up left down right
        SPACE - interact
        */

        // OFFSET CONTROL (to deal with song delays)
        Button increase = new Button(1110, 205, 50, 50);
        Button decrease = new Button(1000, 205, 50, 50);
        increase.draw(g, MAIN);
        increase.drawHover(g, SECONDARY, mx, my);
        decrease.draw(g, MAIN);
        decrease.drawHover(g, SECONDARY, mx, my);
        if (increase.isClicked(mx, my, mb)) {
            offset += 10;
            mb = 0;
        }
        if (decrease.isClicked(mx, my, mb)) {
            offset -= 10;
            mb = 0;
        }

        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        g.drawString(offset + "", 1058, 240);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));

        if (decrease.isHovered(mx, my)) {
            g.setColor(new Color(100, 100, 100));
        } else {
            g.setColor(SECONDARY);
        }
        g.drawString("-", 1016, 240);

        if (increase.isHovered(mx, my)) {
            g.setColor(new Color(100, 100, 100));
        } else {
            g.setColor(SECONDARY);
        }
        g.drawString("+", 1125, 242);
    }

    public void drawBattle(Graphics g) {
        battle.draw(g);
        g.setColor(Color.RED);//health bar
        g.drawRect(20,20,400,50);
        g.fillRect(20,20,400*battle.getHP()/10,50);
        g.drawImage(heart,440,20,null);
    }
    public void drawUI(Graphics g){

    }

    public void addOffset() {
        for (int i = 0; i < times.length; i++) {
            times[i] += offset;
        }
    }

    public void drawSong(Graphics g) {
        addOffset();
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
                noteX.add((int) (Math.random() * 1000 + 200)); // Generate the first note
                noteY.add((int) (Math.random() * 500 + 200));
            } else {
                int tmpX = (int) (Math.random() * 1000 + 200); // Generate new notes
                int tmpY = (int) (Math.random() * 500 + 200);

                if (Math.sqrt(Math.pow((tmpX - noteX.getLast()), 2) + Math.pow((tmpY - noteY.getLast()), 2)) >= 150) { // Compare distance to last note
                    noteX.add(tmpX);
                    noteY.add(tmpY);
                }
            }
        }
    }

    public void createNotes() {
        notes.add(new Note(noteX.get(timeCounter), noteY.get(timeCounter)));
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

    public void drawPause(Graphics g) {
        //layer underneath (before pause)
        if (resume == GAME) {
            drawGame(g);
        } else if (resume == BATTLE) {
            drawBattle(g);
        }
        g.setColor(new Color(10, 10, 10, 100));//semi transparent
        g.fillRect(0, 0, WIDTH, HEIGHT);//whole screen
    }
    public void drawWin(Graphics g){
        player.stopMove();
        BKG.stopMove();
        codAngle+=Math.PI/50;
        Image codImg=rotateImage(codFishBase, codAngle);
        g.drawImage(codImg,WIDTH/2- codFishBase.getWidth(null)/2,HEIGHT/2- codFishBase.getHeight(null)/2,null);
        Color brown =new Color(100,78,40);
        g.setColor(brown);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        g.drawString("+3 Fish",WIDTH/2-100,HEIGHT/2+200);
        if(timeMill(3000,0)){
            battle.setResult(false);//stop win animation
            codAngle=0;
        }
    }
    public void draw(Graphics g) {//test
        if (screen == MENU) {
            drawMenu(g);
        } else if (screen == GAME) {
            drawGame(g);
            if(battle.getWin()){//when win battle
                drawWin(g);
            }
        } else if (screen == TUTORIAL) {
            drawTutorial(g);
        } else if (screen == MUSIC) {
            drawSong(g);
        } else if (screen == BATTLE) {
            drawBattle(g);
        } else if (screen == PUZZLE) {

        } else if (screen == PAUSE) {
            drawPause(g);
            super.timer.stop();
        }
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
    //taken from battle for win animation
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
                battle.setResult(false);
                battle.stopAll();
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
//            System.out.println(codAmount);
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
                if(battle.getWin()){
                    codAmount+=3;
                    memTime[0]=System.currentTimeMillis();//simple timer for win
                }
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
