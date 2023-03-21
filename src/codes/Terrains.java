package codes;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3f;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class Terrains {
    protected abstract Node create_Object() throws IOException;

    public abstract Node position_Object() throws IOException;

}

class Landscape extends Terrains {


    protected Node create_Object() throws IOException {
        return Commons.f_load("Imports/Objects/landscape.obj");
    }


    public Node position_Object() throws IOException {
        return create_Object();
    }
}

class City extends Terrains{

    protected Node create_Object() throws IOException {

        BranchGroup scifiBG = Commons.f_load("Imports/Objects/scifi.obj");
        TransformGroup scifiTG = new TransformGroup();
        scifiTG.addChild(scifiBG);

        BranchGroup buildingBG = Commons.f_load("Imports/Objects/building.obj");
        Transform3D buildingT3D = new Transform3D();
        buildingT3D.setTranslation(new Vector3f(0.8f,0.4f,0.9f));
        buildingT3D.setScale(0.75);
        TransformGroup buildingTG = new TransformGroup(buildingT3D);
        buildingTG.addChild(buildingBG);

        BranchGroup nimbasaBG = Commons.f_load("Imports/Objects/Nimbasa.obj");
        Transform3D nimbasaT3D = new Transform3D();
        nimbasaT3D.setTranslation(new Vector3f(-0.25f,-0.25f,-1.75f));
        TransformGroup nimbasaTG = new TransformGroup(nimbasaT3D);
        nimbasaTG.addChild(nimbasaBG);

        BranchGroup casteliaBG = Commons.f_load("Imports/Objects/castelia.obj");
        Transform3D casteliaT3D = new Transform3D();
        casteliaT3D.setTranslation(new Vector3f(1.5f,-0.25f,0f));
        casteliaT3D.setScale(1.75);
        TransformGroup casteliaTG = new TransformGroup(casteliaT3D);
        casteliaTG.addChild(casteliaBG);

        Transform3D sceneT3D = new Transform3D();
        sceneT3D.setScale(10);
        TransformGroup sceneTG = new TransformGroup(sceneT3D);
        sceneTG.addChild(scifiTG);
        sceneTG.addChild(buildingTG);
        sceneTG.addChild(nimbasaTG);
        sceneTG.addChild(casteliaTG);

        return sceneTG;
    }

    public Node position_Object() throws IOException {
        return create_Object();
    }
}