package fr.dauphine.javaavance.phineloops; 

import java.io.FileNotFoundException;

import fr.dauphine.javaavance.phineloops.model.Game;
import junit.framework.Assert;
import junit.framework.TestCase;


public class MainTest extends TestCase
{
   
    public void testLoadFile() throws FileNotFoundException {
        Game game = Main.loadFile(getClass().getClassLoader().getResource("fr/dauphine/javaavance/phineloops/correct_board.txt").getFile());
        Assert.assertEquals(3, game.getHeight() );
        Assert.assertEquals(3, game.getWidth() );
        game.write(getClass().getClassLoader().getResource("fr/dauphine/javaavance/phineloops/test.txt").getFile());
    }
}
