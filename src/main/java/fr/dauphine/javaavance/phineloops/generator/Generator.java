package fr.dauphine.javaavance.phineloops.generator;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fr.dauphine.javaavance.phineloops.model.EmptyShape;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.IShape;
import fr.dauphine.javaavance.phineloops.model.LShape;
import fr.dauphine.javaavance.phineloops.model.QShape;
import fr.dauphine.javaavance.phineloops.model.Shape;
import fr.dauphine.javaavance.phineloops.model.TShape;
import fr.dauphine.javaavance.phineloops.model.XShape;

public class Generator {
	private Game game;
	private static int NORTH = 0;
	private static int EAST = 1;
	private static int SOUTH = 2;
	private static int WEST = 3;
	private int maxCc;

	public Generator(Game game) {
		this.game = game;
		this.maxCc = game.getMaxCC();
	}

	/**
	 * Generate a solution which is a set of connected shapes 
	 */
	public void generateSolution() {
		int h = game.getHeight();
		int w = game.getWidth();
		Shape[][] board = game.getBoard();
		// Generation of the set of Legal Shapes that we will put
		Shape topLeftCornerLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(1, 0, 0), new QShape(2, 0, 0),
				new LShape(1, 0, 0) };
		Shape topBorderLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(1, 0, 0), new QShape(2, 0, 0),
				new QShape(3, 0, 0), new IShape(1, 0, 0), new TShape(2, 0, 0), new LShape(1, 0, 0),
				new LShape(2, 0, 0) };
		Shape topRightCornerLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(2, 0, 0), new QShape(3, 0, 0),
				new LShape(2, 0, 0) };
		Shape leftBorderLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(1, 0, 0), new QShape(0, 0, 0),
				new QShape(2, 0, 0), new IShape(0, 0, 0), new TShape(1, 0, 0), new LShape(0, 0, 0),
				new LShape(1, 0, 0) };
		Shape bottomLeftCornerLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(0, 0, 0), new QShape(1, 0, 0),
				new LShape(0, 0, 0) };
		Shape bottomBorderLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(0, 0, 0), new QShape(1, 0, 0),
				new QShape(3, 0, 0), new IShape(1, 0, 0), new TShape(0, 0, 0), new LShape(0, 0, 0),
				new LShape(3, 0, 0) };
		Shape bottomRightCornerLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(0, 0, 0), new QShape(3, 0, 0),
				new LShape(3, 0, 0) };
		Shape rightBorderLegalShapes[] = { new EmptyShape(0, 0, 0), new QShape(0, 0, 0), new QShape(3, 0, 0),
				new QShape(2, 0, 0), new IShape(0, 0, 0), new TShape(3, 0, 0), new LShape(2, 0, 0),
				new LShape(3, 0, 0) };
		List<Shape> allShape = Arrays.asList(new EmptyShape(0, 0, 0), new QShape(0, 0, 0), new QShape(1, 0, 0),
				new QShape(2, 0, 0), new QShape(3, 0, 0), new IShape(0, 0, 0), new IShape(1, 0, 0), new TShape(0, 0, 0),
				new TShape(1, 0, 0), new TShape(2, 0, 0),new TShape(3, 0, 0), new XShape(0, 0, 0), new LShape(0, 0, 0), new LShape(1, 0, 0),
				new LShape(2, 0, 0), new LShape(3, 0, 0));
		Random rand = new Random();

		// Grid Traversal putting each correct shape regarding the legal and feasible shapes
		for (int i = 0; i < h; i++) 
		{
			for (int j = 0; j < w; j++) 
			{
				if (i == 0 && j == 0) //Top Left Corner
				{
					int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
					board[i][j] = topLeftCornerLegalShapes[randomIndex];
				}
				else if (i == 0 && j < w - 1) // Top Border
				{
					//We make sure that a shape is added to the feasible shapes only if it has a connection with the previous one 
					if (board[i][j - 1].getConnections()[EAST]) 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : topBorderLegalShapes) 
						{ 
							if (shape.getConnections()[WEST])
								feasibleShapes.add(shape);
						}
						//Then we pick a random shapes among the feasible ones
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : topBorderLegalShapes) 
						{
							if (!shape.getConnections()[WEST])
								feasibleShapes.add(shape);
						}
						//Or we pick a random shapes among the feasible ones that does not have a connection 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else if (i == 0 && j == (w - 1)) // Top Right Corner
				{
					if (board[i][j - 1].getConnections()[EAST]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : topRightCornerLegalShapes) {
							if (shape.getConnections()[WEST])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} else {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : topRightCornerLegalShapes) {
							if (!shape.getConnections()[WEST])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else if (i < h - 1 && j == 0) //Left Border
				{
					if (board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : leftBorderLegalShapes) {
							if (shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} else {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : leftBorderLegalShapes) {
							if (!shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else if (i < h - 1 && j == w - 1) //Right Border
				{

					if (board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : rightBorderLegalShapes) {
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else if (board[i][j - 1].getConnections()[EAST] && !board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : rightBorderLegalShapes) {
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else if (!board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : rightBorderLegalShapes) {
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : rightBorderLegalShapes) {
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else if (i == h - 1 && j == 0) //Bottom Left Corner
				{
					if (board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomLeftCornerLegalShapes) {
							if (shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomLeftCornerLegalShapes) {
							if (!shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else if (i == h - 1 && j < w - 1) //Bottom Border
				{
					if (board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomBorderLegalShapes) {
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else if (board[i][j - 1].getConnections()[EAST] && !board[i - 1][j].getConnections()[SOUTH]) 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomBorderLegalShapes) {
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else if (!board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomBorderLegalShapes) {
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomBorderLegalShapes) {
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else if (i == h - 1 && j == w - 1) //Bottom Right Corner
				{
					if (board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomRightCornerLegalShapes) {
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else if (board[i][j - 1].getConnections()[EAST] && !board[i - 1][j].getConnections()[SOUTH]) 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomRightCornerLegalShapes) {
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else if (!board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomRightCornerLegalShapes) {
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} 
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : bottomRightCornerLegalShapes) {
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				} 
				else //Rest of cases
				{
					//We test each time the connection that the north neighbor and the left neighbour has in order to determine the shapes we can put
					if (board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : allShape) {
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} else if (board[i][j - 1].getConnections()[EAST] && !board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : allShape) {
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} else if (!board[i][j - 1].getConnections()[EAST] && board[i - 1][j].getConnections()[SOUTH]) {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : allShape) {
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					} else {
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape : allShape) {
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH])
								feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j] = feasibleShapes.get(randomIndex);
					}
				}
			}
		}
	}

	
	/**
	 * Generate a solution  with an exact number of connected components 
	 * @param nbcc the number of connected component to generate
	 */
	public void generateSolution(int nbcc) 
	{
		Random rand= new Random();
		int h = game.getHeight();
		int w = game.getWidth();
		int starti=rand.nextInt(h);
		int startj=rand.nextInt(w);
		//We divide the number of boxes between each connected component (We add a minus two so they very rarely take all the feasible new entries)
		int traversalEnd=(int)((float)(h*w)/(float)(nbcc))-2;
		Shape[][] board = game.getBoard();
		// Generation of the set of Legal Shapes //Should be member of game 
		Shape topLeftCornerLegalShapes[] = generateTopLeftCornerLegalShapes();
		Shape topBorderLegalShapes[] = generateTopBorderLegalShapes();
		Shape topRightCornerLegalShapes[] = generateTopRightCornerLegalShapes();
		Shape leftBorderLegalShapes[] = generateLeftBorderLegalShapes();
		Shape bottomLeftCornerLegalShapes[] = generateBottomLeftCornerLegalShapes();
		Shape bottomBorderLegalShapes[] = generateBottomBorderLegalShapes();
		Shape bottomRightCornerLegalShapes[] = generateBottomRightCornerLegalShapes();
		Shape rightBorderLegalShapes[] = generateRightBorderLegalShapes();
		List<Shape> allShape = generateAllShapes();
		ArrayList<Shape> toPutShapes = new ArrayList<Shape>();//The empty shapes that are neighbor of the shapes already put
		ArrayList<Shape> firstConnectedComponent = new ArrayList<Shape>();//The first connected component, has more liberty than the other one 
		ArrayList<ArrayList<Shape>> allGameConnectedComponent = new ArrayList<ArrayList<Shape>>();

		//We initialize the boxes with emptyshapes first since it's the shape we will favour
		for (int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				board[i][j]=new EmptyShape(0,i,j);
			}
		}
		
		//Almost deterministic generation of shapes, has to be dealt with specifically for each case as an optimization
		/*
		 * if(traversalEnd==2) {
		 * 
		 * }
		 */

		//We put the first piece on the board depending on the position which contains less restriction
		if (starti==0 && startj==0)
		{
			putFirstShape(starti,startj,toPutShapes,topLeftCornerLegalShapes,board);
		}
		else if (starti==0 && startj<w-1)
		{
			putFirstShape(starti,startj,toPutShapes,topBorderLegalShapes,board);
		}
		else if (starti == 0 && startj == (w - 1))
		{
			putFirstShape(starti,startj,toPutShapes,topRightCornerLegalShapes,board);
		}
		else if (starti<(h-1) && startj==0)
		{
			putFirstShape(starti,startj,toPutShapes,leftBorderLegalShapes,board);
		}
		else if (starti<(h-1) && startj==(w-1))
		{
			putFirstShape(starti,startj,toPutShapes,rightBorderLegalShapes,board);
		}
		else if (starti==h-1 && startj==0)
		{
			putFirstShape(starti,startj,toPutShapes,bottomLeftCornerLegalShapes,board);
		}
		else if (starti==h-1 && startj<(w-1))
		{
			putFirstShape(starti,startj,toPutShapes,bottomBorderLegalShapes,board);
		}
		else if (starti==h-1 && startj==(w-1))
		{
			putFirstShape(starti,startj,toPutShapes,bottomRightCornerLegalShapes,board);
		}
		else 
		{
			int randomIndex = rand.nextInt(allShape.size());
			Shape placedShape = allShape.get(randomIndex); 
			placedShape.setI(starti);
			placedShape.setJ(startj);
				board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				toPutShapes.add(neighbour); 
			}
		}
		firstConnectedComponent.add(board[starti][startj]);
		
		
		//********************************************* TRAVERSAL ******************************************************************			
		

		for(int i=0;i<traversalEnd && toPutShapes.size()!=0 && i<(traversalEnd-toPutShapes.size());i++)
		{
			//We have to regenerate the legal values because otherwise they could overlap
			int nextPlacedShapeIndex = rand.nextInt(toPutShapes.size()); // -1 ? 
			Shape nextPlacedShape = toPutShapes.get(nextPlacedShapeIndex);
			toPutShapes.remove(nextPlacedShapeIndex);
			starti=nextPlacedShape.getI();
			startj=nextPlacedShape.getJ();
			//We put the first piece on the board depending on the position
			if (starti==0 && startj==0)
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateTopLeftCornerLegalShapes(),board);
			}
			else if (starti==0 && startj<w-1)
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateTopBorderLegalShapes(),board);
			}
			else if (starti == 0 && startj == (w - 1))
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateTopRightCornerLegalShapes(),board);
			}
			else if (starti<(h-1) && startj==0)
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateLeftBorderLegalShapes(),board); 
			}
			else if (starti<(h-1) && startj==(w-1))
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateRightBorderLegalShapes(),board); 

			}
			else if (starti==h-1 && startj==0)
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateBottomLeftCornerLegalShapes(),board); 
			}
			else if (starti==h-1 && startj<(w-1))
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateBottomBorderLegalShapes(),board); 
			}
			else if (starti==h-1 && startj==(w-1))
			{
				putOtherShapes(starti,startj,toPutShapes,nextPlacedShape,generateBottomRightCornerLegalShapes(),board); 
			}
			else 
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateAllShapes())
				{
					int token=0;
					sh.setI(starti);
					sh.setJ(startj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,nextPlacedShape.getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj); 
				board[starti][startj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) // && neighbour.getType()==0
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}		
		}
		connectedComponentCloser(toPutShapes,board, firstConnectedComponent);
		//At the end we flush the toPutShapes  
		toPutShapes.clear();			
		//Now lets create the other connected components
		for(int k=0;k<nbcc-1;k++)
		{
			allGameConnectedComponent.add(generateConnectedComponent(board,game,traversalEnd));
		}
		System.out.println(game);		
	}
	
	/**
	 * Generate a solvable game board with random shapes 
	 */
	public void generate() {
		this.generateSolution();
		this.shuffleGame(this.game);
	}

	/**
	 * Generate a solvable game board with random shapes and a maximum number of connected components, 
	 * @param nbcc the number of connected components that is the limit of the connected component generated
	 */
	public void generate(int nbcc)
	{
		Random rand = new Random();
		this.maxCc=rand.nextInt(nbcc);
		if(maxCc==0) maxCc++;//An empty grid would be useless 
		this.generateSolution(this.maxCc);
		this.shuffleGame(this.game);	
	}
	
	/**
	 * Generate a solvable game board with random shapes and an exact number of connected components
	 * @param cc the exact number of connected component to generate 
	 */
	public void generateWithExactNumberOfConnectedComponents(int cc)
	{
		this.generateSolution(cc);
		this.shuffleGame(this.game);	
	}
	
	
	/** Generate a connected component which is a set of random connected shapes 
	 * @param board the board to fill
	 * @param game the game from which the board is from
	 * @param traversalEnd : the limit of the traversal 
	 * @return A connected component composed of a List of all his shapes 
	 */
	private ArrayList<Shape> generateConnectedComponent(Shape[][] board,Game game,int traversalEnd) {
		 
	 	ArrayList<Shape> connectedComponent= new ArrayList<Shape>();
	 	ArrayList<Shape> toPutShapes = new ArrayList<Shape>();
		List<Shape> allShape = generateAllShapes();
		Random rand = new Random();
		int w=game.getWidth();
		int h=game.getHeight();
		//We choose another emptyshapes in the board for a new entry 
		ArrayList<Shape> feasibleEntry = new ArrayList<Shape>();
		for(Shape[] shapelines:board)
		{
			for(Shape sh:shapelines)
			{
				if(sh.getType()==0 && game.hasEmptyNeighbor(sh))
					feasibleEntry.add(sh);
			}
		}
		/*
		 * The feasible entries could be empty and throw an exception in rand for a number of connected component close enough to height*width/2...
		 * To tackle that we could monitor the number of empty shapes left by each generation of connected component
		 */
		if (feasibleEntry.isEmpty())
		{
			return null;
		}
		int newEntryIndex = rand.nextInt(feasibleEntry.size());
		Shape newEntry = feasibleEntry.get(newEntryIndex);
		//Now we get the coord for the new entry
		int starti=newEntry.getI();
		int startj=newEntry.getJ();
	
		//We place a random shapes for the new connected component but by paying caution to the already placed shapes
		if (starti==0 && startj==0)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateTopLeftCornerLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false; //If we have a box to complete for this shape but it is already occupied (i.e not empty) ,we don't put it 
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj); 
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour);  
			}
		}
		else if (starti==0 && startj<w-1)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateTopBorderLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj);
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			} 
		}
		else if (starti == 0 && startj == (w - 1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateTopRightCornerLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj);
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (starti<(h-1) && startj==0)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateLeftBorderLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj);
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (starti<(h-1) && startj==(w-1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateRightBorderLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj);
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				toPutShapes.add(neighbour);
			}
		}
		else if (starti==h-1 && startj==0)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateBottomLeftCornerLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj);
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (starti==h-1 && startj<(w-1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateBottomBorderLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj);
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour);
			}
			
		}
		else if (starti==h-1 && startj==(w-1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:generateBottomRightCornerLegalShapes())
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj); 
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour);  
			}
		}
		else 
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:allShape)
			{
				boolean willWork=true;
				sh.setI(starti);
				sh.setJ(startj);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(starti);
			placedShape.setJ(startj); 
			board[starti][startj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
			{
				neighbour.setReservedBy(board[starti][startj]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		connectedComponent.add(board[starti][startj]);
			
		//********************************TRAVERSAL************************************************** 
		
		for(int i=0;i<traversalEnd && toPutShapes.size()!=0 && i<(traversalEnd-toPutShapes.size());i++)
		{
			List<Shape> allShape11 = generateAllShapes();
			int nextPlacedShapeIndex = rand.nextInt(toPutShapes.size());
			Shape nextPlacedShape = toPutShapes.get(nextPlacedShapeIndex);
			toPutShapes.remove(nextPlacedShapeIndex);
			starti=nextPlacedShape.getI();
			startj=nextPlacedShape.getJ();
			
			//We put the rest of the shapes for the connected component 
			if (starti==0 && startj==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateTopLeftCornerLegalShapes()) //We try the shapes to know if we put it in the feasible ones 
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh); //We put the chosen shape and put it on the empty box  
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false; //if a shape cannot connect to his neighbor, we don't add it 
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; 
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj);
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); 
				}
			}
			else if (starti==0 && startj<w-1)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateTopBorderLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; 
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj); 
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (starti == 0 && startj == (w - 1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateTopRightCornerLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; 
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj);
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (starti<(h-1) && startj==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateLeftBorderLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false;
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj); 
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (starti<(h-1) && startj==(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateRightBorderLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put shape that can be connected to all his reservedBy neighbors 
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj);
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour);  
				}
			}
			else if (starti==h-1 && startj==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateBottomLeftCornerLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false;  
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj);
				board[starti][startj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (starti==h-1 && startj<(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateBottomBorderLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; 
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj);
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (starti==h-1 && startj==(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateBottomRightCornerLegalShapes())
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; 
					}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj);
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				} 
			}
			else 
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:allShape11)
				{
					boolean willWork=true;
					sh.setI(starti);
					sh.setJ(startj);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					} 
						for(Shape toConnectsh:nextPlacedShape.getReservedBy())
						{
							if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; 
						}
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(starti);
				placedShape.setJ(startj); 
				board[starti][startj] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
				{
					neighbour.setReservedBy(board[starti][startj]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
		}
		//Closer
		connectedComponentCloser(toPutShapes,board, connectedComponent);
		toPutShapes.clear();
		return connectedComponent;
	 }
	 
	
	 
	 //****************************UTIL METHODS FOR THE GENERATOR**************************
	 
	
	
	/**
	 *  Put the first Shape of the connected component 
	 * @param starti the 
	 * @param startj
	 * @param toPutShapes
	 * @param legalShapes
	 * @param board
	 */
	private void putFirstShape(int starti,int startj,ArrayList<Shape> toPutShapes,Shape[] legalShapes,Shape[][] board)
	{
		Random rand = new Random();
		int randomIndex = rand.nextInt(legalShapes.length);
		Shape placedShape = legalShapes[randomIndex];
		placedShape.setI(starti);
		placedShape.setJ(startj);
		board[starti][startj] = placedShape;
		for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
		{
			neighbour.setReservedBy(board[starti][startj]);
			toPutShapes.add(neighbour);  
		}
	}
	
	/**
	 *  Put the other shapes of the connected component 
	 * @param starti
	 * @param startj
	 * @param toPutShapes
	 * @param nextPlacedShape
	 * @param legalShapes
	 * @param board
	 */
	private void putOtherShapes(int starti, int startj,ArrayList<Shape> toPutShapes,Shape nextPlacedShape,Shape[] legalShapes,Shape[][] board)
	{
		Random rand = new Random();
		ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
		for(Shape sh:legalShapes)
		{
			int token=0;
			sh.setI(starti);
			sh.setJ(startj);
			for(Shape reservation : nextPlacedShape.getReservedBy() )
			{
				if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[starti][startj].getReservedBy())) //We need to know who has put the shape as his neighbour in the board 
					token++;
			}
			if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
		}
		int randomIndex = rand.nextInt(feasibleShapes.size());
		Shape placedShape = feasibleShapes.get(randomIndex);
		placedShape.setI(starti);
		placedShape.setJ(startj);
		board[starti][startj] = placedShape;
		for (Shape neighbour:game.getToConnectNeighbors(board[starti][startj]))
		{
			neighbour.setReservedBy(board[starti][startj]);
			if(!toPutShapes.contains(neighbour))
				toPutShapes.add(neighbour); // We add the position that we will have to put a piece in to close the connected component
		}
	}
	 
	/**
	 * Put the correct shapes to connected Component to close them 
	 * @param toPutShapes
	 * @param board
	 * @param connectedComponent 
	 */
	private void connectedComponentCloser(ArrayList<Shape> toPutShapes,Shape[][] board, List<Shape> connectedComponent)
	{
		if (!toPutShapes.isEmpty())
		{
			for(Shape sh:toPutShapes)
			{
				switch(sh.getReservedBy().size())
				{
					case 1:
						board[sh.getI()][sh.getJ()]=new QShape(game.getQOrientationForOpenConnection(sh),sh.getI(),sh.getJ());
						connectedComponent.add(board[sh.getI()][sh.getJ()]);
						break;
					case 2:
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						Shape selectedShape = null;
						for(Shape twoOutShapes: this.generateAllShapes())
						{
							if(twoOutShapes.getVConnections()==2)
							feasibleShapes.add(twoOutShapes);
						}
						for(Shape ssh: feasibleShapes) 
						{
							ssh.setI(sh.getI());
							ssh.setJ(sh.getJ());
							if(game.areShapesConnected(ssh,sh.getReservedBy().get(0)) && game.areShapesConnected(ssh,sh.getReservedBy().get(1)))
							{
								selectedShape=ssh;
							}
						}
						board[sh.getI()][sh.getJ()]=selectedShape; 
						connectedComponent.add(selectedShape);
						break;
						//board[sh.getI()][sh.getJ()]=(I Shape)
					case 3:
						board[sh.getI()][sh.getJ()]=new TShape(game.getTOrientationForOpenConnection(sh),sh.getI(),sh.getJ());
						connectedComponent.add(board[sh.getI()][sh.getJ()]);
						break;
					case 4:
						board[sh.getI()][sh.getJ()]=new XShape(0,sh.getI(),sh.getJ());
						connectedComponent.add(board[sh.getI()][sh.getJ()]);
						break;
					default: 
						break;
				}
			}
		}
		toPutShapes.clear();
	}
	
	/**
	 * 	Shuffle to a random position, the shapes of the board given in parameters 
	 * @param game
	 */
	private void shuffleGame(Game game) {
		Random rand = new Random();
		for (Shape[] shapes : game.getBoard()) {
			for (Shape shape : shapes) {
				for (int i = 0; i < rand.nextInt(4); i++)
					shape.rotate();
			}
		}
	}
 
	 
	//****************************SET OF SHAPES GENERATORS ****************************************
											
	
	/**
	 * 
	 * @return TopLeftCornerLegalShapes
	 */
	private Shape[] generateTopLeftCornerLegalShapes()
	{
		return new Shape[]{ new QShape(1, 0, 0), new QShape(2, 0, 0),
				new LShape(1, 0, 0) };
	}
	
	/**
	 * 
	 * @return TopBorderLegalShapes
	 */
	private Shape[] generateTopBorderLegalShapes()
	{
		return new Shape[]{ new QShape(1, 0, 0), new QShape(2, 0, 0),new QShape(3, 0, 0), 
				new IShape(1, 0, 0), new TShape(2, 0, 0), new LShape(1, 0, 0), new LShape(2, 0, 0) };
	}
	
	/**
	 * 
	 * @return
	 */
	private Shape[] generateTopRightCornerLegalShapes()
	{
		return new Shape[]{ new QShape(2, 0, 0), new QShape(3, 0, 0),
				new LShape(2, 0, 0) };
	}
	
	/**
	 * 
	 * @return LeftBorderLegalShapes
	 */
	private Shape[] generateLeftBorderLegalShapes()
	{
		return new Shape[]{ new QShape(1, 0, 0), new QShape(0, 0, 0),
				new QShape(2, 0, 0), new IShape(0, 0, 0), new TShape(1, 0, 0), new LShape(0, 0, 0),
				new LShape(1, 0, 0) };
	}
	
	/**
	 * 
	 * @return RightBorderLegalShapes
	 */
	private Shape[] generateRightBorderLegalShapes()
	{
		return new Shape[]{ new QShape(0, 0, 0), new QShape(3, 0, 0),
				new QShape(2, 0, 0), new IShape(0, 0, 0), new TShape(3, 0, 0), new LShape(2, 0, 0),
				new LShape(3, 0, 0) };
	}
	
	/**
	 * 
	 * @return BottomLeftCornerLegalShapes
	 */
	private Shape[] generateBottomLeftCornerLegalShapes()
	{
		return new Shape[]{new QShape(0, 0, 0), new QShape(1, 0, 0),
			new LShape(0, 0, 0) };
	}
	
	/**
	 * 
	 * @return BottomBorderLegalShapes
	 */
	private Shape[] generateBottomBorderLegalShapes()
	{
		return new Shape[]{ 
				new QShape(0, 0, 0), new QShape(1, 0, 0),new QShape(3, 0, 0), 
				new IShape(1, 0, 0), new TShape(0, 0, 0), new LShape(0, 0, 0), new LShape(3, 0, 0) };
	}
	
	/**
	 * 
	 * @return BottomRightCornerLegalShapes
	 */
	private Shape[] generateBottomRightCornerLegalShapes()
	{
		return new Shape[]{ new QShape(0, 0, 0), new QShape(3, 0, 0),
				new LShape(3, 0, 0) };
	}
	
	/**
	 * 
	 * @return AllShapes
	 */
	private List<Shape> generateAllShapes()
	{
		List<Shape> allshapes = Arrays.asList(
				new QShape(0, 0, 0), new QShape(1, 0, 0), new QShape(2, 0, 0), new QShape(3, 0, 0), 
				new IShape(0, 0, 0), new IShape(1, 0, 0), 
				new TShape(0, 0, 0), new TShape(1, 0, 0), new TShape(2, 0, 0),new TShape(3, 0, 0), 
				new XShape(0, 0, 0), 
				new LShape(0, 0, 0), new LShape(1, 0, 0), new LShape(2, 0, 0), new LShape(3, 0, 0));
		return allshapes;
	}
}