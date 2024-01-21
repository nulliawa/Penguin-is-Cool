import javax.swing.*;
import java.awt.*;

//class to organize image getting
/*
SOURCES:
Battle background - https://www.gameassetdeals.com/asset/254988/2d-parallax-background-parallax-2d-parallax-effect
Tiles - https://graphicriver.net/item/winterland-top-down-tileset/17565368?ref=evtheme
Cloud - https://www.reddit.com/r/PixelArt/comments/dhdgab/storm_clouds_practice/
Snowflakes - https://opengameart.org/content/pixel-art-snowflakes
Golem - https://craftpix.net/product/golem-character-sprite-sheets-pixel-art/?num=1&count=35&sq=snow%20golem&pos=0
 */
public class ImageInit {//initialize all images
    private static final int WIDTH = 1400, HEIGHT = 800;
    private static final String[] backNames=new String[]{"battleBack"};
    private static final Image[] backgrounds=new Image[backNames.length];
    private static final String[] blockNames =new String[]{"iceTileBasic","iceTileWall","iceTileWater","iceTileTop","iceTileBot",
            "iceTileLeft","iceTileRight","iceTileStair",
            "iceTileTopLeft","iceTileTopRight","iceTileBotLeft","iceTileBotRight",
            };
    private static final Image[] blocks =new Image[blockNames.length];
    private static final String[] projNames=new String[]{"snowCloud"};
    private static final Image[] projectiles=new Image[projNames.length+7];
    private static final Image[] golems=new Image[6];
    private static final Image[] spirits=new Image[6];
    public static void setUp(){
        for(int h=0;h<backNames.length;h++){
            backgrounds[h]=new ImageIcon(backNames[h]+".png").getImage().getScaledInstance(WIDTH,HEIGHT,Image.SCALE_SMOOTH);
        }
        for(int i = 0; i< blockNames.length; i++){
            blocks[i]= new ImageIcon("iceTile/"+blockNames[i]+".png").getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        }
        for(int j=1;j<8;j++){//7 snowflake variants
            projectiles[j]=new ImageIcon("snow/snowFlake"+j+".png").getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
        }
        projectiles[0]=new ImageIcon(projNames[0]+".png").getImage().getScaledInstance(500,100,Image.SCALE_SMOOTH);
        for(int k=1;k<7;k++){//6 idle frames
            golems[k-1]=new ImageIcon("golem/golemIdle"+k+".png").getImage().getScaledInstance(50,70,Image.SCALE_SMOOTH);
            spirits[k-1]=new ImageIcon("spirit/spiritIdle"+k+".png").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH);
        }
    }
    public static Image[] getBlocks(){
        return blocks;
    }
    public static Image[] getProjectiles(){
        return projectiles;
    }
    public static Image[] getGolems(){
        return golems;
    }
    public static Image[] getSpirits(){
        return spirits;
    }
    public static Image[] getBacks(){
        return backgrounds;
    }
}