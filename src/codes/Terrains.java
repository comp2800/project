package codes;

import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;

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
