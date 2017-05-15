package de.telekom.jhsc.labyrinth;

public class Node {
	private final boolean isObject;
	private final boolean west;
	private final boolean east;
	private final boolean north;
	private final boolean south;

	public Node(boolean isObject, boolean west, boolean east, boolean north, boolean south) {
		this.west = west;
		this.east = east;
		this.north = north;
		this.south = south;
		this.isObject = isObject;
	}

	/**
	 * Gives back whether this node has been an object the moment it was polled.
	 * 
	 * @return true when it contained an object, false if not
	 */
	public boolean isObject() {
		return isObject;
	}

	/**
	 * Gives back whether there is a connection from this node into the given {@link Direction}.
	 * If a invalid direction is given it returns false.
	 * 
	 * @param dir the direction you want to check
	 * @return true when it is passable, false if not
	 */
	public boolean isPassable(Direction dir) {
		if (dir == null)
			return false;

		switch (dir) {
		case UP:
			return north;
		case DOWN:
			return south;
		case LEFT:
			return west;
		case RIGHT:
			return east;
		default:
			return false;
		}
	}

}
