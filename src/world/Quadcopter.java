package world;

import static org.lwjgl.opengl.GL11.*;

import com.metaplains.core.GameScreen;
import com.metaplains.entities.Entity;
import com.metaplains.gfx.Model;
import com.metaplains.gfx.cam.ClippingSphere;
import com.metaplains.world.scenes.Scene;

public class Quadcopter extends Entity {

	public Quadcopter(Scene scene) {
		super(scene, 0, 0, 0);
	}

	@Override
	public ClippingSphere getClippingSphere() {
		return null;
	}
	
	public float yaw, z;

	@Override
	public void render(GameScreen screen) {
		glPushMatrix();
		glEnable(GL_TEXTURE_2D);
		glTranslatef(0, z, 0);
		glRotatef(yaw, 0, 1, 0);
		screen.renderModel(Model.QUAD);
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();
	}

}
