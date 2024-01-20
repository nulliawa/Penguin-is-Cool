import javax.swing.*;
import java.awt.*;

public class ImageInit {//initialize all images
    private static final String[] blockNames =new String[]{"iceTileBasic","iceTileWall","iceTileWater","iceTileTop","iceTileBot",
            "iceTileLeft","iceTileRight","iceTileStair",
            "iceTileTopLeft","iceTileTopRight","iceTileBotLeft","iceTileBotRight",

            };
    private static final Image[] blocks =new Image[blockNames.length];
    public ImageInit(){
        for(int i = 0; i< blockNames.length; i++){
            blocks[i]= new ImageIcon(blockNames[i]+".png").getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        }
    }
    public Image[] getBlocks(){
        return blocks;
    }
}