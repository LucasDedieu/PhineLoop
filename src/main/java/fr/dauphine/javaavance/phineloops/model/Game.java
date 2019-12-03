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

public class Game {
	private static int NORTH = 0;
	private static int SOUTH = 1;
	private static int EAST = 2;
	private static int WEST = 3;
	private int width;
	private int height;
	private Shape[][] board;
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
	
	
	public Shape[][] getBoard() {
		return board;
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
	
	public boolean isShapeFullyConnected(Shape shape) {
		if(shape == null) {
			return false;
		}
		Shape[] neighbors = getNeighbors(shape); 
		List<Connection> connections = shape.getConnections();
		if(countNeighbors(neighbors)<connections.size()) {
			return false;
		}
		
		for(Connection connection : connections) {
			switch(connection) {
				case NORTH : if (neighbors[NORTH] == null || !neighbors[NORTH].hasConnection(Connection.SOUTH)) {return false;} break;
				case SOUTH : if (neighbors[SOUTH] == null || !neighbors[SOUTH].hasConnection(Connection.NORTH)) {return false;} break;
				case EAST :  if (neighbors[EAST] == null || !neighbors[EAST].hasConnection(Connection.WEST)) {return false;} break;
				case WEST :  if (neighbors[WEST] == null || !neighbors[WEST].hasConnection(Connection.EAST)) {return false;} break;
				default : break;
			}
		}
		return true;
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
	
	
	
	
	
	
	
	
}
