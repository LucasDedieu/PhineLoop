package fr.dauphine.javaavance.phineloops; 

import java.io.FileNotFoundException;

import org.junit.Test;

import fr.dauphine.javaavance.phineloops.model.Game;
import junit.framework.Assert;


public class MainTest
{
	@Test
    public void testLoadFile() throws FileNotFoundException {
        Game game = Main.loadFile(getClass().getClassLoader().getResource("correct_board.txt").getFile());
        Assert.assertEquals(3, game.getHeight() );
        Assert.assertEquals(3, game.getWidth() );
    }
}
