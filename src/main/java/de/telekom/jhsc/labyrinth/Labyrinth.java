package de.telekom.jhsc.labyrinth;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

class Labyrinth {
	private static final int CONNECTION_CHANCE = 66;
	static final int NUM_OBJECTS = 3;

	private boolean[][] horizontal;
	private boolean[][] vertical;
	private boolean[][] node;

	public Labyrinth(int x, int y, Random r) {
		this.horizontal = new boolean[x - 1][y];
		this.vertical = new boolean[x][y - 1];
		this.node = new boolean[x][y];

		List<Tuple> traversed = new LinkedList<>();
		Set<Tuple> horTraversed = new HashSet<>();
		Set<Tuple> vertTraversed = new HashSet<>();

		Queue<Tuple> queue = new LinkedList<>();

		queue.add(new Tuple(0, 0));

		while (!queue.isEmpty()) {
			Tuple t = queue.poll();
			if (traversed.contains(t))
				continue;
			traversed.add(t);

			Tuple tt = new Tuple(t.x - 1, t.y);
			if (horTraversed.add(tt) && (getHoriConn(tt) || setHoriConn(tt, r.nextInt(100) < CONNECTION_CHANCE)))
				queue.add(new Tuple(t.x - 1, t.y));

			tt = new Tuple(t.x, t.y);
			if (horTraversed.add(tt) && (getHoriConn(tt) || setHoriConn(tt, r.nextInt(100) < CONNECTION_CHANCE)))
				queue.add(new Tuple(t.x + 1, t.y));

			if (vertTraversed.add(tt) && (getVertConn(tt) || setVertConn(tt, r.nextInt(100) < CONNECTION_CHANCE)))
				queue.add(new Tuple(t.x, t.y + 1));

			tt = new Tuple(t.x, t.y - 1);
			if (vertTraversed.add(tt) && (getVertConn(tt) || setVertConn(tt, r.nextInt(100) < CONNECTION_CHANCE)))
				queue.add(new Tuple(t.x, t.y - 1));
		}

		for (int i = 0; i < NUM_OBJECTS; i++) {
			int selection = r.nextInt(traversed.size());
			Tuple t = traversed.get(selection);
			node[t.x][t.y] = true;
			traversed.remove(selection);
		}

	}

	public int getWidth() {
		return node.length;
	}

	public int getHeight() {
		return node.length > 0 ? node[0].length : 0;
	}

	public Node getNode(int x, int y) {
		if (x >= getWidth() || y >= getHeight())
			throw new IllegalArgumentException("Given coordinates are out of bounds!");

		return new Node(node[x][y], getHoriConn(x - 1, y), getHoriConn(x, y), getVertConn(x, y - 1), getVertConn(x, y));
	}

	private boolean getHoriConn(int x, int y) {
		if (x < 0 || x >= horizontal.length)
			return false;

		if (y < 0 || y >= horizontal[x].length)
			return false;

		return horizontal[x][y];
	}

	private boolean getVertConn(int x, int y) {
		if (x < 0 || x >= vertical.length)
			return false;

		if (y < 0 || y >= vertical[x].length)
			return false;

		return vertical[x][y];
	}

	private boolean getHoriConn(Tuple t) {
		return getHoriConn(t.x, t.y);
	}

	private boolean getVertConn(Tuple t) {
		return getVertConn(t.x, t.y);
	}

	private boolean setHoriConn(Tuple t, boolean value) {
		return setHoriConn(t.x, t.y, value);
	}

	private boolean setVertConn(Tuple t, boolean value) {
		return setVertConn(t.x, t.y, value);
	}

	private boolean setHoriConn(int x, int y, boolean value) {
		if (x < 0 || x >= horizontal.length)
			return false;

		if (y < 0 || y >= horizontal[x].length)
			return false;

		horizontal[x][y] = value;
		return value;
	}

	private boolean setVertConn(int x, int y, boolean value) {
		if (x < 0 || x >= vertical.length)
			return false;

		if (y < 0 || y >= vertical[x].length)
			return false;

		vertical[x][y] = value;
		return value;
	}

	public void setObjectFound(int x, int y) {
		if (x >= getWidth() || y >= getHeight())
			throw new IllegalArgumentException("Given coordinates are out of bounds!");

		node[x][y] = false;
		// TODO update visuals
	}

}

class Tuple {
	int x;
	int y;

	public Tuple(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return x + " " + y;
	}
}
