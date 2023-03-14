package codes;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class Terrains {
    protected abstract Node create_Object() throws IOException;

    public abstract Node position_Object() throws IOException;

}

class Mountains extends Terrains {

    protected Node create_Object() throws IOException {

        BranchGroup objBG = Commons.f_load("Imports/Objects/Mountains.obj");
        Shape3D mountains = (Shape3D) objBG.getChild(0);

        TextureLoader loader = new TextureLoader("Imports/TIFs/Mountains.tif", null);
        Texture2D texture = (Texture2D) loader.getTexture();

        TextureAttributes texAttribs = new TextureAttributes();
        texAttribs.setTextureMode(TextureAttributes.REPLACE);

        Appearance app = new Appearance();
        app.setTexture(texture);
        app.setTextureAttributes(texAttribs);

        mountains.setAppearance(app);
        return objBG;
    }


    public Node position_Object() throws IOException {
        return create_Object();
    }
}

class Map extends Terrains {

    protected Node create_Object() throws IOException {

        BranchGroup objBG = Commons.f_load("Imports/Objects/Map.obj");
        return objBG;
    }


    public Node position_Object() throws IOException {
        return create_Object();
    }
}
