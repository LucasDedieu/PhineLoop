package fr.dauphine.javaavance.phineloops.model;

import java.util.Stack;

public class Solver2 {
	private Game originalGame ;
	private int height;
	private int width;
	private Stack<State2> stack = new Stack<>();
	private Shape[][] board;
	int nb=0;

	public Solver2(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
		this.board = game.getBoard();
	}

	
	public Game solve() {
		Game testGame  = new Game(originalGame);
		Shape[][] testBoard = testGame.getBoard();
		State2 initialState = new State2(0,0,0);
		stack.push(initialState);
		while(!stack.isEmpty()) {
//			nb++;
			State2 iteration = stack.peek();
			int i = iteration.getI();
			int j = iteration.getJ();
			Shape shape = testBoard[i][j];
/*			
			//Print 
			if(nb%10000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+iteration);
			}
/*
			//Check
			if(Checker.checkFromIJ(testGame, i, j)) {
				return testGame;
			}
*/
			
			
			//Can rotate ?		
			if(iteration.canRotate(shape)) {
				/*
				iteration.rotate(shape);
				//Game game = iteration.getGame();
				//rotate until shape is well placed
				if(testGame.iShapeConnectedToBoardBorder(shape)  ||  !testGame.isShapeWellConnectedWithNorthAndWest(shape)) {
					continue;
				}*/
				
				boolean isWellPlaced = false;
				do{
					iteration.rotate(shape);
					if(!testGame.iShapeConnectedToBoardBorder(shape)  &&  testGame.isShapeWellConnectedWithNorthAndWest(shape)) {
						isWellPlaced = true;
					}
				}while(isWellPlaced == false && iteration.canRotate(shape));
				
				//if shape has no possible good rotation -> backtrack
				if(!isWellPlaced) {
					stack.pop();
					continue;
				}
				
				
				
			}
			else {
				String shapeClassName =shape.getClass().getSimpleName();
				//Case XShape or EmptyShape (do not rotate)
				if(shapeClassName.equals("XShape") || shapeClassName.equals("EmptyShape") ) {
					stack.pop();
					if(!testGame.isShapeWellConnectedWithNorthAndWest(shape)) {
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
			State2 nextIteration = null;
			//Case last shape of the board
			if(i==height-1 && j ==width-1) {
				if(testGame.isShapeFullyConnected(shape)) {
					return testGame;
				}

				stack.pop();
				continue;
			}
			else {
				//Case shape on right border
				if(j ==width-1) {
					nextIteration = new State2(i+1,0,0);
				}
				else{
					nextIteration = new State2(i,j+1,0);
				}		
			}
			//Add next iteration to stack
			stack.push(nextIteration);
		}
		return null;
	}
}
