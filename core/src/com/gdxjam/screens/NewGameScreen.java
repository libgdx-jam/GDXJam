package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.utils.Constants;

public class NewGameScreen extends AbstractScreen {

	Stage stage;
	Table table;
	Label description;

	@Override
	public void show() {
		stage = new Stage();
		table = new Table();
		table.debug();
		table.setFillParent(true);
		table.defaults().pad(10);
		LabelStyle labelStyle = new LabelStyle(Assets.fonts.font, new Color(1,
				1, 1, 1));

		Label label = new Label("Choose Your Faction", labelStyle);
		label.setFontScale(2);
		label.setAlignment(Align.top);

		ImageButton faction0 = newImageButton(Assets.spacecraft.ships.get(0));
		faction0.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				showFaction(Faction.FACTION0);
			}
		});

		ImageButton faction1 = newImageButton(Assets.spacecraft.ships.get(1));
		faction1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				showFaction(Faction.FACTION1);
			}
		});

		ImageButton faction2 = newImageButton(Assets.spacecraft.ships.get(2));
		faction2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				showFaction(Faction.FACTION2);
			}
		});

		description = new Label("DERP DERP DERP", labelStyle);

		table.align(Align.top).add(label).colspan(3);
		table.row().maxWidth(Gdx.graphics.getWidth() / 3 - 30);

		table.add(faction0);
		table.add(faction1);
		table.add(faction2);
		table.row();
		table.add(description).colspan(3);
		table.row();
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);

	}

	public void showFaction(Faction faction) {
		switch (faction) {
		default:
		case FACTION0:
			description.setText(Constants.FACTION0_DESC);
			break;
		case FACTION1:
			description.setText(Constants.FACTION1_DESC);
			break;
		case FACTION2:
			description.setText(Constants.FACTION2_DESC);
			break;
		}
	}

	public ImageButton newImageButton(TextureRegion region) {
		TextureRegionDrawable drawable = new TextureRegionDrawable(region);
		return new ImageButton(drawable);
	}

	public void start(Faction faction) {
		Constants.playerFaction = faction;
		GameManager.setScreen(new GameScreen());
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.act();
		stage.draw();

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
