package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import java.io.FileNotFoundException;
import java.util.Iterator;

import static codes.Commons.setApp;

public class MovingPlane extends Behavior {
    public TransformGroup objTG = new TransformGroup();
    public Transform3D trans3d = new Transform3D();
    public Transform3D apache = new Transform3D();
    public ViewingPlatform vp;
    float speed = -0.03f;
    private Transform3D trans = new Transform3D();
    private WakeupOnElapsedFrames wakeUpCall;
    private Transform3D T3D = new Transform3D();

    public MovingPlane(String name, ViewingPlatform vp) {
        this.vp = vp;
        this.trans3d.setScale(1 / 2f); // SOLO SCALE
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
        ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
        Scene s = null;
        try {
            s = f.load(name);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        apache.rotY((float) Math.PI);
        BranchGroup objBG;
        TransformGroup filler = new TransformGroup(apache);
        objBG = s.getSceneGroup();
        Shape3D ringObj = (Shape3D) objBG.getChild(0);
        ringObj.setAppearance(setApp("Imports/Textures/Fighter_01_Body_BaseColor.png"));
        filler.addChild(objBG);
        objTG.addChild(filler);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
    }

    public void initialize() {
        wakeUpCall = new WakeupOnElapsedFrames(0);
        wakeupOn(wakeUpCall);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {

        vp.getViewPlatformTransform().setTransform(this.trans3d);
        vp.getViewPlatformTransform().getTransform(T3D);
        vp.getViewPlatformTransform().setTransform(this.trans3d);
        trans.set(new Vector3d(0.0f, 0.0f, speed));
        trans3d.mul(trans);
        objTG.setTransform(trans3d);
        wakeupOn(wakeUpCall);
    }
}