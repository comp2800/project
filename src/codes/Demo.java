package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.AudioDevice;
import org.jogamp.java3d.Background;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.MediaContainer;
import org.jogamp.java3d.PointSound;
import org.jogamp.java3d.audioengines.javasound.JavaSoundMixer;
import org.jogamp.java3d.utils.image.TextureLoader;

import static codes.Commons.*;

public class Demo extends JPanel {
    private Canvas3D[] canvas;
    private Matrix4d mtrx = new Matrix4d();
    private static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private static TransformGroup objTG;                              // use 'objTG' to position an object

    SoundEffect sound = new SoundEffect();

    public static BranchGroup create_Scene() throws IOException {
        BranchGroup sceneBG = new BranchGroup();           // create the scene's BranchGroup
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
        Texture bgTexture = new TextureLoader("Imports/Textures/bg2.png", null).getTexture();
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
        canvas = new Canvas3D[2];
        for (int i = 0; i < 1; i++) { // MULTI i=2
            canvas[i] = new Canvas3D(config);
//            canvas[i].setSize(600, 800); // MULTI SCREEN
            canvas[i].setSize(1920, 1080); // SOLO SCREEN
            add(canvas[i]);                            // add 2 Canvas3D to Frame
        }
        objTG = new TransformGroup();
        SimpleUniverse su = new SimpleUniverse();    // create a SimpleUniverse
        Locale lcl = su.getLocale();
        ViewingPlatform vp = new ViewingPlatform(1);
        ViewingPlatform vp2 = new ViewingPlatform(1);
        lcl.addBranchGraph(createViewer(vp, canvas[0], 10, 0, 0));
        lcl.addBranchGraph(createViewer(vp2, canvas[1], -10, 0, 0)); // MULTI CANVAS
        CommonsAR.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
        su.getViewer().getView().setBackClipDistance(1000);

        Background bg = new Background();
        bg.setImage(new TextureLoader("Imports/Textures/bg2.png", null).getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));

        sound.setFile("Imports/Sounds/background.wav");
        sound.loop();


        sceneBG.addChild(bg);


        TurretBehaviour gunTurret1 = new TurretBehaviour(hundredBS);
        Transform3D trans1 = new Transform3D();
        trans1.setTranslation(new Vector3f(100, 0, 0));
        MovingPlane plane1 = new MovingPlane("Imports/Objects/Fighter_01.obj", vp);
        objTG.addChild(plane1.objTG);
        objTG.addChild(plane1);

        TurretBehaviour gunTurret2 = new TurretBehaviour(hundredBS);
        Transform3D trans2 = new Transform3D();
        trans2.setTranslation(new Vector3f(0, 0, 0));
        MovingPlane plane2 = new MovingPlane("Imports/Objects/Fighter_01.obj", vp2);
//        objTG.addChild(plane2.objTG); // MULTI
//        objTG.addChild(plane2);       // MULTI
        canvas[0].addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (key == 'a') {
                    trans1.rotY(Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);

                }

                if (key == 'd') {
                    trans1.rotY(-Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }


                if (key == 'w') {
                    trans1.rotX(-Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }

                if (key == 's') {
                    trans1.rotX(Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    gunTurret1.aimBot.getTransform(gunTurret1.trans33);
                    gunTurret1.trans33.get(mtrx);
                    gunTurret1.trans33.mul(trans1);
                    gunTurret1.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret1.aimBot.setTransform(gunTurret1.trans33);
                }
                if (key == 'q') {
                    if (plane1.speed <= -1) {
                        plane1.speed += 0.005;
//                        plane1.speed += 5; // SOLO HYPERSPACE
                        System.out.println("You Are Stalling");
                    } else {
                        plane1.speed += 0.001;
//                        plane1.speed += 1; // SOLO MACH 10
                    }
                }
                if (key == 'e') {
                    if (plane1.speed <= 0) {
                        plane1.speed -= 0.001;
//                        plane1.speed -= 1; // SOLO CRASH
                    } else {
                        plane1.speed = 0;

                    }
                }
                if (plane1.speed > 0) {
                    plane1.speed = 0;
                }
                if (key == 'f') {
                    gunTurret1.alpha.setStartTime(System.currentTimeMillis());
                    sound.setFile("Imports/Sounds/s2.wav");
                    sound.play();
                }
                if (key == 'j') {
                    trans2.rotY(Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }

                if (key == 'l') {
                    trans2.rotY(-Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }

                if (key == 'k') {
                    trans2.rotX(-Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }

                if (key == 'i') {
                    trans2.rotX(Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    gunTurret2.aimBot.getTransform(gunTurret2.trans33);
                    gunTurret2.trans33.get(mtrx);
                    gunTurret2.trans33.mul(trans2);
                    gunTurret2.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    gunTurret2.aimBot.setTransform(gunTurret2.trans33);
                }

                if (key == 'u') {
                    if (plane2.speed <= -1) {
                        plane2.speed += 0.005;
                        System.out.println("You Are Stalling");
                    } else {
                        plane2.speed += 0.001;
                    }
                }

                if (key == 'o') {
                    if (plane2.speed <= 0) {
                        plane2.speed -= 0.001;
                    } else {
                        plane2.speed = 0;

                    }
                }
                if (plane2.speed > 0) {
                    plane2.speed = 0;
                }
                if (key == ';') {
                    gunTurret2.alpha.setStartTime(System.currentTimeMillis());
                    sound.setFile("Imports/Sounds/s2.wav");
                    sound.play();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }


        });

        sceneBG.addChild(objTG);
        sceneBG.addChild(gunTurret1);
//        sceneBG.addChild(gunTurret2); // MULTI
        sceneBG.addChild(CommonsAR.key_Navigation(su));     // allow key navigation
        sceneBG.compile();                                   // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas[0], su);


    }

    public class SoundEffect {

        Clip clip;

        public void setFile(String filename) {

            try {
                File file = new File(filename);
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } catch (Exception e) {

            }
        }

        public void play() {
            clip.setFramePosition(clip.getFramePosition());
            clip.start();

        }

        public void loop() {
            clip.setFramePosition(clip.getFramePosition());
            clip.start();
            clip.loop(10000);


        }

        public void stop() {
            clip.stop();
            clip.close();
        }

    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame();
        frame.getContentPane().add(new Demo(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
