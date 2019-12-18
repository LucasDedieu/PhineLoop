package fr.dauphine.javaavance.phineloops.view;

import java.awt.Graphics;

import fr.dauphine.javaavance.phineloops.model.IShape;

public class IShapeDrawer extends Drawer{
	private IShape iShapeToDraw;
	
	public IShapeDrawer(IShape is) {
		// TODO Auto-generated constructor stub
		iShapeToDraw = is;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawLine((int)(iShapeToDraw.getType()),(int)(iShapeToDraw.getOrientation()), (int)(iShapeToDraw.getI()), (int)(iShapeToDraw.getJ()));
	}

}
