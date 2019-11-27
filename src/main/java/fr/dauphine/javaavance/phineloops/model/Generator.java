package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Generator {
	private Game game;
	
	
	public Generator(Game game) {
		this.game = game;
	}
	
	public Game generate() {
		int h = game.getHeight();
		int w = game.getWidth();
		Shape[][] board = game.getBoard();
		//int topLeftCornerLegalShapes[][]= {{0,0},{1,1},{1,2},{5,1}};
		Shape topLeftCornerLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new LShape(1, 0,0)};
		//int topBorderLegalShapes[][]={{0,0},{1,1},{1,2},{1,3},{2,1},{3,2},{5,1},{5,2}}; //Pour (0,1) random entre emptyShape,Ishapen.orientation.EAST ou Ishapen.orientation.WEST, Lshape.SOUTH_WEST ou Lshape.SOUTH_EAST,Tshape.SOUTH,Qshape	
		Shape topBorderLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w),new IShape(1,w,w),new TShape(2,w,w),new LShape(1, 0,0),new LShape(2, 0,0)};
	    //int topRightCornerLegalShapes[][]= {{0,0},{1,2},{1,3},{5,2}};
		Shape topRightCornerLegalShapes[]= {new EmptyShape(0, w, w),new QShape(2, w, w),new QShape(3, w, w),new LShape(2, 0,0)};
		//int leftBorderLegalShapes[][]={{0,0},{1,1},{1,0},{1,2},{2,0},{3,1},{5,0},{5,1}};
		Shape leftBorderLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(0, w, w),new QShape(2, w, w),new IShape(0,w,w),new TShape(1,w,w),new LShape(0, 0,0),new LShape(1, 0,0)};
		//int BottomLeftCornerLegalShapes[][]= {{0,0},{1,0},{1,1},{5,0}};
		Shape bottomLeftCornerLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new LShape(0, 0,0)};
		//int bottomBorderLegalShapes[][]={{0,0},{1,0},{1,1},{1,3},{2,1},{3,0},{5,0},{5,3}};
		Shape bottomBorderLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(3, w, w),new IShape(1,w,w),new TShape(0,w,w),new LShape(0, 0,0),new LShape(3, 0,0)};
		//int bottomRightCornerLegalShapes[][]= {{0,0},{1,0},{1,3},{5,3}};
		Shape bottomRightCornerLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(3, w, w),new LShape(3, 0,0)};
		//int rightBorderLegalShapes[][]={{0,0},{1,0},{1,1},{1,2},{2,1},{3,3},{5,2},{5,3}};
		Shape rightBorderLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new IShape(0,w,w),new TShape(3,w,w),new LShape(2, 0,0),new LShape(3, 0,0)};
		List<Shape> allShape= Arrays.asList(new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w),new IShape(0,w,w),new IShape(1,w,w),new TShape(0,w,w),new TShape(1,w,w),new TShape(2,w,w),new XShape(0, 0, 0),new LShape(0, 0,0),new LShape(1, 0,0),new LShape(2, 0,0),new LShape(3, 0,0));
		Random rand = new Random();
		for(int i=0; i<h;i++) {
			for(int j =0; j<w;j++) {
				if (i==0 && j==0) //Top Left Corner
				{
					int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
					board[i][j] = topLeftCornerLegalShapes[randomIndex];
				}
				else if (i==0 && j<w-1) // Top Border
				{
					if (board[i][j-1].connections.contains(Connection.EAST))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topBorderLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topBorderLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==0 && j==(w-1)) // Top Right Corner
				{
					if (board[i][j-1].connections.contains(Connection.EAST))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topRightCornerLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topRightCornerLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i<h-1 && j==0) //Left Border
				{
					if (board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:leftBorderLegalShapes)
						{
							if (shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:leftBorderLegalShapes)
						{
							if (!shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i<h-1 && j==w-1) // Right Border
				{

					if (board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].connections.contains(Connection.EAST) && !board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==h-1 && j==0) // Bottom Left Corner 
				{
					if (board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:bottomLeftCornerLegalShapes)
						{
							if (shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:bottomLeftCornerLegalShapes)
						{
							if (!shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==h-1 && j<w-1) // Bottom Border
				{
					if (board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].connections.contains(Connection.EAST) && !board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==h-1 && j==w-1) // Bottom Right Corner
				{
					if (board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].connections.contains(Connection.EAST) && !board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (!shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else //Rest of cases 
				{
					if (board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].connections.contains(Connection.EAST) && !board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].connections.contains(Connection.EAST) && board[i-1][j].connections.contains(Connection.SOUTH))
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (!shape.connections.contains(Connection.WEST) && shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (!shape.connections.contains(Connection.WEST) && !shape.connections.contains(Connection.NORTH)) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
			}
		}
		//TODO : MODIFY THE ORIENTATION OF EACH SHAPE 
		return null;
	}
}
