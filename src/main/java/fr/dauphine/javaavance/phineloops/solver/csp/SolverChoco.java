package fr.dauphine.javaavance.phineloops.solver.csp;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class SolverChoco {
	private Game game;
	int height ;
	int width ;
	
	int[] topLeftCorners = {00,12,11,51};
	int[] topRightCorners = {00,12,13,52};
	int[] bottomLeftCorners = {00,10,11,50};
	int[] bottomRightCorners = {00,12,13,53};
	int[] topBorders = {00,11,12,13,21,32,33,51,52};
	int[] rightBorders = {00,10,12,13,20,33,52,53};
	int[] bottomBorders = {00,10,11,13,21,30,50,53};
	int[] leftBorders = {00,10,11,12,20,31,50,51};
	
	public SolverChoco(Game game) {
		this.game = game;
		this.height = game.getHeight();
		this.width= game.getWidth();
	}
	
	
	public void solve_choco() {
		Model model = new Model();
		Shape[][] board= game.getBoard();
		IntVar[][] vars = new IntVar[height][width];
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				int[] domain = board[i][j].getDomain();
				vars[i][j] = model.intVar(domain);
			}
		}
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				if(isTopLeftCorner(i, j)) {
					model.member(vars[i][j], topLeftCorners).post();
				}
				else if(isTopRightCorner(i, j)) {
					model.member(vars[i][j], topRightCorners).post();
				}
				else if(isBottomLeftCorner(i, j)) {
					model.member(vars[i][j], bottomLeftCorners).post();
				}
				else if(isBottomRightCorner(i, j)) {
					model.member(vars[i][j], bottomRightCorners).post();
				}
				
				else if(isTopBorder(i, j)) {
					model.member(vars[i][j], topBorders).post();
				}
				else if(isRightBorder(i, j)) {
					model.member(vars[i][j], rightBorders).post();
				}
				else if(isBottomBorder(i, j)) {
					model.member(vars[i][j], bottomBorders).post();
				}
				else if(isLeftBorder(i, j)) {
					model.member(vars[i][j], leftBorders).post();
				}
				
			}
		}
		if(model.getSolver().solve()) {
			for(int i=0; i<height;i++) {
				for(int j =0; j<width;j++) {
					System.out.print(vars[i][j].getValue()+" ");
				}
				System.out.println();
			}
		}
		
	
		
	}
	
	//is i j Corner
	private boolean isTopLeftCorner(int i, int j) {
		return (i==0 && j == 0);
	}
	private boolean isTopRightCorner(int i, int j) {
		return (i==0 && j == width-1);
	}
	private boolean isBottomLeftCorner(int i, int j) {
		return (i==height-1 && j == 0);
	}
	private boolean isBottomRightCorner(int i, int j) {
		return (i==height-1 && j == width-1);
	}
	
	//is i j Border
	private boolean isTopBorder(int i, int j) {
		return (i==0);
	}
	private boolean isRightBorder(int i, int j) {
		return (j == width-1);
	}
	private boolean isBottomBorder(int i, int j) {
		return (i==height-1);
	}
	private boolean isLeftBorder(int i, int j) {
		return (j==0);
	}
	
	
	public void solve() {
		
	}



	

}
