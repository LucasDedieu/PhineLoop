package fr.dauphine.javaavance.phineloops.controller;

import fr.dauphine.javaavance.phineloops.model.Game;

public class ThreadController {
	private final static ThreadController INSTANCE = new ThreadController();
	private int nbMaxThread;
	private int nbThreadRemaining;
	private Game solvedGame;
	private boolean rotateFirst;
	private boolean topLeft;
	
	
	private ThreadController() {
		
	}
	
	public static ThreadController getInstance() {
		return INSTANCE;
	}
	
	public void setNbMaxThread(int nb) {
		this.nbMaxThread = nb;
	}
	
	public int getNbMaxThread() {
		return nbMaxThread;
	}
	
	public void setNbThreadRemaining(int nb) {
		this.nbThreadRemaining = nb;
	}
	
	public int getNbThreadRemaining() {
		return nbThreadRemaining;
	}

	public Game getSolvedGame() {
		return solvedGame;
	}

	
	public synchronized void setSolvedGame(Game solvedGame, boolean rotateFirst, boolean topLeft) {
		if(this.solvedGame == null) {
			this.solvedGame = solvedGame;
			this.rotateFirst = rotateFirst;
			this.topLeft = topLeft;
		}
	}
	
	public synchronized void setSolvedGame(Game solvedGame) {
		if(this.solvedGame == null) {
			this.solvedGame = solvedGame;
		}
	}
	
	public String getMessage() {
		return ("rotate first method :"+rotateFirst+"   top left method :"+topLeft);
	}
}
