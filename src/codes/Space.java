package codes;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;

public class Space {
    private Sphere bgSphere;
    private Appearance bgAppearance;
    private Texture bgTexture;

    public Space(String texPath) {
        bgAppearance = new Appearance();
        bgTexture = new TextureLoader(texPath, null).getTexture();
        bgTexture.setBoundaryModeS(Texture.WRAP);
        bgTexture.setBoundaryModeT(Texture.WRAP);
        bgAppearance.setTexture(bgTexture);

        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        bgAppearance.setPolygonAttributes(pa);

        bgSphere = new Sphere(10000f, Sphere.GENERATE_TEXTURE_COORDS, bgAppearance);
        bgSphere.setCapability(Sphere.ALLOW_BOUNDS_WRITE);
        bgSphere.setCapability(Sphere.ALLOW_LOCAL_TO_VWORLD_READ);
    }

    public Sphere setApp() {
        return bgSphere;
    }
}
