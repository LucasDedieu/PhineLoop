package fr.dauphine.javaavance.phineloops;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import fr.dauphine.javaavance.phineloops.model.Connection;
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
}
