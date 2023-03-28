package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import java.io.FileNotFoundException;
import java.util.Iterator;

import static codes.Commons.hundredBS;
import static codes.Commons.setApp;

class Jet {

    private Canvas3D canvas = null;
    private Matrix4d mtrx = new Matrix4d();
    public static float speed = -0.03f;
    private static BranchGroup ammoBG;
    private static TransformGroup aimBot;
    private static BoundingSphere thousandBS = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
    private static TransformGroup objTG;                              // use 'objTG' to position an object
    public static float n = (float) 1.0;
    static class MovingPlane extends Behavior {
        public TransformGroup objTG = new TransformGroup();
        public Transform3D trans3d = new Transform3D();
        private Transform3D trans = new Transform3D();
        public static float speed = -0.03f;
        private WakeupOnElapsedFrames wakeUpCall;
        public Transform3D apache = new Transform3D();

        public MovingPlane(String name) {
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
            Appearance app = new Appearance();
            apache.rotY((float) Math.PI);
            BranchGroup objBG;
            TransformGroup filler = new TransformGroup(apache);
            objBG = s.getSceneGroup();
            Shape3D ringObj = (Shape3D) objBG.getChild(0);
            ringObj.setAppearance(setApp("Imports/Textures/Fighter_01_Body_BaseColor.png"));
            filler.addChild(objBG);
            objTG.addChild(filler);
            if (name == "src/codesAR280/objects/Chassis.obj") {
                PLaneObjects[] prop = new PLaneObjects[1];
                prop[0] = new createPropeller();
                objTG.addChild(prop[0].position_Object());
            }
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            this.setSchedulingBounds(thousandBS);
        }

        public void initialize() {
            wakeUpCall = new WakeupOnElapsedFrames(0);
            wakeupOn(wakeUpCall);
        }

        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            trans.set(new Vector3d(0.0f, 0.0f, MovingPlane.speed));
            trans3d.mul(trans);
            objTG.setTransform(trans3d);
            wakeupOn(wakeUpCall);
        }
    }

    static class TurretBehaviour extends Behavior {
        private Alpha bigAlpha = new Alpha();

        public static Alpha alpha = new Alpha(1, 500);
        public TransformGroup aimBot = new TransformGroup();
        private TransformGroup gggg = new TransformGroup();
        private WakeupOnElapsedFrames wakeUpCall;
        private Transform3D trans3 = new Transform3D();
        public static Transform3D trans33 = new Transform3D();
        public TurretBehaviour(Bounds bounds) {
            ammoBG = new BranchGroup();
            Appearance app = CommonsAR.obj_Appearance(CommonsAR.Green);
            Box laser = new Box(0.03f, 0.01f, 0.03f, app);
            TransformGroup turretTG = new TransformGroup();
            turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            turretTG.addChild(laser);
            ammoBG.addChild(turretTG);
            Transform3D axis = new Transform3D();
            axis.rotY(Math.PI / 2);
            PositionInterpolator getter = new PositionInterpolator(alpha, turretTG, axis, 0.0f, 50.0f);
            bigAlpha = alpha;
            getter.setSchedulingBounds(hundredBS);
            ammoBG.addChild(getter);
            aimBot.addChild(ammoBG);
            aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            this.setSchedulingBounds(bounds);
        }
        public void initialize() {
            wakeUpCall = new WakeupOnElapsedFrames(0);
            wakeupOn(wakeUpCall);
        }
        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            trans3.set(new Vector3d(0.0f, 0.0f, speed));
            trans33.mul(trans3);
            aimBot.setTransform(trans33);
            wakeupOn(wakeUpCall);

        }
    }
}