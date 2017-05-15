package de.telekom.jhsc.labyrinth.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import de.telekom.jhsc.labyrinth.Direction;
import de.telekom.jhsc.labyrinth.Node;

public class TestPlugin extends Plugin {

	public TestPlugin() {
		super();
	}

	@Override
	public Direction getNextMove(Node currentTile) {
		if(Gdx.input.isKeyPressed(Keys.UP))
			return Direction.UP;
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			return Direction.LEFT;
		if(Gdx.input.isKeyPressed(Keys.DOWN))
			return Direction.DOWN;
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			return Direction.RIGHT;
		
		return null;
	}

}
