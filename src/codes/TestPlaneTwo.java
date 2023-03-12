package codesAR280; import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class TestPlaneTwo extends JPanel {
	private Canvas3D canvas = null;
	private Matrix4d mtrx = new Matrix4d();
	private static float speed = -0.03f;
	private static float height = 0.0f;
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
		return sceneBG;
	}
	public TestPlaneTwo(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		objTG = new TransformGroup();
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
		
		Transform3D trans = new Transform3D();
		MovingPlane plane = new MovingPlane("src/codesAR280/objects/Chassis.obj");
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
					trans.rotY(Math.PI/45);
					plane.objTG.getTransform(plane.trans3d);
					plane.trans3d.get(mtrx);
					plane.trans3d.mul(trans);
					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
					plane.objTG.setTransform(plane.trans3d);
				}

				if (key == 'd') {
					trans.rotY(-Math.PI/45);
					plane.objTG.getTransform(plane.trans3d);
					plane.trans3d.get(mtrx);
					plane.trans3d.mul(trans);
					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
					plane.objTG.setTransform(plane.trans3d);
				}
				

				if (key == 'w') {
					trans.rotX(-Math.PI/145);
					plane.objTG.getTransform(plane.trans3d);
					plane.trans3d.get(mtrx);
					plane.trans3d.mul(trans);
					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
					plane.objTG.setTransform(plane.trans3d);
				}

				if (key == 's') {
					trans.rotX(Math.PI/145);
					plane.objTG.getTransform(plane.trans3d);
					plane.trans3d.get(mtrx);
					plane.trans3d.mul(trans);
					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
					plane.objTG.setTransform(plane.trans3d);
				}
				if (key == 's') {
					trans.rotX(Math.PI/145);
					plane.objTG.getTransform(plane.trans3d);
					plane.trans3d.get(mtrx);
					plane.trans3d.mul(trans);
					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
					plane.objTG.setTransform(plane.trans3d);
				}
				if (key == 'q') {
					if(speed >= 30) {
						speed = speed;
					}
					else {
						speed -= 0.001;
						height += 0.0005;
					}
				}
				if (key == 'e') {
					if(speed <= 0) {
						speed += 0.001;
						trans.rotX(-Math.PI/180);
						plane.objTG.getTransform(plane.trans3d);
						plane.trans3d.get(mtrx);
						plane.trans3d.mul(trans);
						plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
						plane.objTG.setTransform(plane.trans3d);
						height -= 0.0005;
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
	public static void main(String[] args) {
		frame = new JFrame();                   // NOTE: change AR to student's initials
		frame.getContentPane().add(new TestPlaneTwo(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}