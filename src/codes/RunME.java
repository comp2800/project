package codes;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import static codes.Commons.loadTexture;
import static codes.Commons.orbitControls;

public class RunME extends JPanel {

    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    /* a function to build the content branch */
    public static BranchGroup create_Scene() throws IOException {
        BranchGroup sceneBG = new BranchGroup();           // create the scene's BranchGroup
        TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1));
        Background bg = new Background();
        bg.setImage(new TextureLoader("Imports/Textures/bg.png", null).getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));
        Transform3D T3D = new Transform3D();
        T3D.setRotation(new AxisAngle4f(-1,0,0, (float) Math.PI/2));
        TransformGroup TG = new TransformGroup(T3D);
        sceneTG.addChild(new Detroit().position_Object());

        sceneTG.addChild(TG);
//        sceneTG.addChild(bg);
        sceneBG.addChild(sceneTG);
        return sceneBG;
    }

    /* NOTE: Keep the constructor for each of the labs and assignments */
    public RunME(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);

        SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
        Commons.define_Viewer(su, new Point3d(0.0d, 5.0d, 1.0d));
        su.getViewer().getView().setBackClipDistance(1000);

        sceneBG.addChild(Commons.key_Navigation(su));     // allow key navigation

        sceneBG.compile();                                   // optimize the BranchGroup
        su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

        orbitControls(canvas, su);

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(800, 800);                           // set the size of the JFrame
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame("Project");                   // NOTE: change XY to student's initials
        frame.getContentPane().add(new RunME(create_Scene()));  // create an instance of the class
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}