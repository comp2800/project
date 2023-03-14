package codes;
// MultiFloor.java
// Andrew Davison, June 2006, ad@fivedots.coe.psu.ac.th

/* MultiFloor is a QuadArray mesh of sides FLOOR_LEN, centered
   at (0,0) on the XZ plane, with random generated vertex heights.
   The vertices are spaced out at 1 unit intervals along the x- and 
   z- axes.

   The mesh uses multitexturing to combine two ground detail textures 
   (e.g. grass and bits of stone) and a light map texture.

   The ground detail textures can be repeated at different frequencies 
   over the mesh depending on the freq values supplied in MultiFloor's 
   constructor.

   The light map can be a texture loaded from a file, or be generated 
   at runtime.

   The vertex heights can be accessed as a 2D array of floats by calling
   getHeights(). The heights are stored in a left-to-right, back-to-front 
   order starting from the left, back corner of the mesh at 
   (-FLOOR_LEN/2, -FLOOR_LEN/2).
*/

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.TexCoord2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class MultiFloor2 extends Shape3D
{
  private final static int FLOOR_LEN = 20;
    /* Should be even, since the code assumes that FLOOR_LEN/2 is
       a whole number. */

  private static final int LIGHT_MAP_SIZE = 128;
     // the light map is a texture, so it's size should be a power of 2

  // hill creation constants for the heights map
  private static final int NUM_HILL_INCRS = 2000;
  private static final float PEAK_INCR = 0.3f;    // height incrs for a hill
  private static final float SIDE_INCR = 0.25f;


  private float[][] heights;   // height map for the floor



  public MultiFloor2(String texFnm1, int freq1,
                     String texFnm2, int freq2)
  {
    System.out.println("floor len: " + FLOOR_LEN);
    // freq is the no. of texture repeats along the sides of the scene
    int texLen1 = calcTextureLength(texFnm1, freq1);
    int texLen2 = calcTextureLength(texFnm2, freq2);

    heights = makeHills();
    createGeometry(texLen1, texLen2);
    createAppearance(texFnm1, texFnm2);
  } // end of MultiFloor()


  private int calcTextureLength(String texFnm, int freq)
  /* Texture length is defined as:
        texture length * freq == FLOOR_LEN
     It is the length of the texture such that the texture will
     be repeated freq times along the floor side.

     Due to the use of integer division, freq should be chosen to
     be a factor of the floor length value. For example, if the floor
     length is 20, then freq should be 1, 2, 4, 5, 10, or 20. 
  */
  {
    if (freq < 1)   // if 0 or negative
      freq = 1;
    else if (freq > FLOOR_LEN)  // if bigger than the floor length
      freq = FLOOR_LEN;

    int texLen = FLOOR_LEN / freq;   // integer division
    System.out.println(texFnm + " with frequency " + freq + 
                       "; texture length will be " + texLen);
    return texLen;
  } // end of calcTextureLength()


  // ----------------- height map generation ---------------------

  private float[][] makeHills()
  /* Generate floor vertex heights by making hills.
     The heights are stored in a left-to-right, back-to-front 
     order starting from the left, back corner of the floor at 
     (-FLOOR_LEN/2, -FLOOR_LEN/2). Only the heights are stored, not the
     (x,z) coordinates.

     A hill increases the height of a (x,z) coordinate by PEAK_INCR, and
     the heights of its 4 neighbouring vertices by SIDE_INCR.
  */
  {
    float[][] heights = new float[FLOOR_LEN+1][FLOOR_LEN+1];  
                   // include front and right edges of floor

    Random rand = new Random();

    int x, z;   // index into array (x == column, z == row)
    for (int i=0; i < NUM_HILL_INCRS; i++) {
      x = (int)(rand.nextDouble()*FLOOR_LEN);
      z = (int)(rand.nextDouble()*FLOOR_LEN);
      if (addHill(x, z, rand)) {
        heights[z][x] += PEAK_INCR;
        if (x > 0)
          heights[z][x-1] += SIDE_INCR;   // left
        if (x < (FLOOR_LEN-1))
          heights[z][x+1] += SIDE_INCR;   // right
        if (z > 0)
          heights[z-1][x] += SIDE_INCR;   // back
        if (z < (FLOOR_LEN-1))
          heights[z+1][x] += SIDE_INCR;   // front
      }
    }
    return heights;
  }  // end of makeHills()


  private boolean addHill(int x, int z, Random rand)
  /* Test the (x,z) coord against hardwired probability ranges.
     The code here favours more hills at the back, left
     and right sides of the floor, with the middle and front areas
     left alone. */
  {
    if (z < FLOOR_LEN/5.0f)   // towards the back of the floor (away from the camera)
      return (rand.nextDouble() < 0.5);  // 50% chance of a hill

    if (x < FLOOR_LEN/10.0f)  // on the left side of the floor
      return (rand.nextDouble() < 0.5);   // 50% chance

    if ((FLOOR_LEN - x) < FLOOR_LEN/10.0f) // on the right side
      return (rand.nextDouble() < 0.5);   // 50% chance

    // other areas of the floor
    return (rand.nextDouble() < 0.05);  // 5% chance
  }  // end of addHill()


  public float[][] getHeightMap()
  { return heights; }

  
  // ------------------- floor's geometry ----------------------------


  private void createGeometry(int texLen1, int texLen2)
  /* Create a quad array using one set of (x,y,z) coordinates and
     three texture coords (i.e. the floor's appearnce uses multi-texturing. 
     The geometry does not have normals, so will not be affected by light.

     The first two textures are for floor details, while the third is a
     light map. The light map will cover the entire floor, so it's 
     texture length == FLOOR_LEN.
  */
  {
    // create (x,y,z) coordinates
    Point3f[] coords = createCoords();

    // create (s,t) coordinates for the three textures
    TexCoord2f[] tCoords1 = createTexCoords(coords, texLen1);
    TexCoord2f[] tCoords2 = createTexCoords(coords, texLen2);
    TexCoord2f[] tCoords3 = createTexCoords(coords, FLOOR_LEN);  // i.e. freq == 1

    QuadArray plane = new QuadArray(coords.length,
						GeometryArray.COORDINATES | 
						GeometryArray.TEXTURE_COORDINATE_2, 3, new int[]{0,1,2});

    plane.setCoordinates(0, coords);
    plane.setTextureCoordinates(0, 0, tCoords1);   // first texture
    plane.setTextureCoordinates(1, 0, tCoords2);   // second texture
    plane.setTextureCoordinates(2, 0, tCoords3);  // light map
    setGeometry(plane);

/*
    // create geometryInfo
    GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
    gi.setCoordinates(coords);
    gi.setTextureCoordinateParams(3, 2); // 3 sets of 2D texels
    gi.setTexCoordSetMap( new int[]{0, 1, 2} );
    gi.setTextureCoordinates(0, tCoords1);   // for first texture
    gi.setTextureCoordinates(1, tCoords2);   // second texture
    gi.setTextureCoordinates(2, tCoords3);   // light map

    // stripifier to use triangle strips
    Stripifier st = new Stripifier();
    st.stripify(gi);

    // extract and use GeometryArray
    setGeometry( gi.getGeometryArray() );
*/
  }  // end of createGeometry()


  // ------------------ geometry's vertices --------------------

  private Point3f[] createCoords()
  /* Create the (x,y,z) coordinates for the scene. The (x,z) 
     values run from -FLOOR_LEN/2 to FLOOR_LEN/2, centered on (0,0). Each
     vertex is 1 unit apart, so FLOOR_LEN should be even.
     For each (x,z) value, createTile() creates for 4 coordinates for 
     a quad (tile).
  */
  {
    Point3f[] coords = new Point3f[FLOOR_LEN*FLOOR_LEN*4];   // since each tile has 4 coords
    int i = 0;
    for(int z=0; z <= FLOOR_LEN-1; z++) {    // skip z's front row
      for(int x=0; x <= FLOOR_LEN-1; x++) {  // skip x's right column
        createTile(coords, i, x, z);
        i = i + 4;  // since 4 coords created for 1 tile
      }
    }
    return coords;
  }  // end of createCoords()



  private void createTile(Point3f[] coords, int i, int x, int z)
  // Coords for a single quad (tile), its top left hand corner is at (x,height,z)
  {
    // (xc, zc) is the (x,z) coordinate in the scene
    float xc = x - FLOOR_LEN/2;
    float zc = z - FLOOR_LEN/2;

    // points created in counter-clockwise order from bottom left
    coords[i] = new Point3f(xc, heights[z+1][x], zc+1.0f);
    coords[i+1] = new Point3f(xc+1.0f, heights[z+1][x+1], zc+1.0f);
    coords[i+2] = new Point3f(xc+1.0f, heights[z][x+1], zc);
    coords[i+3] = new Point3f(xc, heights[z][x], zc);   
  }  // end of createTile()


  // ------------------ geometry's texture coordinates --------------------

  private TexCoord2f[] createTexCoords(Point3f[] coords, int texLen)
  /* Create texture coordinates in tcoords[] for the (x,y,z) coords in
     the coords[] array. The (x,y,z) coords are grouped in 4's for
     each quad (tile), so the texture coordinates are similarly grouped.
  */
  {
    int numPoints = coords.length;
    TexCoord2f[] tcoords = new TexCoord2f[numPoints];

    TexCoord2f dummyTC = new TexCoord2f(-1,-1);   // dummy tex coord
    for(int i=0; i < numPoints; i=i+4)
      createTexTile(tcoords, i, texLen, coords, dummyTC); 
                         // 4 tex coords for 1 coordinates tile
    return tcoords;
  }  // end of createTexCoords()


  private void createTexTile(TexCoord2f[] tcoords, int i, int texLen, 
                                   Point3f[] coords, TexCoord2f dummyTC)
  /* Each group of 4 (x,y,z) coords in coords[] for a quad are
     stored in counter-clockwise order, starting from the 
     bottom left vertex. This same ordering is used for storing the
     texture coordinates in tcoords[].
  */
  { // make the bottom-left tex coord, i
    tcoords[i] = makeTexCoord(coords[i], texLen, dummyTC);

    for (int j = 1; j < 4; j++)   // add the other three coords
      tcoords[i+j] = makeTexCoord(coords[i+j], texLen, tcoords[i]);
  }  // end of createTexTile()


  private TexCoord2f makeTexCoord(Point3f coord, int texLen, TexCoord2f firstTC)
  /* The (s,t) texture coordinate for a given (x,y,z) coordinate
     depends on the (x,z) position modulo the texture length, and 
     divided by that length.
     The t value is adjusted so that it increases as z decreases.

     The use of modulo means that an s or t value of 1 is impossible,
     instead being rounded to 0. This is detected by comparing the
     calculated value with the bottom left tex coord (firstTC), 
     which should always be smaller.  
  */
  {
    float s, t;
    if (texLen > 1) {
      s = ((float)((coord.x + FLOOR_LEN/2) % texLen))/texLen;
      t = ((float)((FLOOR_LEN/2 - coord.z) % texLen))/texLen;
    }
    else {   // don't use modulo when texLen == 1
      s = ((float)(coord.x + FLOOR_LEN/2))/texLen;
      t = ((float)(FLOOR_LEN/2 - coord.z))/texLen;
    }
    if (s < firstTC.x)    // deal with right edge rounding
      s = 1.0f - s;
    if (t < firstTC.y)   // deal with top edge rounding
      t = 1.0f - t;

    // System.out.println("[x,y] [" + coord.x + "," + coord.z + 
    //                          "] = (s,t) (" + s + "," + t + ")");

    return new TexCoord2f(s, t);
  }  // end of makeTexCoord


  // ------------------- floor's appearance -----------------------

  private void createAppearance(String texFnm1, String texFnm2)
  /* Load two textures and combine them into a multi-texture. 
     The second texture has transparent parts so both textures
     can be seen at runtime.

     The third texture is a light map which is modulated with the
     others so they're all visible.

    The light map can either be loaded from a file, or be generated at
    runtime.
  */
  {
    Appearance app = new Appearance();

    // switch off face culling
    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_NONE);
    app.setPolygonAttributes(pa);

    // texture units 
    TextureUnitState tus[] = new TextureUnitState[3];

    // cover the floor with the first texture
    tus[0] = loadTextureUnit(texFnm1, TextureAttributes.DECAL);  

    // add second texture (it has transparent parts)
    tus[1] = loadTextureUnit(texFnm2, TextureAttributes.DECAL); 

    // modulated light map
    tus[2] = loadTextureUnit("Imports/Textures/sand.jpg", TextureAttributes.MODULATE);
    // tus[2] = lightMapTUS();

    app.setTextureUnitState(tus);

    setAppearance(app);
  }  // end of createAppearance()


  private TextureUnitState loadTextureUnit(String fnm, int texAttr)
  /* Create a texture unit state by combining a loaded texture
     and texture attributes. Mipmaps are generated for the texture.
  */
  {
    TextureLoader loader =
        new TextureLoader(fnm, TextureLoader.GENERATE_MIPMAP, null);
    System.out.println("Loaded floor texture: " + fnm);

    Texture2D tex = (Texture2D) loader.getTexture();
    tex.setMinFilter(Texture2D.MULTI_LEVEL_LINEAR);

    TextureAttributes ta = new TextureAttributes();
    ta.setTextureMode(texAttr);  

    TextureUnitState tus =  new TextureUnitState(tex, ta, null);
    return tus;
  }  // end of loadTextureUnit()



  // ---------- runtime light map ------------------------------
  
  private TextureUnitState lightMapTUS()
  // make a texture unit out of a light map created at runtime
  {
    Texture2D tex = createLightMap(); // runtime light map

    TextureAttributes ta = new TextureAttributes();
    ta.setTextureMode(TextureAttributes.MODULATE); // for light

    TextureUnitState tus =  new TextureUnitState(tex, ta, null);
    return tus;
  }  // end of lightMapTUS()


  private Texture2D createLightMap()
  /* The light map texture is made from a RGB BufferedImage, which can be 
     filled with colours or greyscales (as here). The standard Java 2D ops 
     can be used to draw the light map by manipulating it via a Graphics2D
     object.

     The code creates a series of concentric circles, filled with various
     shades of grey.
  */
  { // draw the buffered image
    BufferedImage img = new BufferedImage(LIGHT_MAP_SIZE, LIGHT_MAP_SIZE,
                                            BufferedImage.TYPE_INT_RGB);
    Graphics2D g = img.createGraphics();

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(Color.gray);
    g.fillRect(0, 0, LIGHT_MAP_SIZE, LIGHT_MAP_SIZE);

    int xCenter = LIGHT_MAP_SIZE/2; 
    int yCenter = LIGHT_MAP_SIZE/2;

    int diameter = LIGHT_MAP_SIZE*7/10;   // 70% of light map size
    g.setColor(Color.lightGray);
    g.fillOval(xCenter-diameter/2, yCenter-diameter/2, diameter, diameter);

    diameter = LIGHT_MAP_SIZE*4/10;   // 40% of light map size
    g.setColor(Color.white);
    g.fillOval(xCenter-diameter/2, yCenter-diameter/2, diameter, diameter);

    diameter = LIGHT_MAP_SIZE*2/10;   // 20% of light map size
    g.setColor(Color.lightGray);
    g.fillOval(xCenter-diameter/2, yCenter-diameter/2, diameter, diameter);

    diameter = LIGHT_MAP_SIZE*15/100;   // 15% of light map size
    g.setColor(Color.gray);
    g.fillOval(xCenter-diameter/2, yCenter-diameter/2, diameter, diameter);

    g.dispose();

    // convert the buffered image into a texture
    ImageComponent2D grayImage = new ImageComponent2D(ImageComponent.FORMAT_RGB, img);
    Texture2D lightTex = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, 
                                                      LIGHT_MAP_SIZE, LIGHT_MAP_SIZE);
    lightTex.setImage(0, grayImage);

    return lightTex;
  } // end of createLightMap()


} // end of MultiFloor class
