package Classes.JOGL;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javax.swing.JFrame;

/**
 * Project #2, by Jesse Young.  Creates a 3D scene using JOGL.
 */
public class Primitives extends GLJPanel implements GLEventListener, KeyListener {
  public static void main(String[] args) {
    JFrame window = new JFrame("Test Draw Primitives");
    Primitives panel = new Primitives();
    window.setContentPane(panel);
    window.pack();
    window.setLocation(50,50);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setVisible(true);
    panel.requestFocusInWindow();

  }
  
  // variables to translate and rotate the scene
  private double rotateX = 0;
  private double rotateY = 0;
  private double rotateZ = 0;
  private double transX = 0;
  private double transY = 0;
  private double transZ = -5;

  private final GLJPanel display;

  /*
   * Main constructor, sets dimensions and adds a key listener.
  */
  @SuppressWarnings("LeakingThisInConstructor")
  public Primitives() {
    super( new GLCapabilities(null) ); // Makes a panel with default OpenGL "capabilities".
    GLCapabilities caps = new GLCapabilities(null);
    display = new GLJPanel(caps);
    display.setPreferredSize( new Dimension(600,480) );  // TODO: set display size here
    display.addGLEventListener(this);
    setLayout(new BorderLayout());
    add(display,BorderLayout.CENTER);
    requestFocusInWindow();
    addKeyListener(this);
  }

  private Camera camera; // used for projection
  
  /**
   * This method is called when the OpenGL display needs to be redrawn.
   * @param drawable
   */
  @Override
  public void display(GLAutoDrawable drawable) { // called when the panel needs to be drawn
    GL2 gl = drawable.getGL().getGL2();
    gl.glClearColor(0, 0.4f, 0.8f, 0);
    gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT ); // TODO? Omit depth buffer for 2D.
    gl.glLoadIdentity();
    camera.apply(gl);
    gl.glPushMatrix();
    
    //move the world to respond to user input
    gl.glTranslated(transX, transY, transZ);
    gl.glRotated(rotateZ,0,0,1);
    gl.glRotated(rotateY,0,1,0);
    gl.glRotated(rotateX,1,0,0);
    
    // draw seven objects
    drawSun(gl, 30, 15, -30); // draw objects at given location
    drawFin(gl, -15, 8, -40);
    drawSand(gl); // sand is centered, no need to move
    drawPuddle(gl, 20, 0.01, -10);
    drawBeachBall(gl, -10, 2, 10);
    drawBucket(gl, 16, 1.5, 20);
    drawUmbrella(gl, -25, 5, 0);
    
    gl.glPopMatrix();
  }

  /**
   * This is called when the GLJPanel is first created.  It can be used to initialize
   * the OpenGL drawing context.
   * @param drawable
   */
  @Override
  public void init(GLAutoDrawable drawable) { // called when the panel is created
    GL2 gl = drawable.getGL().getGL2();
    gl.glEnable(GL2.GL_DEPTH_TEST);
    camera = new Camera();
    camera.lookAt( 0,25,25, 0,0,0, 0,1,0 );
    camera.setLimits( -30,30, -30,30, -30,30 );
    camera.setOrthographic( false );    // ortho is of type boolean
    camera.setPreserveAspect( true ); // preserve is of type boolean
    camera.installTrackball(this);
    
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glClearColor( 0, 0, 0, 1 );
    
    initSun(gl); // adds calls to GPU to draw a sun, called later
    initUmbrella(gl); // adds calls to GPU to draw top of umbrella, called later
    initBeachBall(gl); // adds calls to GPU to draw a beach ball, called later
  }
  
  /** 
   * Draws a rectangle with the current color based on the given scales, centered
   * at (0,0,0) and facing in the +z direction.
   * @param gl
   * @param width
   * @param height
  */
  public static void drawRectangle(GL2 gl, double width, double height) {
    gl.glPushMatrix();
    gl.glScaled(width, height, 1);
    gl.glBegin(GL.GL_TRIANGLE_FAN);
    gl.glVertex3d(-0.5, -0.5, 0);
    gl.glVertex3d(0.5, -0.5, 0);
    gl.glVertex3d(0.5, 0.5, 0);
    gl.glVertex3d(-0.5, 0.5, 0);
    gl.glEnd();
    gl.glPopMatrix();
  }
  
  /** 
   * Draws a square with dimensions size x size, using the current color, centered at 0,0,0.
   * @param gl
   * @param size
  */
  public static void drawSquare(GL2 gl, double size) {
    drawRectangle(gl, size, size);
  }

  /**
   * Draws a 3D box at the origin, scaled to size specifications.
   * This method draws two sides at a time, then rotates about the axis between the origin and a
   * corner of box, before repeating two more times to complete the box.
   * @param gl
   * @param width
   * @param height
   * @param length
  */
  public static void draw3DRect(GL2 gl, double width, double height, double length) {
    gl.glPushMatrix();
    
    gl.glScaled(width, height, length); //scale whole object to size
    for(int i = 0; i < 3; i++) {
      gl.glRotated(120, 0.5, 0.5, 0.5);
      for(int j = 0; j < 2; j++) {
        gl.glPushMatrix();
        gl.glRotated(j*90, 1, 0, 0);
        gl.glTranslated(0, 0, 0.5);
        drawSquare(gl, 1); // draw a unit rectangle
        gl.glPopMatrix();
      }
    }
    
    gl.glPopMatrix();
  }
 
  /**
   * Draws an oval at the origin, using scale to alter a unit circle.
   * @param gl
   * @param r1
   * @param r2
   * @param numberOfLines
  */
  public static void drawOval(GL2 gl, double r1, double r2, int numberOfLines) { // with center (x,y), horizontal radius r1, and vertical radius r2:
    double x1 = 0.5*cos(0); // would be r1, but scale took care of it
    double y1 = 0.5*sin(0); // so instead I create a unit circle
    
    gl.glPushMatrix();
    
    gl.glScaled(r1, r2, 1);
    gl.glBegin(GL.GL_TRIANGLE_FAN);
    gl.glVertex3d(0, 0, 0);
    gl.glVertex3d(x1, y1, 0);
    for (int i = 0; i < numberOfLines; i++) {
      double angle2 = (i+1) * (2*PI/numberOfLines);
      double x2 = 0.5*cos(angle2); // continue a unit circle
      double y2 = 0.5*sin(angle2); // and let scale change dimensions
      gl.glVertex3d(x2, y2, 0);
    }
    gl.glEnd();
    
    gl.glPopMatrix();
  }
  
  /**
   * Draws an open cylinder at the origin.
   * @param gl
   * @param r1
   * @param r2
   * @param height
   * @param numberOfLines
  */
  public static void drawStraw(GL2 gl, double r1, double r2, double height, int numberOfLines) { // with center (x,y), horizontal radius r1, and vertical radius r2:
    double x1 = 0.5*cos(0); // would be r1, but scale takes care of it
    double y1 = 0.5*sin(0); // so instead I create a unit circle
    
    gl.glPushMatrix();
    
    gl.glScaled(r1, r2, 1);
    gl.glBegin(GL.GL_TRIANGLE_STRIP);
    gl.glVertex3d(x1, y1, -height/2); // keep straw centered
    gl.glVertex3d(x1, y1, height/2);
    for(int i = 0; i < numberOfLines; i++) {
      double angle2 = (i+1) * (2*PI/numberOfLines);
      double x2 = 0.5*cos(angle2); // continue a unit circle
      double y2 = 0.5*sin(angle2); // and let scale change dimensions
      gl.glVertex3d(x2, y2, -height/2);
      gl.glVertex3d(x2, y2, height/2);
    }
    gl.glVertex3d(x1, y1, -height/2); // close the cylinder
    gl.glEnd();
    
    gl.glPopMatrix();
  }
  
  /**
   * Called when the size of the GLJPanel changes.  Note:  glViewport(x,y,width,height)
   * has already been called before this method is called!
   * @param drawable
   * @param x
   * @param y
   * @param width
   * @param height
   */
  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    // TODO: Add any code required to respond to the size of the display area.
    //             (Not usually needed.)
  }

  /**
   * This is called before the GLJPanel is destroyed.  It can be used to release OpenGL resources.
   * @param drawable
   */
  @Override
  public void dispose(GLAutoDrawable drawable) {
  }
  
  /*
   * Create and fin into the ocean to resemble a shark in the water.
  */
  private void drawFin(GL2 gl, double transX, double transY, double transZ) {
    gl.glPushMatrix();
    gl.glColor3ub((byte)176, (byte)176, (byte)176);
    gl.glTranslated(transX, transY, transZ);
    gl.glPushMatrix();
    gl.glBegin(GL.GL_TRIANGLES);
    gl.glVertex3d(0, 0, 0);
    gl.glVertex3d(8, 0, 0);
    gl.glVertex3d(0, 10, 0);
    gl.glEnd();
    gl.glPopMatrix();
    gl.glPopMatrix();
  }

  /*
   * Create and draw the sand into the scene.  The sand is simply a 2D triangle fan.
  */
  private void drawSand(GL2 gl) {
    gl.glPushMatrix();
    gl.glColor3d(1.0, 1.0, 0.0);
    gl.glRotated(90, 1,0,0);
    drawRectangle(gl, 200, 75);
    gl.glPopMatrix();
  }

  /*
   * Draw the pre-defined sun by calling its list on the GPU.
  */
  private void drawSun(GL2 gl, double transX, double transY, double transZ) {
    gl.glPushMatrix();
    gl.glColor3d(1.0, 0.6, 0.0);
    gl.glTranslated(transX, transY, transZ);
    gl.glCallList(sunId);  // Draw the sun by calling its display list.
    gl.glPopMatrix();
  }
  
  private int sunId;  // A display list ID for drawing the sun
  
  /*
   * Initialize a special sphere on the GPU, used to draw a sun in the distance.
  */
  private void initSun(GL2 gl) {
    sunId = gl.glGenLists(1);
    gl.glNewList(sunId, GL2.GL_COMPILE);
    uvCustomSphere(gl, 2, 32, 16, false);
    gl.glEndList();
  }
  
  private int umbrellaId;  // A display list ID for drawing the top of umbrella
  
  /*
   * Initialize a special 'half' sphere on the GPU, used to draw the top of the umbrella.
  */
  private void initUmbrella(GL2 gl) {
    umbrellaId = gl.glGenLists(1);
    gl.glNewList(umbrellaId, GL2.GL_COMPILE);
    uvCustomHalfSphere(gl, 4, 32, 16, true);
    gl.glEndList();
  }

  
  private int beachBallId;  // A display list ID for drawing the sun
  
  /*
   * Initialize a special sphere on the GPU, used to draw a beach ball.
  */
  private void initBeachBall(GL2 gl) {
    beachBallId = gl.glGenLists(1);
    gl.glNewList(beachBallId, GL2.GL_COMPILE);
    uvCustomSphere(gl, 4, 32, 16, true);
    gl.glEndList();
  }
  
  /*
   * Draw a beach ball into the scene.
  */
  private void drawBeachBall(GL2 gl, double transX, double transY, double transZ) {
    gl.glPushMatrix();
    gl.glTranslated(transX, transY, transZ);
    gl.glRotated(110, 1, 0.5, 0);
    gl.glCallList(beachBallId);  // Draw the sun by calling its display list.
    gl.glPopMatrix();
  }

  /*
   * Create and draw a bucket into the scene.
  */
  private void drawBucket(GL2 gl, double transX, double transY, double transZ) {
    double sizeL = 3; // size of base
    double sizeS = 0.75; // size of smaller cubes
    double heightOffset = (sizeL + sizeS)/2; // attach to a upper corner
    double sideOffset = (sizeL - sizeS)/2; // ensure end faces align with base
    
    gl.glPushMatrix();
    
    gl.glTranslated(transX, transY, transZ);
    gl.glColor3f(0.8f, 0, 0);
    draw3DRect(gl, sizeL,sizeL,sizeL); // base of bucket
    
    // draw the front left corner of bucket
    gl.glPushMatrix();
    gl.glColor3f(0, 0.8f, 0); // color of corners
    gl.glTranslated(-sideOffset, heightOffset, sideOffset);
    draw3DRect(gl, sizeS, sizeS, sizeS);
    gl.glPopMatrix();
    
    // draw the rear left corner of bucket
    gl.glPushMatrix();
    gl.glTranslated(-sideOffset, heightOffset, -sideOffset);
    draw3DRect(gl, sizeS, sizeS, sizeS);
    gl.glPopMatrix();
    
    // draw the rear right corner of bucket
    gl.glPushMatrix();
    gl.glTranslated(sideOffset, heightOffset, -sideOffset);
    draw3DRect(gl, sizeS, sizeS, sizeS);
    gl.glPopMatrix();
    
    // draw the front right corner of bucket
    gl.glPushMatrix();
    gl.glTranslated(sideOffset, heightOffset, sideOffset);
    draw3DRect(gl, sizeS, sizeS, sizeS);
    gl.glPopMatrix();
    
    // draw the top middle of bucket
    gl.glPushMatrix();
    gl.glColor3f(1.0f, 1.0f, 1.0f); // make the middle cube white
    gl.glTranslated(0, heightOffset, 0);
    draw3DRect(gl, sizeS, sizeS, sizeS);
    gl.glPopMatrix();
    
    gl.glPopMatrix();
  }

  /*
   * Create and draw an umbrella into the scene.
  */
  private void drawUmbrella(GL2 gl, double transX, double transY, double transZ) {
    double radius = 1.0;
    double height = 10.0;
    
    gl.glPushMatrix();
    
    gl.glColor3ub((byte)139, (byte)69, (byte)19);
    gl.glTranslated(transX, transY, transZ);
    gl.glRotated(90, 1, 0, 0);
    
    gl.glPushMatrix();
    drawStraw(gl, radius, radius, height, 64);
    gl.glTranslated(0, 0, -height/2);
    gl.glColor3ub((byte)139, (byte)69, (byte)19);
    drawOval(gl, radius, radius, 64);
    gl.glPopMatrix();
    
    gl.glPushMatrix();
    gl.glTranslated(0, 0, height/2);
    gl.glRotated(180, 1, 0, 0);
    drawOval(gl, radius, radius, 64);
    gl.glPopMatrix();
    
    gl.glPushMatrix();
    gl.glTranslated(0, 0, -2); //position top of umbrella
    gl.glCallList(umbrellaId);  // Draw the sun by calling its display list.
    gl.glPopMatrix();
    
    gl.glPopMatrix();
  }
  
  /*
   * Draw a sphere with a given radius, number of slices, and number
   * of stacks.
   */
  private static void uvCustomSphere(GL2 gl, double radius, int slices, int stacks, boolean beachBall) {
    if (radius <= 0) {
      throw new IllegalArgumentException("Radius must be positive.");
    }
    if (slices < 3) {
      throw new IllegalArgumentException("Number of slices must be at least 3.");
    }
    if (stacks < 2) {
      throw new IllegalArgumentException("Number of stacks must be at least 2.");
    }
    
    // used to make slices different colors
    double[][] colors = {
      { 1.0, 1.0, 0.0 },
      { 1.0, 0.0, 0.0 },
      { 0.0, 1.0, 0.0 },
      { 0.0, 0.0, 1.0 },
      { 1.0, 0.0, 1.0 }
    };
    
    for (int j = 0; j < stacks; j++) {
      double latitude1 = (Math.PI/stacks) * j - Math.PI/2;
      double latitude2 = (Math.PI/stacks) * (j+1) - Math.PI/2;
      double sinLat1 = Math.sin(latitude1);
      double cosLat1 = Math.cos(latitude1);
      double sinLat2 = Math.sin(latitude2);
      double cosLat2 = Math.cos(latitude2);
      gl.glBegin(GL2.GL_QUAD_STRIP);
      for (int i = 0; i <= slices; i++) {
        double longitude = (2*Math.PI/slices) * i;
        double sinLong = Math.sin(longitude);
        double cosLong = Math.cos(longitude);
        double x1 = cosLong * cosLat1;
        double y1 = sinLong * cosLat1;
        double z1 = sinLat1;
        double x2 = cosLong * cosLat2;
        double y2 = sinLong * cosLat2;
        double z2 = sinLat2;
        
        // change color for each slice of the sphere
        if(beachBall) {
          if(j < 2 || j >= (stacks-2)) {
            gl.glColor3d(1.0, 1.0, 1.0); // make the first and last two stacks white
          } else { //otherwise continue iterating thru color array
            gl.glColor3d(colors[i%colors.length][0], colors[i%colors.length][1], colors[i%colors.length][2]);
          }
        }
        
        gl.glNormal3d(x2,y2,z2);
        gl.glVertex3d(radius*x2,radius*y2,radius*z2);
        gl.glNormal3d(x1,y1,z1);
        gl.glVertex3d(radius*x1,radius*y1,radius*z1);
      }
      gl.glEnd();
    }
  } // end uvSphere

  /**
   * Draw a sphere with a given radius, number of slices, and number
   * of stacks.  The number of slices is the number of lines of longitude
   * (like the slices of an orange).  The number of stacks is the number
   * of divisions perpendicular the axis; the lines of latitude are the
   * dividing lines between stacks, so there are stacks-1 lines of latitude.
   * The last parameter tells whether or not to generate texture
   * coordinates for the sphere.  The texture wraps once around the sphere.
   * The sphere is centered at (0,0,0), and its axis lies along the z-axis.
   * (Copied from TexturedShapes.uvSphere().)
   * NOTE: added boolean to change visual effect to 'beach ball' look.  The
   * first and last two stacks will be white, and the slices pull their color
   * from a multi-dimensional array.  Slices will iterate and repeat the array
   * of colors.
   * @param gl
   * @param radius
   * @param slices
   * @param stacks
   * @param makeTexCoords
   */
  public static void uvSphere(GL2 gl, double radius, int slices, int stacks, boolean makeTexCoords) {
    if (radius <= 0)
      throw new IllegalArgumentException("Radius must be positive.");
    if (slices < 3)
      throw new IllegalArgumentException("Number of slices must be at least 3.");
    if (stacks < 2)
      throw new IllegalArgumentException("Number of stacks must be at least 2.");
    for (int j = 0; j < stacks; j++) {
      double latitude1 = (Math.PI/stacks) * j - Math.PI/2;
      double latitude2 = (Math.PI/stacks) * (j+1) - Math.PI/2;
      double sinLat1 = Math.sin(latitude1);
      double cosLat1 = Math.cos(latitude1);
      double sinLat2 = Math.sin(latitude2);
      double cosLat2 = Math.cos(latitude2);
      gl.glBegin(GL2.GL_QUAD_STRIP);
      for (int i = 0; i <= slices; i++) {
        double longitude = (2*Math.PI/slices) * i;
        double sinLong = Math.sin(longitude);
        double cosLong = Math.cos(longitude);
        double x1 = cosLong * cosLat1;
        double y1 = sinLong * cosLat1;
        double z1 = sinLat1;
        double x2 = cosLong * cosLat2;
        double y2 = sinLong * cosLat2;
        double z2 = sinLat2;
        gl.glNormal3d(x2,y2,z2);
        if (makeTexCoords)
          gl.glTexCoord2d(1.0/slices * i, 1.0/stacks * (j+1));
        gl.glVertex3d(radius*x2,radius*y2,radius*z2);
        gl.glNormal3d(x1,y1,z1);
        if (makeTexCoords)
          gl.glTexCoord2d(1.0/slices * i, 1.0/stacks * j);
        gl.glVertex3d(radius*x1,radius*y1,radius*z1);
      }
      gl.glEnd();
    }
  } // end uvSphere
  
  /*
   * Custom sphere, will only draw 1/2 of the stacks, making a half sphere.
  */
  private static void uvCustomHalfSphere(GL2 gl, double radius, int slices, int stacks, boolean colorful) {
    if (radius <= 0) {
      throw new IllegalArgumentException("Radius must be positive.");
    }
    if (slices < 3) {
      throw new IllegalArgumentException("Number of slices must be at least 3.");
    }
    if (stacks < 2) {
      throw new IllegalArgumentException("Number of stacks must be at least 2.");
    }
    
    // used to make slices different colors
    double[][] colors = { // use each color twice, to spread color between slices
      { 0.0, 0.0, 0.0 },
      { 0.0, 0.0, 0.0 },
      { 1.0, 0.0, 0.0 },
      { 1.0, 0.0, 0.0 },
      { 0.0, 1.0, 0.0 },
      { 0.0, 1.0, 0.0 },
      { 0.0, 0.0, 1.0 },
      { 0.0, 0.0, 1.0 },
      { 0.0, 1.0, 1.0 },
      { 0.0, 1.0, 1.0 }
    };
    
    for (int j = 0; j < stacks/2; j++) {
      double latitude1 = (Math.PI/stacks) * j - Math.PI/2;
      double latitude2 = (Math.PI/stacks) * (j+1) - Math.PI/2;
      double sinLat1 = Math.sin(latitude1);
      double cosLat1 = Math.cos(latitude1);
      double sinLat2 = Math.sin(latitude2);
      double cosLat2 = Math.cos(latitude2);
      gl.glBegin(GL2.GL_QUAD_STRIP);
      for (int i = 0; i <= slices; i++) {
        double longitude = (2*Math.PI/slices) * i;
        double sinLong = Math.sin(longitude);
        double cosLong = Math.cos(longitude);
        double x1 = cosLong * cosLat1;
        double y1 = sinLong * cosLat1;
        double z1 = sinLat1;
        double x2 = cosLong * cosLat2;
        double y2 = sinLong * cosLat2;
        double z2 = sinLat2;
        
        // change color for each slice of the sphere
        if(colorful) {
          if(j < 1) {
            gl.glColor3d(0.0, 0.0, 0.0); // make the first stack black, as 'button' on umbrella
          } else { //otherwise continue iterating thru color array
            gl.glColor3d(colors[i%colors.length][0], colors[i%colors.length][1], colors[i%colors.length][2]);
          }
        }
        
        gl.glNormal3d(x2,y2,z2);
        gl.glVertex3d(radius*x2,radius*y2,radius*z2);
        gl.glNormal3d(x1,y1,z1);
        gl.glVertex3d(radius*x1,radius*y1,radius*z1);
      }
      gl.glEnd();
    }
  } // end uvHalfSphere

  /*
   * Draw a blue puddle on top of the sand
  */
  private void drawPuddle(GL2 gl, double transX, double transY, double transZ) {
    double radius = 20.0;
    
    gl.glPushMatrix();
    gl.glColor3ub((byte)50, (byte)0, (byte)255);
    gl.glTranslated(transX, transY, transZ);
    gl.glRotated(90, 1, 0, 0);
    drawOval(gl, radius*1.5, radius, 64); //draw an oval shape
    gl.glPopMatrix();
  }

  // ------------ Support for keyboard handling  ------------
  /**
   * Called when the user presses any key on the keyboard, including
   * special keys like the arrow keys, the function keys, and the shift key.
   * Note that the value of key will be one of the constants from
   * the KeyEvent class that identify keys such as KeyEvent.VK_LEFT,
   * KeyEvent.VK_RIGHT, KeyEvent.VK_UP, and KeyEvent.VK_DOWN for the arrow
   * keys, KeyEvent.VK_SHIFT for the shift key, and KeyEvent.VK_F1 for a
   * function key.
   * @param e
   */
  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();  // Tells which key was pressed.
    // TODO:  Add code to respond to key presses.
    switch (key) {
      case KeyEvent.VK_LEFT:
        rotateY -= 15;
        break;
      case KeyEvent.VK_RIGHT:
        rotateY += 15;
        break;
      case KeyEvent.VK_DOWN:
        rotateX += 15;
        break;
      case KeyEvent.VK_UP:
        rotateX -= 15;
        break;
      case KeyEvent.VK_PAGE_UP:
        rotateZ += 15;
        break;
      case KeyEvent.VK_PAGE_DOWN:
        rotateZ -= 15;
        break;
      case KeyEvent.VK_HOME:
        rotateX = rotateY = rotateZ = 0;
        break;
      case KeyEvent.VK_A:
        transX -= 0.2;
        break;
      case KeyEvent.VK_D:
        transX += 0.2;
        break;
      case KeyEvent.VK_S:
        transZ -= 0.2;
        break;
      case KeyEvent.VK_W:
        transZ += 0.2;
        break;
      case KeyEvent.VK_R:
        transY -= 0.2;
        break;
      case KeyEvent.VK_F:
        transY += 0.2;
        break;
      default:
        break;
    }
    display.repaint();  // Causes the display() function to be called.
  }

  /**
   * Called when the user types a character.  This function is called in
   * addition to one or more calls to keyPressed and keyTyped. Note that ch is an
   * actual character such as 'A' or '@'.
   * @param e
   */
  @Override
  public void keyTyped(KeyEvent e) { 
    char ch = e.getKeyChar();  // Which character was typed.
    // TODO:  Add code to respond to the character being typed.
    display.repaint();  // Causes the display() function to be called.
  }

  /**
   * Called when the user releases any key.
   * @param e
   */
  @Override
  public void keyReleased(KeyEvent e) { 
  }

}
