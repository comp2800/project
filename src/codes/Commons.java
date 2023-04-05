package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Commons extends JPanel {

    public final static Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
    public final static Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
    public final static Color3f Blue = new Color3f(0.0f, 0.0f, 1.0f);
    public final static Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
    public final static Color3f Cyan = new Color3f(0.0f, 1.0f, 1.0f);
    public final static Color3f Orange = new Color3f(1.0f, 0.5f, 0.0f);
    public final static Color3f Magenta = new Color3f(1.0f, 0.0f, 1.0f);
    public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
    public final static Color3f Grey = new Color3f(0.35f, 0.35f, 0.35f);
    public final static Color3f Black = new Color3f(0.0f, 0.0f, 0.0f);
    public final static Color3f[] clr_list = {Blue, Green, Red, Yellow,
            Cyan, Orange, Magenta, Grey};
    public final static int clr_num = 8;
    public final static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
    public final static BoundingSphere twentyBS = new BoundingSphere(new Point3d(), 20.0);
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private static Color3f[] mtl_clrs = {White, Grey, Black};

    /* Loads blender obj file and returns BranchGroup */
    public static BranchGroup f_load(String path) throws IOException {
        // Loading wanted object's BranchGroup
        ObjectFile f = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY, (float) (60 * Math.PI / 180.0));
        Scene s = f.load(path);
        return s.getSceneGroup();
    }

    /* A1: function to define object's material and use it to set object's appearance */
    public static Appearance obj_Appearance(Color3f m_clr) {
        Material mtl = new Material();                     // define material's attributes
        mtl.setShininess(32);
        mtl.setAmbientColor(mtl_clrs[0]);                   // use them to define different materials
        mtl.setDiffuseColor(m_clr);
        mtl.setSpecularColor(mtl_clrs[1]);
        mtl.setEmissiveColor(mtl_clrs[2]);                  // use it to switch button on/off
        mtl.setLightingEnable(true);

        Appearance app = new Appearance();
        app.setMaterial(mtl);                              // set appearance's material
        return app;
    }

    public static Texture texturedApp(String filepath) {
        TextureLoader loader = new TextureLoader(filepath, null);
        ImageComponent2D image = loader.getImage();
        if (image == null)
            System.out.println("Cannot load file: " + filepath);
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
                Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        return texture;
    }

    public static Appearance setApp(String filepath) {
        Appearance app = new Appearance();
        app.setTexture(texturedApp(filepath));
        return app;
    }

    public static TransformGroup addRotation(TransformGroup TG, int speed, AxisAngle4f V) {
        TransformGroup rotatedTG = new TransformGroup();
        rotatedTG.addChild(TG);
        RotationInterpolator rot = Commons.rotate_Behavior(speed, rotatedTG);
        Transform3D rotation = new Transform3D();
        rotation.setRotation(V);
        rot.setTransformAxis(rotation);
        rotatedTG.addChild(rot);
        return rotatedTG;
    }

    public static ViewingPlatform createViewer(ViewingPlatform vp, Canvas3D canvas3D) {
        // a Canvas3D can only be attached to a single Viewer
        Viewer viewer = new Viewer(canvas3D);                 // attach a Viewer to its canvas
        viewer.getView().setBackClipDistance(10000);

        // set TG's capabilities to allow KeyNavigatorBehavior modify the Viewer's position
        vp.getViewPlatformTransform().setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        vp.getViewPlatformTransform().setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        KeyNavigatorBehavior key = new KeyNavigatorBehavior(vp.getViewPlatformTransform());
        key.setSchedulingBounds(new BoundingSphere());          // enable viewer navigation
        key.setEnable(false);
        vp.addChild(key);                                   // add KeyNavigatorBehavior to VP
        viewer.setViewingPlatform(vp);                      // set VP for the Viewer
        return vp;
    }

    /* a function to create a rotation behavior and refer it to 'my_TG' */
    public static RotationInterpolator rotate_Behavior(int r_num, TransformGroup rotTG) {

        rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, r_num);
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotationAlpha, rotTG, yAxis, 0.0f, (float) Math.PI * 2.0f);
        rot_beh.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100000));
        return rot_beh;
    }

    public static void orbitControls(Canvas3D c, SimpleUniverse su)
  /* OrbitBehaviour allows the user to rotate around the scene, and to
     zoom in and out.  */ {
        OrbitBehavior orbit =
                new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100000));

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    /* a function to place one light or two lights at opposite locations */
    public static BranchGroup add_Lights(Color3f clr, int p_num) {
        BranchGroup lightBG = new BranchGroup();
        Point3f atn = new Point3f(0.5f, 0.0f, 0.0f);
        PointLight ptLight;
        float adjt = 1f;
        for (int i = 0; (i < p_num) && (i < 2); i++) {
            if (i > 0)
                adjt = -1f;
            ptLight = new PointLight(clr, new Point3f(3.0f * adjt, 1.0f, 3.0f * adjt), atn);
            ptLight.setInfluencingBounds(hundredBS);
            lightBG.addChild(ptLight);
        }
        return lightBG;
    }

    /* a function to position viewer to 'eye' location */
    public static void define_Viewer(SimpleUniverse simple_U, Point3d eye) {

        TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);             // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);               // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);               // set the TransformGroup of ViewingPlatform
    }

    /* a function to allow key navigation with the ViewingPlateform */
    public static KeyNavigatorBehavior key_Navigation(SimpleUniverse simple_U) {
        ViewingPlatform view_platfm = simple_U.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
        keyNavBeh.setSchedulingBounds(twentyBS);
        return keyNavBeh;
    }
}
