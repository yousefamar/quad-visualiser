package world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import java.awt.Dimension;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.XRandR.Screen;

import com.metaplains.core.GameClient;
import com.metaplains.core.GameScreen;
import com.metaplains.gfx.Model;
import com.metaplains.gfx.Texture;
import com.metaplains.gfx.cam.Camera;
import com.metaplains.world.SkyBox;
import com.metaplains.world.scenes.Scene;

public class QuadScene extends Scene {

	public Quadcopter quad;
	
	public QuadScene() {
		entityManager.spawnEntity(quad = new Quadcopter(this));
		initScene();
	}

	@Override
	public boolean isSinglePlayer() {
		return true;
	}
	
	public void mouseMoved(int x, int y) {
		if (Mouse.isButtonDown(0)) {
			camera.setKite(camera.yaw + Mouse.getDX(), camera.pitch + Mouse.getDY(), 0F);
		}
	}
	
	public void mousePressed(int button, int x, int y) {
	}

	public void mouseReleased(int button, int x, int y) {
	}
	
	public void keyPressed(int keyCode, char keyChar) {
	}
	
	public void keyReleased(int keyCode, char keyChar) {
	}
	
	protected void initScene() {
		camera = new Camera() {
			@Override
			public void tick() {
			}
		};
		camera.translatePosition(0, 0, 1);
		camera.setDollyDistance(14);
		camera.setKite(0F, -45F, 0F);
		
		skyBox = new SkyBox(this);
		sunAngle = 0.0F;
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
	}

	public void render(GameScreen screen) {
		Dimension screenDims = GameClient.game.screen.screenDims;
		
		/*glViewport (window_width/2, window_height/2, window_width/2, window_height/2);
	    glMatrixMode (GL_PROJECTION);                       // Select The Projection Matrix
	    glLoadIdentity ();                          // Reset The Projection Matrix
	    // Set Up Perspective Mode To Fit 1/4 The Screen (Size Of A Viewport)
	    gluPerspective( 45.0, (GLfloat)(width)/(GLfloat)(height), 0.1f, 500.0 );*/
	    
		glViewport(256, 0, screenDims.width, screenDims.height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(90F, ((float) screenDims.width / (float) screenDims.height), 0.1F, 1000.0F);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		if (camera==null)
			return;
		camera.onRender();

		
		//renderFog();
		skyBox.render();
		//water.renderReflection(screen);
	    //water.renderRefractionAndDepth(screen);
//		renderAxes();
		//camera.frustum.render();
		
		//glUseProgram(Shader.TEST);
		//terrain.render(screen);
		entityManager.render(screen);
		renderGround(screen);
		//glUseProgram(Shader.NONE);
		
		glViewport(0, 0, screenDims.width, screenDims.height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(90F, ((float) screenDims.width / (float) screenDims.height), 0.1F, 1000.0F);
	}

	private void renderGround(GameScreen screen) {
		glPushMatrix();
		glColor4f(1, 1, 1, 1);
		glNormal3f(0, 1, 0);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, Texture.GRASSDARK);
		glTranslated(-20, -3, -20);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 1);
		glVertex3f(0, 0, 40);
		glTexCoord2f(1, 1);
		glVertex3f(40, 0, 40);
		glTexCoord2f(1, 0);
		glVertex3f(40, 0, 0);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, 0);
		glEnd();
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();		
	}

}
