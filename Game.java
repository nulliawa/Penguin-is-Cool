import java.awt.*;
import java.awt.event.KeyEvent;

public class Game extends BaseFrame {
    private Player player;
    private BKG bkg;
    private int offsetDistance = 10; // How far from the edge of the offset

    public Game(String title, int width, int height) {
        super(title, width, height);

        // bkg = new BKG(width / 2, height / 2);
        bkg = new BKG(3000, 1000);
        player = new Player(width/2, height/2);
        player.setX(width/2 - player.getRect().width);
        player.setY(height/2 - player.getRect().height);

    }

    public void move(KeyEvent e) {
        /*
        if (x < Math.abs(background.x - offsetDistance) || y < Math.abs(background.y - offsetDistance)) { // If the penguin is within the offset range
        if player distance is near the edge, then player.move(), else bkg.move()
         */
        System.out.println(bkg.getOffX()); // bkg.getRect().x does not update @Ederexre
        if (player.getRect().x < Math.abs(bkg.getOffX() + offsetDistance) || player.getRect().y < Math.abs(bkg.getRect().y - offsetDistance)) {
            player.move(e.getKeyCode());
        } else {
            bkg.move(e.getKeyCode());
        }
    }

    public void draw(Graphics g) {//test

        bkg.draw(g, null);
        g.setColor(Color.red);
        player.draw(g);
//        g.drawRect(mx,my,20,20);
//        if(mb==1){
//            g.fillRect(mx,my,20,20);
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        move(e);
        // player.keyPress(e.getKeyCode());
    }


    public static void main(String[] args) {
        Game game = new Game("Penguin Is Cool", 1200, 600);

    }
}
