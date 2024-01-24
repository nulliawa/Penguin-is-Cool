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

public class Game extends BaseFrame{
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
    private final long[] memTime=new long[2];
    private final ArrayList<Integer> noteX = new ArrayList<>();
    private final ArrayList<Integer> noteY = new ArrayList<>();
    private final ArrayList<Note> notes = new ArrayList<>();
    private int offset = 100; // CHANGE LATENCY BASED OFF OF YOUR MACHINE
    private final int[] times = {2300, 700, 1200, 700, -1};
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
    private final Puzzle puzzle = new Puzzle();

    private int tutorialScreen = 0;

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
        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 150));
        g.setColor(MAIN);
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
        Player menuPlayer1=new Player(165,200);
        menuPlayer1.draw(g,0);
        //temp enemy draw
        Enemy menuEnemy1=new Enemy(1200,200,0,0);
        menuEnemy1.draw(g,0);

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
        if(!battle.getLose()) {//player does not lose
            player.draw(g, bkg);
        }
        drawUI(g);
    }

    public void drawTutorial(Graphics g) {
        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // BACK
        Button back = new Button(20, HEIGHT - 120, 200, 100);
        back.draw(g, MAIN);
        back.drawHover(g, SECONDARY, mx, my);
        if (back.isClicked(mx, my, mb) && tutorialScreen == 0) {
            screen = MENU;
        } else if (back.isClicked(mx, my, mb) && tutorialScreen != 0) {
            tutorialScreen--;
            mb = 0;
        }

        // NEXT
        Button next = new Button(WIDTH - 220, HEIGHT - 120, 200, 100);
        next.draw(g, MAIN);
        next.drawHover(g, SECONDARY, mx, my);
        if (next.isClicked(mx, my, mb) && tutorialScreen < 2) {
            tutorialScreen++;
            mb = 0;
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("SnowtopCaps", Font.PLAIN, 70));
        g.drawString("BACK", 40, HEIGHT - 45);
        g.drawString("NEXT", WIDTH - 203, HEIGHT - 45);

        switch (tutorialScreen) {
            case 0:
                // Draw story
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
                g.setColor(MAIN);
                g.drawString("Penguin wants to achieve maximum happiness. Help him by defeating every enemy for fish!", 20, 30);

                break;
            case 1:
                // Controls
                g.setColor(MAIN);
                g.setFont(new Font("SnowtopCaps", Font.PLAIN, 70));
                g.drawString("CONTROLS", 20, 100);
                g.drawString("SETTINGS", 850, 100);


                g.drawRect(125, 150, 80, 80);
                g.drawRect(125, 240, 80, 80);
                g.drawRect(35, 240, 80, 80);
                g.drawRect(215, 240, 80, 80);

                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));

                String[] instructions = {"W - UP", "A - LEFT", "S - DOWN", "D - RIGHT"};

                // UP
                g.drawString(instructions[0], 141, 190);

                // LEFT
                g.drawString(instructions[1], 46, 270);
                Player tutorialPeng = new Player(61, 277);
                tutorialPeng.draw(g, 2);

                //DOWN
                g.drawString(instructions[2], 128, 270);

                // RIGHT
                g.drawString(instructions[3], 220, 270);
                tutorialPeng.setPos(239, 277);
                tutorialPeng.draw(g, 3);


                // OFFSET CONTROL (to deal with song delays)
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
                g.drawString("Offset: ", 850, 175);

                Button decrease = new Button(1035, 135, 50, 50);
                decrease.draw(g, MAIN);
                decrease.drawHover(g, SECONDARY, mx, my);
                if (decrease.isClicked(mx, my, mb)) {
                    offset -= 10;
                    mb = 0;
                }
                if (decrease.isHovered(mx, my)) {
                    g.setColor(new Color(75, 75, 75));
                } else {
                    g.setColor(new Color(125, 125, 125));
                }
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
                g.drawString("-", 1052, 171);

                Button increase = new Button(1135, 135, 50, 50);
                increase.draw(g, MAIN);
                increase.drawHover(g, SECONDARY, mx, my);
                if (increase.isClicked(mx, my, mb)) {
                    offset += 10;
                    mb = 0;
                }
                if (increase.isHovered(mx, my)) {
                    g.setColor(new Color(75, 75, 75));
                } else {
                    g.setColor(new Color(125, 125, 125));
                }
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
                g.drawString("+", 1150, 173);


                g.setColor(new Color(75, 75, 75));
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 23));
                g.drawString(offset + "", 1088, 170);
        }
    }

    public void drawBattle(Graphics g) {
        battle.draw(g);
        drawUI(g);
    }
    public void drawUI(Graphics g){
        g.drawImage(codUI,WIDTH-codUI.getWidth(null),0,null);
        Font numCod =new Font("Comic Sans MS", Font.PLAIN, 40);
        g.setFont(numCod);
        String codText=String.valueOf(codAmount);//amount of fish and image
        g.drawString(codText,WIDTH-codUI.getWidth(null)-codText.length()*20,60);
        //20 gap for all
        //health bar
        g.setColor(Color.RED);
        g.drawRect(20,20,400,50);
        g.fillRect(20,20,400*battle.getHP()/10,50);
        g.drawImage(heart,440,20,null);


        //button to increase health at cost of fish
        Button fishToHealth=new Button(20,90,200,100);
        fishToHealth.draw(g,MAIN);
        fishToHealth.drawHover(g,SECONDARY,mx,my);
        if (fishToHealth.isHovered(mx, my)) {
            g.setColor(new Color(100, 100, 100));
        } else {
            g.setColor(SECONDARY);
        }
        g.drawString("to",105,150);
        if(fishToHealth.isClicked(mx,my,mb)&&battle.getHP()<10&&codAmount>0){//10 is max hp
            battle.setHP(battle.getHP()+1);
            codAmount-=1;
            mb=0;
        }
        g.drawImage(codUI,20,90,null);
        g.drawImage(heart,145,115,null);

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

    public void testCreate(){

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
            battle.setWin(false);//stop win animation
            codAngle=0;
        }
    }
    public void drawLose(Graphics g){
        player.stopMove();
        BKG.stopMove();
        System.out.println(battle.getLose());
        if(player.loseAnimation(g)){
            battle.setLose(false);
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
            else if(battle.getLose()){
                drawLose(g);
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
        if(screen!=PAUSE&&screen!=MENU&&screen!=TUTORIAL&&!battle.getWin()) {
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
                battle.setWin(false);
                battle.setLose(true);
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
            move();
            enemy.destroy();
            if (enemy.pCollision(player)) {
                screen = BATTLE;
                battle = new Battle(battle.getHP());//new battle carries over hp
                battle.start();
            }
        }
        if (screen == BATTLE) {
            if (battle.result()) {//ending of battle
                screen = GAME;
                if(battle.getWin()){//win
                    codAmount+=3;
                    memTime[0]=System.currentTimeMillis();//simple timer for win
                }
                else{//lose
                    Player.setFrames(0);
                    memTime[1]=System.currentTimeMillis();//lose animation
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