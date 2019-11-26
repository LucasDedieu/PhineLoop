package fr.dauphine.javaavance.phineloops.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
	private static int NORTH = 0;
	private static int SOUTH = 1;
	private static int EAST = 2;
	private static int WEST = 3;
	private int width;
	private int height;
	private Shape[][] board;
	public Shape[][] getBoard() {
		return board;
	}

	private int cc;
	
	public Game(int width, int height, int cc) {
		if(width<1) {
			throw new IllegalArgumentException("Width must be >= 1");
		}
		if(height<1) {
			throw new IllegalArgumentException("Height must be >= 1");
		}
		if(cc<0) {
			throw new IllegalArgumentException("Number of  must be positive");
			//FIXME
		}
		this.width = width;
		this.height = height;
		this.cc = cc;
		board = new Shape[height][width];
	}


	public Game(File file) {
		load(file);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void generate() {
		Generator generator = new Generator(this);
		generator.generate();
	}
	
	private void load(File file) {
		
	}
	
	public void solve() {
		
	}
	
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
	}
	
	public boolean shapeIsFullyConnected(Shape shape) {
		Shape[] neighbors = getNeighbors(shape); 
		List<Connection> connections = shape.getConnections();
		if(countNeighbors(neighbors)<connections.size()) {
			return false;
		}
		boolean isShapeFullyConnected = true;
		for(Connection connection : connections) {
			switch(connection) {
				case NORTH : isShapeFullyConnected = isShapeFullyConnected && neighborContainConnection(neighbors[NORTH], Connection.SOUTH);break;
				case SOUTH : isShapeFullyConnected = isShapeFullyConnected && neighborContainConnection(neighbors[SOUTH], Connection.NORTH);break;
				case EAST :  isShapeFullyConnected = isShapeFullyConnected && neighborContainConnection(neighbors[EAST], Connection.WEST);break;
				case WEST :  isShapeFullyConnected = isShapeFullyConnected && neighborContainConnection(neighbors[WEST], Connection.EAST);break;
				default : break;
			}
		}
		return isShapeFullyConnected;
	}
	
	private int countNeighbors(Shape[] neighbors) {
		int count = 0;
		for(int i=0;i<4;i++) {
			if(neighbors[i] != null) {
				count++;
			}
		}
		return count;
	}
	
	public boolean neighborContainConnection(Shape neighbor,Connection connection) {
		if(neighbor == null) {
			return false;
		}
		return neighbor.getConnections().contains(connection);
			
	}
	
	
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
	
	public void addShape(Shape shape) throws Exception {
		int shapeI =shape.getI();
		int shapeJ = shape.getJ();
		if(board[shapeI][shapeJ] == null) {
			board[shapeI][shapeJ] = shape;
			return;
		}
		throw new Exception("There is already a shape at "+shapeI+","+shapeJ);
			
		
	}
	
	
	
	
	
	
	
	
}
