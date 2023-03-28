package codes;

import java.io.FileNotFoundException;
import java.util.Iterator;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ColoringAttributes;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnElapsedFrames;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public abstract class PLaneObjects {
	protected abstract Node create_Object();	           // use 'Node' for both Group and Shape3D
	public abstract Node position_Object();
	public float n = (float) 0.6;                         // create a variable named n to change the coordinates on the X, Y and Z axis
	int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
	ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
	Scene s = null;
}
class createPropeller extends PLaneObjects {
	private TransformGroup objTG;                          // use 'objTG' to position an object
	private BranchGroup objBG;
	public createPropeller() {
		Transform3D trans3D = new Transform3D();
		trans3D.setTranslation(new Vector3d(0.0f, -0.03f,-0.87f));
		trans3D.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, (float) (Math.PI/2)));                 // apply rotation first
		objTG = new TransformGroup(trans3D);                  // set the combined transformation
		objTG.addChild(create_Object());
	}
	private Appearance setApp(Color3f clr) {
		Appearance app = new Appearance();
		TextureLoader loader = new TextureLoader("Imports/Textures/output-onlinepngtools.png", null);
		app.setTexture(loader.getTexture());
		ColoringAttributes ca = new ColoringAttributes(CommonsAR.White, ColoringAttributes.FASTEST);
		app.setColoringAttributes(ca);
		TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.0f);
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		app.setTransparencyAttributes(ta);
		app.setPolygonAttributes(pa);
		return app;
	}
	protected Node create_Object() {
		TransformGroup transG = new TransformGroup();
		Box boxc = new Box(0.2f, 0.2f, 0.001f, Primitive.GENERATE_TEXTURE_COORDS, setApp(CommonsAR.Orange));
		objBG = new BranchGroup();
		objBG.addChild(boxc);
		objBG.addChild(CommonsAR.rotate_Behavior(500, transG, new AxisAngle4f(1.0f, 0.0f, 0.0f, (float) Math.PI/2), new Vector3d(0.0f, 0.0f,-0.87f)));
		transG.addChild(objBG);
		return transG;
	}
	public Node position_Object() {
		return objTG;
	}
}