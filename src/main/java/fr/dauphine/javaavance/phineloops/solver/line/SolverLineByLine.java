package fr.dauphine.javaavance.phineloops.solver.line;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;

import fr.dauphine.javaavance.phineloops.controller.ThreadController;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

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
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.getType() == 4 && (i==0||j==0||i==height-1||j==width-1)) {
					//unsolvable
					return null;
				}
			}
		}
		//Freeze shapes
		firstFreeze(testGame);
		int nbFreeze =0;
		do {
			nbFreeze = refreeze(testGame);
			System.out.println("New shape froze : "+nbFreeze);
		}while(nbFreeze>0);
		int n = lastFreeze(testGame);
		System.out.println("New shape froze : "+n);

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

			/*
			//Print 
			if(nb%100000000==0) {
				System.out.println("itération :"+nb+"  stack :"+stack.size()+"\n"+testGame);
			}
			 */

			//Case frozen shape
			if(shape.isFroze()) {
				stack.pop();
				if(!testGame.isShapeWellConnectedWithSouthAndEast(shape)) {
					hasPop =true;
					continue;
				}
			}


			/*
			else if(!hasPop && !testGame.iShapeConnectedToBoardBorder(shape)  &&  testGame.isShapeWellConnectedWithSouthAndEast(shape)&& testGame.isShapeWellConnectedWithFrozenNeighbors(shape)) {
				//empty
			}*/
			//Can rotate ?		
			else if(iteration.canRotate(shape)) {
				boolean isWellPlaced = false;
				do{
					iteration.rotate(shape);
					if(!testGame.iShapeConnectedToBoardBorder(shape)  &&  testGame.isShapeWellConnectedWithSouthAndEast(shape) && testGame.isShapeWellConnectedWithFrozenNeighbors(shape)) {
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

			hasPop = false;
			//Case last shape of the board
			if(i==0 && j ==0) {
				if(testGame.isShapeFullyConnected(shape)) {
					return testGame;
				}
				stack.pop();
				hasPop=true;
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



	private void firstFreeze(Game game) {
		Shape[][] board = game.getBoard();
		for(int i = 0; i<height;i++) {
			for(int j = 0; j<width;j++) {
				Shape shape = board[i][j];
				if(shape.isFroze()) {

				}
				//TOP BORDER
				else if(i==0) {
					//LShape on top left corner
					if(j==0) {

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

					}
					//LShape on top right corner
					else if(j==width-1) {
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
					//LShape on bottom left corner
					if(j==0) {
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
					}
					//LShape on bottom right corner
					else if(j==width-1) {
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
					//EmptyShape in the middle
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


	private int refreeze(Game game) {
		Shape[][] board = game.getBoard();
		int frozen = 0;
		for(int i = 1; i<height-1;i++) {
			for(int j = 1; j<width-1;j++) {
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
				/*
				else if(shape.getType()==3) {
					frozen += freezeQShapeOrTShape(game, i, j, shape);
				}*/
			}
		}
		return frozen;
	}

	private int freezeLShapeOrIShape(Game game, int i, int j, Shape shape) {
		Shape[][] board = game.getBoard();
		Shape leftNeighbor = board[i][j-1];
		Shape rightNeighbor = board[i][j+1];
		Shape bottomNeighbor = board[i+1][j];
		Shape topNeighbor = board[i-1][j];
		if(topNeighbor.isFroze() && rightNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndEast(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor.isFroze() && leftNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor.isFroze() && rightNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithSouthAndEast(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor.isFroze() && leftNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithSouthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}	
		return 0;
	}
	
	private int freezeQShapeOrTShape(Game game, int i, int j, Shape shape) {
		Shape[][] board = game.getBoard();
		Shape leftNeighbor = board[i][j-1];
		Shape rightNeighbor = board[i][j+1];
		Shape bottomNeighbor = board[i+1][j];
		Shape topNeighbor = board[i-1][j];
		if(topNeighbor.isFroze() && rightNeighbor.isFroze()&&leftNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndEastAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(topNeighbor.isFroze() && leftNeighbor.isFroze()&& bottomNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndSouthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor.isFroze() && rightNeighbor.isFroze() && topNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithNorthAndEastAndSouth(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		else if(bottomNeighbor.isFroze() && leftNeighbor.isFroze() && rightNeighbor.isFroze()){
			while(!game.isShapeWellConnectedWithEastAndSouthAndWest(shape)) {
				shape.rotate();
			}
			shape.setFroze(true);
			return 1;
		}
		return 0;
	}
	
	
	private int freezeQShapeNeighbors(Shape[][] board, int i, int j, Shape shape) {
		int frozen =0;
		Shape leftNeighbor = board[i][j-1];
		Shape rightNeighbor = board[i][j+1];
		Shape bottomNeighbor = board[i+1][j];
		Shape topNeighbor = board[i-1][j];
		if(shape.getOrientation() == 0) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
			return frozen;
		}
		else if(shape.getOrientation() == 1) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(shape.getOrientation() == 2) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(shape.getOrientation() == 3) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		return frozen;
	}



	private int freezeTShapeNeighbors(Shape[][] board,  int i, int j, Shape shape) {
		int frozen =0;
		Shape leftNeighbor = board[i][j-1];
		Shape rightNeighbor = board[i][j+1];
		Shape bottomNeighbor = board[i+1][j];
		Shape topNeighbor = board[i-1][j];
		if(shape.getOrientation() == 0) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(3);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		if(shape.getOrientation() == 1) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(shape.getOrientation() == 2) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(shape.getOrientation() == 3) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		return frozen;
	}


	private int freezeLShapeNeighbors(Shape[][] board, int i, int j, Shape shape) {
		int frozen =0;
		Shape leftNeighbor = board[i][j-1];
		Shape rightNeighbor = board[i][j+1];
		Shape bottomNeighbor = board[i+1][j];
		Shape topNeighbor = board[i-1][j];
		if(shape.getOrientation() == 0) {
			
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
			return frozen;
		}
		else if(shape.getOrientation() == 1) {

			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(shape.getOrientation() == 2) {

			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else if(shape.getOrientation() == 3) {

			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		return frozen;
	}
	

	private int freezeIShapeNeighbors(Shape[][] board, int i, int j, Shape shape) {
		int frozen =0;
		Shape leftNeighbor = board[i][j-1];
		Shape rightNeighbor = board[i][j+1];
		Shape bottomNeighbor = board[i+1][j];
		Shape topNeighbor = board[i-1][j];
		if(shape.getOrientation() == 0) {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(0);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 3) {leftNeighbor.rotateTo(3);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(0);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 3) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 1) {bottomNeighbor.rotateTo(0);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 1) {topNeighbor.rotateTo(2);topNeighbor.setFroze(true);frozen++;}
			}
		}
		else {
			if(!leftNeighbor.isFroze()) {
				if (leftNeighbor.getType() == 2) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;}
				else if (leftNeighbor.getType() == 1) {leftNeighbor.rotateTo(1);leftNeighbor.setFroze(true);frozen++;} 
			}
			if(!rightNeighbor.isFroze()) {
				if (rightNeighbor.getType() == 2) {rightNeighbor.rotateTo(1);rightNeighbor.setFroze(true);frozen++;}
				else if (rightNeighbor.getType() == 1) {rightNeighbor.rotateTo(3);rightNeighbor.setFroze(true);frozen++;} 
			}
			if(!bottomNeighbor.isFroze()) {
				if(bottomNeighbor.getType() == 2) {bottomNeighbor.rotateTo(1);bottomNeighbor.setFroze(true);frozen++;}
				else if(bottomNeighbor.getType() == 3) {bottomNeighbor.rotateTo(2);bottomNeighbor.setFroze(true);frozen++;}
			}	
			if(!topNeighbor.isFroze()) {
				if (topNeighbor.getType() == 2) {topNeighbor.rotateTo(1);topNeighbor.setFroze(true);frozen++;}
				else if(topNeighbor.getType() == 3) {topNeighbor.rotateTo(0);topNeighbor.setFroze(true);frozen++;}
			}
		}

		return frozen;
	}


	private int lastFreeze(Game game) {
		Shape[][] board = game.getBoard();
		int frozen = 0;
		for(int i = 1; i<height-1;i++) {
			for(int j = 1; j<width-1;j++) {
				Shape shape = board[i][j];
				if(shape.isFroze()) {
					continue;
				}
				if(game.isShapeFullyConnected(shape)) {
					Shape leftNeighbor = board[i][j-1];
					Shape rightNeighbor = board[i][j+1];
					Shape bottomNeighbor = board[i+1][j];
					Shape topNeighbor = board[i-1][j];
					if(leftNeighbor.isFroze() && rightNeighbor.isFroze() && bottomNeighbor.isFroze() && topNeighbor.isFroze()) {
						while(!game.isShapeFullyConnected(shape)) {
							shape.rotate();
						}
						frozen ++;
						shape.setFroze(true);
					}
				}

			}
		}
		return frozen;
	}
}
