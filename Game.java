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
        bkg.move(p);
    }
    public void draw(Graphics g){//test
        g.setColor(Color.red);
        bkg.draw(g);
//        g.drawRect(mx,my,20,20);
//        if(mb==1){
//            g.fillRect(mx,my,20,20);
//        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        super.keyPressed(e);
        player.playerKeyPress(e.getKeyCode());
    }


    public static void main(String[] args) {
        Game game=new Game("Penguin Is Cool",1200,600);;;;;;;;;;;;;;
    }
}
