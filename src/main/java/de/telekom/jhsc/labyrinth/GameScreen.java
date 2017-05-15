package de.telekom.jhsc.labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.telekom.jhsc.labyrinth.plugin.Plugin;
import de.telekom.jhsc.labyrinth.plugin.PluginManager;

class GameScreen extends ScreenAdapter {
	private static final String PLUGIN_PATH = ".\\solution.jar";

	private static final float SPEED_MOD = 2.5f;
	private static final float STEP = 100;
	private static final int LINE_WIDTH = 5;
	private static final int LINE_LENGTH = 50;
	private static final int STRIDE = LINE_LENGTH + LINE_WIDTH * 3;
	private static final Texture VERT_TEX = Utils.generateColorTexture(LINE_WIDTH, LINE_LENGTH, Color.BLACK);
	private static final Texture HOR_TEX = Utils.generateColorTexture(LINE_LENGTH, LINE_WIDTH, Color.BLACK);
	private static final Texture OBJ_TEX = Utils.generateColorTexture(LINE_WIDTH, LINE_WIDTH, Color.GREEN);
	private static final Texture CRAWLER_TEX = Utils.generateColorTexture(LINE_WIDTH * 3, LINE_WIDTH * 3, Color.RED);

	private Labyrinth labyrinth;
	private Crawler crawler;
	private Plugin plugin;

	private Stage root = new Stage();
	private Stage hud = new Stage();
	private Image crawlerNode = new Image(CRAWLER_TEX);
	private Label objectsFound;
	private MoveToAction action = new MoveToAction();

	private boolean finished;

	class RemoveObjectAction extends Action {
		private int x;
		private int y;

		public RemoveObjectAction(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean act(float delta) {
			if (labyrinth.getNode(x, y).isObject())
				return false;

			getActor().remove();
			return true;
		}
	}

	public GameScreen(LabyrinthWindow labyrinthWindow, int x, int y, String seed) {
		Random r = new Random(seed.hashCode());
		this.labyrinth = new Labyrinth(x, y, r);
		this.crawler = new Crawler(labyrinth);

		Gdx.input.setInputProcessor(root);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		root.addListener(new InputListener() {
			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount) {
				((OrthographicCamera) root.getCamera()).zoom += amount * 0.2f;
				root.getCamera().update();
				return true;
			}
		});

		Group grid = new Group();

		for (int i = 0; i < labyrinth.getWidth(); i++)
			for (int j = 0; j < labyrinth.getHeight(); j++) {
				Node n = labyrinth.getNode(i, j);

				if (n.isPassable(Direction.RIGHT)) {
					Image image = new Image(HOR_TEX);
					image.setPosition(i * STRIDE + LINE_WIDTH * 2, j * -STRIDE);
					grid.addActor(image);
				}

				if (n.isPassable(Direction.DOWN)) {
					Image image = new Image(VERT_TEX);
					image.setPosition(i * STRIDE, j * -STRIDE - LINE_WIDTH - LINE_LENGTH);
					grid.addActor(image);
				}

				if (n.isObject()) {
					Image image = new Image(OBJ_TEX);
					image.setPosition(i * STRIDE, j * -STRIDE);
					image.addAction(new RemoveObjectAction(i, j));
					grid.addActor(image);
				}
			}

		crawlerNode.setPosition(crawler.getX() * STRIDE - LINE_WIDTH, crawler.getY() * STRIDE - LINE_WIDTH);
		action.setPosition(crawler.getX() * STRIDE - LINE_WIDTH, crawler.getY() * -STRIDE - LINE_WIDTH);
		action.setDuration(1);

		crawlerNode.addAction(action);
		grid.addActor(crawlerNode);
		root.addActor(grid);

		try {
			plugin = PluginManager.loadPlugin(new File(PLUGIN_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		grid.moveBy(20, root.getViewport().getScreenHeight()-20);
		objectsFound = new Label("Schritte: 0", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
		hud.addActor(objectsFound);
	}

	@Override
	public void render(float delta) {
		float mod = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? SPEED_MOD : 1;

		if (Gdx.input.isKeyPressed(Keys.W))
			root.getCamera().translate(0, STEP * delta * mod, 0);
		if (Gdx.input.isKeyPressed(Keys.A))
			root.getCamera().translate(-STEP * delta * mod, 0, 0);
		if (Gdx.input.isKeyPressed(Keys.S))
			root.getCamera().translate(0, -STEP * delta * mod, 0);
		if (Gdx.input.isKeyPressed(Keys.D))
			root.getCamera().translate(STEP * delta * mod, 0, 0);

		root.getCamera().update();

		if (!finished && action.getDuration() <= action.getTime()) {
			Direction dir = plugin.getNextMove(crawler.getCurrentTile());
			crawler.move(dir);

			action.setPosition(crawler.getX() * STRIDE - LINE_WIDTH, crawler.getY() * -STRIDE - LINE_WIDTH);
			action.restart();
			objectsFound.setText("Schritte: " + crawler.getMoveCount());
			crawlerNode.addAction(action);
		}

		if (!finished && crawler.getObjectsFound() == Labyrinth.NUM_OBJECTS && crawler.getX() == 0
				&& crawler.getY() == 0) {
			finished = true;
			Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
			style.font.getData().scale(1.4f);
			Label l = new Label("Du hast das Ziel in " + crawler.getMoveCount() + " Schritten erreicht!", style);
			l.setPosition(0, 200);
			hud.addActor(l);
		}

		root.act();
		root.draw();
		hud.draw();
	}
}