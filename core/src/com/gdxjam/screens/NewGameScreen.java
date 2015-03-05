package com.gdxjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gdxjam.Assets;
import com.gdxjam.GameManager;
import com.gdxjam.components.FactionComponent.Faction;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Constants.WorldSize;

public class NewGameScreen extends AbstractScreen {

	Stage stage;
	Table table;
	Label description;
	Faction selected;
	WorldSize size;

	@Override
	public void show() {
		selected = Faction.FACTION0;
		size = WorldSize.MEDIUM;
		stage = new Stage();
		table = new Table();
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
		description.setAlignment(Align.center);

		NinePatchDrawable draw = new NinePatchDrawable(Assets.hotkey.button);

		TextButtonStyle textStyle = new ImageTextButtonStyle();
		textStyle.up = draw;
		textStyle.down = draw.tint(Color.DARK_GRAY);
		textStyle.checked = draw;
		textStyle.font = Assets.fonts.font;

		final SelectBox<String> worldSize = new SelectBox<String>(Assets.skin);
		worldSize.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println(worldSize.getSelected());

				if (worldSize.getSelected().equalsIgnoreCase("small")) {
					Constants.worldSize = WorldSize.SMALL;

				} else if (worldSize.getSelected().equalsIgnoreCase("medium")) {
					Constants.worldSize = WorldSize.MEDIUM;

				} else if (worldSize.getSelected().equalsIgnoreCase("large")) {
					Constants.worldSize = WorldSize.LARGE;

				}
			}
		});
		worldSize.setItems("Small", "Medium", "Large");

		TextButton start = new TextButton("Start", textStyle);
		start.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				start(selected);
			}
		});

		TextButton back = new TextButton("Back", textStyle);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				GameManager.setScreen(new MainMenuScreen());
			}
		});

		table.align(Align.top).add(label).colspan(3);
		table.row();

		table.add(faction0);
		table.add(faction1);
		table.add(faction2);
		table.row();
		table.add(description).colspan(3);
		table.row();
		table.add(worldSize).fill(0.8f, 1f);
		table.add(start);
		table.add(back);
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);

	}

	public void showFaction(Faction faction) {
		switch (faction) {
		default:
		case FACTION0:
			description.setText(Constants.FACTION0_DESC);
			selected = Faction.FACTION0;
			break;
		case FACTION1:
			description.setText(Constants.FACTION1_DESC);
			selected = Faction.FACTION1;
			break;
		case FACTION2:
			description.setText(Constants.FACTION2_DESC);
			selected = Faction.FACTION2;
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
