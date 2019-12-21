package fr.dauphine.javaavance.phineloops.solver.line;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class State {
	private Game game;
	private int i;
	private int j;
	private int r;
	
	public State(Game game, int i, int j, int r) {
		this.game = game;
		this.i = i;
		this.j = j;
		this.r = r;
	}
	
	public State(State state) {
		this.game = new Game(state.getGame());
		this.i = state.i;
		this.j = state.j;
		this.r = 0;
	}
	
	public Game getGame() {
		return game;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getR() {
		return r;
	}

	public boolean canRotate() {
		String shapeClassName = game.getBoard()[i][j].getClass().getSimpleName();
		if(shapeClassName.equals("XShape") || shapeClassName.equals("EmptyShape") ) {
			return false;
		}
		return r < game.getBoard()[i][j].getMaxRotation()+1;
	}
	
	public void rotate() {
		r++;
		game.getBoard()[i][j].rotate();
	}

	public void setJ(int j) {
		this.j=j;
		
	}

	public void setI(int i) {
		this.i=i;
	}
	
	public void setR(int r) {
		this.r=r;
		
	}
	
	public Shape getShape() {
		return game.getBoard()[i][j];
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("grille : \n"+game);
		sb.append("i :"+i+", j :"+j+", r :"+r);
		sb.append("\n_________________\n");
		return sb.toString();
		
	}
	
}
