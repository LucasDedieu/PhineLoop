package fr.dauphine.javaavance.phineloops;

import java.io.FileNotFoundException;

import org.junit.Assert;

import org.junit.Test;

import fr.dauphine.javaavance.phineloops.checker.*;
import fr.dauphine.javaavance.phineloops.model.Game;

public class GeneratorTest {

	@Test
	public void testGenerate() 
	{
		Game game = new Game(3, 3,0);
		game.generateSolution();
        Assert.assertTrue(Checker.check(new Game(game)));
	}

}
