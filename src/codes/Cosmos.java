package codes;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Vector3f;

import java.io.IOException;

import static codes.Commons.addRotation;
import static codes.Commons.setApp;

public abstract class Cosmos {
    protected abstract Node create_Object() throws IOException;

}

class Planet extends Cosmos {
    private String texture;
    private float scale;

    public Planet(String texture, float scale) {
        this.texture = texture;
        this.scale = scale;
    }

    protected Node create_Object() throws IOException {
        Appearance app = setApp(texture);
        Transform3D T3D = new Transform3D();
        T3D.setScale(scale);
        TransformGroup planetTG = new TransformGroup(T3D);
        planetTG.addChild(new Sphere(1, Primitive.GENERATE_TEXTURE_COORDS, 50, app));
        return planetTG;
    }

}

class SysPlanet extends Cosmos {
    private String texture;
    private int speed;
    private float scale;

    private float distance;

    public SysPlanet(String texture, int speed, float scale, float distance) {
        this.texture = texture;
        this.speed = speed;
        this.scale = scale;
        this.distance = distance;
    }

    protected Node create_Object() throws IOException {
        Transform3D planetT3D = new Transform3D();
        planetT3D.setTranslation(new Vector3f(distance, 0, 0));
        TransformGroup earthTG = new TransformGroup(planetT3D);
        earthTG.addChild(new Planet(texture, scale).create_Object());
        TransformGroup TG = new TransformGroup();
        TG.addChild(earthTG);
        TG = addRotation(TG, speed, new AxisAngle4f(0, 0, 0, (float) Math.PI / 2));
        return TG;
    }

}

class Earth extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Earth.jpg", 24 * 10000, 1f, 1.50f * 15).create_Object();
    }

}

class Mercury extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Mercury.jpg", 1392 * 10000, 1 / 3f, .58f * 10).create_Object();
    }

}

class Venus extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Venus.jpg", 5832 * 10000, 0.9f, 1.08f * 10).create_Object();
    }

}

class Mars extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Mars.jpg", 24 * 10000, 1f, 2.28f * 20).create_Object();
    }

}

class Jupiter extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Jupiter.jpg", 9 * 10000, 11f, 7.78f * 10).create_Object();
    }

}

class Saturn extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Saturn.jpg", 10 * 10000, 9f, 14.3f * 10).create_Object();
    }

}

class Uranus extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Uranus.jpg", 17 * 10000, 4f, 28f * 10).create_Object();
    }

}

class Neptune extends Cosmos {

    protected Node create_Object() throws IOException {
        return new SysPlanet("Imports/Textures/SolarSystem/Neptune.jpg", 16 * 10000, 3.75f, 45f * 10).create_Object();
    }

}

class Sun extends Cosmos {

    protected Node create_Object() throws IOException {
        return new Planet("Imports/Textures/SolarSystem/Sun.jpg", 15f).create_Object();
    }
}

class SolarSystem extends Cosmos {

    protected Node create_Object() throws IOException {

        TransformGroup sunTG = new TransformGroup();
        sunTG.addChild(new Sun().create_Object());

        TransformGroup mercuryTG = new TransformGroup();
        mercuryTG.addChild(new Mercury().create_Object());

        TransformGroup venusTG = new TransformGroup();
        venusTG.addChild(new Venus().create_Object());

        TransformGroup earthTG = new TransformGroup();
        earthTG.addChild(new Earth().create_Object());

        TransformGroup marsTG = new TransformGroup();
        marsTG.addChild(new Mars().create_Object());

        TransformGroup jupiterTG = new TransformGroup();
        jupiterTG.addChild(new Jupiter().create_Object());

        TransformGroup saturnTG = new TransformGroup();
        saturnTG.addChild(new Saturn().create_Object());

        TransformGroup uranusTG = new TransformGroup();
        uranusTG.addChild(new Uranus().create_Object());

        TransformGroup neptuneTG = new TransformGroup();
        neptuneTG.addChild(new Neptune().create_Object());

        sunTG.addChild(mercuryTG);
        sunTG.addChild(venusTG);
        sunTG.addChild(earthTG);
        sunTG.addChild(marsTG);
        sunTG.addChild(jupiterTG);
        sunTG.addChild(saturnTG);
        sunTG.addChild(uranusTG);
        sunTG.addChild(neptuneTG);

        return sunTG;
    }

}