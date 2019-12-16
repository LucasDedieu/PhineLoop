package fr.dauphine.javaavance.phineloops.model;

import java.util.Stack;

public class Solver3 {
	private Game originalGame ;
	private int height;
	private int width;
	private Stack<State3> stack = new Stack<>();
	private Shape[][] board;
	private int nb=0;
	private int iEnd;
	private int jEnd;

	public Solver3(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
		this.board = game.getBoard();
		iEnd = (int)Math.ceil((height-1)/2.0);
		jEnd = (int)((width-1)/2.0);

	}


	public Game solve() {
		Game testGame  = new Game(originalGame);
		Shape[][] testBoard = testGame.getBoard();
		int maxStackSize = height*width;
		boolean[][] bool = new boolean[height][width];
		for(int i = 0; i<height-1;i++) {
			for(int j = 0; j<height-1;j++) {
				bool[i][j] = false;
			}
		}
		int i = 0;
		int j = 0;
		int nb = 1;
		State3 initialState = new State3(Direction.EAST,0,i,j,0,1);
		stack.push(initialState);
		while(!stack.isEmpty()) {
			nb++;
			State3 iteration = stack.peek();
			i = iteration.getI();
			j = iteration.getJ();
			nb = iteration.getNb();
			bool[i][j] =true;
			int level = iteration.getLevel();
			Shape shape = testBoard[i][j];
			Direction currentDirection = iteration.getDir();



			//Print 
			if(nb%1000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+iteration);
				//System.out.println(testGame);
			}

			//Can rotate ?		
			if(iteration.canRotate(shape)) {	
				boolean isWellPlaced = false;
				do{
					iteration.rotate(shape);
					if(!testGame.iShapeConnectedToBoardBorder(shape)) {
						switch(currentDirection) {
						case EAST : if(testGame.isShapeWellConnectedWithNorthAndWest(shape)) {isWellPlaced = true;}break;
						case SOUTH : if(testGame.isShapeWellConnectedWithNorthAndEast(shape)) {isWellPlaced = true;}break;
						case WEST : if(testGame.isShapeWellConnectedWithSouthAndEast(shape)) {isWellPlaced = true;}break;
						case NORTH : if(testGame.isShapeWellConnectedWithSouthAndWest(shape)) {isWellPlaced = true;}break;
						}

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
					switch(currentDirection) {
					case EAST : if(!testGame.isShapeWellConnectedWithNorthAndWest(shape)) {continue;}break;
					case SOUTH : if(!testGame.isShapeWellConnectedWithNorthAndEast(shape)) {continue;}break;
					case WEST : if(!testGame.isShapeWellConnectedWithSouthAndEast(shape)) {continue;}break;
					case NORTH : if(!testGame.isShapeWellConnectedWithSouthAndWest(shape)) {continue;}break;
					}
				}
				//Case shape already test all rotation
				else {	
					stack.pop();
					continue;
				}
			}


			//When the shape is well placed, we prepare next iteration
			State3 nextIteration = null;
			//Case last shape of the board
			if(nb==maxStackSize) {
				while(iteration.canRotate(shape)) {
					if(testGame.isShapeFullyConnected(shape)) {
						return testGame;
					}
					iteration.rotate(shape);		
				}
				if(testGame.isShapeFullyConnected(shape)) {
					return testGame;
				}
				stack.pop();
				continue;
			}
			switch(currentDirection) {
			case EAST : 
				if(j<width-1-level) {
					nextIteration = new State3(currentDirection,level, i,j+1,0,nb+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithEast(shape)) {
						nextIteration = new State3(Direction.SOUTH,level,i+1,j,0,nb+1);
					}
					else{
						continue;
					}

				}
				break;
			case SOUTH : 
				if(i<height-1-level) {
					nextIteration = new State3(currentDirection,level,i+1,j,0,nb+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithSouth(shape)) {
						nextIteration = new State3(Direction.WEST,level,i,j-1,0,nb+1);
					}
					else{
						continue;
					}

				}
				break;
			case WEST : 
				if(j>0+level) {
					nextIteration = new State3(currentDirection,level,i,j-1,0,nb+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithWest(shape)) {
						nextIteration = new State3(Direction.NORTH,level,i-1,j,0,nb+1);
					}
					else{
						continue;
					}

				}
				break;
			case NORTH : 
				if(i>1+level) {
					nextIteration = new State3(currentDirection,level,i-1,j,0,nb+1);
				}
				else {
					if(testGame.isShapeWellConnectedWithNorth(shape)) {
						nextIteration = new State3(Direction.EAST,level+1,i,j+1,0,nb+1);
					}
					else{
						continue;
					}
				}
				break;
			}
			//Add next iteration to stack
			stack.push(nextIteration);
		}
		return null;
	}


	private int calcMaxStackSize() {
		int count =0;
		for(int i = 0; i<height-1;i++) {
			for(int j = 0; j<height-1;j++) {
				Shape shape = board[i][j];
				String shapeClassName =shape.getClass().getSimpleName();
				//Case XShape or EmptyShape (do not rotate)
				if(!shapeClassName.equals("XShape") && !shapeClassName.equals("EmptyShape") ) {
					count++;
				}
			}
		}
		return count;
	}
	

}
