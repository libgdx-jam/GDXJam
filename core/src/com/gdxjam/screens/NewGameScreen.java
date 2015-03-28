
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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
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
	Label name;
	Faction selected;
	WorldSize size;

	@Override
	public void show () {
		selected = Faction.FACTION0;
		size = WorldSize.MEDIUM;
		stage = new Stage();
		table = new Table();
		table.setFillParent(true);
		table.defaults().pad(10);
		LabelStyle labelStyle = new LabelStyle(Assets.fonts.font, new Color(1, 1, 1, 1));

		Label label = new Label("Choose Your Faction", labelStyle);
		label.setFontScale(2);
		label.setAlignment(Align.top);

		/** Moved descriptions into the faction enum with the names already there. Removes need to hardcode any faction related
		 * elements. Creation of new factions should be as simple as draging 3 new asset files into the folder and creating an entry
		 * in the faction enum for them */

		Table factionTable = new Table();
		for (int i = 0; i < Faction.values().length - 1; i++) { // Length - 1 to ingore the neutral faction
			factionTable.add(createFactionButton(Faction.values()[i]));
		}

		name = new Label("Faction Name", labelStyle);
		description = new Label("Faction Description", labelStyle);
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
			public void changed (ChangeEvent event, Actor actor) {
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
			public void clicked (InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				start(selected);
			}
		});

		TextButton back = new TextButton("Back", textStyle);
		back.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);
				GameManager.setScreen(new MainMenuScreen());
			}
		});

		table.align(Align.top).add(label).colspan(3);
		table.row();
		table.add(factionTable).colspan(3);
		table.row();
		table.add(name).colspan(3);
		table.row();
		table.add(description).colspan(3);
		table.row();
		table.add(worldSize).fill(0.8f, 1f);
		table.add(start);
		table.add(back);
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);

	}

	// Creates the faction buttons using the faction enum. Removes need to hard code the buttons

	public ImageButton createFactionButton (final Faction faction) {
		ImageButton button = newImageButton(Assets.spacecraft.ships.get(faction.ordinal()));
		button.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				showFaction(faction);
				selected = faction;
			}
		});
		return button;
	}

	// Now uses the parameters in the faction enum rather than constants
	public void showFaction (Faction faction) {
		name.setText(faction.name);
		description.setText(faction.description);
	}

	public ImageButton newImageButton (TextureRegion region) {
		TextureRegionDrawable drawable = new TextureRegionDrawable(region);
		return new ImageButton(drawable);
	}

	public void start (Faction faction) {
		Constants.playerFaction = faction;
		GameManager.setScreen(new GameScreen());
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void render (float delta) {
		super.render(delta);
		stage.act();
		stage.draw();

	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void dispose () {
		stage.dispose();
	}

}
