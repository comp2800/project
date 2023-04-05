package codes;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnCollisionEntry;

import java.util.Iterator;

public class AsteroidCollisionDetector extends Behavior {
    private final Asteroid asteroid;
    private WakeupOnCollisionEntry wakeEnter;


    public AsteroidCollisionDetector(Asteroid asteroid) {
        this.asteroid = asteroid;
    }

    public void initialize() {
        wakeEnter = new WakeupOnCollisionEntry(asteroid.shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wakeupOn(wakeEnter);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        System.out.print(" Asteroid blew ");
        asteroid.boom();
        this.setEnable(false);
    }
}
