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
	
	public static ObjectMap<PatternType, FormationPattern<Vector2>> patterns;
	public static final PatternType defaultPattern = PatternType.Ring;
	public static final SlotCostProvider<Vector2> costProvider = new DistanceSlotCostProvider();

	public static void initPatterns(){
		patterns = new ObjectMap<PatternType, FormationPattern<Vector2>>();
		patterns.put(PatternType.Line, new LineFormationPattern(Constants.unitRadius * 1.25f));
		patterns.put(PatternType.Square, new SquareFormationPattern(Constants.unitRadius * 1.25f));
		patterns.put(PatternType.Ring, new DefensiveCircleFormationPattern<Vector2>(Constants.unitRadius * 1.25f));
		patterns.put(PatternType.V, new VFormationPattern(30 * MathUtils.degreesToRadians, Constants.unitRadius * 1.25f));
		patterns.put(PatternType.Wedge, new WedgeFormationPattern(Constants.unitRadius * 1.25f));
	}
	
}
