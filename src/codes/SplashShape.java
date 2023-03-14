package codes;
// SplashShape.java
// Andrew Davison, June 2006, ad@fivedots.coe.psu.ac.th

/* A SplashShape combines an alpha mask with a texture to break
   up the texture's boundary, making it irregular in a random way. 
   Multi-texturing is used to combine the two textures.
 
   The alpha mask is a texture that only contains an alpha channel (i.e.
   transparency information). 

   An alpha mask can be implemented as a full RGBA image with its colour 
   channels ignored at runtime, or as a greyscale image with a single 
   channel (as here), which is interpreted as the alpha channel at run time.

   The shape's geometry comes from the scene's floor. A fragment of the scene's
   height map is copied, to make a mesh that matches a portion of the floor's
   mesh. 

   The floor mesh is made from quads. SplashShape copies SPLASH_SIZE*SPLASH_SIZE 
   quads from the floor mesh.
*/

/* The texture manipulation code here owes a lot to the SplatShape class
   developed by David Yazel (david@yazel.net) in May 2003,
   and the alpha textures AlphaDemo class by Justin Couch (justin@vlc.com.au) 
   from July 2002. They posted their work to the JAVA3D-INTEREST list 
   mailing list.
*/


import org.jogamp.java3d.*;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.TexCoord2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class SplashShape extends Shape3D
{
  private static final int ALPHA_SIZE = 64;
    // the size of the alpha texture

  private static final int SPLASH_SIZE = 3;   
     /* SplashShape copies SPLASH_SIZE*SPLASH_SIZE quads from 
        the floor mesh. SPLASH_SIZE should be less than the 
        floor length (which is stored in floorLen). */
    

  private int floorLen;  // length of floor's side
  private int xH, zH;  
     /* indicies of the top-left starting point of the splash in the 
        height map (whose indicies go from 0 -- FLOOR_LEN) */


  public SplashShape(Texture2D tex, float[][] heights)
  { 
    floorLen = heights.length-1; 

    // make the ground TUS
    TextureUnitState groundTUS = makeGround(tex);

    // make the alpha TUS
    TextureUnitState alphaTUS = new TextureUnitState();
    alphaTUS.setTextureAttributes( getAlphaTA() );
    alphaTUS.setTexture( getAlphaTexture() );

    // 2 texture units, one for the main texture, one for the alpha
    TextureUnitState[] tus = new TextureUnitState[2];
    tus[0] = groundTUS;
    tus[1] = alphaTUS;

    createGeometry(heights);
    makeAppearance(tus);
  } // end of SplashShape()



  // ---------------------- create the texture units --------------------------

  private TextureUnitState makeGround(Texture2D tex)
  // The texture for the ground detail covers the splat shape.
  {
    TextureAttributes groundTAs = new TextureAttributes();
    groundTAs.setTextureMode(TextureAttributes.REPLACE);
    groundTAs.setPerspectiveCorrectionMode(TextureAttributes.NICEST);

    TextureUnitState groundTUS = new TextureUnitState();
    groundTUS.setTextureAttributes(groundTAs);
    groundTUS.setTexture(tex);

    return groundTUS;
  } // end of makeGround()


  private TextureAttributes getAlphaTA()
  /* The alpha texture attributes defined here combine RGB colour channels 
     from the first texture unit with the alpha channel from the second 
     texture unit. 
  */
  {
    TextureAttributes alphaTA = new TextureAttributes();
            
    alphaTA.setPerspectiveCorrectionMode(alphaTA.NICEST);
    alphaTA.setTextureMode(TextureAttributes.COMBINE);
          // COMBINE gives us individual control over the RGBA channels

    /* use COMBINE_REPLACE to replace the colour and alpha of the geometry */
    alphaTA.setCombineRgbMode(TextureAttributes.COMBINE_REPLACE);   
    alphaTA.setCombineAlphaMode(TextureAttributes.COMBINE_REPLACE);

    /* the source RGB == previous texture unit (i.e. the first unit), 
       and the source alpha == the second texture unit. */
    alphaTA.setCombineRgbSource(0, TextureAttributes.COMBINE_PREVIOUS_TEXTURE_UNIT_STATE);
    alphaTA.setCombineAlphaSource(0, TextureAttributes.COMBINE_TEXTURE_COLOR);

    /* The combined texture gets its colour from the source RGB and 
       its alpha from the source alpha. */
    alphaTA.setCombineRgbFunction(0, TextureAttributes.COMBINE_SRC_COLOR);
    alphaTA.setCombineAlphaFunction(0, TextureAttributes.COMBINE_SRC_ALPHA);

    return alphaTA;
  }  // end of getAlphaTA()


  private Texture2D getAlphaTexture()
  /* Create an alpha texture by generating a buffered image at runtime
     using alphaSplash(). This mask is mostly opaque in the center, and
     more transparent at the edges.

     An alternative is to load a buffered image from a file, which can 
     done by calling loadAlpha(). 
  */
  {
    /* the image is defined to have a single 8-bit channel,
       which will hold greyscale information created by alphaSplash() */
    ImageComponent2D alphaIC =
             new ImageComponent2D(ImageComponent2D.FORMAT_CHANNEL8, 
                                  ALPHA_SIZE, ALPHA_SIZE, true, false);
    alphaIC.set( alphaSplash() );  // generate a buffered image
      /* instead of generating a buffered image at run time, it's also
         possible to load one, using loadAlpha():
            alphaIC.set( loadAlpha(<filename>) ); 
      */

    // convert the buffered image into an alpha texture
    Texture2D tex = new Texture2D(Texture2D.BASE_LEVEL, Texture.ALPHA,
                                                 ALPHA_SIZE, ALPHA_SIZE);
    tex.setMagFilter(Texture.BASE_LEVEL_LINEAR);
    tex.setMinFilter(Texture.BASE_LEVEL_LINEAR);
    tex.setImage(0, alphaIC);
    return tex;
  } // end of getAlphaTexture()


  // ----------------- the splash shape's geometry -----------------------

  private void createGeometry(float[][] heights)
  /* The splash shape's geometry is a subsection of the floor's geometry,
     starting at a random vertex, and extending by SPLASH_SIZE vertices along
     the x- and z- axes. 
     The geometry uses a QuadArray like the floor, and two texture units, one
     for the texture detail, the other for the alpha mask.
  */
  {
    // get coordinates for the splash shape
    getHeightIndicies(heights);
    float[][] splashHeights = getSplashHeights(heights);  
              // copy heights from the floor's heights map
    Point3f[] coords = createCoords(splashHeights);
              // generate (x,y,z) coords using the copied heights

    // create texture coordinates for the splash shape
    TexCoord2f[] tCoords = createTexCoords(coords);

    // make a mesh with two texture units
    QuadArray plane = new QuadArray(coords.length, 
						GeometryArray.COORDINATES | 
						GeometryArray.TEXTURE_COORDINATE_2,
                        2, new int[]{0,1}); 

    plane.setCoordinates(0, coords);
    plane.setTextureCoordinates(0, 0, tCoords);    // for the detail texture
    plane.setTextureCoordinates(1, 0, tCoords);    // for the alpha mask texture 
    setGeometry(plane);
  }  // end of createGeometry()


  // -------------- creating the splash shape's height map ---------------

  private void getHeightIndicies(float[][] heights)
  /* Generate starting indicies [xH,zH] for the splash in the
     height map (the indicies will be somewhere between 0 and FLOOR_LEN).
  */
  {
    Random rand = new Random();
    zH = (int)(rand.nextDouble()*floorLen);   // zH is the row index
    xH = (int)(rand.nextDouble()*floorLen);   // xH is the column index

    if (zH+SPLASH_SIZE > floorLen)  // if splash extends off front edge
      zH = floorLen - SPLASH_SIZE;  // move back

    if (xH+SPLASH_SIZE > floorLen)  // if splash extends off right edge
      xH = floorLen - SPLASH_SIZE;  // move left

    // System.out.println("Start Point: (" + xH + "," + zH + ")");
  }  // end of getHeightIndicies()



  private float[][] getSplashHeights(float[][] heights)
  /* Copy vertex heights from the floor's height map (heights[][]) into
     a splashHeights[][] array. Start copying at height map index
     position [xH, zH]. Copy ehough vertices to make a mesh of
     SPLASH_SIZE*SPLASH_SIZE.
  */
  {
    float[][] splashHeights = new float[SPLASH_SIZE+1][SPLASH_SIZE+1];
                 // include front and right edges: (xH,zH) to SPLASH_SIZE

    int xch, zch;
    for(int z=0; z <= SPLASH_SIZE; z++) {
      for(int x=0; x <= SPLASH_SIZE; x++) {
        xch = xH + x;    // use indicies in the height map
        zch = zH + z;
        splashHeights[z][x] = heights[zch][xch];
        // System.out.println("[" + x + "," + z + "] Splash Height at (" + 
        //                         xch + "," + zch + "): " + heights[zch][xch]);
      }
    }
    return splashHeights;
  }  // end of getSplashHeights()


  // ------------ the shape's vertices -------------------------

  private Point3f[] createCoords(float[][] splashHeights)
  /* Use the splash height map to build coordinates for the
     splash's mesh. The mesh is a quad array with the quads 
     ordered in the same way as those in the floor mesh.
  */
  {
    Point3f[] coords = new Point3f[SPLASH_SIZE*SPLASH_SIZE*4];   
                   // since each quad in the mesh has 4 coords
    int i = 0;
    for(int z=0; z <= SPLASH_SIZE-1; z++) {    // skip z's front row
      for(int x=0; x <= SPLASH_SIZE-1; x++) {  // skip x's right column
        createTile(coords, i, x, z, splashHeights);
        i = i + 4;  // since 4 coords are needed for 1 quad
      }
    }
    return coords;
  }  // end of createCoords()


  private void createTile(Point3f[] coords, int i, int x, int z,
                                         float[][] splashHeights)
  /* Create coords for a single quad, with its top left hand corner 
     at (xc,zc). */
  {
    // (xc,zc) is the (x,z) coordinate in the floor mesh
    float xc = xH + x - floorLen/2;
    float zc = zH + z - floorLen/2;

    // points created in counter-clockwise order from bottom left
    coords[i] = new Point3f(xc, splashHeights[z+1][x], zc+1.0f);
    coords[i+1] = new Point3f(xc+1.0f, splashHeights[z+1][x+1], zc+1.0f);
    coords[i+2] = new Point3f(xc+1.0f, splashHeights[z][x+1], zc);
    coords[i+3] = new Point3f(xc, splashHeights[z][x], zc);   
  }  // end of createTile()


  // ------------ the shape's texture coordinates -------------------------


  private TexCoord2f[] createTexCoords(Point3f[] coords)
  /* Create texture coordinates in tcoords[] for the (x,y,z) coords in
     the coords[] array. The (x,y,z) coords are grouped in 4's for
     each quad (tile), and so the texture coordinates are similarly
     grouped.

     Each group of 4 (x,y,z) coords in coords[] for a quad stored
     in counter-clockwise order, starting from the bottom left vertex. 
     This same ordering is used for storing the texture coordinates 
     in tcoords[].
  */
  { int numPoints = coords.length;
    TexCoord2f[] tcoords = new TexCoord2f[numPoints];

    for(int i=0; i < numPoints; i=i+4)  // 4 tex coords for 1 quad
      for (int j = 0; j < 4; j++) 
        tcoords[i+j] = makeTexCoord(coords[i+j]);
                         
    return tcoords;
  }  // end of createTexCoords()


  private TexCoord2f makeTexCoord(Point3f coord)
  /* The (s,t) texture coordinate for a given (x,y,z) coordinate
     is obtained by converting the (x,z) parts back to values between
     0 and SPLASH_SIZE, and then dividing by SPLASH_SIZE

     The t value is adjusted so that it increases as z decreases.
  */
  { float s = ((float)(coord.x - xH + floorLen/2))/SPLASH_SIZE;
    float t =  1.0f - ((float)(coord.z - zH + floorLen/2))/SPLASH_SIZE;

    // System.out.println("[x,y] [" + coord.x + "," + coord.z + 
    //                          "] = (s,t) (" + s + "," + t + ")");

    return new TexCoord2f(s, t);
  }  // end of makeTexCoord


  private void makeAppearance(TextureUnitState[] tus)
  // blend the semi-transparent texture units
  {
    Appearance app = new Appearance();
    app.setTextureUnitState(tus);

    TransparencyAttributes ta = new TransparencyAttributes();
    ta.setTransparencyMode(TransparencyAttributes.BLENDED);
    app.setTransparencyAttributes(ta);

    setAppearance(app);
  }  // end of makeAppearance()



  // ----------------- alpha mask methods ---------------


  private BufferedImage loadAlpha(String fnm)
  /* Load a file containing a buffered image for an alpha mask.
     The file should use 8-bit greyscales to match the buffered
     image format used in getAlphaTexture(). */
  {
    try {
      return ImageIO.read( new File("images/" + fnm));
    } 
    catch (IOException e) {
      System.out.println("Could not load alpha mask: images/" + fnm);
      return null;
    }
  }  // end of loadAlpha()


  private BufferedImage alphaSplash()
  /* The alpha mask texture is made from a greyscale BufferedImage.
     The standard Java 2D ops can be used to draw the mask by accessing 
     it via a Graphics2D object.

     The code creates a series of randomly positioned circles with
     various transparency levels. The more opaque circles tend to be
     in the center of the alpha mask.

     Transparancy levels increase as the greyscale gets darker, with 
     black being fully transparent.

     Many numbers are hardwired in this code :)
  */
  {
    Random rand = new Random();

    // create a greyscale buffered image
    BufferedImage img = new BufferedImage(ALPHA_SIZE, ALPHA_SIZE,
                                         BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D g = img.createGraphics();

    // draw into it
    g.setColor(Color.black);                   // fully transparent
    g.fillRect(0, 0, ALPHA_SIZE, ALPHA_SIZE);

    int radius = 3;  // circle radius
    int offset = 8;  // offset of boxed circle from top-left of graphic
    g.setColor(new Color(0.3f, 0.3f, 0.3f));   // almost transparent circles
    boxedCircles(offset, offset, ALPHA_SIZE-(offset*2), radius, 100, g, rand);

    offset = 12;
    g.setColor(new Color(0.6f, 0.6f, 0.6f));   // mid-level transparent circles
    boxedCircles(offset, offset, ALPHA_SIZE-(offset*2), radius, 80, g, rand);

    offset = 16;
    g.setColor(Color.white);                   // fully opaque circles
    boxedCircles(offset, offset, ALPHA_SIZE-(offset*2), radius, 50, g, rand); 

    g.dispose();
    return img;
  }  // end of alphaSplash()


  private void boxedCircles(int x, int y, int len, int radius,
                              int numCircles, Graphics2D g, Random rand)
  /* Generate numCircles circles whose centers are within the square
     whose top-left is (x,y), with sides of len. A circle has a radius 
     equal to the radius value.
  */
  {
    int xc, yc;
    for (int i=0; i < numCircles; i++) {
      xc = x + (int)(rand.nextDouble()*len) - radius;
      yc = y + (int)(rand.nextDouble()*len) - radius;
      // System.out.println("(" + xc + "," + yc + ")");
      g.fillOval(xc, yc, radius*2, radius*2);
    }
  }  // end of boxedCircles()

} // end of SplashShape class
 