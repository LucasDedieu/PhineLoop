package fr.dauphine.javaavance.phineloops.solver;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;

import fr.dauphine.javaavance.phineloops.controller.ThreadController;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class SolverLineByLineMultiThread {
	private Game originalGame ;
	private int height;
	private int width;
	private Deque<StateLineByLine> stack = new ArrayDeque<>();
	//private Stack<State2> stack = new Stack<>();
	private Shape[][] board;
	int nb=0;

	public SolverLineByLineMultiThread(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
		this.board = game.getBoard();
	}
	
	public Game solve(){
		CountDownLatch latch = new CountDownLatch(1);
		Thread t1 = new Thread(new LineByLineThread(latch, originalGame, false, true));
		Thread t2 = new Thread(new LineByLineThread(latch, originalGame, true, true));
		Thread t3 = new Thread(new LineByLineThread(latch, originalGame, true, true));
		Thread t4 = new Thread(new LineByLineThread(latch, originalGame, false, true));
		t1.setDaemon(true);
		t2.setDaemon(true);
		t1.start();
		t2.start();
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ThreadController.getInstance().getSolvedGame();
	}

	public Game solve(boolean rotateFirst) {
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
			//nb++;
			StateLineByLine iteration = stack.peek();
			i = iteration.getI();
			j = iteration.getJ();
			Shape shape = testBoard[i][j];
/*			
			//Print 
			if(nb%10000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+iteration);
			}*/

			//Case XShape or EmptyShape (do not rotate)
			int shapeType = shape.getType();
			if(shapeType == 0 || shapeType ==4) {
				stack.pop();
				if(!testGame.isShapeWellConnectedWithSouthAndEast(shape)) {
					hasPop =true;
					continue;
				}
			}
			
			else if(rotateFirst && !hasPop && !testGame.iShapeConnectedToBoardBorder(shape)  &&  testGame.isShapeWellConnectedWithSouthAndEast(shape)) {
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
		}
		return null;
	}
}
