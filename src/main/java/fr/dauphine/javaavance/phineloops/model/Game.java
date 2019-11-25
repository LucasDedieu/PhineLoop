package fr.dauphine.javaavance.phineloops.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class Game {
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
		return true;
	}
	
	private List<Shape> getNeighbors(){
		return null;
	}
	
	private boolean shapesAreConnected(Shape s1, Shape s2) {
		return true;
	}
	
	
	
	
	
	
	
}
