package fr.dauphine.javaavance.phineloops.solver.csp;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.Shape;

public class SolverChoco implements fr.dauphine.javaavance.phineloops.solver.Solver{
	private Game game;
	//Shapes
	private final static int EMPTYSHAPE=0;
	private final static int XSHAPE=4;

	int height ;
	int width ;
	
	public SolverChoco(Game game) {
		this.game = game;
		this.height = game.getHeight();
		this.width= game.getWidth();
	}
	
	
	public Game solve_choco() {
		Model model = new Model("Phineloops model");
		//prepare(game);
		Shape[][] board= game.getBoard();
		Game game_solved;
		//We define an annex board for the shapetypes so we can reduce the domain to simply their orientations 
		//We define the variables which are the cases along with their shape type
		IntVar[][] vars = new IntVar[height][width];
		//We get the domains of the variables which are their orientations 
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) 
			{
					int[] domain = board[i][j].getDomainWithPruning(game); // We prune the domain regarding the position on the board of the shapes 
					vars[i][j] = model.intVar(domain);
			}
		}
		
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) { 
				if (vars[i][j].isInstantiated()) board[i][j].setOrientation(vars[i][j].getValue());
				Constraint myConstraint = new Constraint("Simple connection with shapes Constraint",
							new MyPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game)
							/*,new InstantiatedAndConnectedPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game)*/);
				myConstraint.post();
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
		Model model = new Model("Phineloops model");
		Shape[][] board= game.getBoard();
		//We define an annex board for the shapetypes so we can reduce the domain to simply their orientations 
		//We define the variables which are the cases along with their shape type
		IntVar[][] vars = new IntVar[height][width];
		//We get the domains of the variables which are their orientations 
		for(int i=0; i<height;i++) 
		{
			for(int j =0; j<width;j++) 
			{
				int[] domain = board[i][j].getDomain(); // Can maybe reduce the domain there ... 
				vars[i][j] = model.intVar(domain);
			}
		}
		//Now lets post the constraint, there is unary contraint for the border and the corner variables that they don't have an orientation that gives them a connection to an empty space
		for(int i=0; i<height;i++) 
		{
			for(int j =0; j<width;j++) 
			{ 	
				if (board[i][j].getType()!=EMPTYSHAPE && board[i][j].getType()!=XSHAPE)
				{
					//Constraint myConstraint = new Constraint("All shapes connected Constraint", new ShapeConnectedPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game, board),new InstanciatedAndConnectedPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game, board));
					Constraint myConstraint = new Constraint("Simple connection with shapes Constraint",new MyPropagator(this.getIntVarNeighbourhood(board[i][j], vars[i][j], vars),this.getNeighbourhood(board[i][j]),this.game));
					myConstraint.post();
				}
			}
		}
		return model;
	}
		
	/*
	 * 
	 * 
	 * 
	 * 
	 * JAVA DOC ALBAN : Create a model for the resolution of the problem using multi-threading  
	 */
	public Game solve_choco_with_multithreading(int nbThreads) // Ou autre classe qui hérite de solverChoco 
	{
		ParallelPortfolio portfolio = new ParallelPortfolio();
		int nbModels = nbThreads;
		for(int s=0;s<nbModels;s++)
		{
			portfolio.addModel(makeModel());
		}
		portfolio.solve();
		return null;
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
