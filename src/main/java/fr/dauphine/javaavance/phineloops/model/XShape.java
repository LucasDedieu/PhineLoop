package fr.dauphine.javaavance.phineloops.model;

public class XShape extends Shape {

	private int[] domain = {0};

	private int type = 4;
	private int nbConnection = 4;
	
	public XShape(int orientation,int i, int j) {
		super(orientation,i,j);
		this.possibleOrientation = new boolean[]{true};
		this.domainSize = 1;
		connections[NORTH]=true; connections[SOUTH] = true; connections[WEST] =true; connections[EAST] =true;
	}
	@Override
	public void rotate() {
	}


	
	public int[] getDomainWithPruning(Game game) {
		return domain;
	}
	
	public String getSymbol() {
		return "┼";
	}
	
	public int getMaxRotation() {
		return 0;
	}
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}
	public int getNbConnection() {
		return nbConnection;
	}
	public void setNbConnection(int nbConnection) {
		this.nbConnection = nbConnection;
	}
	@Override
	public void rotateTo(int orientation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int[] getDomain() {
		// TODO Auto-generated method stub
		return this.domain;
	}
}
