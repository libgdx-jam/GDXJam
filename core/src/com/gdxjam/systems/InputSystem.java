package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class InputSystem extends EntitySystem {

	private InputMultiplexer input;

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		input = new InputMultiplexer();
	}

	public void add(InputProcessor processor) {
		input.addProcessor(processor);
	}

	public void add(int index, InputProcessor processor) {
		input.addProcessor(index, processor);
	}

	public void remove(InputProcessor processor) {
		input.removeProcessor(processor);
	}

	public void remove(int index) {
		input.removeProcessor(index);
	}

	public InputMultiplexer getMultiplexer() {
		return input;
	}
}
