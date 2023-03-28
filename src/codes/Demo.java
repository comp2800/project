package codes;

import org.jogamp.java3d.*;
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
import java.io.IOException;

import static codes.Commons.*;

public class Demo extends JPanel {
    private Canvas3D canvas = null;
    private Matrix4d mtrx = new Matrix4d();
    private static float speed = -0.03f;
    protected PositionInterpolator targetting;

    private static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
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

        Jet.TurretBehaviour gunTurret = new Jet.TurretBehaviour(hundredBS);
        aimBot.addChild(gunTurret.aimBot);
        aimBot.addChild(gunTurret);
        Transform3D trans = new Transform3D();
        Jet.MovingPlane plane = new Jet.MovingPlane("Imports/Objects/Fighter_01.obj");
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
                    trans.rotY(Math.PI/20);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                    gunTurret.aimBot.getTransform(gunTurret.trans33);
                    gunTurret.trans33.get(mtrx);
                    gunTurret.trans33.mul(trans);
                    gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret.aimBot.setTransform(gunTurret.trans33);
                }

                if (key == 'd') {
                    trans.rotY(-Math.PI/20);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                    gunTurret.aimBot.getTransform(gunTurret.trans33);
                    gunTurret.trans33.get(mtrx);
                    gunTurret.trans33.mul(trans);
                    gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret.aimBot.setTransform(gunTurret.trans33);
                }


                if (key == 'w') {
                    trans.rotX(-Math.PI/20);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                    gunTurret.aimBot.getTransform(gunTurret.trans33);
                    gunTurret.trans33.get(mtrx);
                    gunTurret.trans33.mul(trans);
                    gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret.aimBot.setTransform(gunTurret.trans33);
                }

                if (key == 's') {
                    trans.rotX(Math.PI/20);
                    plane.objTG.getTransform(plane.trans3d);
                    plane.trans3d.get(mtrx);
                    plane.trans3d.mul(trans);
                    plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane.objTG.setTransform(plane.trans3d);
                    gunTurret.aimBot.getTransform(gunTurret.trans33);
                    gunTurret.trans33.get(mtrx);
                    gunTurret.trans33.mul(trans);
                    gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret.aimBot.setTransform(gunTurret.trans33);
                }
                if (key == 'q') {
                    if(plane.speed <= -1) {
                        plane.speed += 0.005;
                        System.out.println("You Are Stalling");
                    }
                    else {
                        plane.speed += 0.001;
                    }
                }
                if (key == 'e') {
                    if(plane.speed <= 0) {
                        plane.speed -= 0.001;
                    }
                    else {
                        plane.speed = 0;

                    }
                }
                if(plane.speed > 0) {
                    plane.speed = 0;
                }
                if (key == 'f') {
                    gunTurret.alpha.setStartTime(System.currentTimeMillis());
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });
        sceneBG.addChild(objTG);
        sceneBG.addChild(aimBot);
        sceneBG.addChild(CommonsAR.key_Navigation(su));     // allow key navigation
        sceneBG.compile();		                           // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas,su);

        setLayout(new BorderLayout());
        add("Center", canvas);

    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame();                   // NOTE: change AR to student's initials
        frame.getContentPane().add(new Demo(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
