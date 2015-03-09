
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BehaviorTreeComponent extends Component implements Poolable {

	private BehaviorTree<Entity> btree;

	/** Can only be created by PooledEngine */
	private BehaviorTreeComponent () {
		// private constructor
	}

	public void createTestTree (Entity entity) {
		BehaviorTreeParser<Entity> parser = new BehaviorTreeParser<Entity>(BehaviorTreeParser.DEBUG_HIGH);
		btree = parser.parse(Gdx.files.internal("btree/squad.tree"), entity);
	}

	public void step () {
		btree.step();
	}

	@Override
	public void reset () {
		// TODO Auto-generated method stub

	}

}
