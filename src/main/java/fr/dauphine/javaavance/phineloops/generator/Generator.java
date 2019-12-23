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
	private int node = 0;
	private int road = 0;
<<<<<<< HEAD
	private int maxCc; 
	
	
	
=======
	private int maxCc; //DON'T PUT MORE CONNECTED COMPONENTS THAN CC ! 



>>>>>>> branch 'master' of https://github.com/Dauphine-Java-M1/phineloops-alt.git
	public Generator(Game game) {
		this.game = game;
		this.maxCc = game.getMaxCC(); 
	}
<<<<<<< HEAD
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * JAVA DOC : GENERATE A GAME WITH RANDOM SHAPES
	 */
	public void generateSolution() {
=======



	public void generate() {
>>>>>>> branch 'master' of https://github.com/Dauphine-Java-M1/phineloops-alt.git
		int h = game.getHeight();
		int w = game.getWidth();
		Shape[][] board = game.getBoard();
		//Generation of the set of Legal Shapes 
		Shape topLeftCornerLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new LShape(1, 0,0)};
		Shape topBorderLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w),new IShape(1,w,w),new TShape(2,w,w),new LShape(1, 0,0),new LShape(2, 0,0)};
		Shape topRightCornerLegalShapes[]= {new EmptyShape(0, w, w),new QShape(2, w, w),new QShape(3, w, w),new LShape(2, 0,0)};
		Shape leftBorderLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(0, w, w),new QShape(2, w, w),new IShape(0,w,w),new TShape(1,w,w),new LShape(0, 0,0),new LShape(1, 0,0)};
		Shape bottomLeftCornerLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new LShape(0, 0,0)};
		Shape bottomBorderLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(3, w, w),new IShape(1,w,w),new TShape(0,w,w),new LShape(0, 0,0),new LShape(3, 0,0)};
		Shape bottomRightCornerLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(3, w, w),new LShape(3, 0,0)};
		Shape rightBorderLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(3, w, w),new QShape(2, w, w),new IShape(0,w,w),new TShape(3,w,w),new LShape(2, 0,0),new LShape(3, 0,0)};
		//restriction for connected components case
		//Shape rightTripleBorderLegalShape[]
		List<Shape> allShape= Arrays.asList(new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w),new IShape(0,w,w),new IShape(1,w,w),new TShape(0,w,w),new TShape(1,w,w),new TShape(2,w,w),new XShape(0, 0, 0),new LShape(0, 0,0),new LShape(1, 0,0),new LShape(2, 0,0),new LShape(3, 0,0));
		Random rand = new Random();

		//Grid Traversal putting each correct shape regarding the legal and feasible shapes 
		for(int i=0; i<h;i++) {
			for(int j =0; j<w;j++) {


				if (i==0 && j==0) //Top Left Corner
				{
					int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
					board[i][j] = topLeftCornerLegalShapes[randomIndex];
				}



				else if (i==0 && j<w-1) // Top Border
				{
					if (board[i][j-1].getConnections()[EAST])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topBorderLegalShapes)
						{
							if (shape.getConnections()[WEST]) feasibleShapes.add(shape);
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
							if (!shape.getConnections()[WEST]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==0 && j==(w-1)) // Top Right Corner
				{
					if (board[i][j-1].getConnections()[EAST])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topRightCornerLegalShapes)
						{
							if (shape.getConnections()[WEST]) feasibleShapes.add(shape);
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
							if (!shape.getConnections()[WEST]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i<h-1 && j==0) //Left Border
				{
					if (board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:leftBorderLegalShapes)
						{
							if (shape.getConnections()[NORTH]) feasibleShapes.add(shape);
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
							if (!shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i<h-1 && j==w-1) // Right Border
				{

					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else 
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==h-1 && j==0) // Bottom Left Corner 
				{
					if (board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:bottomLeftCornerLegalShapes)
						{
							if (shape.getConnections()[NORTH]) feasibleShapes.add(shape);
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
							if (!shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==h-1 && j<w-1) // Bottom Border
				{
					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else if (i==h-1 && j==w-1) // Bottom Right Corner
				{
					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
				else //Rest of cases 
				{
					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
					else
					{
						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						int randomIndex = rand.nextInt(feasibleShapes.size());
						board[i][j]=feasibleShapes.get(randomIndex);
					}
				}
			}
		}
		System.out.println(game);
		/*for (Shape[] shapes:board)
		{
			for (Shape shape:shapes)
			{
				for (int i=0;i<rand.nextInt(4);i++) shape.rotate();
			}
		}*/
	}
	
	/**
	 * 
	 * @param game
	 */
	public void shuffleGame(Game game)
	{
		Random rand = new Random();
		for (Shape[] shapes:game.getBoard())
		{
			for (Shape shape:shapes)
			{
				for (int i=0;i<rand.nextInt(4);i++) shape.rotate();
			}
		}
<<<<<<< HEAD
	}
	
	public void generate()
	{
		this.generateSolution();
		this.shuffleGame(this.game);
	}
	
	

	/** Generate a solution with exactly cc connected component, we will favour the empty shapes 
	 * 
	 * @param cc
	 */
	public void generate(int cc)
	{
		
		
		
		
		
=======

>>>>>>> branch 'master' of https://github.com/Dauphine-Java-M1/phineloops-alt.git
	}

	public void generate(int nbcc) {
		int h = game.getHeight();
		int w = game.getWidth();
		Shape[][] board = game.getBoard();
		//Generation of the set of Legal Shapes 
		Shape topLeftCornerLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new LShape(1, 0,0)};
		Shape topBorderLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w),new IShape(1,w,w),new TShape(2,w,w),new LShape(1, 0,0),new LShape(2, 0,0)};
		Shape topRightCornerLegalShapes[]= {new EmptyShape(0, w, w),new QShape(2, w, w),new QShape(3, w, w),new LShape(2, 0,0)};
		Shape leftBorderLegalShapes[]= {new EmptyShape(0, w, w),new QShape(1, w, w),new QShape(0, w, w),new QShape(2, w, w),new IShape(0,w,w),new TShape(1,w,w),new LShape(0, 0,0),new LShape(1, 0,0)};
		Shape bottomLeftCornerLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new LShape(0, 0,0)};
		Shape bottomBorderLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(3, w, w),new IShape(1,w,w),new TShape(0,w,w),new LShape(0, 0,0),new LShape(3, 0,0)};
		Shape bottomRightCornerLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(3, w, w),new LShape(3, 0,0)};
		Shape rightBorderLegalShapes[]={new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(3, w, w),new QShape(2, w, w),new IShape(0,w,w),new TShape(3,w,w),new LShape(2, 0,0),new LShape(3, 0,0)};
		//restriction for connected components case
		//Shape rightTripleBorderLegalShape[]
		List<Shape> allShape= Arrays.asList(new EmptyShape(0, w, w),new QShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w),new IShape(0,w,w),new IShape(1,w,w),new TShape(0,w,w),new TShape(1,w,w),new TShape(2,w,w),new XShape(0, 0, 0),new LShape(0, 0,0),new LShape(1, 0,0),new LShape(2, 0,0),new LShape(3, 0,0));
		Random rand = new Random();

		int nbNode = 0;
		int nbLink = 0;
		int lastNbNode = 0;
		int lastNbLink = 0;
		Shape noLinkLegalShape[] = {new EmptyShape(0, w, w)};
		Shape oneLinkLegalShape[] = {new QShape(0, w, w),new QShape(1, w, w),new QShape(2, w, w),new QShape(3, w, w)};
		Shape twoLinkLegalShape[] = {new IShape(0,w,w),new IShape(1,w,w),new LShape(0, 0,0),new LShape(1, 0,0),new LShape(2, 0,0),new LShape(3, 0,0)};
		Shape threeLinkLegalShape[] = {new TShape(0, 0,0),new TShape(1, 0,0),new TShape(2, 0,0),new TShape(3, 0,0)};
		Shape fourLinkLegalShape[] = {new QShape(0,0,0)};


		//Grid Traversal putting each correct shape regarding the legal and feasible shapes 
		for(int i=0; i<h;i++) {
			for(int j =0; j<w;j++) {

				if (i==0 && j==0) //Top Left Corner
				{

					int randomIndex = rand.nextInt(topLeftCornerLegalShapes.length);
					board[i][j] = topLeftCornerLegalShapes[randomIndex];
					Shape shape =board[i][j];
					if(shape.getType()!=0) {
						nbNode++ ;

						nbLink += shape.getNbConnection();

					}
					lastNbNode = nbNode;
					lastNbLink = nbLink;

				}



				else if (i==0 && j<w-1) // Top Border
				{
					if (board[i][j-1].getConnections()[EAST])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topBorderLegalShapes)
						{
							if (shape.getConnections()[WEST]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());

							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else 
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							//random parmi les shapes qui n'ont pas connection WEST 
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else if (i==0 && j==(w-1)) // Top Right Corner
				{
					if (board[i][j-1].getConnections()[EAST])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topRightCornerLegalShapes)
						{
							if (shape.getConnections()[WEST]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else 
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:topRightCornerLegalShapes)
						{
							if (!shape.getConnections()[WEST]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else if (i<h-1 && j==0) //Left Border
				{
					if (board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:leftBorderLegalShapes)
						{
							if (shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else 
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:leftBorderLegalShapes)
						{
							if (!shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else if (i<h-1 && j==w-1) // Right Border
				{

					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else 
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:rightBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else if (i==h-1 && j==0) // Bottom Left Corner 
				{
					if (board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:bottomLeftCornerLegalShapes)
						{
							if (shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui ont connection WEST
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else 
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>() ;
						for (Shape shape:bottomLeftCornerLegalShapes)
						{
							if (!shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						//random parmi les shapes qui n'ont pas connection WEST 
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else if (i==h-1 && j<w-1) // Bottom Border
				{
					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomBorderLegalShapes)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else if (i==h-1 && j==w-1) // Bottom Right Corner
				{
					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:bottomRightCornerLegalShapes)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
				else //Rest of cases 
				{
					if (board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (board[i][j-1].getConnections()[EAST] && !board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else if (!board[i][j-1].getConnections()[EAST] && board[i-1][j].getConnections()[SOUTH])
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (!shape.getConnections()[WEST] && shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
					else
					{

						ArrayList<Shape> feasibleShapes = new ArrayList<Shape>();
						for (Shape shape:allShape)
						{
							if (!shape.getConnections()[WEST] && !shape.getConnections()[NORTH]) feasibleShapes.add(shape);
						}
						do {
							nbNode = lastNbNode;
							nbLink = lastNbLink;
							int randomIndex = rand.nextInt(feasibleShapes.size());
							board[i][j]=feasibleShapes.get(randomIndex);
							Shape shape =board[i][j];
							if(shape.getType()!=0) {
								nbNode++ ;
								nbLink += shape.getNbConnection();
							}

						}while(!(nbLink == (nbNode*2)+1));
						lastNbNode = nbNode;
						lastNbLink = nbLink;
					}
				}
			}
		}
		System.out.println(game);
		for (Shape[] shapes:board)
		{
			for (Shape shape:shapes)
			{
				for (int i=0;i<rand.nextInt(4);i++) shape.rotate();
			}
		}

	}

}
