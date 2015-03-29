
package com.gdxjam.components;

import java.util.Comparator;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMotionModerator;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.OffensiveCircleFormationPattern;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gdxjam.ai.formation.ColumnFormationPattern;
import com.gdxjam.ai.formation.DistanceSlotCostProvider;
import com.gdxjam.ai.formation.LineFormationPattern;
import com.gdxjam.ai.formation.SquareFormationPattern;
import com.gdxjam.ai.formation.VFormationPattern;
import com.gdxjam.ai.formation.WedgeFormationPattern;
import com.gdxjam.ai.state.SquadCombatState;
import com.gdxjam.ai.state.TelegramMessage;
import com.gdxjam.ecs.Components;
import com.gdxjam.ecs.EntityCategory;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.EntityUtils;
import com.gdxjam.utils.Location2;

public class SquadComponent extends Component implements Poolable {

	public enum FormationPatternType {
		Line, Column, Square, Ring, V, Wedge;
	}

	public static final float PATTERN_SPACING = Constants.unitRadius * 0.25f;
	public static final FormationPatternType DEFAULT_PATTERN = FormationPatternType.V;
	public static final State<Entity> DEFAULT_STATE = SquadCombatState.IDLE;

	// Arrays used for determining available targets
	public Array<Entity> enemiesTracked = new Array<Entity>();
	public Array<Entity> resourcesTracked = new Array<Entity>();
	public Array<Entity> friendliesTracked = new Array<Entity>();

	// Steerable group behavior group steering arrays
	// Steering behaviors use these for group separation / collision avoidance
	public Array<Steerable<Vector2>> resourceAgents = new Array<Steerable<Vector2>>();
	public Array<Steerable<Vector2>> friendlyAgents = new Array<Steerable<Vector2>>();

	// Array for formation API and ashley entity reference
	public Array<Entity> members = new Array<Entity>();
	public Array<Steerable<Vector2>> memberAgents = new Array<Steerable<Vector2>>();

	// Formation
	public Formation<Vector2> formation;
	public FormationMotionModerator<Vector2> moderator;
	public Location2 targetLocation = new Location2();
	
	private final Comparator<Entity> resourceComparator = new Comparator<Entity>() {
		@Override
		public int compare (Entity e1, Entity e2) {
			Vector2 squadPos = steerable.getPosition();
			Vector2 pos1 = Components.STEERABLE.get(e1).getPosition();
			Vector2 pos2 = Components.STEERABLE.get(e2).getPosition();

			float dist1 = squadPos.dst2(pos1);
			float dist2 = squadPos.dst2(pos2);

			return dist1 > dist2 ? 1 : -1;
		}
	};


	private Steerable<Vector2> steerable;

	/** Can only be created by PooledEngine */
	private SquadComponent () {
		// private constructor
	}

	public SquadComponent init (Steerable<Vector2> steerable) {
		this.steerable = steerable;
		SoftRoleSlotAssignmentStrategy<Vector2> slotAssignmentStrategy = new SoftRoleSlotAssignmentStrategy<Vector2>(
			new DistanceSlotCostProvider());
		formation = new Formation<Vector2>(steerable, getFormationPattern(DEFAULT_PATTERN), slotAssignmentStrategy);
		return this;
	}

	public void addMember (Entity entity) {
		members.add(entity);
		memberAgents.add(Components.STEERABLE.get(entity));
		formation.addMember(Components.UNIT.get(entity));
	}

	public void removeMember (Entity entity) {
		members.removeValue(entity, true);
		memberAgents.removeValue(Components.STEERABLE.get(entity), true);
		formation.removeMember(Components.UNIT.get(entity));
	}

	public void track (Entity self, Entity target) {
		if ((target.flags & EntityCategory.RESOURCE) == EntityCategory.RESOURCE) {
			resourcesTracked.add(target);
			resourceAgents.add(Components.STEERABLE.get(target));
			sortTrackedResources();
			MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(self),
				TelegramMessage.DISCOVERED_RESOURCE.ordinal());
		} else if ((target.flags & EntityCategory.SQUAD) == EntityCategory.SQUAD) {
			if (EntityUtils.isSameFaction(self, target)) {
				friendliesTracked.add(target);
				friendlyAgents.add(Components.STEERABLE.get(target));
			} else {
				enemiesTracked.add(target);
				MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(self), TelegramMessage.DISCOVERED_ENEMY.ordinal());
			}

		}
	}

	public void untrack (Entity self, Entity target) {
		if ((target.flags & EntityCategory.RESOURCE) == EntityCategory.RESOURCE) {
			resourcesTracked.removeValue(target, true);
			resourceAgents.removeValue(Components.STEERABLE.get(target), true);
		} else if ((target.flags & EntityCategory.SQUAD) == EntityCategory.SQUAD) {
			if (EntityUtils.isSameFaction(self, target)) {
				friendliesTracked.removeValue(target, true);
				friendlyAgents.removeValue(Components.STEERABLE.get(target), true);
			}
			enemiesTracked.removeValue(target, true);
		}
	}

	private Vector2 getPosition () {
		return steerable.getPosition();
	}

	private void sortTrackedResources () {
		resourcesTracked.sort(resourceComparator);
	}

	public void setFormationPattern (FormationPatternType pattern) {
		FormationPattern<Vector2> formationPattern = getFormationPattern(pattern);
		formation.changePattern(formationPattern);
	}

	public FormationPattern<Vector2> getFormationPattern (FormationPatternType pattern) {
		switch (pattern) {
		case Line:
			return new LineFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		case Column:
			return new ColumnFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		case Square:
			return new SquareFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		default:
		case Ring:
			return new OffensiveCircleFormationPattern<Vector2>(Constants.unitRadius + PATTERN_SPACING);
		case V:
			return new VFormationPattern(60 * MathUtils.degreesToRadians, Constants.unitRadius + PATTERN_SPACING);
		case Wedge:
			return new WedgeFormationPattern(Constants.unitRadius + PATTERN_SPACING);
		}
	}

	public void setTarget (Vector2 target) {
		targetLocation.getPosition().set(target);
	}

	@Override
	public void reset () {
		members.clear();
		memberAgents.clear();
	}

}
