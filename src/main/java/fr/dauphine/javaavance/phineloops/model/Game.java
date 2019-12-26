package fr.dauphine.javaavance.phineloops.model;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


import fr.dauphine.javaavance.phineloops.generator.Generator;

public class Game {
	public static int NORTH = 0;
	public static int EAST = 1;
	public static int SOUTH = 2;
	public static int WEST = 3;
	private int width;
	private int height;
	private int maxcc;
	private Shape[][] board;

	public Game(int height, int width, int maxcc) {
		if(width<1) {
			throw new IllegalArgumentException("Width must be >= 1");
		}
		if(height<1) {
			throw new IllegalArgumentException("Height must be >= 1");
		}
		if(maxcc<=0 || maxcc>(width*height)/2) {
			//|| maxcc<= (width*height)/2 / The smallest connected component you can put in a grid fill 2 cases => maxcc must be < numberOfCases/2
			throw new IllegalArgumentException("Maximum cc must be positive and you have to put at least less cc than the number of cases divided by 2");
			//FIXME
		}
		this.width = width;
		this.height = height;
		this.maxcc = maxcc;
		board = new Shape[height][width];
	}

	public Game( int height, int width) {
		if(width<1) {
			throw new IllegalArgumentException("Width must be >= 1");
		}
		if(height<1) {
			throw new IllegalArgumentException("Height must be >= 1");
		}
		this.height = height;
		this.width = width;
		board = new Shape[height][width];
	}

	

	public Game(Game game) {
		this.height = game.height;
		this.width = game.width;
		board = new Shape[height][width];
		Shape[][] boardToClone = game.board;
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				Shape s = boardToClone[i][j];
				switch(s.getType()){
					case 0 : board[i][j] = new EmptyShape(s.getOrientation(), i, j);break;
					case 1 : board[i][j] = new QShape(s.getOrientation(), i, j);break;
					case 2 : board[i][j] = new IShape(s.getOrientation(), i, j);break;
					case 3 : board[i][j] = new TShape(s.getOrientation(), i, j);break;
					case 4 : board[i][j] = new XShape(s.getOrientation(), i, j);break;
					case 5 : board[i][j] = new LShape(s.getOrientation(), i, j);break;
				}
			}
		}
	}



	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Shape[][] getBoard() {
		return board;
	}
	public void setBoard(Shape[][] board) {
		this.board = board;
	}

	public int getMaxCC()
	{
		return this.maxcc;
	}

	
	/**
	 * Generate a random shuffle board for the game. Board is solvable
	 */
	public void generate() {
		Generator generator = new Generator(this);
		generator.generate();
	}
	
	/*
	 *  Generate a valid board for the game
	 */
	public void generateSolution() {
		Generator generator = new Generator(this);
		generator.generateSolution();
	}
	
	/**
	 * Generate a random shuffle board for the game. Board is solvable and has a limit of connected components
	 * @param nbcc : maximum number of connected component
	 */
	public void generate(int nbcc) {
		Generator generator = new Generator(this);
		generator.generate(nbcc);
	}


	/**
	 *  Generate a valid board for the game. Board has a limit of connected components
	 * @param nbcc: maximum number of connected component
	 */
	public void generateSolution(int nbcc) {
		Generator generator = new Generator(this);
		generator.generateSolution(nbcc);
	}
	
	


	/**
	 * Write the board in a file
	 * @param outputFile :the file
	 * @throws FileNotFoundException
	 */
	public void write(String outputFile) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(outputFile);
		PrintStream ps = new PrintStream(fos);
		ps.println(height);
		ps.println(width);
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				ps.println(board[i][j]);
			}
		}
		ps.close();
	}

	/**
	 * Check if a shape is fully well connected with its neighbors.
	 * @param shape : shape to check
	 * @return true is shape well connected
	 */
	public boolean isShapeFullyConnected(Shape shape) {
		return isShapeWellConnectedWithEast(shape)&&isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithWest(shape);
	}

	
	/**
	 * Count all the "non-border" neighbors of a shape
	 * @param neighbors :all the neighbors of a shape
	 * @return number of real neighbors
	 */
	private int countNeighbors(Shape[] neighbors) {
		int count = 0;
		for(int i=0;i<4;i++) {
			if(neighbors[i] != null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Check if a shape is connected to the border.
	 * @param shape: shape to test
	 * @return true if shape is connected to the border
	 */
	public boolean iShapeConnectedToBoardBorder(Shape shape) {
		int i = shape.getI();
		int j = shape.getJ();
		
		if(i>0 && j>0 && i<height-1 && j<width-1 ) {
			return false;
		}
		if(i == 0) {
			if(shape.getConnections()[NORTH]) {
				return true;
			}
		}
		else if(i == height-1) {
			if(shape.getConnections()[SOUTH]) {
				return true;
			}
		}
		if(j == 0) {
			if(shape.getConnections()[WEST]) {
				return true;
			}
		}
		else if(j == width-1) {
			if(shape.getConnections()[EAST]) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Check if a shape is connected with North and West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndWest(Shape shape) {
		return isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithWest(shape);
	}

	/**
	 * Check if a shape is connected with North and East.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndEast(Shape shape) {
		return isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithEast(shape);
	}

	/**
	 * Check if a shape is connected with South and East.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithSouthAndEast(Shape shape) {
		return isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithEast(shape);
	}

	/**
	 * Check if a shape is connected with South and West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithSouthAndWest(Shape shape) {
		return isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithWest(shape);
	}
	
	/**
	 * Check if a shape is connected with North and South.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndSouth(Shape shape) {
		return isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithNorth(shape);
	}
	
	/**
	 * Check if a shape is connected with East and West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithEastAndWest(Shape shape) {
		return isShapeWellConnectedWithEast(shape)&&isShapeWellConnectedWithWest(shape);
	}
	
	/**
	 * Check if a shape is connected with East, North and West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithEastAndSouthAndWest(Shape shape) {
		return isShapeWellConnectedWithEast(shape)&&isShapeWellConnectedWithWest(shape)&&isShapeWellConnectedWithSouth(shape);
	}
	
	/**
	 * Check if a shape is connected with North, South and West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndSouthAndWest(Shape shape) {
		return isShapeWellConnectedWithNorth(shape)&&isShapeWellConnectedWithWest(shape)&&isShapeWellConnectedWithSouth(shape);
	}
	
	/**
	 * Check if a shape is connected with North, East and West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndEastAndWest(Shape shape) {
		return isShapeWellConnectedWithEast(shape)&&isShapeWellConnectedWithWest(shape)&&isShapeWellConnectedWithNorth(shape);
	}
	
	/**
	 * Check if a shape is connected with North, East and South.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndEastAndSouth(Shape shape) {
		return isShapeWellConnectedWithEast(shape)&&isShapeWellConnectedWithSouth(shape)&&isShapeWellConnectedWithNorth(shape);
	}
 
	/**
	 * Check if a shape is connected with South.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithSouth(Shape shape) {
		boolean[] connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(i+1<height) {
			neighbor = board[i+1][j];
		}
		if (neighbor != null ) {
			if(neighbor.getConnections()[NORTH]) {
				if (!connections[SOUTH]) {
					return false;
				}
			}
			else if (connections[SOUTH]){
				return false;
			}
		}
		else if (connections[SOUTH]) {
			return false;
		}
		return true;
	}

	/**
	 * Check if a shape is connected with North.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorth(Shape shape) {
		boolean[] connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(i-1>=0) {
			neighbor = board[i-1][j];
		}
		if (neighbor != null ) {
			if(neighbor.getConnections()[SOUTH]) {
				if (!connections[NORTH]) {
					return false;
				}
			}
			else if (connections[NORTH]){
				return false;
			}
		}
		else if (connections[NORTH]) {
			return false;
		}
		return true;
	}

	
	/**
	 * Check if a shape is connected with East.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithEast(Shape shape) {
		boolean[] connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(j+1<width) {
			neighbor = board[i][j+1];
		}
		if (neighbor != null ) {
			if(neighbor.getConnections()[WEST]) {
				if (!connections[EAST]) {
					return false;
				}
			}
			else if (connections[EAST]){
				return false;
			}
		}
		else if (connections[EAST]) {
			return false;
		}
		return true;
	}

	/**
	 * Check if a shape is connected with West.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithWest(Shape shape) {
		boolean[] connections = shape.getConnections();
		int i = shape.getI();
		int j = shape.getJ();
		Shape neighbor = null;
		if(j-1>=0) {
			neighbor = board[i][j-1];
		}
		if (neighbor != null ) {
			if(neighbor.getConnections()[EAST]) {
				if (!connections[WEST]) {
					return false;
				}
			}
			else if (connections[WEST]){
				return false;
			}
		}
		else if (connections[WEST]) {
			return false;
		}
		return true;
	}

	/**
	 * Check if a shape is connected with North and West frozen neighbors.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithNorthAndWestFrozenNeighbors(Shape shape) {
		//North
		int i = shape.getI();
		int j = shape.getJ();
		//NORTH
		Shape neighbor = null;
		if(i-1>=0) {
			neighbor = board[i-1][j];
		}
		if(neighbor != null && neighbor.isFroze()) {
			if(!isShapeWellConnectedWithNorth(shape)) {
				return false;
			}
		}
		//WEST
		neighbor = null;
		if(j-1>=0) {
			neighbor = board[i][j-1];
		}
		if(neighbor != null && neighbor.isFroze()) {
			if(!isShapeWellConnectedWithWest(shape)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if a shape is connected with all its frozen neighbors.
	 * @param shape :shape to test
	 * @return true if shape well connected
	 */
	public boolean isShapeWellConnectedWithFrozenNeighbors(Shape shape) {
		//North
		int i = shape.getI();
		int j = shape.getJ();
		//NORTH
		Shape neighbor = null;
		if(i-1>=0) {
			neighbor = board[i-1][j];
		}
		if(neighbor != null && neighbor.isFroze()) {
			if(!isShapeWellConnectedWithNorth(shape)) {
				return false;
			}
		}
		//WEST
		neighbor = null;
		if(j-1>=0) {
			neighbor = board[i][j-1];
		}
		if(neighbor != null && neighbor.isFroze()) {
			if(!isShapeWellConnectedWithWest(shape)) {
				return false;
			}
		}
		//EAST
		neighbor = null;
		if(j+1<width) {
			neighbor = board[i][j+1];
		}
		if(neighbor != null && neighbor.isFroze()) {
			if(!isShapeWellConnectedWithEast(shape)) {
				return false;
			}
		}
		//SOUTH
		neighbor = null;
		if(i+1<height) {
			neighbor = board[i+1][j];
		}
		if(neighbor != null && neighbor.isFroze()) {
			if(!isShapeWellConnectedWithSouth(shape)) {
				return false;
			}
		}
		return true;
		
		
	}
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * JAVA DOC ALBAN 
	 */
	public boolean areShapesConnected(Shape shape1,Shape shape2)
	{
			boolean areShapesConnected=false;
			if (shape1.getConnections()[EAST] && shape2.getConnections()[WEST] && shape1.getJ()+1==shape2.getJ()) areShapesConnected = true;
			else if (shape1.getConnections()[WEST] && shape2.getConnections()[EAST] && shape1.getJ()-1==shape2.getJ()) areShapesConnected = true;
			else if (shape1.getConnections()[NORTH] && shape2.getConnections()[SOUTH] && shape1.getI()-1==shape2.getI()) areShapesConnected = true;
			else if (shape1.getConnections()[SOUTH] && shape2.getConnections()[NORTH] && shape1.getI()+1==shape2.getI()) areShapesConnected = true;
			return areShapesConnected;
	}
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * JAVA DOC ALBAN 
	 */
	public boolean lookingButNotConnected(Shape shape1,Shape shape2)
	{
		boolean cc=false;
		boolean[] connections = shape1.getConnections();
		Shape[] neighbors = getNeighbors(shape1);
		if (neighbors[NORTH] != null && neighbors[NORTH]==shape2 && connections[NORTH] && !shape2.hasConnection(SOUTH)) cc=true;
		if (neighbors[SOUTH] != null && neighbors[SOUTH]==shape2 && connections[SOUTH] && !shape2.hasConnection(NORTH)) cc=true;
		if (neighbors[EAST] != null && neighbors[EAST]==shape2 && connections[EAST] && !shape2.hasConnection(WEST)) 
			{
				cc=true;
			}
		if (neighbors[WEST] != null && neighbors[WEST]==shape2 && connections[WEST] && !shape2.hasConnection(EAST)) cc=true;
		return cc;
		
	}
	
	
	/*public Shape reservedBy(Shape shape1)
	{
		
	}*/

	
	/**
	 * Return neighbors of a shape
	 * @param shape : the shape
	 * @return the neighbors
	 */
	public Shape[] getNeighbors(Shape shape){
		int i = shape.getI();
		int j = shape.getJ();

		Shape[] neighbors = new Shape[4];

		//north neighbor
		if(i-1>=0) {
			neighbors[NORTH] = board[i-1][j];
		}
		//south neighbor
		if(i+1<height) {
			neighbors[SOUTH] = board[i+1][j];
		}
		//east neighbor
		if(j+1<width) {
			neighbors[EAST] = board[i][j+1];
		}
		//west neighbor
		if(j-1>=0) {
			neighbors[WEST] = board[i][j-1];
		}
		return neighbors;
	}

	
	/**
	 * 
	 * @param shape
	 * @return
	 */
	public Shape[] getToConnectNeighbors(Shape shape){
		int i = shape.getI();
		int j = shape.getJ();
		
		ArrayList<Shape> aux = new ArrayList<Shape>();
		//Shape[] neighbors = new Shape[4];
		boolean[] connections = shape.getConnections();
		
		//north neighbor
		if(i-1>=0 && connections[NORTH] && board[i-1][j]!=null && board[i-1][j].getType()==0 ) {
			aux.add(board[i-1][j]);
		}
		//south neighbor
		if(i+1<height && connections[SOUTH] && board[i+1][j]!=null && board[i+1][j].getType()==0 ) {
			aux.add(board[i+1][j]);
		}
		//east neighbor
		if(j+1<width  && connections[EAST] && board[i][j+1]!=null && board[i][j+1].getType()==0 ) {
			aux.add(board[i][j+1]);
		}
		//west neighbor
		if(j-1>=0  && connections[WEST] && board[i][j-1]!=null && board[i][j-1].getType()==0 ) {
			aux.add(board[i][j-1]);
		}
		Shape[] neighbors = new Shape[aux.size()];
		for (int k=0;k<aux.size();k++)
		{
			neighbors[k]=aux.get(k);
		}
		return neighbors;
	}
	
	
	public void addShape(Shape shape) throws Exception {
		if(shape == null) {
			return;
		}
		int shapeI =shape.getI();
		int shapeJ = shape.getJ();
		if(board[shapeI][shapeJ] == null) {
			board[shapeI][shapeJ] = shape;
			return;
		}
		throw new Exception("There is already a shape at "+shapeI+","+shapeJ);
	}
	
	public int getQOrientationForOpenConnection(Shape shape)
	{
		int qorientation=0;
		if(!isShapeWellConnectedWithNorth(shape)) qorientation= 2;
		else if (!isShapeWellConnectedWithEast(shape)) qorientation= 3;
		else if (!isShapeWellConnectedWithSouth(shape)) qorientation= 0;
		else if (!isShapeWellConnectedWithWest(shape)) qorientation= 1;
		return qorientation;
	}
	
	public boolean hasEmptyNeighbor(Shape shape)
	{
		boolean hasEmptyNeighbor=false;
		Shape[] nb=this.getNeighbors(shape);
		for (Shape sh:nb)
		{
			if(sh!=null && sh.getType()==0) hasEmptyNeighbor=true;
		}
		return hasEmptyNeighbor;
	}

	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<height;i++) {
			for(int j =0; j<width;j++) {
				Shape shape = board[i][j];
				/*
				boolean f = shape.isFroze();
				
				if(f) {
					sb.append("\033[36m");
				}
				else if(shape.getDomainSize() == 0) {
					sb.append("\033[92m");
				}
				else if(shape.getDomainSize() == 1) {
					sb.append("\033[35m");
				}
				else if(shape.getDomainSize() == 2) {
					sb.append("\033[31m");
				}
				else if(shape.getDomainSize() == 3) {
					sb.append("\033[33m");
				}*/
				sb.append(shape.getSymbol());/*
				if(f) {
					sb.append("\033[0m");
				}
				else if(shape.getDomainSize() == 0) {
					sb.append("\033[0m");
				}
				else if(shape.getDomainSize() == 1) {
					sb.append("\033[0m");
				}
				else if(shape.getDomainSize() == 2) {
					sb.append("\033[0m");
				}
				else if(shape.getDomainSize() == 3) {
					sb.append("\033[0m");
				}*/
				
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
