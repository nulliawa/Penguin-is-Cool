import javax.swing.*;
import java.awt.*;
/*
SOURCES:
Tiles - https://graphicriver.net/item/winterland-top-down-tileset/17565368?ref=evtheme
Cloud - https://www.reddit.com/r/PixelArt/comments/dhdgab/storm_clouds_practice/
Snowflakes - https://opengameart.org/content/pixel-art-snowflakes
Golem - https://craftpix.net/product/golem-character-sprite-sheets-pixel-art/?num=1&count=35&sq=snow%20golem&pos=0
 */
public class ImageInit {//initialize all images
    private static final String[] blockNames =new String[]{"iceTileBasic","iceTileWall","iceTileWater","iceTileTop","iceTileBot",
            "iceTileLeft","iceTileRight","iceTileStair",
            "iceTileTopLeft","iceTileTopRight","iceTileBotLeft","iceTileBotRight",
            };
    private static final Image[] blocks =new Image[blockNames.length];
    private static final String[] projNames=new String[]{"snowCloud"};
    private static final Image[] projectiles=new Image[projNames.length+7];

    public ImageInit(){
        for(int i = 0; i< blockNames.length; i++){
            blocks[i]= new ImageIcon(blockNames[i]+".png").getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        }
        for(int j=1;j<8;j++){//7 snowflake variants
            projectiles[j]=new ImageIcon("snowFlake"+j+".png").getImage().getScaledInstance(10,10,Image.SCALE_SMOOTH);
        }
        projectiles[0]=new ImageIcon(projNames[0]+".png").getImage().getScaledInstance(500,100,Image.SCALE_SMOOTH);
    }
    public static Image[] getBlocks(){
        return blocks;
    }
    public static Image[] getProjectiles(){
        return projectiles;
    }

}