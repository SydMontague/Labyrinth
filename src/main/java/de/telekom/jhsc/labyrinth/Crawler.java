package de.telekom.jhsc.labyrinth;

class Crawler {
	private Labyrinth labyrinth;
	private int x;
	private int y;
	private int objectsFound;
	
	private int moveCount;

	public Crawler(Labyrinth labyrinth) {
		this.labyrinth = labyrinth;
	}

	public boolean move(Direction dir) {
		if (!getCurrentTile().isPassable(dir))
			return false;

		moveCount++;
		x += dir.xMod;
		y += dir.yMod;

		if (getCurrentTile().isObject()) {
			labyrinth.setObjectFound(x, y);
			objectsFound++;
		}

		return true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Node getCurrentTile() {
		return labyrinth.getNode(x, y);
	}

	public int getObjectsFound() {
		return objectsFound;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
}