package codes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.*;

import static codes.Commons.setApp;

public class Asteroid2 extends Behavior {

    private final Transform3D trans3d = new Transform3D();
    public TransformGroup objTG = new TransformGroup();
    private final int clock = new Random().nextInt(2);
    private final double t = (clock==1 ? 1: -1);
    private WakeupOnElapsedFrames wakeUpCall;

    Switch mySwitch = new Switch();

    public Asteroid2() throws IOException {
        mySwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        mySwitch.setCapability(Switch.ALLOW_SWITCH_READ);
        mySwitch.setCapability(Switch.ALLOW_COLLIDABLE_WRITE);
        trans3d.setTranslation(new Vector3f(new Random().nextInt(2500) - 1250,
                new Random().nextInt(2500) - 1250,
                new Random().nextInt(2500) - 1250));
        BranchGroup BG = Commons.f_load("Imports/Objects/Asteroid.obj");
        mySwitch.addChild(BG);
        Shape3D shape = (Shape3D) BG.getChild(0);
        shape.setAppearance(setApp("Imports/Textures/Asteroids/tex" + (new Random().nextInt(5) + 1) + ".jpg"));
        TransformGroup TG = new TransformGroup();
        Asteroid[] asteroids = new Asteroid[25];
        for (int i = 0; i < asteroids.length; i++){
            asteroids[i] = new Asteroid();
            TG.addChild(asteroids[i].objTG);
        }
        mySwitch.addChild(TG);
        mySwitch.setWhichChild(1);
        objTG.addChild(mySwitch);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000));
    }

    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(0);
        wakeupOn(wakeUpCall);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        mySwitch.setWhichChild(Switch.CHILD_NONE);
    }


}