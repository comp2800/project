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

public class Asteroid extends Behavior {

    private final Transform3D trans3d = new Transform3D();
    public TransformGroup objTG = new TransformGroup(trans3d);
    private final Transform3D trans = new Transform3D();
    private final int trajectory = new Random().nextInt(3);
    private final int clock = new Random().nextInt(2);
    private final double t = (clock==1 ? 1: -1);
    private WakeupOnElapsedFrames wakeUpCall;

    public Asteroid() throws IOException {
        trans3d.setTranslation(new Vector3f(new Random().nextInt(2500) - 1250,
                new Random().nextInt(2500) - 1250,
                new Random().nextInt(2500) - 1250));
        BranchGroup BG = Commons.f_load("Imports/Objects/Asteroid.obj");
        Shape3D shape = (Shape3D) BG.getChild(0);
        shape.setAppearance(setApp("Imports/Textures/Asteroids/tex" + (new Random().nextInt(5) + 1) + ".jpg"));
        objTG.addChild(BG);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000));
    }

    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(10);
        wakeupOn(wakeUpCall);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {

        switch (trajectory) {
            case 0:
                trans.set(new Vector3d(t, 0, 0));
            case 1:
                trans.set(new Vector3d(0, t, 0));
            case 2:
                trans.set(new Vector3d(0, 0, t));
        }

        trans3d.mul(trans);
        objTG.setTransform(trans3d);
        wakeupOn(wakeUpCall);
    }


}
