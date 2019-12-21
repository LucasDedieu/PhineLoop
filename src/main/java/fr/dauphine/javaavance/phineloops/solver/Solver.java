package fr.dauphine.javaavance.phineloops.solver;

import java.util.Stack;

import fr.dauphine.javaavance.phineloops.checker.Checker;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class Solver {
	private Game originalGame ;
	private int height;
	private int width;
	private Stack<State> stack = new Stack<>();
	private Shape[][] board;
	int nb=0;

	public Solver(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
		this.board = game.getBoard();
	}

	
	public Game solve() {
		State initialState = new State(originalGame,0,0,0);
		stack.push(initialState);
		while(!stack.isEmpty()) {
			nb++;
			State iteration = stack.peek();
			if(nb%1000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+iteration);
			}
			int i = iteration.getI();
			int j = iteration.getJ();
			
			//Game solved ?
			Game game = iteration.getGame();
			if(Checker.checkFromIJ(game, i, j)) {
				return game;
			}
			
			
			
			if(iteration.canRotate()) {
				iteration.rotate();
				//Game game = iteration.getGame();
				Shape shape  = iteration.getShape();
				//rotate until shape is well placed
				if(game.iShapeConnectedToBoardBorder(shape)  ||  !game.isShapeWellConnectedWithNorthAndWest(shape)) {
					continue;
				}
			}
			
			else {
				Shape shape =  board[i][j];
				String shapeClassName =shape.getClass().getSimpleName();
				//Case XShape or EmptyShape (do not rotate)
				if(shapeClassName.equals("XShape") || shapeClassName.equals("EmptyShape") ) {
					stack.pop();
					if(!iteration.getGame().isShapeWellConnectedWithNorthAndWest(shape)) {
						continue;
					}
				}
				//Case shape already test all rotation
				else {	
					stack.pop();
					continue;
				}
			}

			//When the shape is well placed, we prepare next iteration
			State nextIteration = null;
			//Case last shape of the board
			if(i==height-1 && j ==width-1) {
				while(iteration.canRotate()) {
					if(iteration.getGame().isShapeFullyConnected(iteration.getShape())) {
						return iteration.getGame();
					}
					iteration.rotate();		
				}
				//if last shape has no possible good rotation -> backtrack
				stack.pop();
				continue;
			}
			else {
				//Case shape on right border
				if(j ==width-1) {
					nextIteration = new State(iteration);
					nextIteration.setJ(0);
					nextIteration.setI(i+1);
				}
				else{
					nextIteration = new State(iteration);
					nextIteration.setJ(j+1);
				}		
			}
			//Add next iteration to stack
			stack.push(nextIteration);
		}
		return null;
	}
	/*
	public Game solve(int ii, int jj) {
		stack.push(new State(game));
		for(int i= ii;i<heigh;i++)	{
			for(int j = jj;j<width;j++) {
				Shape shape = board[i][j];
				for(int rot = 0;rot<shape.getMaxRotation(); rot++ ) {
					Game iteration = new Game(game);


					if(iteration.isUnsolvable(shape, iteration.getNeighbors(shape))) {
						stack.pop();
					}
					if(Checker.check(iteration)) {
						return iteration;
					}
					solve
					shape.rotate();
					if(Checker.check(game)) {
						return game;
					}
					
					
				}

			}
		}
		return null;
		
		
		
		if(i==height-1 && j ==width-1) {
					stack.pop();
					if(r<board[i][j].getMaxRotation()) {
						stack.push(new State(game,i,j,r++));
					}
					else {
						
					}
					
				}
			}
			else {
				
			}
			if(Checker.check(iteration.getGame())) {
				return iteration.getGame();
			}
			
			
			else if(j==width-1) {
				i++;
				j=0;
				stack.push(new State(game,i,j,0));
			}
			else{
				j++;
				stack.push(new State(game,i,j,0));
				
				
			
			
			
			///
			
			if(i==height-1 && j ==width-1) {
				for(int k = 0; k<3;k++) {
					if(Checker.check(nextIteration)) {
						return nextIteration.getGame();
					}
					nextIteration.rotate();		
				}
				iteration.setJ(j-1);
				iteration.rotate();
				iteration.setJ(j);
				continue;
			}
			else {
				if(j ==width-1) {
					nextIteration.setJ(0);
					nextIteration.setI(i+1);
				}
				else{
					nextIteration.setJ(j+1);
				}
				
			}
			
			
			////
			 * if(iteration.canRotate()) {
				nextIteration = new State(iteration);
				nextIteration.rotate();
			}
			else {
				stack.pop();
				nextIteration = stack.pop();
				int i = nextIteration.getI();
				int j = nextIteration.getJ();
				if(j-1>=0) {
					nextIteration.setJ(j-1);
				}
				else if(i-1>=0) {
					nextIteration.setI(i-1);
					nextIteration.setJ(width-1);
				}
				nextIteration.rotate();
				//nextIteration.setI(i);
				//nextIteration.setJ(j);
				//nextIteration.setR(0);
				stack.push(nextIteration);
				continue;
				
			}
			
		
	}*/
	
}
