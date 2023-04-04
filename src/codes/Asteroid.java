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
    public TransformGroup objTG = new TransformGroup(trans3d);
    private final TransformGroup TG = new TransformGroup();
    private WakeupOnElapsedFrames wakeUpCall;
    private final Pebbles pebbles = new Pebbles();
    private final Switch mySwitch;

    public Asteroid() throws IOException {
        mySwitch = new Switch();
        mySwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        BranchGroup BG = Commons.f_load("Imports/Objects/Asteroid.obj");
        Shape3D shape = (Shape3D) BG.getChild(0);
        shape.setAppearance(setApp("Imports/Textures/Asteroids/tex" + (new Random().nextInt(5) + 1) + ".jpg"));
        mySwitch.addChild(BG);
        TG.addChild(pebbles);
        TG.addChild(pebbles.TG);
        TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mySwitch.addChild(TG);
        mySwitch.setWhichChild(0);
        objTG.addChild(mySwitch);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.setEnable(false);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000));
    }

    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(0);
        wakeupOn(wakeUpCall);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {

        float t = 0.0005F;
        switch (trajectory) {
            case 0 -> trans.setTranslation(new Vector3f(t, 0, 0));
            case 1 -> trans.setTranslation(new Vector3f(0, t, 0));
            case 2 -> trans.setTranslation(new Vector3f(0, 0, t));
        }

        trans3d.mul(trans);
        objTG.setTransform(trans3d);
        wakeupOn(wakeUpCall);
    }

    public void boom() {
        pebbles.TG.setTransform(this.trans3d);
        mySwitch.setWhichChild(1);
    }


}
