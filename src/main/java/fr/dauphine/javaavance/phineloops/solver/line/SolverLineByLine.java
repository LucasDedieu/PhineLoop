package fr.dauphine.javaavance.phineloops.solver.line;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import fr.dauphine.javaavance.phineloops.controller.ClusterManager;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class SolverLineByLine {
	protected static int NORTH = 0;
	protected static int EAST = 1;
	protected static int SOUTH = 2;
	protected static int WEST = 3;
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


	public Game solve(int threads) {
		Game testGame  = new Game(originalGame);
		Shape[][] testBoard = testGame.getBoard();

		//Is game solvable
		if(checkIfXShapeOnBorder(testBoard)) {
			return null;
		}

		long startTime = System.currentTimeMillis();
		//Freeze all shapes that have only one possible orientation
		try {
			prepare(testGame);
		} catch (Exception e) {
			return null;
		}
		long deltaTime = System.currentTimeMillis()-startTime;
		System.out.println("Freeze time :"+deltaTime+" ms");
		
		
		//Find cluster
		startTime = System.currentTimeMillis();
		ClusterManager.getInstance().findClusters(testGame);
		deltaTime = System.currentTimeMillis()-startTime;
		System.out.println("Find Cluster time :"+deltaTime+" ms");
		
		//Prepare games
		startTime = System.currentTimeMillis();
		Set<Game> games = ClusterManager.getInstance().getClusterGames(testGame);
		deltaTime = System.currentTimeMillis()-startTime;
		System.out.println("Delimit games times :"+deltaTime+" ms");

		//Prepare producer
		BlockingQueue<Game> queue = new LinkedBlockingQueue<Game>(games);
		CountDownLatch latch = new CountDownLatch(games.size());

		startTime = System.currentTimeMillis();
		//Start consumers
		for(int i =0; i<threads; i++) {
			Thread consumer = new Thread(new LineByLineConsumer(queue, latch));
			consumer.start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		deltaTime = System.currentTimeMillis() - startTime;
		System.out.println("Solving time :"+deltaTime+" ms");
		return testGame;


		/*
		//Solve all games
		for(Game game : games) {
			try {
				if(!solveCluster(game)) {
					return null;
				}
			}
			//case unsolvable
			catch(Exception e) {
				return null;
			}
		}
		return testGame;
		 */
	}



	private boolean solveCluster(Game game) {
		//System.out.println(game);
		int height = game.getHeight();
		int width = game.getWidth();
		Shape[][] board = game.getBoard(); 
		int i =height-1;
		int j=width-1;
		StateLineByLine initialState = new StateLineByLine(i,j,0);
		stack.push(initialState);		
		while(!stack.isEmpty() ) {
			/*
			if(height<=128 && width <=128) {
				if(System.currentTimeMillis()-startTime>8000) {
					return false;
				}
			}*/
			StateLineByLine iteration = stack.peek();
			i = iteration.getI();
			j = iteration.getJ();
			Shape shape = board[i][j];
			//Case frozen shape

			if(shape.isFroze()) {
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
				if(shape.isFroze()) {
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

	private int prepare(Game game) throws Exception {
		firstFreeze(game);
		int total = 0;
		int nbFreeze =0;
		int shapeWithOneOrientation=0;
		do {
			nbFreeze = refreeze(game);
			total+=nbFreeze;
			//System.out.println("New shape froze : "+nbFreeze);
		}while(nbFreeze>10);
		reduceDomainBorder(game);
		reduceDomain(game);
		total+=freezeShapeWithOneOrientation(game);
		do {
			do {
				nbFreeze = refreeze(game);
				total+=nbFreeze;
				//System.out.println("New shape froze : "+nbFreeze);
			}while(nbFreeze>10);
			//Reduce domain
			reduceDomain(game);
			//System.out.println("Domain reduce : "+reduceDomain(testGame));
			shapeWithOneOrientation = freezeShapeWithOneOrientation(game);
			total+=shapeWithOneOrientation;
			//System.out.println("Only one orientation remaining : "+shapeWithOneOrientation);
		}while(shapeWithOneOrientation>0);
		//System.out.println("Nb freeze :"+total);
		return total;
	}

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

	private int refreeze(Game game) throws Exception {
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

	private int freezeLShapeOrIShape(Game game, int i, int j, Shape shape) throws Exception {
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
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithNorthAndEast(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithNorthAndEast(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor!=null && leftNeighbor!= null && topNeighbor.isFroze() && leftNeighbor.isFroze()){
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithNorthAndWest(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithNorthAndWest(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor!=null && rightNeighbor!= null && bottomNeighbor.isFroze() && rightNeighbor.isFroze()){
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithSouthAndEast(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithSouthAndEast(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor!=null && leftNeighbor!= null && bottomNeighbor.isFroze() && leftNeighbor.isFroze()){
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithSouthAndWest(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithSouthAndWest(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}	
		else if( shape.getType() == 2) {
			if (bottomNeighbor!=null && topNeighbor!= null && bottomNeighbor.isFroze() && topNeighbor.isFroze()){
				int r=0;
				while(r<4 && !game.isShapeWellConnectedWithNorthAndSouth(shape)) {
					shape.rotate();
					r++;
				}
				if(!game.isShapeWellConnectedWithNorthAndSouth(shape)) {
					throw new Exception("unsolvable");
				}
				shape.setFroze(true);
				return 1;
			}	
			if (leftNeighbor!=null && rightNeighbor!= null && leftNeighbor.isFroze() && rightNeighbor.isFroze()){
				int r=0;
				while(r<4 && !game.isShapeWellConnectedWithEastAndWest(shape)) {
					shape.rotate();
					r++;
				}
				if(!game.isShapeWellConnectedWithEastAndWest(shape)) {
					throw new Exception("unsolvable");
				}
				shape.setFroze(true);
				return 1;
			}	
		}

		return 0;
	}

	private int freezeQShapeOrTShape(Game game, int i, int j, Shape shape) throws Exception {
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
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithNorthAndEastAndWest(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithNorthAndEastAndWest(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor!=null && bottomNeighbor!= null && leftNeighbor!=null && topNeighbor.isFroze() && leftNeighbor.isFroze()&& bottomNeighbor.isFroze()){
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithNorthAndSouthAndWest(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithNorthAndSouthAndWest(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor!=null && rightNeighbor!= null && bottomNeighbor!=null && bottomNeighbor.isFroze() && rightNeighbor.isFroze() && topNeighbor.isFroze()){
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithNorthAndEastAndSouth(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithNorthAndEastAndSouth(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor!=null && rightNeighbor!= null && leftNeighbor!=null && bottomNeighbor.isFroze() && leftNeighbor.isFroze() && rightNeighbor.isFroze()){
			int r=0;
			while(r<4 && !game.isShapeWellConnectedWithEastAndSouthAndWest(shape)) {
				shape.rotate();
				r++;
			}
			if(!game.isShapeWellConnectedWithEastAndSouthAndWest(shape)) {
				throw new Exception("unsolvable");
			}
			shape.setFroze(true);
			return 1;
		}
		return 0;
	}

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

	private int reduceDomain(Game game) {
		Shape[][] board = game.getBoard();
		int nb = 0;
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
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
							case 3: leftNeighbor.removePossibleOrientation(new int[]{3});nb++;break;
							case 5:	leftNeighbor.removePossibleOrientation(new int[]{2,3});nb++;break;	
							}
						}
						else {
							switch(type) {
							case 1: leftNeighbor.removePossibleOrientation(new int[]{1});nb++;break;	
							case 5:	leftNeighbor.removePossibleOrientation(new int[]{0,1});nb++;break;	
							}
						}
					}
					//RIGHT NEIGHBOR
					if(rightNeighbor != null && !rightNeighbor.isFroze()) {
						int type = rightNeighbor.getType();
						if(shape.getConnections()[EAST]) {
							switch(type) {
							case 3: rightNeighbor.removePossibleOrientation(new int[]{1});nb++;break;	
							case 5:	rightNeighbor.removePossibleOrientation(new int[]{0,1});nb++;break;			
							}
						}
						else {
							switch(type) {
							case 1: rightNeighbor.removePossibleOrientation(new int[]{3});nb++;break;	
							case 5:	rightNeighbor.removePossibleOrientation(new int[]{2,3});
							}
						}
					}
					//BOTTOM NEIGHBOR
					if(bottomNeighbor != null && !bottomNeighbor.isFroze()) {
						int type = bottomNeighbor.getType();
						if(shape.getConnections()[SOUTH]) {
							switch(type) {
							case 3: bottomNeighbor.removePossibleOrientation(new int[]{2});nb++;break;	
							case 5:	bottomNeighbor.removePossibleOrientation(new int[]{1,2});nb++;break;			
							}
						}
						else {
							switch(type) {
							case 1: bottomNeighbor.removePossibleOrientation(new int[]{0});nb++;break;	
							case 5:	bottomNeighbor.removePossibleOrientation(new int[]{0,3});nb++;break;	
							}
						}
					}
					//TOP NEIGHBOR
					if(topNeighbor != null && !topNeighbor.isFroze()) {
						int type = topNeighbor.getType();
						if(shape.getConnections()[NORTH]) {
							switch(type) {
							case 3: topNeighbor.removePossibleOrientation(new int[]{0});nb++;break;	
							case 5:	topNeighbor.removePossibleOrientation(new int[]{0,3});nb++;break;			
							}
						}
						else {
							switch(type) {
							case 1: topNeighbor.removePossibleOrientation(new int[]{2});nb++;break;	
							case 5:	topNeighbor.removePossibleOrientation(new int[]{1,2});nb++;break;	
							}
						}
					}
				}
			}
		}
		return nb;
	}

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
