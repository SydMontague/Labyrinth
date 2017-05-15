package de.telekom.jhsc.labyrinth;

public enum Direction {
	UP(0,-1), RIGHT(1,0), DOWN(0,1), LEFT(-1,0);
	
	int xMod;
	int yMod;
	
	private Direction(int xMod, int yMod) {
		this.xMod = xMod;
		this.yMod = yMod;
	}
}