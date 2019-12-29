package fr.dauphine.javaavance.phineloops.controller;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.view.ShapesDrawer;

public class RenderManager {
	private static final RenderManager INSTANCE = new RenderManager();
	private ShapesDrawer drawer;
	
	private RenderManager() {
		
	}
	
	public void init(ShapesDrawer drawer) {
		this.drawer = drawer;
		
	}
	
	public static RenderManager getIntance() {
		return INSTANCE;
	}
	
	public void updateShape(Shape shape) {
		drawer.updateShape(shape);
	}
	
	public boolean isInit() {
		return drawer != null;
	}

	public void updateGame(Game game) {
		drawer.updateButtons(game);
		drawer.drawGame();
	}
	

	
}
