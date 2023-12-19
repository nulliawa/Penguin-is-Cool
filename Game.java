import java.awt.*;
import java.awt.event.KeyEvent;

public class Game extends BaseFrame{
    private Player player;
    private BKG bkg;

    public Game(String title,int width,int height){
        super(title,width,height);

        bkg=new BKG(width/2,height/2);
        player=new Player(0,0);

    }

    public void move(Player p){
        /*
        if (x < Math.abs(background.x - offsetDistance) || y < Math.abs(background.y - offsetDistance)) { // If the penguin is within the offset range
        if player distance is near the edge, then player.move(), else bkg.move()
         */
        if (player.getRect().x < Math.abs(bkg.getRect.x - offsetDistance)) {
            
        }
        bkg.move(p);
    }
    public void draw(Graphics g){//test
        g.setColor(Color.red);
        player.draw(g);
        bkg.draw(g,null);
//        g.drawRect(mx,my,20,20);
//        if(mb==1){
//            g.fillRect(mx,my,20,20);
//        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        super.keyPressed(e);
        player.keyPress(e.getKeyCode());
    }


    public static void main(String[] args) {
        Game game=new Game("Penguin Is Cool",1200,600);
    }
}
