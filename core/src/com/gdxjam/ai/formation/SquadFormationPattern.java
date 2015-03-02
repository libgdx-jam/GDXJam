package com.gdxjam.ai.formation;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.SoftRoleSlotAssignmentStrategy.SlotCostProvider;
import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.gdxjam.utils.Constants;

public class SquadFormationPattern {
	
	public enum PatternType {
		Line,
		Square,
		Ring,
		V,
		Wedge;
	}

	private static float UNIT_RADIUS = Constants.unitRadius * 1.25f;

	public static ObjectMap<PatternType, FormationPattern<Vector2>> patterns;
	public static final PatternType defaultPattern = PatternType.Ring;
	public static final SlotCostProvider<Vector2> costProvider = new DistanceSlotCostProvider();

	public static void initPatterns(){
		patterns = new ObjectMap<PatternType, FormationPattern<Vector2>>();
		patterns.put(PatternType.Line, new LineFormationPattern(UNIT_RADIUS));
		patterns.put(PatternType.Square, new SquareFormationPattern(UNIT_RADIUS));
		patterns.put(PatternType.Ring, new DefensiveCircleFormationPattern<Vector2>(UNIT_RADIUS));
		patterns.put(PatternType.V, new VFormationPattern(40 * MathUtils.degreesToRadians, UNIT_RADIUS));
		patterns.put(PatternType.Wedge, new WedgeFormationPattern(UNIT_RADIUS));
	}
	
}
