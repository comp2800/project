package codes;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import static codes.Commons.setApp;

public class Asteroid extends Behavior {

    private final Transform3D trans3d = new Transform3D();
    private final Transform3D trans = new Transform3D();
    private final int trajectory = new Random().nextInt(3);
    private final TransformGroup TG = new TransformGroup();
    private final Pebbles pebbles;
    private final Switch mySwitch;
    public TransformGroup objTG = new TransformGroup(trans3d);
    public Shape3D shape;
    private WakeupOnElapsedFrames wakeUpCall;

    public Asteroid() throws IOException {
        trans3d.setTranslation(new Vector3f(new Random().nextInt(250) - 125,
                new Random().nextInt(250) - 125,
                new Random().nextInt(250) - 125));
        objTG.setTransform(trans3d);
        mySwitch = new Switch();
        mySwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        BranchGroup BG = Commons.f_load("Imports/Objects/Asteroid.obj");
        shape = (Shape3D) BG.getChild(0);
        shape.setAppearance(setApp("Imports/Textures/Asteroids/tex" + (new Random().nextInt(5) + 1) + ".jpg"));
        pebbles = new Pebbles(shape);
        mySwitch.addChild(BG);
        TG.addChild(pebbles);
        TG.addChild(pebbles.TG);
        pebbles.TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mySwitch.addChild(TG);
        mySwitch.setWhichChild(0);
        objTG.addChild(mySwitch);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTG.setCollidable(true);
        this.setEnable(true);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000));
    }

    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(0);
        wakeupOn(wakeUpCall);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {

        float t = 0.005F;
        switch (trajectory) {
            case 0 -> trans.setTranslation(new Vector3f(t, 0, 0));
            case 1 -> trans.setTranslation(new Vector3f(0, t, 0));
            case 2 -> trans.setTranslation(new Vector3f(0, 0, t));
        }
        pebbles.TG.setTransform(this.trans3d);
        trans3d.mul(trans);
        objTG.setTransform(trans3d);
        wakeupOn(wakeUpCall);
    }

    public void boom() {
        mySwitch.setWhichChild(1);
        this.setEnable(false);
    }


}
