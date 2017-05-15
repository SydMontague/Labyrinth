package de.telekom.jhsc.labyrinth;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

class LabyrinthWindow extends Game {
	
	@Override
	public void create() {
		setScreen(new InitScreen(this));
	}

	public void startGame(int x, int y, String seed) {
		setScreen(new GameScreen(this, x, y, seed));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		super.render();
	}
}