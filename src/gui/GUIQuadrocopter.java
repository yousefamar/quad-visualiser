package gui;

import com.metaplains.core.GameClient;
import com.metaplains.core.GameScreen;
import com.metaplains.gfx.gui.GUIElement;

public class GUIQuadrocopter extends GUIElement {

	private GUIQuadGraph graph;

	public GUIQuadrocopter() {
		super();
		subElements.add(graph = new GUIQuadGraph(0, 0, 512, GameClient.game.screen.getHeight(), this));
	}
	
	@Override
	public void render(GameScreen screen) {
		for (GUIElement element : subElements)
			element.render(screen);
	}
	
	public void setParameters(int accX, int accY, int accZ, float angle) {
		graph.updateParameters(accX, accY, accZ, angle);
	}
	
	public void onButtonDown() {
		graph.onButtonDown();
	}
}
