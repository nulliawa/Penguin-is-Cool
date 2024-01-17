import java.util.ArrayList;
public class Battle {
    private class Projectile{
        private int x,y,spdX,spdY,width,height;
        private int radiusX,radiusY,elli;
        private double heading;
        public Projectile(int x,int y,int width,int height){
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
        }
        public Projectile(int x,int y,int radiusX,int radiusY,int elli){
            this.x=x;
            this.y=y;
            this.radiusX=radiusX;
            this.radiusY=radiusY;
        }
    }

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

}