package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;

import fr.dauphine.javaavance.phineloops.controller.ThreadController;

public class SolverLineByLine {
	private Game originalGame ;
	private int height;
	private int width;
	private Deque<StateLineByLine> stack;
	//private Stack<State2> stack = new Stack<>();
	private Shape[][] board;
	int nb=0;

	public SolverLineByLine(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
		this.board = game.getBoard();
		stack = new ArrayDeque<StateLineByLine>(height*width);
	}
	
	

	public Game solve() {
		Game testGame  = new Game(originalGame);
		Shape[][] testBoard = testGame.getBoard();
		for(int i = 0; i<height-1;i++) {
			for(int j = 0; j<height-1;j++) {
				Shape shape = board[i][j];
				if(shape.getType() == 4 && (i==0||j==0||i==height-1||j==width-1)) {
					//unsolvable
					return null;
				}
			}
		}
		int i =height-1;
		int j=width-1;
		StateLineByLine initialState = new StateLineByLine(i,j,0);
		stack.push(initialState);
		boolean hasPop = false;
		while(!stack.isEmpty()) {
			nb++;
			StateLineByLine iteration = stack.peek();
			i = iteration.getI();
			j = iteration.getJ();
			Shape shape = testBoard[i][j];
		
			//Print 
			if(nb%100000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+testGame);
			}

			//Case XShape or EmptyShape (do not rotate)
			int shapeType = shape.getType();
			if(shapeType == 0 || shapeType ==4) {
				stack.pop();
				if(!testGame.isShapeWellConnectedWithSouthAndEast(shape)) {
					hasPop =true;
					continue;
				}
			}
			
			else if( !hasPop && !testGame.iShapeConnectedToBoardBorder(shape)  &&  testGame.isShapeWellConnectedWithSouthAndEast(shape)) {
				//empty
			}
			//Can rotate ?		
			else if(iteration.canRotate(shape)) {
				boolean isWellPlaced = false;
				do{
					iteration.rotate(shape);
					if(!testGame.iShapeConnectedToBoardBorder(shape)  &&  testGame.isShapeWellConnectedWithSouthAndEast(shape)) {
							isWellPlaced = true;
					}
				}while(!isWellPlaced && iteration.canRotate(shape));
				//if shape has no possible good rotation -> backtrack
				if(!isWellPlaced) {
					stack.pop();
					hasPop = true;
					continue;
				}	
			}
			//Case shape already test all rotation	
			else{	
				stack.pop();
				hasPop = true;
				continue;
			}
			
			
			
			
			
			
			
			
			
			
			//When the shape is well placed, we prepare next iteration
			StateLineByLine nextIteration = null;
			//hasPop = false;
			//Case last shape of the board
			if(i==0 && j ==0) {
				if(testGame.isShapeFullyConnected(shape)) {
					return testGame;
				}
				stack.pop();
				continue;
			}
			else {
				//Case shape on right border
				if(j ==0) {
					nextIteration = new StateLineByLine(i-1,width-1,0);
				}
				else{
					nextIteration = new StateLineByLine(i,j-1,0);
				}		
			}
			//Add next iteration to stack
			stack.push(nextIteration);
		}
		return null;
	}
}
