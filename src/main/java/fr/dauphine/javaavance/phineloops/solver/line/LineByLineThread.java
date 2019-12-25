package fr.dauphine.javaavance.phineloops.solver.line;

import java.util.concurrent.CountDownLatch;

import fr.dauphine.javaavance.phineloops.controller.ThreadController;
import fr.dauphine.javaavance.phineloops.model.Game;

public class LineByLineThread implements Runnable {
	private Game originalGame ;
	private CountDownLatch latch;


	public LineByLineThread(Game game, CountDownLatch latch) {
		this.originalGame = game;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		SolverLineByLine solver = new SolverLineByLine(originalGame);
		Game solvedGame  = solver.solve();
		ThreadController.getInstance().setSolvedGame(solvedGame);
		latch.countDown();
	}

}
