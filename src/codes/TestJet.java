//package codes; import java.awt.BorderLayout;
//import java.awt.GraphicsConfiguration;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import org.jogamp.java3d.Alpha;
//import org.jogamp.java3d.Background;
//import org.jogamp.java3d.BoundingSphere;
//import org.jogamp.java3d.BranchGroup;
//import org.jogamp.java3d.Canvas3D;
//import org.jogamp.java3d.ImageComponent2D;
//import org.jogamp.java3d.PositionInterpolator;
//import org.jogamp.java3d.Transform3D;
//import org.jogamp.java3d.TransformGroup;
//import org.jogamp.java3d.utils.image.TextureLoader;
//import org.jogamp.java3d.utils.picking.PickTool;
//import org.jogamp.java3d.utils.universe.SimpleUniverse;
//import org.jogamp.vecmath.Matrix4d;
//import org.jogamp.vecmath.Point3d;
//import org.jogamp.vecmath.Vector3d;
//
//public class TestJet extends JPanel {
//	private Canvas3D canvas = null;
//	private Matrix4d mtrx = new Matrix4d();
//	private static BranchGroup ammoBG;
//	private static TransformGroup aimBot;
//	private static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 100.0);
//	private static PickTool pickTool;
//	private Alpha alpha;
//	protected PositionInterpolator targetting;
//	private static final long serialVersionUID = 1L;
//	private static JFrame frame;
//	private BoundingSphere thousandBS = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
//	private static TransformGroup objTG;                              // use 'objTG' to position an object
//	public static float n = (float) 1.0;
//
//	public static BranchGroup create_Scene() {
//		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
//		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
//		sceneBG.addChild(sceneTG);
//		sceneBG.addChild(CommonsAR.add_Lights(CommonsAR.White, 1));
//		pickTool = new PickTool(sceneBG);                // allow picking of objects in 'sceneBG'
//		pickTool.setMode(PickTool.GEOMETRY);                 // pick by bounding volume
//		pickTool.setMode(PickTool.BOUNDS);
//		return sceneBG;
//	}
//	public TestJet(BranchGroup sceneBG) {
//		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
//		canvas = new Canvas3D(config);
//		objTG = new TransformGroup();
//		aimBot = new TransformGroup();
//		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
//		CommonsAR.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
//		BranchGroup sceneBX = new BranchGroup();
//        TextureLoader txtrLdr = new  TextureLoader("TrollFace.jpg",null);
//        ImageComponent2D trippyImage = txtrLdr.getImage( );
//        Background background = new Background();
//        background.setImage(trippyImage);
//        background.setApplicationBounds(thousandBS);
//        sceneBX.addChild(background);
//		sceneBG.addChild(sceneBX);
//
//		Jet.TurretBehaviour gunTurret = new Jet.TurretBehaviour(hundredBS);
//		aimBot.addChild(gunTurret.aimBot);
//		aimBot.addChild(gunTurret);
//		Transform3D trans = new Transform3D();
//		Jet.MovingPlane plane = new Jet.MovingPlane("Imports/Objects/Fighter_01.obj");
//		objTG.addChild(plane.objTG);
//		objTG.addChild(plane);
//		canvas.addKeyListener(new KeyListener(){
//			@Override
//			public void keyTyped(KeyEvent e) {
//				// TODO Auto-generated method stub
//			}
//			public void keyPressed(KeyEvent e) {
//				char key = e.getKeyChar();
//
//				if (key == 'a') {
//					trans.rotY(Math.PI/20);
//					plane.objTG.getTransform(plane.trans3d);
//					plane.trans3d.get(mtrx);
//					plane.trans3d.mul(trans);
//					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					plane.objTG.setTransform(plane.trans3d);
//					gunTurret.aimBot.getTransform(gunTurret.trans33);
//					gunTurret.trans33.get(mtrx);
//					gunTurret.trans33.mul(trans);
//					gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					gunTurret.aimBot.setTransform(gunTurret.trans33);
//				}
//
//				if (key == 'd') {
//					trans.rotY(-Math.PI/20);
//					plane.objTG.getTransform(plane.trans3d);
//					plane.trans3d.get(mtrx);
//					plane.trans3d.mul(trans);
//					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					plane.objTG.setTransform(plane.trans3d);
//					gunTurret.aimBot.getTransform(gunTurret.trans33);
//					gunTurret.trans33.get(mtrx);
//					gunTurret.trans33.mul(trans);
//					gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					gunTurret.aimBot.setTransform(gunTurret.trans33);
//				}
//
//
//				if (key == 'w') {
//					trans.rotX(-Math.PI/20);
//					plane.objTG.getTransform(plane.trans3d);
//					plane.trans3d.get(mtrx);
//					plane.trans3d.mul(trans);
//					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					plane.objTG.setTransform(plane.trans3d);
//					gunTurret.aimBot.getTransform(gunTurret.trans33);
//					gunTurret.trans33.get(mtrx);
//					gunTurret.trans33.mul(trans);
//					gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					gunTurret.aimBot.setTransform(gunTurret.trans33);
//				}
//
//				if (key == 's') {
//					trans.rotX(Math.PI/20);
//					plane.objTG.getTransform(plane.trans3d);
//					plane.trans3d.get(mtrx);
//					plane.trans3d.mul(trans);
//					plane.trans3d.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					plane.objTG.setTransform(plane.trans3d);
//					gunTurret.aimBot.getTransform(gunTurret.trans33);
//					gunTurret.trans33.get(mtrx);
//					gunTurret.trans33.mul(trans);
//					gunTurret.trans33.setTranslation(new Vector3d(mtrx.m03, mtrx.m13, mtrx.m23));
//					gunTurret.aimBot.setTransform(gunTurret.trans33);
//				}
//				if (key == 'q') {
//					if(plane.speed <= -1) {
//						plane.speed += 0.005;
//						System.out.println("You Are Stalling");
//					}
//					else {
//						plane.speed += 0.001;
//					}
//				}
//				if (key == 'e') {
//					if(plane.speed <= 0) {
//						plane.speed -= 0.001;
//					}
//					else {
//						plane.speed = 0;
//
//					}
//				}
//				if(plane.speed > 0) {
//					plane.speed = 0;
//				}
//				if (key == 'f') {
//					gunTurret.alpha.setStartTime(System.currentTimeMillis());
//				}
//			}
//			@Override
//			public void keyReleased(KeyEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		sceneBG.addChild(objTG);
//		sceneBG.addChild(aimBot);
//		sceneBG.addChild(CommonsAR.key_Navigation(su));     // allow key navigation
//		sceneBG.compile();		                           // optimize the BranchGroup
//		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse
//
//		setLayout(new BorderLayout());
//		add("Center", canvas);
//		frame.setSize(800, 800);                           // set the size of the JFrame
//		frame.setVisible(true);
//	}
//
//	public static void main(String[] args) {
//		frame = new JFrame();
//		frame.getContentPane().add(new TestJet(create_Scene()));  // create an instance of the class
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
//}