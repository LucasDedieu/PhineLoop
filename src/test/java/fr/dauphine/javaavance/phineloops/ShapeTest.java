package fr.dauphine.javaavance.phineloops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import fr.dauphine.javaavance.phineloops.model.EmptyShape;
import fr.dauphine.javaavance.phineloops.model.IShape;
import fr.dauphine.javaavance.phineloops.model.LShape;
import fr.dauphine.javaavance.phineloops.model.QShape;
import fr.dauphine.javaavance.phineloops.model.TShape;

public class ShapeTest {

	@Test
	public void testRotate() {
		
		TShape t = new TShape(2,0,0);
		t.rotate();
		assertEquals(3,t.getOrientation());
		assertTrue(t.getConnections()[0] && t.getConnections()[2] && t.getConnections()[3]);
		assertTrue(!t.getConnections()[1]);
		 
	}
	
	@Test
	public void testOrientation() {
		TShape t = new TShape(2,0,0);
		EmptyShape empty = new EmptyShape(0,0,0);
		t.rotate();
		assertEquals(3,t.getOrientation());
		//assertTrue(t.getConnections().equals(new boolean[]{false,true,true,true}));
		t.setOrientation(1);
	//	assertTrue(t.getConnections().equals(new boolean[]{true,true,true,false}));
		try {
			new IShape(3,0,0);
			fail();
		}
		catch(IllegalArgumentException e) {
			
		}
		
		
	}
	
	
	
	@Test
	public void testDomainPruning() {
		/*
		 * TShape t = new TShape(2,0,0); t.getDomainWithPruning();
		 */
	}
	
	@Test 
	public void testGetVConnection()
	{
		QShape q = new QShape(2,0,0);
		TShape t = new TShape(2,0,0);
		LShape l = new LShape(3,0,0);
		EmptyShape empty = new EmptyShape(0,0,0);
		assertEquals(q.getVConnections(),1);
		assertEquals(t.getVConnections(),3);
		assertEquals(l.getVConnections(),2);
		assertEquals(empty.getVConnections(),0);
		
	}
}
