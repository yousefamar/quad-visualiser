package core;

import gui.GUIQuadrocopter;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import world.QuadScene;

import com.metaplains.core.*;

public class QuadClient extends GameClient implements Runnable {
	
	public QuadClient() {
		super(0, "");
		new SerialDataHandler(this);
	}
	
	private void tickGame() {
		//TODO: Consider creating a separate thread for network IO.
		if (netIOManager != null)
			netIOManager.handlePackets();
		inputManager.handleInput();
		if(currentWorld != null)
			currentWorld.tick();
		currentGUIScreen.tick();
	}
	
	@Override
	public void run() {
		try {
			Display.setParent(screen);
			//Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		screen.init();

		currentGUIScreen = new GUIQuadrocopter();
		currentWorld = new QuadScene();
		
		long time = getTimeMS();
		long lastSecond = time;
		long lastGameTick = time;
		int tpsCounter = 0;
		while (!isCloseRequested && !Display.isCloseRequested()) {
			//TODO: Find a more stable way to cap TPS to 20.
			time = getTimeMS();
			if (time - lastSecond > 1000) {
				tpsCounter = 0;
				lastSecond += 1000;
			}
			if (tpsCounter >= 20)
				lastGameTick = time;
			if (time - lastGameTick > 50) {
				tickGame();
				tpsCounter++;
				lastGameTick += 50;
			}
			screen.render();
			Display.sync(100);
			Display.update();
		}
		Display.destroy();
		System.exit(0);
	}
		
	private long getTimeMS() {
		return (System.nanoTime() / 1000000);
	}
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Realtime Quadcopter Visualisation");
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("icon16.png")));
		game = new QuadClient();
		frame.add(game.screen, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.remove(game.screen);
			}
		});
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}