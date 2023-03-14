package codes;
// WrapTexLand3D.java
// Andrew Davison, June 2006, ad@fivedots.coe.psu.ac.th

/* The scene's floor is multi-textured with two ground detail
   textures (grass and bits of stone) and a light map.

   The floor also has 'splashes' of extra textures (purple flowers
   and water).

   Several (NUM_BALLS) textured balls move over the floor 
   in random ways. Their position are updated at regular intervals 
   by a TimeBehavior object.

   The background is light blue. The balls reflect two light sources, 
   and the camera can be moved using OrbitBehavior controls.
 */


import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;


public class WrapTexLand3D extends JPanel
{
  private static final int PWIDTH = 512;   // size of panel
  private static final int PHEIGHT = 512; 

  private static final int BOUNDSIZE = 100;  // larger than world

  private static final Point3d USERPOSN = new Point3d(0,5,20);
    // initial user position

  private static final int NUM_BALLS = 10;     // no. of moving balls in scene
  private static final int UPDATE_TIME = 100;  // ms, for updating the balls


  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes

  private float[][] heights;  // height map for the floor


  public WrapTexLand3D()
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true);     // give focus to the canvas 
    canvas3D.requestFocus();

    su = new SimpleUniverse(canvas3D);

    reportTextureUnitInfo(canvas3D);
    createSceneGraph();
    initUserPosition();        // set user's viewpoint
    orbitControls(canvas3D);   // controls for moving the viewpoint
    
    su.addBranchGraph( sceneBG );
  } // end of WrapTexLand3D()



  private void reportTextureUnitInfo(Canvas3D c3d)
  /* Report the number of texture units supported by the machine's 
     graphics card. */
  {
    Map c3dMap = c3d.queryProperties();

    if (!c3dMap.containsKey("textureUnitStateMax"))
      System.out.println("Texture unit state maximum not found");
    else {
      int max  = ((Integer)c3dMap.get("textureUnitStateMax")).intValue();
      System.out.println("Texture unit state maximum: " + max);
    }
  }  // end of reportTextureUnitInfo()



  private void createSceneGraph() 
  // initilise the scene
  { 
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);   

    // depth-sort transparent objects on a per-geometry basis
    View view = su.getViewer().getView();
    view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

    lightScene();         // the lights
    addBackground();      // the sky
    addFloor();           // the multi-textured floor (and splashes)
    sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()


  private void lightScene()
  /* One ambient light, 2 directional lights */
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // Set up the ambient light
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(bounds);
    sceneBG.addChild(ambientLightNode);

    // Set up the directional lights
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
       // left, down, backwards 
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
       // right, down, forwards

    DirectionalLight light1 = 
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 = 
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneBG.addChild(light2);
  }  // end of lightScene()



  private void addBackground()
  // A blue sky
  { Background back = new Background();
    back.setApplicationBounds( bounds );
    back.setColor(0.17f, 0.65f, 0.92f);    // sky colour
    sceneBG.addChild( back );
  }  // end of addBackground()


  private void addFloor()
  // the floor is a multi-textured mesh, with splashes of extra textures
  {
    MultiFloor floor = new MultiFloor("grass.gif", 4, "stoneBits.gif", 2);  
             // the ground detail textures are grass and bits of stone
             /* the frequencies (4, 2) should divide into the floor length
                (FLOOR_LEN (20) in MultiFloor) with no remainder */

    heights = floor.getHeightMap();

    /* Start building an ordered group of floor meshes.
       Ordering avoids rendering conflicts between the meshes. */
    OrderedGroup floorOG = new OrderedGroup();
    floorOG.addChild(floor);

    // load the textures for the splashes
    Texture2D flowersTex = loadTexture("images/flowers.jpg");
    Texture2D waterTex = loadTexture("images/water.jpg");

    // add splashes
    for(int i=0; i < 8; i++)     // 8 splashes of flowers
      floorOG.addChild( new SplashShape(flowersTex, heights) );

    for (int i=0; i < 3; i++)    // 3 pools of water
      floorOG.addChild( new SplashShape(waterTex, heights) );

    // add all the meshes to the scene
    sceneBG.addChild( floorOG ); 
  }  // end of addFloor()



  private void orbitControls(Canvas3D c)
  /* OrbitBehaviour allows the user to rotate around the scene, and to
     zoom in and out.  */
  {
    OrbitBehavior orbit =
		new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
    orbit.setSchedulingBounds(bounds);

    ViewingPlatform vp = su.getViewingPlatform();
    vp.setViewPlatformBehavior(orbit);	 
  }  // end of orbitControls()


  private void initUserPosition()
  // Set the user's initial viewpoint using lookAt()
  {
    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup steerTG = vp.getViewPlatformTransform();

    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);

    // args are: viewer posn, where looking, up direction
    t3d.lookAt( USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
    t3d.invert();

    steerTG.setTransform(t3d);
  }  // end of initUserPosition()



  private Texture2D loadTexture(String fn)
  // load image from file fn as a texture
  {
    TextureLoader texLoader = new TextureLoader(fn, null);
    Texture2D texture = (Texture2D) texLoader.getTexture();
    if (texture == null)
      System.out.println("Cannot load texture from " + fn);
    else {
      System.out.println("Loaded texture from " + fn);
      texture.setEnable(true);
    }
    return texture;
  }  // end of loadTexture()


} // end of WrapTexLand3D class