package codes;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class Pebbles extends Behavior {

    private final Transform3D trans = new Transform3D();
    private final int[] trajectories = new int[10];

    public TransformGroup TG = new TransformGroup();
    private WakeupOnElapsedFrames wakeUpCall;

    public Pebbles(Shape3D asteroid) throws IOException {
        for (int i = 0; i < 10; i++) {
            trajectories[i] = new Random().nextInt(3);
            BranchGroup BG = Commons.f_load("Imports/Objects/Asteroid.obj");
            Shape3D shape = (Shape3D) BG.getChild(0);
            shape.setAppearance(asteroid.getAppearance());
            Transform3D trans3d = new Transform3D();
            trans3d.rotX(Math.PI / (new Random().nextInt(20) + 1));
            trans3d.rotY(Math.PI / (new Random().nextInt(20) + 1));
            trans3d.rotZ(Math.PI / (new Random().nextInt(20) + 1));
            TransformGroup objTG = new TransformGroup(trans3d);
            objTG.addChild(BG);
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            TG.addChild(objTG);
        }
        this.setEnable(true);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000));
    }

    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(0);
        wakeupOn(wakeUpCall);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {

        float t = 0.005F;
        for (int i = 0; i < 10; i++) {
            switch (trajectories[i]) {
                case 0 -> trans.setTranslation(new Vector3f(t, 0, 0));
                case 1 -> trans.setTranslation(new Vector3f(0, t, 0));
                case 2 -> trans.setTranslation(new Vector3f(0, 0, t));
            }
            TransformGroup pebbleTG = (TransformGroup) TG.getChild(i);
            Transform3D T3D = new Transform3D();
            pebbleTG.getTransform(T3D);
            T3D.mul(trans);
            T3D.setScale(1f / 5);
            pebbleTG.setTransform(T3D);
        }
        wakeupOn(wakeUpCall);
    }

}
