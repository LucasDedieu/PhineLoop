package fr.dauphine.javaavance.phineloops.solver.line;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import fr.dauphine.javaavance.phineloops.controller.RenderManager;
import fr.dauphine.javaavance.phineloops.controller.ThreadController;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class LineByLineConsumer implements Runnable {
	private final BlockingQueue<Game> queue;
	private Deque<StateLineByLine> stack;
	private CountDownLatch latch;
	

	public LineByLineConsumer(BlockingQueue<Game> queue,  CountDownLatch latch) {
		this.queue = queue;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			while(!ThreadController.getInstance().isStop()) {
				Game game = queue.take();
				if(solve(game)) {
					latch.countDown();
				}
				else {
					abort();
				}
			}
		}
		catch(Exception e) {
			abort();
			e.printStackTrace();
		}
	}
	
	/**
	 * Kill all the other threads and handing back to the main thread by decrementing the latch
	 */
	private void abort() {
		ThreadController.getInstance().stop();
		while(latch.getCount()>0) {
			latch.countDown();
		}
	}
	
	private boolean solve(Game game) {
		//System.out.println(game);
		int height = game.getHeight();
		int width = game.getWidth();
		stack = new ArrayDeque<StateLineByLine>(height*width);
		Shape[][] board = game.getBoard(); 
		int i =height-1;
		int j=width-1;
		
		StateLineByLine initialState = new StateLineByLine(i,j,0);
		stack.push(initialState);		
		while(!stack.isEmpty() ) {
			StateLineByLine iteration = stack.peek();
			i = iteration.getI();
			j = iteration.getJ();
			Shape shape = board[i][j];
			//Case frozen shape
			
			if(shape.isFrozen()) {
				stack.pop();
			}

			//Can rotate ?		
			else if(iteration.canRotate(shape)) {
				boolean isWellPlaced = false;
				do{

					iteration.rotate(shape);


					if(shape.getPossibleOrientation()[shape.getOrientation()]&&  game.isShapeWellConnectedWithSouthAndEast(shape)) {
						isWellPlaced = true;
					}
				}while(!isWellPlaced && iteration.canRotate(shape));
				//if shape has no possible good rotation -> backtrack
				if(!isWellPlaced) {
					stack.pop();

					continue;
				}	
			}
			//Case shape already test all rotation	
			else{	
				stack.pop();

				continue;
			}

			//When the shape is well placed, we prepare next iteration
			StateLineByLine nextIteration = null;
			//Case last shape of the board
			if(i==0 && j ==0) {
				if(shape.isFrozen()) {
					return true;
				}
				if(game.isShapeFullyConnected(shape)) {

					return true;
				}
				stack.pop();

				continue;
			}
			else {	
				//Case shape on right border
				if(j ==0) {
					nextIteration = new StateLineByLine(i-1, width-1,0);
				}
				else{
					nextIteration = new StateLineByLine(i,j-1,0);
				}		
			}
			//Add next iteration to stack
			stack.push(nextIteration);
		}
		return false;
	}

}
