package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
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

import static codes.Commons.orbitControls;

public class Demo extends JPanel {
    private Canvas3D canvas = null;
    private Matrix4d mtrx = new Matrix4d();
    private static float speed = -0.03f;
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
        sceneBG.addChild(new City().position_Object());
        return sceneBG;
    }
    public Demo(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        objTG = new TransformGroup();
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

        Transform3D trans = new Transform3D();
        MovingPlane plane = new MovingPlane("Imports/Objects/Chassis.obj");
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
        public MovingPlane(String name){
            int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
            ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
            f.setBasePath("src/codesAR280/objects/Chassis.mtl");
            Scene s = null;
            try {
                f.getBasePath();
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
            PLaneObjects[] prop = new PLaneObjects[1];
            prop[0] = new createPropeller();
            objTG.addChild(s.getSceneGroup());
            objTG.addChild(prop[0].position_Object());
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            this.setSchedulingBounds(thousandBS);
        }
        public void initialize() {
            wakeUpCall = new WakeupOnElapsedFrames(0);
            wakeupOn(wakeUpCall);
        }
        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            trans.set(new Vector3d(0.0, height, speed));
            trans3d.mul(trans);
            objTG.setTransform(trans3d);
            wakeupOn(wakeUpCall);
        }
    }
    public static void main(String[] args) throws IOException {
        frame = new JFrame();                   // NOTE: change AR to student's initials
        frame.getContentPane().add(new Demo(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
