package com.gdxjam.ai.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.FSMComponent;
import com.gdxjam.components.ResourceComponent;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.TargetFinderComponent;
import com.gdxjam.components.WeaponComponent;
import com.gdxjam.utils.Constants;
import com.gdxjam.utils.Constants.BUILD;
import com.gdxjam.utils.EntityFactory;

public enum UnitState implements State<Entity> {

	FORMATION (){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			
			SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SquadMemberComponent squadMember = Components.SQUAD_MEMBER.get(entity);

			Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable, squadMember.getTargetLocation())
				.setTimeToTarget(0.001f)
				.setArrivalTolerance(0.01f)
				.setDecelerationRadius(2f);

			ReachOrientation<Vector2> reachOrientationSB = new ReachOrientation<Vector2>(steerable, squadMember.getTargetLocation()) //
				.setTimeToTarget(0.001f) //
				.setAlignTolerance(0.001f) //
				.setDecelerationRadius(MathUtils.PI);
			
//			RaycastObstacleAvoidance<Vector2> raycastSB = new RaycastObstacleAvoidance<Vector2>(steerable)
//				.setRayConfiguration(new RayConfiguration<Vector2>() {
//					
//					@Override
//					public Ray<Vector2>[] updateRays () {
//						// TODO Auto-generated method stub
//						return null;
//					}
//				})
//				.setDistanceFromBoundary(Constants.unitRadius * 4);
			
			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
//			blendSB.add(raycastSB, 1000);
			blendSB.add(arriveSB, 1.0f);
			if (steerable.isIndependentFacing())
				blendSB.add(reachOrientationSB, 1.0f);
			behavior.setBehavior(blendSB);
		}
		
	},
	
	HARVEST_RETURN(){
		
	},
	
	FIND_RESOURCE(){
		
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			//get target components from the unit and the squad
  			Entity squad = Components.SQUAD_MEMBER.get(entity).squad;
  			
			MessageManager.getInstance().dispatchMessage(null, Components.FSM.get(squad), Messages.requestTarget, entity);
			
			
			if(Components.TARGET.get(entity) != null){
				Components.FSM.get(entity).changeState(HARVEST);
			} else {
				Components.FSM.get(entity).changeState(FORMATION);
			}
		}
		
		
	},
	
    HARVEST(){
   	 @Override
   	 public void enter(Entity entity){
   		super.enter(entity);
   		
			Entity target = Components.TARGET.get(entity).target;
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SteerableComponent targetSteerable = Components.STEERABLE.get(target);
			
			Entity squad = Components.SQUAD_MEMBER.get(entity).squad;
			TargetFinderComponent targetFinderComp = Components.TARGET_FINDER.get(squad);
			RadiusProximity<Vector2> radiusProximity = new RadiusProximity<Vector2>(steerable, targetFinderComp.resourceAgents, 0.1f);
			
			
			Arrive<Vector2> arrive = new Arrive<Vector2>(steerable)
				 .setTarget(targetSteerable)
				 .setTimeToTarget(0.01f)
				 .setArrivalTolerance(0.001f)
				 .setDecelerationRadius(4f);
			 
			Face<Vector2> faceSB = new Face<Vector2>(steerable)
				 .setTarget(targetSteerable)
				 .setAlignTolerance(0.001f)
				 .setTimeToTarget(0.001f)
				 .setDecelerationRadius(2.0f);
			 
			CollisionAvoidance<Vector2> collisionAvoidanceSB = new CollisionAvoidance<Vector2>(steerable, radiusProximity);
			 
			BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
			blendSB.add(arrive, 0.2f);
			blendSB.add(faceSB, 1.0f);
			blendSB.add(collisionAvoidanceSB, 1000.0f);
			 
			SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
			behavior.setBehavior(blendSB);
   	 }
   	 
		public void update(Entity entity){
			super.update(entity);
			Entity target = Components.TARGET.get(entity).target;
			if(target == null) return;
			
			SteerableComponent steerable = Components.STEERABLE.get(entity);
			SteerableComponent targetSteerable = Components.STEERABLE.get(target);
			
			if(targetSteerable == null) return;
			
			if(targetSteerable.getPosition().dst2(steerable.getPosition()) <= targetSteerable.getBoundingRadius() + Constants.unitRadius * 2.0f){
				ResourceComponent targetResourceComp = Components.RESOURCE.get(target);
				targetResourceComp.value -= Constants.resourceCollectionSpeed;
			}
   	 }
		
		@Override
		public boolean onMessage (Entity entity, Telegram telegram) {
			if(telegram.message == Messages.targetDestroyed){
				Entity squad = Components.SQUAD_MEMBER.get(entity).squad;
				FSMComponent squadFSM = Components.FSM.get(squad);
				MessageManager.getInstance().dispatchMessage(null, squadFSM, Messages.targetDestroyed, telegram.extraInfo);
				
				
				Components.FSM.get(entity).changeState(FIND_RESOURCE);
				return true;
			}
			return false;
		}
	},
    
	FIND_TARGET(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
			//get target components from the unit and the squad
			SquadMemberComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
			TargetComponent squadTargetComp = Components.TARGET.get(squadMemberComp.squad);
			
			//if the squad has a target
			if(squadTargetComp.target != null){
				//get the enemy squad and get our targetComponent
				SquadComponent enemySquadComp = Components.SQUAD.get(squadTargetComp.target);
				TargetComponent targetComp = Components.TARGET.get(entity);
				
				//Set our target to a random member in the enemy squad
				targetComp.target = enemySquadComp.members.random();
				//Start attacking our target
				Components.FSM.get(entity).changeState(COMBAT_ENGAGE);
			}
		}
		
		@Override
		public void update (Entity entity) {
			super.update(entity);
			enter(entity);
		}
		
		
	},
    
    COMBAT_ENGAGE(){
   	 @Override
   	public void enter (Entity entity) {
   		super.enter(entity);
   		SteerableComponent steerable = Components.STEERABLE.get(entity);
   		TargetComponent targetComp = Components.TARGET.get(entity);
   		SteeringBehaviorComponent behaviorComp = Components.STEERING_BEHAVIOR.get(entity);
			SquadMemberComponent squadMember = Components.SQUAD_MEMBER.get(entity);
			
   		if(targetComp.target != null){
	   		Face<Vector2> faceSB = new Face<Vector2>(steerable)
	   			.setTarget(Components.STEERABLE.get(targetComp.target))
	   			.setAlignTolerance(0.0001f)
	   			.setDecelerationRadius(2f)
	   			.setTimeToTarget(0.00001f);
	   		
				Arrive<Vector2> arriveSB = new Arrive<Vector2>(steerable, squadMember.getTargetLocation())
					.setTimeToTarget(0.001f)
					.setArrivalTolerance(0.01f)
					.setDecelerationRadius(2f);
				
				BlendedSteering<Vector2> blendSB = new BlendedSteering<Vector2>(steerable);
				blendSB.add(arriveSB, 1.0f);
				blendSB.add(faceSB, 1.0f);
				
				behaviorComp.setBehavior(blendSB);
   		} else{
				Components.FSM.get(entity).changeState(FIND_TARGET);
   		}
   		
   	}
   	 
   	 @Override
   	public void update (Entity entity) {
   		super.update(entity);
   		//Get our target and our weapon
   		WeaponComponent weaponComp = Components.WEAPON.get(entity);
   		TargetComponent targetComp = Components.TARGET.get(entity);
   		
   		//Make sure we have somthing to attack
   		if(targetComp.target != null){
   			//If our weapon is off cooldown
	   		if(weaponComp.cooldown <= 0){
	      		SteerableComponent steerable = Components.STEERABLE.get(entity);
	      		SteerableComponent targetSteerable = Components.STEERABLE.get(targetComp.target);
	      		
	      		//Get the angle between our target and us
	      		float angle = steerable.getPosition().angleRad(targetSteerable.getPosition());
	      		float orientation = steerable.getOrientation();
	      		
	      		//If were facing the angle between us
	      		if(MathUtils.isEqual(orientation, angle, 0.001f));{
		      		float unitDiameter = Constants.unitRadius * 2;
		      		
		      		Vector2 position = new Vector2();
		      		position.set(steerable.getPosition());
		      		position.add(unitDiameter * MathUtils.cos(orientation), unitDiameter * MathUtils.sin(orientation));
		      		
		      		Vector2 velocity = new Vector2(45, 0).setAngle(orientation * MathUtils.radDeg);
		      		
		      		EntityFactory.createProjectile(position, velocity, Components.FACTION.get(entity).faction, 20);
		      		weaponComp.cooldown += weaponComp.attackSpeed;
	      		}
	   		}
   		}
   		else{
   			Components.FSM.get(entity).changeState(FIND_TARGET);
   		}
   		
   		weaponComp.cooldown -= Gdx.graphics.getDeltaTime();
   	}
    };


 	private static final String TAG = UnitState.class.getSimpleName();
    
    @Override
    public void enter(Entity entity) {
 		if(Constants.build == BUILD.DEV){
 			FSMComponent fsm = Components.FSM.get(entity);
			State<Entity> state = Components.FSM.get(entity).getStateMachine().getCurrentState();
			Gdx.app.debug(TAG, "Entered: " + state.toString());
		}
    }

    @Override
    public void update(Entity entity) {

    }


    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {

   	 switch(telegram.message){
   	 case Messages.order:
   		 State<Entity> state = (State<Entity>) telegram.extraInfo;
   		 Components.FSM.get(entity).changeState(state);
   		 return true;
   	default:
   		return false;
   	 }
    }
}
