package fr.dauphine.javaavance.phineloops;

import org.junit.Test;


import fr.dauphine.javaavance.phineloops.model.EmptyShape;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.LShape;
import fr.dauphine.javaavance.phineloops.model.QShape;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.model.TShape;
import fr.dauphine.javaavance.phineloops.model.XShape;
import junit.framework.Assert;

public class GameTest {
	Game game = new Game(3,3,1);
	
	
	@Test
	public void testAddShape_possible() {
		Shape s1 = new TShape(0, 1,1);
		game.getBoard()[s1.getI()][s1.getJ()] = s1;
		Shape s2 = new TShape(0, 1,2);
		
		try {
			game.addShape(s2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(game.getBoard()[s2.getI()][s2.getJ()],s2);
	}
	
	@Test(expected = Exception.class)
	public void testAddShape_impossible() throws Exception {
		Shape s1 = new TShape(0, 1,1);
		game.addShape(s1);
		Shape s2 = new TShape(0, 1,1);
		game.addShape(s2);
	}
	
	@Test
	public void testGetNeighbors() throws Exception {
		Shape s1 = new TShape(0, 1,1);
		Shape s2 = new XShape(0, 0,1);
		Shape s3 = new XShape(0, 1,2);
		Shape s4 = new EmptyShape(0, 2,1);
		Shape s5 = new TShape(0, 1,0);
		
		game.addShape(s1);
		game.addShape(s2);
		game.addShape(s3);
		game.addShape(s4);
		game.addShape(s5);
		
		Shape[] neighbors = game.getNeighbors(s1);
		Assert.assertEquals(s2, neighbors[0]);
		Assert.assertEquals(s3, neighbors[1]);
		Assert.assertEquals(s4, neighbors[2]);
		Assert.assertEquals(s5, neighbors[3]);
	}
	
	@Test
	public void testIsShapeFullyConnected_positive() throws Exception {
		Shape s1 = new TShape(0, 1,1);
		Shape s2 = new XShape(0, 0,1);
		Shape s3 = new XShape(0, 1,2);
		Shape s4 = new EmptyShape(0, 2,1);
		Shape s5 = new TShape(0, 1,0);
		
		game.addShape(s1);
		game.addShape(s2);
		game.addShape(s3);
		game.addShape(s4);
		game.addShape(s5);
		
		Assert.assertEquals(true, game.isShapeFullyConnected(s1));
	}
	
	@Test
	public void testIsShapeFullyConnected_negative() throws Exception {
		Shape s1 = new TShape(0, 1,1);
		Shape s2 = new XShape(0, 0,1);
		Shape s3 = new XShape(0, 1,2);
		Shape s4 = new EmptyShape(0, 2,1);
		Shape s5 = new TShape(0, 1,0);
		
		game.addShape(s1);
		game.addShape(s2);
		game.addShape(s3);
		game.addShape(s4);
		game.addShape(s5);
		
		Assert.assertEquals(false, game.isShapeFullyConnected(s2));
	}
	
	//To Update
	@Test
	public void areShapesConnected() throws Exception
	{
		/*
		Shape s1 = new LShape(1,1,1);
		Shape s2 = new LShape(2,0,1);
		Shape s3 = new QShape(0,1,2);
		Shape s4 = new QShape(0,1,0);
		game.addShape(new LShape(1,1,1));
		game.addShape(new LShape(2,0,1));
		game.addShape(new QShape(0,1,2));
		game.addShape(new QShape(0,1,0));
		Assert.assertTrue(game.areShapesConnected(s1, s2));
		s2.setOrientation(0);
		Assert.assertFalse(game.areShapesConnected(s1, s2));
		Assert.assertFalse(game.areShapesConnected(s2, s3));
		*/
	}
	
	//To update
	@Test
	public void lookingButNotConnected() throws Exception
	{
		/*
		Shape s1 = new LShape(1,1,1);
		Shape s2 = new LShape(2,0,1);
		Shape s3 = new QShape(0,1,2);
		Shape s4 = new QShape(0,1,0);
		game.addShape(new LShape(1,1,1));
		game.addShape(new LShape(2,0,1));
		game.addShape(new QShape(0,1,2));
		game.addShape(new QShape(0,1,0));
		Assert.assertTrue(game.areShapesConnected(s1, s2));
		s2.setOrientation(0);
		Assert.assertFalse(game.areShapesConnected(s1, s2));
		Assert.assertFalse(game.areShapesConnected(s2, s3));
		*/
	}

}
