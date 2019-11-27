package fr.dauphine.javaavance.phineloops.model;

public enum Connection {
	NORTH,
	SOUTH,
	EAST,
	WEST;
	
	public Connection getOppositeConnection() {
		switch(this) {
			case NORTH:return SOUTH;
			case SOUTH:return NORTH;
			case WEST:return EAST;
			default: return WEST;
		}
	}
	public Connection getNextConnection() {
		switch(this) {
			case NORTH:return EAST;
			case EAST:return SOUTH;
			case SOUTH:return WEST;
			default: return NORTH;
		}
	}
}
