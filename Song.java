import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;

public class Song {
    private final int RADIUS = 150;
    private Timer timer = new Timer();
    // Constructor
    // BASE SONG OFF FRAMES/SECONDS?
    private ArrayList<Double> notes; // Time for when a note shows up
    private ArrayList<Integer> noteX; // X-coordinates for notes on the screen
    private ArrayList<Integer> noteY;

    public Song(ArrayList<Double> notes, ArrayList<Integer> x, ArrayList<Integer> y) {
        this.notes = notes;
        noteX = x;
        noteY = y;
    }

    public void display(Graphics g) {
        // TIMER
        g.drawOval(noteX.get(0), noteY.get(0), RADIUS, RADIUS);
    }
}
