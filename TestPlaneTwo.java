package codesAR280; import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.Alpha;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Background;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Bounds;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.IndexedTriangleArray;
import org.jogamp.java3d.PositionInterpolator;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnElapsedFrames;
import org.jogamp.java3d.WakeupOnElapsedTime;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3b;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Matrix4d;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Tuple3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;
import java.awt.event.*;
import java.util.*;
public class TestPlaneTwo extends JPanel {
	private Canvas3D canvas = null;
	private Matrix4d mtrx = new Matrix4d();
	private static float speed = -0.03f;
	private static BranchGroup ammoBG;
	private static TransformGroup aimBot;
	private static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
	private static PickTool pickTool;
	private Alpha alpha;
	PositionInterpolator targetting;
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private BoundingSphere thousandBS = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
	private static TransformGroup objTG;                              // use 'objTG' to position an object
	public static float n = (float) 1.0;
	
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
		sceneBG.addChild(sceneTG);
		sceneBG.addChild(CommonsAR.add_Lights(CommonsAR.White, 1));	
		pickTool = new PickTool(sceneBG);                // allow picking of objects in 'sceneBG'
		pickTool.setMode(PickTool.GEOMETRY);                 // pick by bounding volume
		pickTool.setMode(PickTool.BOUNDS);
		return sceneBG;
	}
	public TestPlaneTwo(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		objTG = new TransformGroup();
		aimBot = new TransformGroup();
		alpha = new Alpha(1, 500);
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		CommonsAR.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
		BranchGroup sceneBX = new BranchGroup();
        TextureLoader txtrLdr = new  TextureLoader("src/codesAR280/images/TrollFace.jpg",null);
        ImageComponent2D trippyImage = txtrLdr.getImage( );
        Background background = new Background();
        background.setImage(trippyImage);
        background.setApplicationBounds(thousandBS);
        sceneBX.addChild(background);   
		sceneBG.addChild(sceneBX);
		
		TurretBehaviour gunTurret = new TurretBehaviour(targetting, hundredBS);
		aimBot.addChild(gunTurret.aimBot);
		aimBot.addChild(gunTurret);
		Transform3D trans = new Transform3D();
		MovingPlane plane = new MovingPlane("src/codesAR280/objects/Fighter_01.obj");
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
					if(speed <= -1) {
						speed += 0.005;
						System.out.println("You Are Stalling");
					}
					else {
						speed += 0.001;
					}
				}
				if (key == 'e') {
					if(speed <= 0) {
						speed -= 0.001;
					}
					else {
						speed = 0;
						
					}
				}
				if(speed > 0) {
					speed = 0;
				}
				if (key == 'f') {
					alpha.setStartTime(System.currentTimeMillis());
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

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
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
			app.setTexture(textured_App("Fighter_01_Body_BaseColor"));
			apache.rotY((float)Math.PI);
			BranchGroup objBG = new BranchGroup();
			TransformGroup filler = new TransformGroup(apache);
			objBG = s.getSceneGroup();
			Shape3D ringObj = (Shape3D) objBG.getChild(0);
			ringObj.setAppearance(app);
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
		private static Texture textured_App(String name) {
			String filename = "src/codesAR280/images/" + name + ".png";       // tell the folder of the image
			TextureLoader loader = new TextureLoader(filename, null);
			ImageComponent2D image = loader.getImage();        // load the image
			if (image == null)
				System.out.println("Cannot load file: " + filename);

			Texture2D texture = new Texture2D(Texture.BASE_LEVEL,Texture.RGBA, image.getWidth(), image.getHeight());
			texture.setImage(0, image);                        // set image for the texture

			return texture;
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
	public static void main(String[] args) {
		frame = new JFrame();                   
		frame.getContentPane().add(new TestPlaneTwo(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}