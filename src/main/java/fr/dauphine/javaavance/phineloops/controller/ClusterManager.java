package fr.dauphine.javaavance.phineloops.controller;

import java.util.HashSet;
import java.util.Set;

import fr.dauphine.javaavance.phineloops.model.Cluster;
import fr.dauphine.javaavance.phineloops.model.EmptyShape;
import fr.dauphine.javaavance.phineloops.model.Game;
import fr.dauphine.javaavance.phineloops.model.QShape;
import fr.dauphine.javaavance.phineloops.model.Shape;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class ClusterManager {
	public static int NORTH = 0;
	public static int EAST = 1;
	public static int SOUTH = 2;
	public static int WEST = 3;
	private Set<Cluster> clusterSet = new ObjectOpenHashSet<>(100);
	private static final ClusterManager INSTANCE = new ClusterManager();
	
	private ClusterManager() {
		
	}
	
	public static ClusterManager getInstance() {
		return INSTANCE;
	}
	
	
	public Set<Cluster> findClusters(Game game){
		Shape[][] board = game.getBoard();
		for(int i = 0; i<game.getHeight(); i++) {
			for(int j = 0; j<game.getWidth(); j++) {
				Shape s = board[i][j];
				if(!s.isFrozen()) {
					addShape(board[i][j]);
				}
			}
		}
		return clusterSet;
	}
	
	public void addShape(Shape s) {
		Set<Cluster> acceptSet = new ObjectOpenHashSet<>();
		for(Cluster c : clusterSet) {
			if(c.accept(s)) {
				c.add(s);
				acceptSet.add(c);
			}
		}
		if(acceptSet.isEmpty()) {
			Cluster c  = new Cluster();
			c.add(s);
			clusterSet.add(c);
		}
		else {
			mergeClusters(acceptSet);
		}
	}
	
	private void mergeClusters(Set<Cluster> mergeSet) {
		if(mergeSet.size() == 1) {
			return ;
		}
		Cluster container = mergeSet.iterator().next();
		mergeSet.remove(container);
		for(Cluster c : mergeSet) {
			container.merge(c);
			clusterSet.remove(c);
		}
	}
	
	public Set<Game> getClusterGames(Game game){
		Shape[][] board = game.getBoard();
		Set<Game> games = new HashSet<Game>();
		for(Cluster c : clusterSet) {
			Shape[][] subBoard = getSubBoard(board, c);
			int height = subBoard.length;
			int width = subBoard[0].length;
			Game subGame = new Game(height, width);
			subGame.setBoard(subBoard);
			games.add(subGame);
		}
		return games;
	}
	
	
	
	public Shape[][] getSubBoard(Shape[][] board, Cluster c){
		int minI=c.getMinI();
		int minJ=c.getMinJ();
		int maxI=c.getMaxI();
		int maxJ=c.getMaxJ();
		
		int h = (maxI-minI)+1;
		int w = (maxJ-minJ)+1;
		
		Shape[][] subBoard = new Shape[h][w];
		for(int i = minI; i<=maxI; i++) {
			for(int j = minJ; j<=maxJ; j++) {
				Shape shape = board[i][j];
				subBoard[i-minI][j-minJ] = shape;
			}
		}
		for(int i = 0; i<h; i++) {
			for(int j = 0; j<w; j++) {
				Shape shape = subBoard[i][j];
				 if(!shape.isFrozen() && !c.contains(shape)) {
					 subBoard[i][j] = new EmptyShape(0, i, j);
					 subBoard[i][j].setFroze(true);
				 }
			}
		}
		subBoard = addBorder(board, subBoard, c);
		for(int i = 0; i<subBoard.length; i++) {
			for(int j = 0; j<subBoard[0].length; j++) {
				Shape shape = subBoard[i][j];
				shape.setI(i);
				shape.setJ(j);
			}
		}
		return subBoard;
	}
	
	
	private Shape[][] addBorder(Shape[][] board, Shape[][] subBoard, Cluster c) {	
		int minI=c.getMinI();
		int minJ=c.getMinJ();
		int originalHeight = board.length;
		int originalWidth = board[0].length;
		int newHeight = subBoard.length+2;
		int newWidth = subBoard[0].length+2;
		Shape[][] subBoardWithLimit = new Shape[newHeight][newWidth];
		
		//Enlarge Board 
		for(int i = 1; i<newHeight-1; i++) {
			for(int j = 1; j<newWidth-1; j++) {
				Shape shape = subBoard[i-1][j-1];
				subBoardWithLimit[i][j] = shape;		
			}
		}
		
		//Fill Border
		for(int i = 0; i<newHeight; i++) {
			for(int j = 0; j<newWidth; j++) {
				int originalI = minI+i-1;
				int originalJ = minJ+j-1;
				if(originalI < 0 || originalI>=originalHeight || originalJ<0 || originalJ>=originalWidth) {
					subBoardWithLimit[i][j] = new EmptyShape(0,i,j);
					subBoardWithLimit[i][j].setFroze(true);
					continue;
				}
				
				//TOP BORDER
				if(i==0) {
					if(board[originalI][originalJ].hasConnection(SOUTH)) {
						subBoardWithLimit[i][j] = new QShape(2,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}
					else {
						subBoardWithLimit[i][j] = new EmptyShape(0,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}
				}
				//BOTTOM BORDER
				else if(i==newHeight-1) {
					if(board[originalI][originalJ].hasConnection(NORTH)) {
						subBoardWithLimit[i][j] = new QShape(0,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}
					else {
						subBoardWithLimit[i][j] = new EmptyShape(0,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}
				}
				//LEFT BORDER
				else if(j==0) {
					if(board[originalI][originalJ].hasConnection(EAST)) {
						subBoardWithLimit[i][j] = new QShape(1,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}
					else {
						subBoardWithLimit[i][j] = new EmptyShape(0,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}		
				}
				//RIGHT BORDER
				else if(j==newWidth-1) {
					 if(board[originalI][originalJ].hasConnection(WEST)) {
						subBoardWithLimit[i][j] = new QShape(3,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}
					else {
						subBoardWithLimit[i][j] = new EmptyShape(0,i,j);
						subBoardWithLimit[i][j].setFroze(true);
					}		
				}		
				
			}
		}
		return subBoardWithLimit;
		
	}
}
