package de.telekom.jhsc.labyrinth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

class InitScreen extends ScreenAdapter {
	private Stage root;

	private TextField dimensionX;
	private TextField dimensionY;
	private TextField seed;

	private static TextFieldStyle getTextFieldStyle() {
		Drawable drawBg = Utils.generateColoredDrawable(new Color(0xEEEEEEFF));
		Drawable drawSelected = Utils.generateColoredDrawable(Color.GRAY);
		Drawable drawSelectedBg = Utils.generateColoredDrawable(Color.WHITE);

		TextFieldStyle style = new TextFieldStyle();
		style.font = new BitmapFont();
		style.fontColor = Color.BLACK;
		style.cursor = drawSelected;
		style.background = drawBg;
		style.selection = drawSelected;
		style.focusedBackground = drawSelectedBg;

		return style;
	}

	public InitScreen(LabyrinthWindow main) {
		root = new Stage();

		Group setupGroup = new Group();
		setupGroup.moveBy(260, 260);

		dimensionX = new TextField("5", getTextFieldStyle());
		dimensionX.setAlignment(Align.right);
		dimensionX.setMaxLength(3);
		dimensionX.setWidth(40);
		dimensionX.setZIndex(0);
		dimensionX.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());

		Label inputLabel = new Label("Labyrinth Size", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		inputLabel.setPosition(0, 24);
		inputLabel.setWidth(100);
		inputLabel.setAlignment(Align.center);

		Label xLabel = new Label("x", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		xLabel.setWidth(20);
		xLabel.moveBy(40, 0);
		xLabel.setZIndex(1);
		xLabel.setAlignment(Align.center);

		dimensionY = new TextField("5", getTextFieldStyle());
		dimensionY.setAlignment(Align.right);
		dimensionY.setMaxLength(3);
		dimensionY.setWidth(40);
		dimensionY.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		dimensionY.moveBy(60, 0);

		seed = new TextField("Random Seed", getTextFieldStyle());
		seed.setMaxLength(16);
		seed.setWidth(100);
		seed.moveBy(0, -24);

		TextButton button = new TextButton("Start",
				new TextButton.TextButtonStyle(Utils.generateColoredDrawable(Color.LIGHT_GRAY),
						Utils.generateColoredDrawable(Color.WHITE), null, new BitmapFont()));
		button.setPosition(25, -48);
		button.setSize(50, 20);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int dimX = Integer.parseInt(dimensionX.getText());
				int dimY = Integer.parseInt(dimensionY.getText());
				String s = seed.getText();

				main.startGame(dimX, dimY, s);
			}
		});

		setupGroup.addActor(dimensionX);
		setupGroup.addActor(dimensionY);
		setupGroup.addActor(seed);
		setupGroup.addActor(xLabel);
		setupGroup.addActor(inputLabel);
		setupGroup.addActor(button);
		root.addActor(setupGroup);
		Gdx.input.setInputProcessor(root);
	}

	@Override
	public void render(float delta) {
		root.act(delta);
		root.draw();
	}
}