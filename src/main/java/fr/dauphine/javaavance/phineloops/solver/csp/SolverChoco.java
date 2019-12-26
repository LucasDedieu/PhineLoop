package fr.dauphine.javaavance.phineloops.solver.csp;

import java.util.ArrayList;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.strategy.Search;

public class SolverChoco implements fr.dauphine.javaavance.phineloops.solver.Solver{
	private Game game;
	//Connections
	private static int NORTH = 0;
	private static int SOUTH = 1;
	private static int EAST = 2;
	private static int WEST = 3;
	//Shapes
	private final static int EMPTYSHAPE=0;
	private final static int QSHAPE=1;
	private final static int ISHAPE=2;
	private final static int TSHAPE=3;
	private final static int XSHAPE=4;
	private final static int LSHAPE=5;

	int height ;
	int width ;
	
	public SolverChoco(Game game) {
		this.game = game;
		this.height = game.getHeight();
		this.width= game.getWidth();
	}
	
	
	public Game solve_choco() {
		Model model = new Model("Phineloops model");
		Shape[][] board= game.getBoard();
		Game game_solved;
		//We define an annex board for the shapetypes so we can reduce the domain to simply their orientations 
		//We define the variables which are the cases along with their shape type
		IntVar[][] vars = new IntVar[height][width];
		//We get the domains of the variables which are their orientations 
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				int[] domain = board[i][j].getDomain(); // Can maybe reduce the domain there ... 
				vars[i][j] = model.intVar(domain);
			}
		}
		
		//Now lets post the constraint, there is unary contraint for the border and the corner variables that they don't have an orientation that gives them a connection to an empty space
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) { //Now we prune the unfeasible value of the domain considering their position on the grid 
				if (board[i][j].getType()!=EMPTYSHAPE && i==0 )  
				{
					vars[i][j].ne(0).post();
					if (board[i][j].getType()==TSHAPE) 
					{
						vars[i][j].ne(1).post();
						vars[i][j].ne(3).post();
					}
					if (board[i][j].getType()==LSHAPE) 
					{
						vars[i][j].ne(3).post();
					}
				}
				if (board[i][j].getType()!=EMPTYSHAPE && j==0 )  
				{
					switch (board[i][j].getType())
					{
						case QSHAPE:  
							vars[i][j].ne(3).post();
							break;
						case ISHAPE:
							vars[i][j].ne(1).post();
							break;
						case TSHAPE: case LSHAPE: 
							vars[i][j].ne(2).post();
							vars[i][j].ne(3).post();
							break;
						default: break;
					}
				}
				if (board[i][j].getType()!=EMPTYSHAPE && i==height-1 )  
				{
					switch (board[i][j].getType())
					{
						case QSHAPE:  
							vars[i][j].ne(2).post();
							break;
						case ISHAPE:
							vars[i][j].ne(0).post();
							break;
						case LSHAPE: 
							vars[i][j].ne(1).post();
							vars[i][j].ne(2).post();
							break;
						case TSHAPE: 
							vars[i][j].ne(1).post();
							vars[i][j].ne(2).post();
							vars[i][j].ne(3).post();
							break;
						default: break;
					}
				}
				if (board[i][j].getType()!=EMPTYSHAPE && j==width-1 )  
				{
					switch (board[i][j].getType())
					{
						case QSHAPE: case ISHAPE: 
							vars[i][j].ne(1).post();
							break;
						case TSHAPE:  
							vars[i][j].ne(1).post();
							vars[i][j].ne(2).post();
							break;
						case LSHAPE:
							vars[i][j].ne(0).post();
							vars[i][j].ne(1).post();
							break;
						default: break;
					}
				}
				
				if (board[i][j].getType()!=EMPTYSHAPE && board[i][j].getType()!=XSHAPE)
				{
					Constraint myConstraint = new Constraint("Are Shapes Connected ",new MyPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars), this.game, this.getNeighbourhood(board[i][j])));
//					Constraint ac = new Constraint("AreFullyConnectedConstraint", new ShapeConnectedPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game, board),new InstanciatedAndConnectedPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game, board));
					myConstraint.post();
				}
			}
		}
		
		
		//Now let's get the solution 
		Solver plsolver = model.getSolver();
		plsolver.showStatistics();
		Solution solution = plsolver.findSolution();
		if (solution != null) {
			System.out.println(solution.toString()); 
			for(int i1=0; i1<height;i1++) {
				for(int j =0; j<width;j++) {
					board[i1][j].setOrientation(solution.getIntVal(vars[i1][j]));
				}
			}
		}
		game_solved=new Game(this.game);
			
		return game_solved;
	}		
	
	
		/*
		 * 
		 * 
		 * 
		 * 
		 * JAVA DOC ALBAN : Create a model for the resolution of the problem using multi-threading  
		 */
		private Model makeModel() {
		// TODO Auto-generated method stub
		return null;
	}

		
		/*
		 * 
		 * 
		 * 
		 * 
		 * JAVA DOC ALBAN : Create a model for the resolution of the problem using multi-threading  
		 */
		public Game solve_choco_with_multithreading(int thread_number) // Ou autre classe qui hérite de solverChoco 
		{
			return game;
			
			/*
			 * ParallelPortfolio portfolio = new ParallelPortfolio();
				int nbModels = 5;
				for(int s=0;s<nbModels;s++){
		    	portfolio.addModel(makeModel());
				}
				portfolio.solve();
			 * 
			 * 
			 */
		}

		/**
		 * 
		 * @param currentPiece
		 * @param currentPieceIntVar
		 * @param vars
		 * @return
		 */
		public IntVar[] getIntVarNeighbourhood(Shape currentPiece,IntVar currentPieceIntVar,IntVar[][] vars)
		{
			Shape[] shapeNeighbour=game.getNeighbors(currentPiece);
			ArrayList<IntVar> transitiveCVN = new ArrayList<IntVar>();
			transitiveCVN.add(currentPieceIntVar);
			for (int v=0;v<shapeNeighbour.length;v++)
			{
				if (shapeNeighbour[v]!=null) 
				{
					transitiveCVN.add(vars[shapeNeighbour[v].getI()][shapeNeighbour[v].getJ()]); //Les domaines d'orientation de ses voisins 
				}
			}
			IntVar[] currentVarWithNeighbours=new IntVar[transitiveCVN.size()];
			for (int v=0;v<transitiveCVN.size();v++)
			{
				currentVarWithNeighbours[v]=transitiveCVN.get(v);
			}
			return currentVarWithNeighbours;
		}
		
		/**
		 * 
		 * @param currentPiece
		 * @return
		 */
		public Shape[] getNeighbourhood(Shape currentPiece)
		{
			Shape[] shapeNeighbour=game.getNeighbors(currentPiece);
			ArrayList<Shape> transitiveCSN = new ArrayList<Shape>();
			transitiveCSN.add(currentPiece);
			for (int v=0;v<shapeNeighbour.length;v++)
			{
				if (shapeNeighbour[v]!=null) 
				{
					transitiveCSN.add(shapeNeighbour[v]);
				}
			}
			Shape[] currentShapeWithNeighbour=new Shape[transitiveCSN.size()];
			for (int v=0;v<transitiveCSN.size();v++)
			{
				currentShapeWithNeighbour[v]=transitiveCSN.get(v);
			}
			return currentShapeWithNeighbour;
		}


		@Override
		public Game solve(int threads) {
			return solve_choco();
		}
}
