package fr.dauphine.javaavance.phineloops;

import org.junit.Assert;
import org.junit.Test;

import fr.dauphine.javaavance.phineloops.model.Checker;
import fr.dauphine.javaavance.phineloops.model.Game;

public class CheckerTest {

	@Test
	public void testSolve() {
        Game game = Main.loadFile(getClass().getClassLoader().getResource("fr/dauphine/javaavance/phineloops/correct_board.txt").getFile());
        //Checker checker = new Checker(game);
        Assert.assertTrue(Checker.check(game));
	}

}
