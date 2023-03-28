package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import static codes.Commons.*;

public class Demo extends JPanel {
    private Canvas3D canvas = null;
    private Matrix4d mtrx = new Matrix4d();
    private static float speed = -0.03f;
    private Alpha alpha;
    protected PositionInterpolator targetting;
    private static BranchGroup ammoBG;
    private static TransformGroup aimBot;
    private static float height = 0.0f;
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private BoundingSphere thousandBS = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
    private static TransformGroup objTG;                              // use 'objTG' to position an object
    public static float n = (float) 1.0;
    public static BranchGroup create_Scene() throws IOException {
        BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
        TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
        sceneBG.addChild(sceneTG);
        sceneBG.addChild(CommonsAR.add_Lights(CommonsAR.White, 1));
        Asteroid[] asteroids = new Asteroid[500];
        for (Asteroid asteroid : asteroids) {
            asteroid = new Asteroid();
            sceneTG.addChild(asteroid);
            sceneTG.addChild(asteroid.objTG);
        }
        sceneTG.addChild(new SolarSystem().create_Object());

        Appearance bgAppearance = new Appearance();
        Texture bgTexture = new TextureLoader("Imports/Textures/bg.png", null).getTexture();
        bgTexture.setBoundaryModeS(Texture.WRAP);
        bgTexture.setBoundaryModeT(Texture.WRAP);
        bgAppearance.setTexture(bgTexture);

        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        bgAppearance.setPolygonAttributes(pa);

        Sphere bgSphere = new Sphere(10000f, Sphere.GENERATE_TEXTURE_COORDS, bgAppearance);
        bgSphere.setCapability(Sphere.ALLOW_BOUNDS_WRITE);
        bgSphere.setCapability(Sphere.ALLOW_LOCAL_TO_VWORLD_READ);

        sceneTG.addChild(bgSphere);

        return sceneBG;
    }
    public Demo(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        objTG = new TransformGroup();
        aimBot = new TransformGroup();
        SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
        CommonsAR.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
        su.getViewer().getView().setBackClipDistance(1000);
        BranchGroup sceneBX = new BranchGroup();

        Background bg = new Background();
        bg.setImage(new TextureLoader("Imports/Textures/bg.png", null).getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));

        sceneBG.addChild(bg);

        sceneBG.addChild(sceneBX);

        TurretBehaviour gunTurret = new TurretBehaviour(targetting, hundredBS);
        aimBot.addChild(gunTurret.aimBot);
        aimBot.addChild(gunTurret);
        Transform3D trans = new Transform3D();
        MovingPlane plane = new MovingPlane("Imports/Objects/Fighter_01.obj");
        objTG.addChild(plane.objTG);
        objTG.addChild(plane);
        canvas.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (key == 'a') {
                    height = 0;
                    trans.rotY(Math.PI/45);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                }

                if (key == 'd') {
                    height = 0;
                    trans.rotY(-Math.PI/45);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                }


                if (key == 'w') {
                    height = 0;
                    trans.rotX(-Math.PI/145);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                }

                if (key == 's') {
                    height = 0;
                    trans.rotX(Math.PI/145);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                }
                if (key == 'q') {
                    if(speed <= -1) {
                        height -= 0.01;
                        speed += 0.005;
                        System.out.println("You Are Stalling");
                    }
                    else {
                        speed += 0.001;
                        height -= 0.0005;
                    }
                }
                if (key == 'e') {
                    if(speed <= 0) {
                        speed -= 0.001;
                        height += 0.0005;
                    }
                    else {
                        speed = 0;

                    }
                }
                if(speed > 0) {
                    speed = 0;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });
        sceneBG.addChild(objTG);

        sceneBG.addChild(CommonsAR.key_Navigation(su));     // allow key navigation
        sceneBG.compile();		                           // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas,su);

        setLayout(new BorderLayout());
        add("Center", canvas);

    }

    class MovingPlane extends Behavior {
        public TransformGroup objTG = new TransformGroup();
        public Transform3D trans3d = new Transform3D();
        private Transform3D trans = new Transform3D();
        private WakeupOnElapsedFrames wakeUpCall;
        public Transform3D apache = new Transform3D();
        public MovingPlane(String name){
            int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
            ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
            Scene s = null;
            try {
                s = f.load(name);
            }catch(FileNotFoundException e){
                System.err.println(e);
                System.exit(1);
            }catch(ParsingErrorException e){
                System.err.println(e);
                System.exit(1);
            }catch(IncorrectFormatException e){
                System.err.println(e);
                System.exit(1);
            }
            Appearance app = new Appearance();
            apache.rotY((float)Math.PI);
            BranchGroup objBG = new BranchGroup();
            TransformGroup filler = new TransformGroup(apache);
            objBG = s.getSceneGroup();
            Shape3D ringObj = (Shape3D) objBG.getChild(0);
            ringObj.setAppearance(setApp("Imports/Textures/Fighter_01_Body_BaseColor.png"));
            filler.addChild(objBG);
            objTG.addChild(filler);
            if(name == "src/codesAR280/objects/Chassis.obj") {
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
            trans.set(new Vector3d(0.0f, 0.0f, speed));
            trans3d.mul(trans);
            objTG.setTransform(trans3d);
            wakeupOn(wakeUpCall);
        }
    }

    class TurretBehaviour extends Behavior {
        private Alpha bigAlpha = new Alpha();
        public TransformGroup aimBot = new TransformGroup();
        private TransformGroup gggg = new TransformGroup();
        private WakeupOnElapsedFrames wakeUpCall;
        private Transform3D trans3 = new Transform3D();
        private Transform3D trans33 = new Transform3D();
        public TurretBehaviour(PositionInterpolator getter, Bounds bounds) {
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
            getter = new PositionInterpolator(alpha, turretTG, axis, 0.0f, 50.0f);
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
    public static void main(String[] args) throws IOException {
        frame = new JFrame();                   // NOTE: change AR to student's initials
        frame.getContentPane().add(new Demo(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
