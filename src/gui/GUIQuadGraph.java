package gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.util.LinkedList;
import com.metaplains.core.GameScreen;
import com.metaplains.gfx.Texture;
import com.metaplains.gfx.TrueTypeFont;
import com.metaplains.gfx.gui.GUIElement;
import com.metaplains.utils.Vec3F;

public class GUIQuadGraph extends GUIElement {

	private int accX, accY, accZ;
	private float angle;
	private int buttonTimer;
	private LinkedList<Vec3F> accs;
	private LinkedList<Float> angles;

	public GUIQuadGraph(int x, int y, int width, int height, GUIElement parent) {
		super(x, y, width, height, parent);
		accs = new LinkedList<Vec3F>();
		angles = new LinkedList<Float>();
		for (int i = 0; i < width-2; i++) {
			accs.add(Vec3F.ZERO);
			angles.add(0.0F);
		}
	}

	public synchronized void updateParameters(int accX, int accY, int accZ, float angle) {
		this.accX = accX;
		this.accY = accY;
		this.accZ = accZ;
		this.angle = angle;
		
		accs.removeFirst();
		angles.removeFirst();
		accs.add(new Vec3F(accX, accY, accZ));
		angles.add(angle);
	}
	
	public void onButtonDown() {
		buttonTimer = 8;
	}
	
	@Override
	public void tick() {
		if (buttonTimer > 0)
			buttonTimer--;
	}
	
	@Override
	public synchronized void render(GameScreen screen) {
		screen.setColor(Color.BLACK);
		screen.fillRect(x, y, width, height);
		screen.setColor(Color.LIGHT_GRAY);
		screen.drawRect(x, y, width, height);
		
		glPushMatrix();
		glTranslated(x+1, y, 0);
		
		screen.setColor(Color.DARK_GRAY);
		glBegin(GL_LINES);
		for (int i = 0; i < 19; i++) {
			glVertex2f(0, height-100-i*20);
			glVertex2f(width-2, height-100-i*20);
		}
		glEnd();
		
		for (int i = 0; i < 19; i++) {
			screen.drawString(""+i*20, x, height-115-i*20, 0.6F, 0.6F, TrueTypeFont.ALIGN_LEFT);
		}
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		screen.setColor((buttonTimer>0)?Color.WHITE:Color.DARK_GRAY);
		glBindTexture(GL_TEXTURE_2D, Texture.RADIO);
		screen.drawTexturedRect(width-53, 0, 50, 50);
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		
		int offset = 0;
		screen.setColor(Color.RED);
		glBegin(GL_LINE_STRIP);
		for (Vec3F acc : accs)
			glVertex2f(offset+=1, height-100-acc.x);
		glEnd();
		
		offset = 0;
		screen.setColor(Color.GREEN);
		glBegin(GL_LINE_STRIP);
		for (Vec3F acc : accs)
			glVertex2f(offset+=1, height-100-acc.y);
		glEnd();
		
		offset = 0;
		screen.setColor(Color.BLUE);
		glBegin(GL_LINE_STRIP);
		for (Vec3F acc : accs)
			glVertex2f(offset+=1, height-100-acc.z);
		glEnd();
		
		offset = 0;
		screen.setColor(Color.YELLOW);
		glBegin(GL_LINE_STRIP);
		for (float angle : angles)
			glVertex2f(offset+=1, height-100-angle);
		glEnd();
		
		glPopMatrix();
		
		screen.setColor(Color.WHITE);
		screen.drawString("AccX: "+accX, x+2, y+height-screen.getTTFont().getHeight(), 1, 1, TrueTypeFont.ALIGN_LEFT);
		screen.drawString("AccY: "+accY, x+102, y+height-screen.getTTFont().getHeight(), 1, 1, TrueTypeFont.ALIGN_LEFT);
		screen.drawString("AccZ: "+accZ, x+202, y+height-screen.getTTFont().getHeight(), 1, 1, TrueTypeFont.ALIGN_LEFT);
		screen.drawString("Angle: "+angle, x+302, y+height-screen.getTTFont().getHeight(), 1, 1, TrueTypeFont.ALIGN_LEFT);
	}
}