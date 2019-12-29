package fr.dauphine.javaavance.phineloops.solver;

import java.util.concurrent.CountDownLatch;

import fr.dauphine.javaavance.phineloops.checker.Checker;
import fr.dauphine.javaavance.phineloops.controller.ThreadController;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.solver.csp.SolverCSP;
import fr.dauphine.javaavance.phineloops.solver.line.SolverLineByLine;

public class MultiSolver implements Solver {
	private Game game;


	public  MultiSolver(Game game) {
		this.game = game;
	}

	@Override
	public Game solve(int threads) {
		if(threads < 3) {
			Solver solver = new SolverLineByLine(game);
			return solver.solve(threads);
		}
		CountDownLatch solversLatch = new CountDownLatch(1);

		Thread cspThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Solver solver = new SolverCSP(game);
				Game gameSolved = solver.solve(1);
				if(gameSolved!=null) {
					if(Checker.check(gameSolved)) {
						ThreadController.getInstance().setSolvedGame(gameSolved);
						ThreadController.getInstance().stop();
						System.out.println("csp");
						solversLatch.countDown();
					}
				}
			}
		});
		Thread lineThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Solver solver = new SolverLineByLine(game);
				Game gameSolved = solver.solve(threads-2);
				ThreadController.getInstance().setSolvedGame(gameSolved);
				System.out.println("line");
				solversLatch.countDown();
			}
		});
		
		cspThread.start();
		lineThread.start();
		
		try {
			solversLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return ThreadController.getInstance().getSolvedGame();
	}

}
