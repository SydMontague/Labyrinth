package de.telekom.jhsc.labyrinth.plugin;

import de.telekom.jhsc.labyrinth.Direction;
import de.telekom.jhsc.labyrinth.Node;

public abstract class Plugin {
	public Plugin() {}

	/**
	 * Polls the bot for the next move to take. The given {@link Node}
	 * represents the immutable state of the current position the crawler is at.
	 * 
	 * If the crawler can't move in the returned direction or the returned
	 * direction is null the crawler will not move at all for that turn.
	 * 
	 * @param currentTile
	 *            the {@link Node} the crawler is currently at
	 * @return the {@link Direction} crawler should move to next.
	 */
	public abstract Direction getNextMove(Node currentTile);
}
