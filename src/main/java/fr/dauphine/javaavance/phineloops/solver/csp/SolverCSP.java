package fr.dauphine.javaavance.phineloops.solver.csp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.solver.Solver;


public class SolverCSP implements Solver{
	protected static int NORTH = 0;
	protected static int EAST = 1;
	protected static int SOUTH = 2;
	protected static int WEST = 3;
	private Game originalGame ;
	private int height;
	private int width;
	private Deque<StateCSP> stack;
	//private Stack<State2> stack = new Stack<>();
	private Shape[][] board;
	int nb=0;

	public SolverCSP(Game game) {
		this.originalGame = game;
		this.height = game.getHeight();
		this.width = game.getWidth();
		this.board = game.getBoard();
		stack = new ArrayDeque<StateCSP>(height*width);
	}


	public Game solve(int threads) {
		Game testGame  = new Game(originalGame);
		Shape[][] testBoard = testGame.getBoard();
		checkIfXShapeOnBorder(testBoard);
		
		
		Shape initShape =pickShape(testGame);
		for (int k = 0; k < initShape.getPossibleOrientation().length; k++) {
			boolean b = initShape.getPossibleOrientation()[k];
			if(b) {
				stack = new ArrayDeque<StateCSP>();
				initShape.rotateTo(k);
				int maxStackSize = height*width - countFrozenShape(testGame);
				StateCSP initialState = new StateCSP(testGame,initShape,0);
				stack.push(initialState);
				
				while(!stack.isEmpty() ) {
					StateCSP iteration = stack.peek();
					int i = iteration.getI();
					int j = iteration.getJ();
					Shape shape = iteration.getShape();	
					System.out.println(testGame);
					if(stack.size() == maxStackSize) {
						if(iteration.canRotate()){
							boolean isWellPlaced = false;
							do{
								iteration.rotate();	
							}while(iteration.canRotate() && !shape.getPossibleOrientation()[shape.getOrientation()] );
							if(shape.getPossibleOrientation()[shape.getOrientation()]) {
								isWellPlaced=true;
							}
							if(!isWellPlaced) {
								backtrack(testGame, iteration, stack);
								continue;
							}
						}
						else {
							backtrack(testGame, iteration, stack);
							continue;
						}
						return testGame;
					}
					if(iteration.canRotate()){
						boolean isWellPlaced = false;
						do{
							iteration.rotate();	
						}while(iteration.canRotate() && !shape.getPossibleOrientation()[shape.getOrientation()] );
						if(shape.getPossibleOrientation()[shape.getOrientation()]) {
							isWellPlaced=true;
						}
						if(!isWellPlaced) {
							backtrack(testGame, iteration, stack);
							continue;
						}
					}
					else {
						backtrack(testGame, iteration, stack);
						continue;
					}
					shape.setFroze(true);
					reduceNeighborsDomain(testGame, shape);
					
					Shape nextShape = pickShape(testGame);
					if(nextShape == null) {
						backtrack(testGame, iteration, stack);
						continue;
					}
					StateCSP nextIteration = new StateCSP(testGame,nextShape,0);
					stack.push(nextIteration);
				}
			}
			
		}
		return null;
	}

	
	/**
	 * Check if the board contain XShape on the border of the board
	 * @param board :the board
	 * @return true if board has XShape on border
	 */
	private boolean checkIfXShapeOnBorder(Shape[][] board) {
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.getType() == 4 && (i==0||j==0||i==height-1||j==width-1)) {
					//unsolvable
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Pick the shape that have the less possible orientation left
	 * @param game :the game
	 * @return the shape with the minimum domain remaining (if there are several in this case, pick the first on the board starting at 0,0)
	 */
	private Shape pickShape(Game game) {
		System.out.println(game);
		Shape[][] board = game.getBoard();
		int max = 4;
		Shape goodShape =null;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Shape shape = board[i][j];
				if(!shape.isFroze() ) {
					int nb=shape.getDomainSize();
					if(nb<max) {
						max = nb;
						goodShape = shape;
						if(max == 0) {
							return null;
						}
					}
				}
			}
		}
		return goodShape;
	}


	/**
	 * Do a backtrack when the solver is in a invalid state
	 * @param game : the game
	 * @param iteration :the current iteration
	 * @param stack :the iteration stack
	 */
	private void backtrack(Game game, StateCSP iteration , Deque<StateCSP> stack) {
		Shape shape = iteration.getShape();
		//restore freeze
		shape.setFroze(false);
		//restore neighbors domains
		Shape[] neighbors = game.getNeighbors(shape);

		if(neighbors[Game.NORTH] !=null) {
			neighbors[Game.NORTH].setPossibleOrientation(iteration.getTopDomain());
		}
		if(neighbors[Game.SOUTH] !=null) {
			neighbors[Game.SOUTH].setPossibleOrientation(iteration.getBottomDomain());
		}
		if(neighbors[Game.EAST] !=null) {
			neighbors[Game.EAST].setPossibleOrientation(iteration.getRightDomain());
		}
		if(neighbors[Game.WEST] !=null) {
			neighbors[Game.WEST].setPossibleOrientation(iteration.getLeftDomain());
		}
		shape.setPossibleOrientation(iteration.getShapeDomain());
		shape.removePossibleOrientation(new int[] {shape.getOrientation()});
		if(iteration.canRotate()) {
			return;
		}
		stack.pop();
		
		StateCSP previousIteration= stack.peek();
		if(previousIteration != null) {
			Shape previousShape = previousIteration.getShape();
			shape.setFroze(false);
			//restore neighbors domains
			 neighbors = game.getNeighbors(previousShape);
	
			if(neighbors[Game.NORTH] !=null) {
				neighbors[Game.NORTH].setPossibleOrientation(iteration.getTopDomain());
			}
			if(neighbors[Game.SOUTH] !=null) {
				neighbors[Game.SOUTH].setPossibleOrientation(iteration.getBottomDomain());
			}
			if(neighbors[Game.EAST] !=null) {
				neighbors[Game.EAST].setPossibleOrientation(iteration.getRightDomain());
			}
			if(neighbors[Game.WEST] !=null) {
				neighbors[Game.WEST].setPossibleOrientation(iteration.getLeftDomain());
			}		
		}
	}

	/**
	 * Freeze all the shape that can be and reduce all the domain that can be
	 * @param game :the game to prepare
	 * @return number of shapes frozen
	 * @throws Exception if game is unsolvable
	 */
	private int prepare(Game game) {
		firstFreeze(game);
		int total = 0;
		int nbFreeze =0;
		int shapeWithOneOrientation=0;
		do {
			nbFreeze = refreeze(game);
			total+=nbFreeze;
			System.out.println("New shape froze : "+nbFreeze);
		}while(nbFreeze>0);
		reduceDomainBorder(game);
		reduceDomain(game);
		total+=freezeShapeWithOneOrientation(game);
		do {
			do {
				nbFreeze = refreeze(game);
				total+=nbFreeze;
				System.out.println("New shape froze : "+nbFreeze);
			}while(nbFreeze>0);
			//Reduce domain
			reduceDomain(game);
			//System.out.println("Domain reduce : "+reduceDomain(testGame));
			shapeWithOneOrientation = freezeShapeWithOneOrientation(game);
			total+=shapeWithOneOrientation;
			//System.out.println("Only one orientation remaining : "+shapeWithOneOrientation);
		}while(shapeWithOneOrientation>0);
		System.out.println("Nb freeze :"+total);
		return total;

	}

	/**
	 * Count the frozen shape on the board
	 * @param game : the game
	 * @return the number of frozen shape
	 */
	private int countFrozenShape(Game game) {
		int nb=0;
		for (Shape[] shapes:game.getBoard()){
			for (Shape shape:shapes){
				if(shape.isFroze()) {
					nb++;
				}
			}
		}
		return nb;
	}
	
	/**
	 * Shuffle randomly the board
	 * @param game :the game to shuffle
	 */
	private void shuffle(Game game) {
		//Shuffle
		Random rand = new Random();
		for (Shape[] shapes:game.getBoard()){
			for (Shape shape:shapes){
				if(!shape.isFroze()) {
					for (int i=0;i<rand.nextInt(4);i++) shape.rotate();
				}
			}
		}
	}

	/**
	 * Perform a first freeze on the board. 
	 * Freeze TShape and IShape on border, LShape on corner and XShape and EmptyShape all over the board.
	 * @param game the game to freeze
	 */
	private void firstFreeze(Game game) {
		Shape[][] board = game.getBoard();
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.isFroze()) {

				}
				//TOP BORDER
				else if(i==0) {
					//top left corner
					if(j==0) {
						//LShape
						if(shape.getType()==5) {
							shape.rotateTo(1);
							shape.setFroze(true);
							//Check if neighbors are QShape || LShape
							Shape rightNeighbor = board[i][j+1];
							Shape bottomNeighbor = board[i+1][j];
							if(!rightNeighbor.isFroze() ) {
								if(rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
								else if(rightNeighbor.getType() == 5) {rightNeighbor.rotateTo(2);rightNeighbor.setFroze(true);}
							}
							if(!bottomNeighbor.isFroze() &(bottomNeighbor.getType() == 1 || bottomNeighbor.getType() == 5 )) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
						}
						//EmptyShape
						else if(shape.getType() == 0) {
							shape.setFroze(true);
							//Check if neighbors are TShape || IShape
							Shape rightNeighbor = board[i][j+1];
							Shape bottomNeighbor = board[i+1][j];
							if(!rightNeighbor.isFroze()) {
								if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
								else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
							}
							if(!bottomNeighbor.isFroze()) {
								if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
								else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
							}						
						}

					}
					//right corner
					else if(j==width-1) {
						//LShape
						if(shape.getType()==5) {
							shape.rotateTo(2);
							shape.setFroze(true);
							//Check if neighbors are QShape || IShape || LShape
							Shape leftNeighbor = board[i][j-1];
							Shape bottomNeighbor = board[i+1][j];

							if(!leftNeighbor.isFroze() && (leftNeighbor.getType() == 1  || leftNeighbor.getType() == 5 )) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}

							if(!bottomNeighbor.isFroze()) {
								if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
								else if(bottomNeighbor.getType() == 5) {bottomNeighbor.rotateTo(3);bottomNeighbor.setFroze(true);}
							}
						}
						//EmptyShape
						else if(shape.getType() == 0) {
							shape.setFroze(true);
							//Check if neighbors are TShape || IShape
							Shape leftNeighbor = board[i][j-1];
							Shape bottomNeighbor = board[i+1][j];
							if(!leftNeighbor.isFroze()) {
								if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
								else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
							}
							if(!bottomNeighbor.isFroze()) {
								if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
								else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
							}						
						}
					}

					//TShape on top border
					else if(shape.getType() == 3) {
						shape.rotateTo(2);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape ||IShape
						Shape leftNeighbor = board[i][j-1];
						Shape rightNeighbor = board[i][j+1];
						Shape bottomNeighbor = board[i+1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 1 || leftNeighbor.getType()==5) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
						}
						if(!rightNeighbor.isFroze()) {
							if(rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
							else if(rightNeighbor.getType() == 5) {rightNeighbor.rotateTo(2);rightNeighbor.setFroze(true);}
						}	
						if(!bottomNeighbor.isFroze()) {
							if (bottomNeighbor.getType() == 1 || bottomNeighbor.getType()==2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
						}
					}

					//IShape on top border
					else if(shape.getType() == 2) {
						shape.rotateTo(1);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape
						Shape leftNeighbor = board[i][j-1];
						Shape rightNeighbor = board[i][j+1];
						Shape bottomNeighbor = board[i+1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 1 || leftNeighbor.getType()==5) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
						}
						if(!rightNeighbor.isFroze()) {
							if(rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
							else if(rightNeighbor.getType() == 5) {rightNeighbor.rotateTo(2);rightNeighbor.setFroze(true);}
						}	
						if(!bottomNeighbor.isFroze()) {
							if (bottomNeighbor.getType() ==2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
							if (bottomNeighbor.getType() ==3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
						}
					}
					//EmptyShape on top border
					else if(shape.getType() == 0) {
						shape.setFroze(true);
						//Check if neighbors are TShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape rightNeighbor = board[i][j+1];
						Shape bottomNeighbor = board[i+1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
						}
						if(!rightNeighbor.isFroze()) {
							if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
							else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
						}
						if(!bottomNeighbor.isFroze()) {
							if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
							else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
						}						
					}

				}

				//BOTTOM BORDER
				else if(i==height-1) {
					//bottom left corner
					if(j==0) {
						//LShape
						if(shape.getType()==5) {
							shape.rotateTo(0);
							shape.setFroze(true);
							//Check if neighbors are QShape || IShape || LShape
							Shape rightNeighbor = board[i][j+1];
							Shape topNeighbor = board[i-1][j];
							if(!rightNeighbor.isFroze() ) {
								if(rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
								else if(rightNeighbor.getType()==5) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
							}
							if(!topNeighbor.isFroze()) {
								if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
								else if(topNeighbor.getType() == 5){topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
							}
						}
						//EmptyShape
						else if(shape.getType() == 0) {
							shape.setFroze(true);
							//Check if neighbors are TShape || IShape
							Shape rightNeighbor = board[i][j+1];
							Shape topNeighbor = board[i-1][j];
							if(!rightNeighbor.isFroze()) {
								if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
								else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
							}
							if(!topNeighbor.isFroze()) {
								if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
								else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
							}
						}			
					}
					//bottom right corner
					else if(j==width-1) {
						//LShape
						if(shape.getType()==5) {
							shape.rotateTo(3);
							shape.setFroze(true);
							//Check if neighbors are QShape || IShape || LShape
							Shape leftNeighbor = board[i][j-1];
							Shape topNeighbor = board[i-1][j];
							if(!leftNeighbor.isFroze() ) {
								if(leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
								else if(leftNeighbor.getType()==5) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
							}
							if(!topNeighbor.isFroze()) {
								if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
								else if(topNeighbor.getType() == 5){topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
							}
						}
						//EmptyShape
						else if(shape.getType() == 0) {
							shape.setFroze(true);
							//Check if neighbors are TShape || IShape
							Shape leftNeighbor = board[i][j-1];
							Shape topNeighbor = board[i-1][j];
							if(!leftNeighbor.isFroze()) {
								if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
								else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
							}	
							if(!topNeighbor.isFroze()) {
								if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
								else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
							}

						}
					}

					//TShape on bottom border
					else if(shape.getType() == 3) {
						shape.rotateTo(0);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape rightNeighbor = board[i][j+1];
						Shape topNeighbor = board[i-1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 5) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);} 
						}
						if(!rightNeighbor.isFroze()) {
							if(rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
							else if(rightNeighbor.getType() == 5) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
						}
					}

					//IShape on bottom border
					else if(shape.getType() == 2) {
						shape.rotateTo(1);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape rightNeighbor = board[i][j+1];
						Shape topNeighbor = board[i-1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 5) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);} 
						}
						if(!rightNeighbor.isFroze()) {
							if(rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
							else if(rightNeighbor.getType() == 5) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
						}	
					}
					//EmptyShape on the bottom border
					else if(shape.getType() == 0) {
						shape.setFroze(true);
						//Check if neighbors are TShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape rightNeighbor = board[i][j+1];
						Shape topNeighbor = board[i-1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
						}
						if(!rightNeighbor.isFroze()) {
							if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
							else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
						}
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
						}

					}


				}

				//LEFT BORDER
				else if(j==0) {
					//TShape on left border
					if(shape.getType() == 3) {
						shape.rotateTo(1);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape || IShape
						Shape rightNeighbor = board[i][j+1];
						Shape bottomtNeighbor = board[i+1][j];
						Shape topNeighbor = board[i-1][j];
						if(!rightNeighbor.isFroze()) {
							if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);}
							else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
						}
						if(!bottomtNeighbor.isFroze()) {
							if(bottomtNeighbor.getType() == 1) {bottomtNeighbor.rotateTo(0);bottomtNeighbor.setFroze(true);}
							else if(bottomtNeighbor.getType() == 5) {bottomtNeighbor.rotateTo(0);bottomtNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 5) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
						}
					}

					//IShape on left border
					else if(shape.getType() == 2) {
						shape.rotateTo(0);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape || IShape
						Shape rightNeighbor = board[i][j+1];
						Shape bottomNeighbor = board[i+1][j];
						Shape topNeighbor = board[i-1][j];
						if(!rightNeighbor.isFroze()) {
							if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
							else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
						}
						if(!bottomNeighbor.isFroze()) {
							if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
							else if(bottomNeighbor.getType() == 5) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 5) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
						}	
					}
					//EmptyShape on left border
					else if(shape.getType() == 0) {
						shape.setFroze(true);
						//Check if neighbors are TShape || IShape
						Shape rightNeighbor = board[i][j+1];
						Shape bottomNeighbor = board[i+1][j];
						Shape topNeighbor = board[i-1][j];
						if(!rightNeighbor.isFroze()) {
							if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
							else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
						}
						if(!bottomNeighbor.isFroze()) {
							if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
							else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
						}

					}
				}

				//RIGHT BORDER
				else if(j==width-1) {
					//TShape on right border
					if(shape.getType() == 3) {
						shape.rotateTo(3);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape bottomNeighbor = board[i+1][j];
						Shape topNeighbor = board[i-1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);} 
						}
						if(!bottomNeighbor.isFroze()) {
							if(bottomNeighbor.getType() == 5) {bottomNeighbor.rotateTo(3);bottomNeighbor.setFroze(true);}
							else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 5) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
						}
					}

					//IShape on right border
					else if(shape.getType() == 2) {
						shape.rotateTo(0);
						shape.setFroze(true);
						//Check if neighbors are QShape || LShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape bottomNeighbor = board[i+1][j];
						Shape topNeighbor = board[i-1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
						}
						if(!bottomNeighbor.isFroze()) {
							if(bottomNeighbor.getType() == 5) {bottomNeighbor.rotateTo(3);bottomNeighbor.setFroze(true);}
							else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 5) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
						}
					}
					//EmptyShape on right border
					else if(shape.getType() == 0) {
						shape.setFroze(true);
						//Check if neighbors are TShape || IShape
						Shape leftNeighbor = board[i][j-1];
						Shape bottomNeighbor = board[i+1][j];
						Shape topNeighbor = board[i-1][j];
						if(!leftNeighbor.isFroze()) {
							if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
							else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
						}
						if(!bottomNeighbor.isFroze()) {
							if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
							else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
						}	
						if(!topNeighbor.isFroze()) {
							if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
							else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
						}

					}
				}

				//XShape in the middle
				else if(shape.getType() == 4) {
					shape.setFroze(true);
					//Check if neighbors are QShape || IShape
					Shape leftNeighbor = board[i][j-1];
					Shape rightNeighbor = board[i][j+1];
					Shape bottomNeighbor = board[i+1][j];
					Shape topNeighbor = board[i-1][j];
					if(!leftNeighbor.isFroze()) {
						if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);}
						else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);} 
					}
					if(!rightNeighbor.isFroze()) {
						if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);}
						else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);} 
					}
					if(!bottomNeighbor.isFroze()) {
						if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
						else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);}
					}	
					if(!topNeighbor.isFroze()) {
						if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
						else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);}
					}

				}
				//EmptyShape in the middle
				else if(shape.getType() == 0) {
					shape.setFroze(true);
					//Check if neighbors are TShape || IShape
					Shape leftNeighbor = board[i][j-1];
					Shape rightNeighbor = board[i][j+1];
					Shape bottomNeighbor = board[i+1][j];
					Shape topNeighbor = board[i-1][j];
					if(!leftNeighbor.isFroze()) {
						if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);}
						else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);} 
					}
					if(!rightNeighbor.isFroze()) {
						if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);}
						else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);} 
					}
					if(!bottomNeighbor.isFroze()) {
						if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);}
						else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);}
					}	
					if(!topNeighbor.isFroze()) {
						if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);}
						else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);}
					}

				}

			}
		}
	}

	/**
	 * Freeze all the shape from the previously frozen shapes
	 * @param game :the game
	 * @return the number of shape frozen
	 * @throws Exception if game is unsolvable
	 */
	private int refreeze(Game game) {
		Shape[][] board = game.getBoard();
		int frozen = 0;
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.isFroze()) {
					//QShape
					if(shape.getType() == 1) {
						frozen += freezeQShapeNeighbors(board,  i, j, shape);
					}
					//TShape
					if(shape.getType() == 3) {
						frozen += freezeTShapeNeighbors(board, i, j, shape);
					}
					//IShape
					else if(shape.getType() == 2) {
						frozen += freezeIShapeNeighbors(board, i, j, shape);
					}
					//LShape
					else if(shape.getType() == 5) {
						frozen += freezeLShapeNeighbors(board, i, j, shape);
					}
				}			
				else if(shape.getType()==5) {
					frozen += freezeLShapeOrIShape(game, i, j, shape);
				}
				else if(shape.getType()==1) {
					frozen += freezeQShapeOrTShape(game, i, j, shape);
				}
				else if(shape.getType()==2) {
					frozen += freezeLShapeOrIShape(game, i, j, shape);
				}

				else if(shape.getType()==3) {
					frozen += freezeQShapeOrTShape(game, i, j, shape);
				}
			}
		}
		return frozen;
	}

	/**
	 * Freeze LShape or IShape in the good orientation when they are surounded by two other frozen shapes
	 * @param game the game
	 * @param i :coordinate i of the shape
	 * @param j :coordinate i of the shape
	 * @param shape :the shape
	 * @return number of LShape and IShape frozen
	 * @throws Exception if game is unsolvable
	 */
	private int freezeLShapeOrIShape(Game game, int i, int j, Shape shape) {
		Shape[][] board = game.getBoard();
		Shape leftNeighbor = null;
		Shape rightNeighbor = null;
		Shape bottomNeighbor = null;
		Shape topNeighbor = null;
		if(j-1>=0) {
			leftNeighbor = board[i][j-1];
		}
		if(j+1<width) {
			rightNeighbor = board[i][j+1];
		}
		if(i+1<height) {
			bottomNeighbor = board[i+1][j];
		}
		if(i-1>=0) {
			topNeighbor = board[i-1][j];
		}
		if(topNeighbor!=null && rightNeighbor!= null && topNeighbor.isFroze() && rightNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndEast(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor!=null && leftNeighbor!= null && topNeighbor.isFroze() && leftNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor!=null && rightNeighbor!= null && bottomNeighbor.isFroze() && rightNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithSouthAndEast(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor!=null && leftNeighbor!= null && bottomNeighbor.isFroze() && leftNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithSouthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}	
		else if( shape.getType() == 2) {
			if (bottomNeighbor!=null && topNeighbor!= null && bottomNeighbor.isFroze() && topNeighbor.isFroze()){
				while(!game.isShapeWellConnectedWithNorthAndSouth(shape)) {
					shape.rotate();
				}
				shape.setFroze(true);
				return 1;
			}	
			if (leftNeighbor!=null && rightNeighbor!= null && leftNeighbor.isFroze() && rightNeighbor.isFroze()){
				while(!game.isShapeWellConnectedWithEastAndWest(shape)) {
					shape.rotate();
				}
				shape.setFroze(true);
				return 1;
			}	
		}

		return 0;
	}

	/**
	 * Freeze QShape or TShape in the good orientation when they are surounded by three other frozen shapes
	 * @param game the game
	 * @param i :coordinate i of the shape
	 * @param j coordinate i of the shape
	 * @param shape :the shape
	 * @return number of QShape and TShape frozen
	 * @throws Exception if game is unsolvable
	 */
	private int freezeQShapeOrTShape(Game game, int i, int j, Shape shape) {
		Shape leftNeighbor = null;
		Shape rightNeighbor = null;
		Shape bottomNeighbor = null;
		Shape topNeighbor = null;
		if(j-1>=0) {
			leftNeighbor = board[i][j-1];
		}
		if(j+1<width) {
			rightNeighbor = board[i][j+1];
		}
		if(i+1<height) {
			bottomNeighbor = board[i+1][j];
		}
		if(i-1>=0) {
			topNeighbor = board[i-1][j];
		}
		if(topNeighbor!=null && rightNeighbor!= null && leftNeighbor!=null && topNeighbor.isFroze() && rightNeighbor.isFroze()&&leftNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndEastAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor!=null && bottomNeighbor!= null && leftNeighbor!=null && topNeighbor.isFroze() && leftNeighbor.isFroze()&& bottomNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndSouthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor!=null && rightNeighbor!= null && bottomNeighbor!=null && bottomNeighbor.isFroze() && rightNeighbor.isFroze() && topNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndEastAndSouth(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor!=null && rightNeighbor!= null && leftNeighbor!=null && bottomNeighbor.isFroze() && leftNeighbor.isFroze() && rightNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithEastAndSouthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		return 0;
	}

	/**
	 * Freeze the neighbors of a frozen QShape (those that have only one position possible)
	 * @param board the board
	 * @param i :coordinate i of the shape
	 * @param j :coordinate j of the shape
	 * @param shape :the QShape
	 * @return number of QShape neighbors frozen
	 */
	private int freezeQShapeNeighbors(Shape[][] board, int i, int j, Shape shape) {
		int frozen =0;
		int orientation = shape.getOrientation();
		Shape leftNeighbor = null;
		Shape rightNeighbor = null;
		Shape bottomNeighbor = null;
		Shape topNeighbor = null;
		if(j-1>=0) {
			leftNeighbor = board[i][j-1];
		}
		if(j+1<width) {
			rightNeighbor = board[i][j+1];
		}
		if(i+1<height) {
			bottomNeighbor = board[i+1][j];
		}
		if(i-1>=0) {
			topNeighbor = board[i-1][j];
		}
		if(orientation == 0) {
			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
			return frozen;
		}
		else if(orientation == 1) {
			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(orientation == 2) {
			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(orientation == 3) {
			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		return frozen;
	}

	/**
	 * Freeze the neighbors of a frozen TShape (those that have only one position possible)
	 * @param board the board
	 * @param i :coordinate i of the shape
	 * @param j :coordinate j of the shape
	 * @param shape :the TShape
	 * @return number of TShape neighbors frozen
	 */
	private int freezeTShapeNeighbors(Shape[][] board,  int i, int j, Shape shape) {
		int frozen =0;
		int orientation = shape.getOrientation();
		Shape leftNeighbor = null;
		Shape rightNeighbor = null;
		Shape bottomNeighbor = null;
		Shape topNeighbor = null;
		if(j-1>=0) {
			leftNeighbor = board[i][j-1];
		}
		if(j+1<width) {
			rightNeighbor = board[i][j+1];
		}
		if(i+1<height) {
			bottomNeighbor = board[i+1][j];
		}
		if(i-1>=0) {
			topNeighbor = board[i-1][j];
		}
		if(orientation == 0) {
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		if(orientation == 1) {
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(orientation == 2) {
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(orientation == 3) {
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		return frozen;
	}

	/**
	 * Freeze the neighbors of a frozen LShape (those that have only one position possible)
	 * @param board the board
	 * @param i :coordinate i of the shape
	 * @param j :coordinate j of the shape
	 * @param shape :the LShape
	 * @return number of LShape neighbors frozen
	 */
	private int freezeLShapeNeighbors(Shape[][] board, int i, int j, Shape shape) {
		int frozen =0;
		int orientation = shape.getOrientation();
		Shape leftNeighbor = null;
		Shape rightNeighbor = null;
		Shape bottomNeighbor = null;
		Shape topNeighbor = null;
		if(j-1>=0) {
			leftNeighbor = board[i][j-1];
		}
		if(j+1<width) {
			rightNeighbor = board[i][j+1];
		}
		if(i+1<height) {
			bottomNeighbor = board[i+1][j];
		}
		if(i-1>=0) {
			topNeighbor = board[i-1][j];
		}
		if(orientation == 0) {

			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
			return frozen;
		}
		else if(orientation == 1) {

			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(orientation == 2) {

			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(orientation == 3) {

			if(leftNeighbor !=null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor !=null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor !=null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor !=null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		return frozen;
	}

	/**
	 * Freeze the neighbors of a frozen IShape (those that have only one position possible)
	 * @param board the board
	 * @param i :coordinate i of the shape
	 * @param j :coordinate j of the shape
	 * @param shape :the IShape
	 * @return number of IShape neighbors frozen
	 */
	private int freezeIShapeNeighbors(Shape[][] board, int i, int j, Shape shape) {
		int frozen =0;
		int orientation = shape.getOrientation();
		Shape leftNeighbor = null;
		Shape rightNeighbor = null;
		Shape bottomNeighbor = null;
		Shape topNeighbor = null;
		if(j-1>=0) {
			leftNeighbor = board[i][j-1];
		}
		if(j+1<width) {
			rightNeighbor = board[i][j+1];
		}
		if(i+1<height) {
			bottomNeighbor = board[i+1][j];
		}
		if(i-1>=0) {
			topNeighbor = board[i-1][j];
		}
		if(orientation == 0) {
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else {
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}

		return frozen;
	}

	/**
	 * Reduce the domain of all the shapes according to their possible orientation 
	 * @param game :the game
	 * @return number of domain reduce
	 */
	private int reduceDomain(Game game) {
		Shape[][] board = game.getBoard();
		int nb = 0;
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.isFroze()) {
					nb+=reduceNeighborsDomain(game, shape);
				}
			}
		}
		return nb;
	}

	/**
	 * Reduce the domain of shape's neighbors
	 * @param game : the game
	 * @param shape : the shape
	 * @return number of domain reduce
	 */
	private int reduceNeighborsDomain(Game game,Shape shape) {
		Shape[][] board = game.getBoard();
		int i = shape.getI();
		int j = shape.getJ();
		int nb=0;
		if(shape.isFroze()) {
			Shape leftNeighbor = null;
			Shape rightNeighbor = null;
			Shape bottomNeighbor = null;
			Shape topNeighbor = null;
			if(j-1>=0) {
				leftNeighbor = board[i][j-1];
			}
			if(j+1<width) {
				rightNeighbor = board[i][j+1];
			}
			if(i+1<height) {
				bottomNeighbor = board[i+1][j];
			}
			if(i-1>=0) {
				topNeighbor = board[i-1][j];
			}
			//LEFT NEIGHBOR
			if(leftNeighbor != null && !leftNeighbor.isFroze()) {
				int type = leftNeighbor.getType();
				if(shape.getConnections()[WEST]) {
					switch(type) {
					case 1: leftNeighbor.removePossibleOrientation(new int[]{0,2,3});nb++;break;
					case 2: leftNeighbor.removePossibleOrientation(new int[]{0});nb++;break;
					case 3: leftNeighbor.removePossibleOrientation(new int[]{3});nb++;break;
					case 5:	leftNeighbor.removePossibleOrientation(new int[]{2,3});nb++;break;	
					}
				}
				else {
					switch(type) {
					case 1: leftNeighbor.removePossibleOrientation(new int[]{1});nb++;break;
					case 2: leftNeighbor.removePossibleOrientation(new int[]{1});nb++;break;
					case 3: leftNeighbor.removePossibleOrientation(new int[]{0,1,2});nb++;break;
					case 5:	leftNeighbor.removePossibleOrientation(new int[]{0,1});nb++;break;	
					}
				}
			}
			//RIGHT NEIGHBOR
			if(rightNeighbor != null && !rightNeighbor.isFroze()) {
				int type = rightNeighbor.getType();
				if(shape.getConnections()[EAST]) {
					switch(type) {
					case 1: rightNeighbor.removePossibleOrientation(new int[]{0,1,2});nb++;break;
					case 2: rightNeighbor.removePossibleOrientation(new int[]{0});nb++;break;
					case 3: rightNeighbor.removePossibleOrientation(new int[]{1});nb++;break;	
					case 5:	rightNeighbor.removePossibleOrientation(new int[]{0,1});nb++;break;			
					}
				}
				else {
					switch(type) {
					case 1: rightNeighbor.removePossibleOrientation(new int[]{3});nb++;break;	
					case 2: rightNeighbor.removePossibleOrientation(new int[]{1});nb++;break;
					case 3: rightNeighbor.removePossibleOrientation(new int[]{0,2,3});nb++;break;
					case 5:	rightNeighbor.removePossibleOrientation(new int[]{2,3});
					}
				}
			}
			//BOTTOM NEIGHBOR
			if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
				int type = bottomNeighbor.getType();
				if(shape.getConnections()[SOUTH]) {
					switch(type) {
					case 1: bottomNeighbor.removePossibleOrientation(new int[]{1,2,3});nb++;break;
					case 2: bottomNeighbor.removePossibleOrientation(new int[]{1});nb++;break;
					case 3: bottomNeighbor.removePossibleOrientation(new int[]{2});nb++;break;	
					case 5:	bottomNeighbor.removePossibleOrientation(new int[]{1,2});nb++;break;			
					}
				}
				else {
					switch(type) {	
					case 1: bottomNeighbor.removePossibleOrientation(new int[]{0});nb++;break;	
					case 2: bottomNeighbor.removePossibleOrientation(new int[]{0});nb++;break;
					case 3: bottomNeighbor.removePossibleOrientation(new int[]{0,1,3});nb++;break;
					case 5:	bottomNeighbor.removePossibleOrientation(new int[]{0,3});nb++;break;	
					}
				}
			}
			//TOP NEIGHBOR
			if(topNeighbor != null && !topNeighbor.isFroze()) {
				int type = topNeighbor.getType();
				if(shape.getConnections()[NORTH]) {
					switch(type) {
					case 1: topNeighbor.removePossibleOrientation(new int[]{0,1,3});nb++;break;
					case 2: topNeighbor.removePossibleOrientation(new int[]{1});nb++;break;
					case 3: topNeighbor.removePossibleOrientation(new int[]{0});nb++;break;	
					case 5:	topNeighbor.removePossibleOrientation(new int[]{0,3});nb++;break;			
					}
				}
				else {
					switch(type) {
					case 1: topNeighbor.removePossibleOrientation(new int[]{2});nb++;break;	
					case 2: topNeighbor.removePossibleOrientation(new int[]{0});nb++;break;
					case 3: topNeighbor.removePossibleOrientation(new int[]{1,2,3});nb++;break;	
					case 5:	topNeighbor.removePossibleOrientation(new int[]{1,2});nb++;break;	
					}
				}
			}
		}
		return nb;
	}

	/**
	 * Reduce the domain of all the shapes on the board border 
	 * @param game :the game
	 * @return number of domain reduce
	 */
	private int reduceDomainBorder(Game game) {
		Shape[][] board = game.getBoard();
		int nb = 0;
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(!shape.isFroze()) {
					int shapeType = shape.getType();
					if(i==0) {						
						switch(shapeType) {
						case 1 :shape.removePossibleOrientation(new int[]{0});break;
						case 5 :shape.removePossibleOrientation(new int[]{0,3});break;
						}			
					}
					else if(j==0) {
						switch(shapeType) {
						case 1 :shape.removePossibleOrientation(new int[]{3});break;
						case 5 :shape.removePossibleOrientation(new int[]{2,3});break;
						}
					}
					else if(i==height-1) {
						switch(shapeType) {
						case 1 :shape.removePossibleOrientation(new int[]{2});break;
						case 5 :shape.removePossibleOrientation(new int[]{1,2});break;
						}
					}

					else if(j==width-1) {
						switch(shapeType) {
						case 1 :shape.removePossibleOrientation(new int[]{1});break;
						case 5 :shape.removePossibleOrientation(new int[]{0,1});break;
						}

					}
				}

			}
		}
		return nb;
	}

	/**
	 * Freeze shapes that only have one possible orientation left
	 * @param game :the game
	 * @return number of shape frozen
	 */
	private int freezeShapeWithOneOrientation(Game game) {
		Shape[][] board = game.getBoard();
		int nb=0;
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.isFroze()) {
					continue;
				}
				int nbO = 0;
				for(boolean b : shape.possibleOrientation) {
					if(b==true) {nbO++;}
				}
				if(nbO==1) {
					while(!game.isShapeWellConnectedWithFrozenNeighbors(shape) || game.iShapeConnectedToBoardBorder(shape)) {
						shape.rotate();
					}
					shape.setFroze(true);
					nb++;
				}
			}		
		}
		return nb;
	}

}
