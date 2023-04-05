package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;

import static codes.Commons.hundredBS;

public class TurretBehaviour {
    public Alpha alpha = new Alpha(1, 500);
    public TransformGroup aimBot = new TransformGroup();
    public BranchGroup ammoBG;

    public TurretBehaviour() {
        ammoBG = new BranchGroup();
        Appearance app = Commons.obj_Appearance(Commons.Green);
        Box laser = new Box(0.09f, 0.03f, 0.09f, app);
        TransformGroup turretTG = new TransformGroup();
        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        turretTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        turretTG.addChild(laser);
        ammoBG.addChild(turretTG);
        Transform3D axis = new Transform3D();
        axis.rotY(Math.PI / 2);
        PositionInterpolator getter = new PositionInterpolator(alpha, turretTG, axis, 0.0f, 100.0f);
        getter.setSchedulingBounds(hundredBS);
        ammoBG.addChild(getter);
        aimBot.addChild(ammoBG);
        aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        aimBot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        aimBot.setCollidable(true);

    }

}
