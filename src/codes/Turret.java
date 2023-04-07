package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.vecmath.Point3d;

import static codes.Commons.hundredBS;

public class Turret {
    public Alpha alpha = new Alpha(1, 500);
    public TransformGroup aimBot = new TransformGroup();
    public BranchGroup ammoBG;

    public Turret() {
        ammoBG = new BranchGroup();
        Appearance app = Commons.obj_Appearance(Commons.Green);
        Box laser = new Box(0.1f, 0.1f, 0.1f, app);
        laser.setCapability(Shape3D.ALLOW_BOUNDS_WRITE);
        laser.setBounds(new BoundingSphere(new Point3d(0, 0, 0), 1));
        TransformGroup turretTG = new TransformGroup();
        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        turretTG.addChild(laser);
        ammoBG.addChild(turretTG);
        Transform3D axis = new Transform3D();
        axis.rotY(Math.PI / 2);
        PositionInterpolator getter = new PositionInterpolator(alpha, turretTG, axis, 0.0f, 50.0f);
        getter.setSchedulingBounds(hundredBS);
        ammoBG.addChild(getter);
        aimBot.addChild(ammoBG);
        aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        aimBot.setCollidable(true);

    }

}
