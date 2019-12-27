package fr.dauphine.javaavance.phineloops.view;

import javax.swing.JButton;

import fr.dauphine.javaavance.phineloops.model.Shape;

public class ShapeButton extends JButton {

	private static final long serialVersionUID = 1L;
	private Shape shape;
	
	public ShapeButton(Shape shape) {
		super();
		this.shape = shape;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
}
