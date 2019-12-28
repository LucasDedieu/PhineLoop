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
		Shape topLeftCornerLegalShapes[] = { new EmptyShape(0, w, w), new QShape(1, w, w), new QShape(2, w, w),
				new LShape(1, 0, 0) };
		Shape topBorderLegalShapes[] = { new EmptyShape(0, w, w), new QShape(1, w, w), new QShape(2, w, w),
				new QShape(3, w, w), new IShape(1, w, w), new TShape(2, w, w), new LShape(1, 0, 0),
				new LShape(2, 0, 0) };
		Shape topRightCornerLegalShapes[] = { new EmptyShape(0, w, w), new QShape(2, w, w), new QShape(3, w, w),
				new LShape(2, 0, 0) };
		Shape leftBorderLegalShapes[] = { new EmptyShape(0, w, w), new QShape(1, w, w), new QShape(0, w, w),
				new QShape(2, w, w), new IShape(0, w, w), new TShape(1, w, w), new LShape(0, 0, 0),
				new LShape(1, 0, 0) };
		Shape bottomLeftCornerLegalShapes[] = { new EmptyShape(0, w, w), new QShape(0, w, w), new QShape(1, w, w),
				new LShape(0, 0, 0) };
		Shape bottomBorderLegalShapes[] = { new EmptyShape(0, w, w), new QShape(0, w, w), new QShape(1, w, w),
				new QShape(3, w, w), new IShape(1, w, w), new TShape(0, w, w), new LShape(0, 0, 0),
				new LShape(3, 0, 0) };
		Shape bottomRightCornerLegalShapes[] = { new EmptyShape(0, w, w), new QShape(0, w, w), new QShape(3, w, w),
				new LShape(3, 0, 0) };
		Shape rightBorderLegalShapes[] = { new EmptyShape(0, w, w), new QShape(0, w, w), new QShape(3, w, w),
				new QShape(2, w, w), new IShape(0, w, w), new TShape(3, w, w), new LShape(2, 0, 0),
				new LShape(3, 0, 0) };
		List<Shape> allShape = Arrays.asList(new EmptyShape(0, w, w), new QShape(0, w, w), new QShape(1, w, w),
				new QShape(2, w, w), new QShape(3, w, w), new IShape(0, w, w), new IShape(1, w, w), new TShape(0, w, w),
				new TShape(1, w, w), new TShape(2, w, w),new TShape(3, w, w), new XShape(0, 0, 0), new LShape(0, 0, 0), new LShape(1, 0, 0),
				new LShape(2, 0, 0), new LShape(3, 0, 0));
		Random rand = new Random();

		// Grid Traversal putting each correct shape regarding the legal and feasible shapes
		for (int i = 0; i < h; i++) 
		{
			for (int j = 0; j < w; j++) 
			{
				if (i == 0 && j == 0) // Top Left Corner
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
		int departi=rand.nextInt(h);
		int departj=rand.nextInt(w);
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
		
		
		if(traversalEnd==2)//Almost deterministic generation of shapes, has to be dealt with specifically for each case 
		{
			
		}

		//We put the first piece on the board depending on the position
		if (departi==0 && departj==0)
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = topLeftCornerLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj);
			board[departi][departj] = placedShape;
			//We go through all the empty neighbor of the placed shape 
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);//We set a reservation for those empty neighbor so we will know how many connection they should have (i.e how many reservations) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (departi==0 && departj<w-1)
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = topBorderLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj); 
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		else if (departi == 0 && departj == (w - 1))
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = topRightCornerLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj);
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		else if (departi<(h-1) && departj==0)
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = leftBorderLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj); 
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		else if (departi<(h-1) && departj==(w-1))
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = rightBorderLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj); 
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		else if (departi==h-1 && departj==0)
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = bottomLeftCornerLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj); 
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		else if (departi==h-1 && departj<(w-1))
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = bottomBorderLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj); 
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		else if (departi==h-1 && departj==(w-1))
		{
			int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
			Shape placedShape = bottomRightCornerLegalShapes[randomIndex];
			placedShape.setI(departi);
			placedShape.setJ(departj);
			board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour);  
			}
		}
		else 
		{
			int randomIndex = rand.nextInt(allShape.size());
			Shape placedShape = allShape.get(randomIndex); 
			placedShape.setI(departi);
			placedShape.setJ(departj);
				board[departi][departj] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
			{
				neighbour.setReservedBy(board[departi][departj]);
				toPutShapes.add(neighbour); 
			}
		}
		firstConnectedComponent.add(board[departi][departj]);
		
		
		//********************************************* TRAVERSAL ******************************************************************			
		

		for(int i=0;i<traversalEnd && toPutShapes.size()!=0 && i<(traversalEnd-toPutShapes.size());i++)
		{
			//We have to regenerate the legal values because otherwise they could overlap
			int nextPlacedShapeIndex = rand.nextInt(toPutShapes.size()); // -1 ? 
			Shape nextPlacedShape = toPutShapes.get(nextPlacedShapeIndex);
			toPutShapes.remove(nextPlacedShapeIndex);
			departi=nextPlacedShape.getI();
			departj=nextPlacedShape.getJ();
			//We put the first piece on the board depending on the position
			if (departi==0 && departj==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateTopLeftCornerLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); // We add the position that we will have to put a piece in to close the connected component
				}
			}
			else if (departi==0 && departj<w-1)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateTopBorderLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); 
				}
			}
			else if (departi == 0 && departj == (w - 1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateTopRightCornerLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour);  
				}
			}
			else if (departi<(h-1) && departj==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateLeftBorderLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj); 
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour);
				} 
			}
			else if (departi<(h-1) && departj==(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateRightBorderLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); //
				} 
			}
			else if (departi==h-1 && departj==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateBottomLeftCornerLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size()); 
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else if (departi==h-1 && departj<(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateBottomBorderLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else if (departi==h-1 && departj==(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateBottomRightCornerLegalShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,board[departi][departj].getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj);
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else 
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:generateAllShapes())
				{
					int token=0;
					sh.setI(departi);
					sh.setJ(departj);
					for(Shape reservation : nextPlacedShape.getReservedBy() )
					{
						if(game.areShapesConnected(reservation, sh) && !game.isObstructed(sh,nextPlacedShape.getReservedBy())) //I need to know who has put the shape as his neighbour in the board 
							token++;
					}
					if(token==nextPlacedShape.getReservedBy().size()) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi);
				placedShape.setJ(departj); 
				board[departi][departj] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi][departj]))
				{
					neighbour.setReservedBy(board[departi][departj]);
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
	 	Shape topLeftCornerLegalShapes2[] = generateTopLeftCornerLegalShapes();
		Shape topBorderLegalShapes2[] = generateTopBorderLegalShapes();
		Shape topRightCornerLegalShapes2[] = generateTopRightCornerLegalShapes();
		Shape leftBorderLegalShapes2[] = generateLeftBorderLegalShapes();
		Shape bottomLeftCornerLegalShapes2[] = generateBottomLeftCornerLegalShapes();
		Shape bottomBorderLegalShapes2[] = generateBottomBorderLegalShapes();
		Shape bottomRightCornerLegalShapes2[] = generateBottomRightCornerLegalShapes();
		Shape rightBorderLegalShapes2[] = generateRightBorderLegalShapes();
		List<Shape> allShape2 = generateAllShapes();
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
		int departi1=newEntry.getI();
		int departj1=newEntry.getJ();
	
		//We place a random shapes for the new connected component but by paying caution to the already placed shapes
		if (departi1==0 && departj1==0)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:topLeftCornerLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false; //If we have a case to complete for this shape but it is already occupied (i.e not empty) ,we don't put it 
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1); 
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour);  
			}
		}
		else if (departi1==0 && departj1<w-1)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:topBorderLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1);
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			} 
		}
		else if (departi1 == 0 && departj1 == (w - 1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:topRightCornerLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1);
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (departi1<(h-1) && departj1==0)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:leftBorderLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1);
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (departi1<(h-1) && departj1==(w-1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:rightBorderLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1);
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				toPutShapes.add(neighbour);
			}
		}
		else if (departi1==h-1 && departj1==0)
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:bottomLeftCornerLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1);
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		else if (departi1==h-1 && departj1<(w-1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:bottomBorderLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1);
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour);
			}
			
		}
		else if (departi1==h-1 && departj1==(w-1))
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:bottomRightCornerLegalShapes2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1); 
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour);  
			}
		}
		else 
		{
			ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
			for(Shape sh:allShape2)
			{
				boolean willWork=true;
				sh.setI(departi1);
				sh.setJ(departj1);
				Shape[] shneighbor = game.getConnectionNeighbors(sh);
				for(Shape nb:shneighbor)
				{
					if (nb.getType()!=0) willWork=false;
				}
				if(willWork) feasibleShapes.add(sh);
			}
			int randomIndex = rand.nextInt(feasibleShapes.size());
			Shape placedShape = feasibleShapes.get(randomIndex);
			placedShape.setI(departi1);
			placedShape.setJ(departj1); 
			board[departi1][departj1] = placedShape;
			for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
			{
				neighbour.setReservedBy(board[departi1][departj1]);
				if(!toPutShapes.contains(neighbour)) 
					toPutShapes.add(neighbour); 
			}
		}
		connectedComponent.add(board[departi1][departj1]);
			
		//********************************TRAVERSAL************************************************** 
		
		for(int i=0;i<traversalEnd && toPutShapes.size()!=0 && i<(traversalEnd-toPutShapes.size());i++)
		{
			//We have to regenerate the legal values because other wise they could overlap
			Shape topLeftCornerLegalShapes11[] = generateTopLeftCornerLegalShapes();
			Shape topBorderLegalShapes11[] = generateTopBorderLegalShapes();
			Shape topRightCornerLegalShapes11[] = generateTopRightCornerLegalShapes();
			Shape leftBorderLegalShapes11[] = generateLeftBorderLegalShapes();
			Shape bottomLeftCornerLegalShapes11[] = generateBottomLeftCornerLegalShapes();
			Shape bottomBorderLegalShapes11[] = generateBottomBorderLegalShapes();
			Shape bottomRightCornerLegalShapes11[] = generateRightBorderLegalShapes();
			Shape rightBorderLegalShapes11[] = generateRightBorderLegalShapes();
			List<Shape> allShape11 = generateAllShapes();
			int nextPlacedShapeIndex = rand.nextInt(toPutShapes.size());
			Shape nextPlacedShape = toPutShapes.get(nextPlacedShapeIndex);
			toPutShapes.remove(nextPlacedShapeIndex);
			departi1=nextPlacedShape.getI();
			departj1=nextPlacedShape.getJ();
			
			//We put the rest of the shapes for the connected component 
			if (departi1==0 && departj1==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:topLeftCornerLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false; //We have to be able to put a shape in his toConnectNeighbors that will close the connected COmponent => All toConnectNeghbors have to be empty 
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put shape that can be connected to all his reservedBy neighbors 
					}
					//I need to know who has put the shape as his neighbour in the board 
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi1);
				placedShape.setJ(departj1);
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour))
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else if (departi1==0 && departj1<w-1)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:topBorderLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put shape that can be connected to all his reservedBy neighbors 
					}
					//I need to know who has put the shape as his neighbour in the board 
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi1);
				placedShape.setJ(departj1); 
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (departi1 == 0 && departj1 == (w - 1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:topRightCornerLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
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
				placedShape.setI(departi1);
				placedShape.setJ(departj1);
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else if (departi1<(h-1) && departj1==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:leftBorderLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put shape that can be connected to all his reservedBy neighbors 
					}
					//I need to know who has put the shape as his neighbour in the board 
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi1);
				placedShape.setJ(departj1);//On ne peut pas connaitre leur position à l'avance ... 
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (departi1<(h-1) && departj1==(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:rightBorderLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
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
				placedShape.setI(departi1);
				placedShape.setJ(departj1);//On ne peut pas connaitre leurs position à l'avance ... 
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) // && neighbour.getType()==0
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else if (departi1==h-1 && departj1==0)
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:bottomLeftCornerLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put shape that can be connected to all his reservedBy neighbors 
					}
					//I need to know who has put the shape as his neighbour in the board 
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi1);
				placedShape.setJ(departj1);
				board[departi1][departj1] = placedShape;
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); 
				}
			}
			else if (departi1==h-1 && departj1<(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:bottomBorderLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
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
				placedShape.setI(departi1);
				placedShape.setJ(departj1);
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				}
			}
			else if (departi1==h-1 && departj1==(w-1))
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:bottomRightCornerLegalShapes11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					}
					for(Shape toConnectsh:nextPlacedShape.getReservedBy())
					{
						if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put shape that can be connected to all his reservedBy neighbors 
					}
					//We need to know who has put the shape as his neighbour in the board 
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi1);
				placedShape.setJ(departj1);
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
					if(!toPutShapes.contains(neighbour)) 
						toPutShapes.add(neighbour); // We add the connection to close to an arraylist 
				} 
			}
			else 
			{
				ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
				for(Shape sh:allShape11)
				{
					boolean willWork=true;
					sh.setI(departi1);
					sh.setJ(departj1);
					Shape[] shneighbor = game.getConnectionNeighbors(sh);
					for(Shape nb:shneighbor)
					{
						if (nb.getType()!=0 && !game.areShapesConnected(sh, nb)) willWork=false;
					} 
						for(Shape toConnectsh:nextPlacedShape.getReservedBy())
						{
							if(!(game.areShapesConnected(toConnectsh, sh))) willWork=false; //We have to put a shape that can be connected to all his reservedBy neighbors 
						}
					//We need to know who has put the shape as his neighbor in the board 
					if (willWork) feasibleShapes.add(sh);
				}
				int randomIndex = rand.nextInt(feasibleShapes.size());
				Shape placedShape = feasibleShapes.get(randomIndex);
				placedShape.setI(departi1);
				placedShape.setJ(departj1); 
				board[departi1][departj1] = placedShape;
				connectedComponent.add(placedShape);
				for (Shape neighbour:game.getToConnectNeighbors(board[departi1][departj1]))
				{
					neighbour.setReservedBy(board[departi1][departj1]);
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