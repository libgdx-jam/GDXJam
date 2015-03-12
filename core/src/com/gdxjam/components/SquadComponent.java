
package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMotionModerator;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy;
import com.badlogic.gdx.ai.fma.patterns.OffensiveCircleFormationPattern;
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
import com.gdxjam.ai.state.SquadState;
import com.gdxjam.ecs.Components;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Location2;

public class SquadComponent extends Component implements Poolable {

	public enum PatternType {
		Line, Column, Square, Ring, V, Wedge;
	}

	public static final float PATTERN_SPACING = Constants.unitRadius * 0.25f;
	public static final PatternType DEFAULT_PATTERN = PatternType.V;
	public static final SquadState DEFAULT_STATE = SquadState.COMBAT_IDLE;

	// So many arrays
	public Array<Entity> members = new Array<Entity>();
	public Array<SteerableComponent> memberAgents = new Array<SteerableComponent>();
	public Array<Entity> enemiesInRange;
	public Array<Entity> resourcesInRange;
	public Array<Entity> friendliesInRange;
	// TODO: remove TargetFinder and use these arrays... mabye

	// Formation
	public Formation<Vector2> formation;
	public FormationMotionModerator<Vector2> moderator;

	public Location2 targetLocation = new Location2();


	/** Can only be created by PooledEngine */
	private SquadComponent () {
		// private constructor
	}

	public SquadComponent init (Steerable<Vector2> steerable) {
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

	public void setFormationPattern (PatternType pattern) {
		FormationPattern<Vector2> formationPattern = getFormationPattern(pattern);
		formation.changePattern(formationPattern);
	}

	public FormationPattern<Vector2> getFormationPattern (PatternType pattern) {
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
