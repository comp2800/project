package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Point3d;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static codes.Commons.orbitControls;

public class RunME extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    /* NOTE: Keep the constructor for each of the labs and assignments */
    public RunME(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);

        SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
        Commons.define_Viewer(su, new Point3d(0.0d, 5.0d, 1000.0d));
        su.getViewer().getView().setBackClipDistance(100000);

        sceneBG.addChild(Commons.key_Navigation(su));     // allow key navigation

        sceneBG.compile();                                   // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas, su);

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(1920, 1080);                           // set the size of the JFrame
        frame.setVisible(true);
    }

    /* a function to build the content branch */
    public static BranchGroup create_Scene() throws IOException {
        BranchGroup sceneBG = new BranchGroup();           // create the scene's BranchGroup
        TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1));
        Transform3D T3D = new Transform3D();
        T3D.setRotation(new AxisAngle4f(-1, 0, 0, (float) Math.PI / 2));
        TransformGroup TG = new TransformGroup(T3D);
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

        sceneTG.addChild(TG);
        sceneBG.addChild(sceneTG);
        return sceneBG;
    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame("Project");                   // NOTE: change XY to student's initials
        frame.getContentPane().add(new RunME(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}