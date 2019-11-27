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
		assertTrue(t.getConnections().containsAll((Arrays.asList(Connection.NORTH,Connection.NORTH,Connection.WEST))));
		assertTrue(!t.getConnections().contains(Connection.EAST));
	}
}
