package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import static codes.Commons.createViewer;
import static codes.Commons.orbitControls;

public class Game extends JPanel {
    private static JFrame frame;
    private static TransformGroup objTG;                              // use 'objTG' to position an object
    SoundEffect sound = new SoundEffect();
    private Canvas3D[] canvas;
    private Matrix4d mtrx = new Matrix4d();

    public Game(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D[2];
        for (int i = 0; i < 2; i++) { // MULTI i=2
            canvas[i] = new Canvas3D(config);
            canvas[i].setSize(600, 800); // MULTI SCREEN
//            canvas[i].setSize(1920, 1080); // SOLO SCREEN
            add(canvas[i]);                            // add 2 Canvas3D to Frame
        }
        objTG = new TransformGroup();
        SimpleUniverse su = new SimpleUniverse();    // create a SimpleUniverse
        Locale lcl = su.getLocale();
        ViewingPlatform vp = new ViewingPlatform(1);
        ViewingPlatform vp2 = new ViewingPlatform(1);
        lcl.addBranchGraph(createViewer(vp, canvas[0]));
        lcl.addBranchGraph(createViewer(vp2, canvas[1])); // MULTI CANVAS
        Commons.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
        su.getViewer().getView().setBackClipDistance(1000);

        Background bg = new Background();
        bg.setImage(new TextureLoader("Imports/Textures/bg2.png", null).getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));

        sound.setFile("Imports/Sounds/background.wav");
        sound.loop();

        sceneBG.addChild(bg);


        Turret gunTurret1 = new Turret();
        Transform3D trans1 = new Transform3D();
        MovingPlane plane1 = new MovingPlane("Imports/Objects/Fighter_01.obj", vp);
        objTG.addChild(plane1.objTG);
        objTG.addChild(plane1);

        Turret gunTurret2 = new Turret();
        Transform3D trans2 = new Transform3D();
        MovingPlane plane2 = new MovingPlane("Imports/Objects/Fighter_01.obj", vp2);
        objTG.addChild(plane2.objTG); // MULTI
        objTG.addChild(plane2);       // MULTI
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
                    Transform3D T3D = new Transform3D();
                    plane1.objTG.getTransform(T3D);
                    gunTurret1.aimBot.setTransform(T3D);

                }

                if (key == 'd') {
                    trans1.rotY(-Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane1.objTG.getTransform(T3D);
                    gunTurret1.aimBot.setTransform(T3D);
                }


                if (key == 'w') {
                    trans1.rotX(-Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane1.objTG.getTransform(T3D);
                    gunTurret1.aimBot.setTransform(T3D);
                }

                if (key == 's') {
                    trans1.rotX(Math.PI / 20);
                    plane1.objTG.getTransform(plane1.trans3d);
                    plane1.trans3d.get(mtrx);
                    plane1.trans3d.mul(trans1);
                    plane1.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane1.objTG.setTransform(plane1.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane1.objTG.getTransform(T3D);
                    gunTurret1.aimBot.setTransform(T3D);
                }
                if (key == 'q') {
                    if (plane1.speed <= -1) {
                        plane1.speed += 0.005;
                        Transform3D T3D = new Transform3D();
                        plane1.objTG.getTransform(T3D);
                        gunTurret1.aimBot.setTransform(T3D);
//                        plane1.speed += 5; // SOLO HYPERSPACE
                        System.out.println("You Are Stalling");
                    } else {
                        plane1.speed += 0.001;
                        Transform3D T3D = new Transform3D();
                        plane1.objTG.getTransform(T3D);
                        gunTurret1.aimBot.setTransform(T3D);
//                        plane1.speed += 1; // SOLO MACH 10
                    }
                }
                if (key == 'e') {
                    if (plane1.speed <= 0) {
                        plane1.speed -= 0.001;
                        Transform3D T3D = new Transform3D();
                        plane1.objTG.getTransform(T3D);
                        gunTurret1.aimBot.setTransform(T3D);
//                        plane1.speed -= 1; // SOLO CRASH
                    } else {
                        plane1.speed = 0;
                        Transform3D T3D = new Transform3D();
                        plane1.objTG.getTransform(T3D);
                        gunTurret1.aimBot.setTransform(T3D);
                    }
                }
                if (plane1.speed > 0) {
                    plane1.speed = 0;
                    Transform3D T3D = new Transform3D();
                    plane1.objTG.getTransform(T3D);
                    gunTurret1.aimBot.setTransform(T3D);
                }
                if (key == 'f') {
                    gunTurret1.alpha.setStartTime(System.currentTimeMillis());
                    sound.setFile("Imports/Sounds/s2.wav");
                    sound.play();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }


        });
        canvas[0].addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (key == 'j') {
                    trans2.rotY(Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane2.objTG.getTransform(T3D);
                    gunTurret2.aimBot.setTransform(T3D);
                }

                if (key == 'l') {
                    trans2.rotY(-Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane2.objTG.getTransform(T3D);
                    gunTurret2.aimBot.setTransform(T3D);
                }

                if (key == 'k') {
                    trans2.rotX(-Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane2.objTG.getTransform(T3D);
                    gunTurret2.aimBot.setTransform(T3D);
                }

                if (key == 'i') {
                    trans2.rotX(Math.PI / 20);
                    plane2.objTG.getTransform(plane2.trans3d);
                    plane2.trans3d.get(mtrx);
                    plane2.trans3d.mul(trans2);
                    plane2.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
                    plane2.objTG.setTransform(plane2.trans3d);
                    Transform3D T3D = new Transform3D();
                    plane2.objTG.getTransform(T3D);
                    gunTurret2.aimBot.setTransform(T3D);
                }

                if (key == 'u') {
                    if (plane2.speed <= -1) {
                        plane2.speed += 0.005;
                        Transform3D T3D = new Transform3D();
                        plane2.objTG.getTransform(T3D);
                        gunTurret2.aimBot.setTransform(T3D);
                        System.out.println("You Are Stalling");
                    } else {
                        plane2.speed += 0.001;
                        Transform3D T3D = new Transform3D();
                        plane2.objTG.getTransform(T3D);
                        gunTurret2.aimBot.setTransform(T3D);
                    }
                }

                if (key == 'o') {
                    if (plane2.speed <= 0) {
                        plane2.speed -= 0.001;
                        Transform3D T3D = new Transform3D();
                        plane2.objTG.getTransform(T3D);
                        gunTurret2.aimBot.setTransform(T3D);
                    } else {
                        plane2.speed = 0;
                        Transform3D T3D = new Transform3D();
                        plane2.objTG.getTransform(T3D);
                        gunTurret2.aimBot.setTransform(T3D);

                    }
                }
                if (plane2.speed > 0) {
                    plane2.speed = 0;
                    Transform3D T3D = new Transform3D();
                    plane2.objTG.getTransform(T3D);
                    gunTurret2.aimBot.setTransform(T3D);
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
        sceneBG.addChild(gunTurret1.aimBot);
        sceneBG.addChild(gunTurret2.aimBot); // MULTI
        sceneBG.addChild(Commons.key_Navigation(su));     // allow key navigation
        sceneBG.compile();                                   // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas[0], su);


    }

    public static BranchGroup create_Scene() throws IOException {
        BranchGroup sceneBG = new BranchGroup();           // create the scene's BranchGroup
        TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
        sceneBG.addChild(sceneTG);
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1));
        Asteroid[] asteroids = new Asteroid[1];
        for (Asteroid asteroid : asteroids) {
            asteroid = new Asteroid();
            sceneTG.addChild(asteroid);
            sceneTG.addChild(asteroid.objTG);
            AsteroidCollisionDetector cdGroup = new AsteroidCollisionDetector(asteroid);
            cdGroup.setSchedulingBounds(Commons.twentyBS);
            sceneTG.addChild(cdGroup);
        }

        sceneTG.addChild(new SolarSystem().create_Object());

        sceneTG.addChild(new Space("Imports/Textures/bg2.png").setApp());

        return sceneBG;
    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame();
        frame.getContentPane().add(new Game(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
