package com.gdxjam.ai.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.components.SquadMemberComponent;
import com.gdxjam.components.SteerableComponent;
import com.gdxjam.components.SteeringBehaviorComponent;
import com.gdxjam.components.TargetComponent;
import com.gdxjam.components.WeaponComponent;
import com.gdxjam.utils.Constants;
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
	
    HARVEST(){
   	 @Override
   	 public void enter(Entity entity){
//      	 //Test code
//   		 ImmutableArray<Entity> entities = GameManager.getEngine().getEntitiesFor(Family.all(ResourceComponent.class).get());
//    		 Entity resource = entities.first();
//    		 
//    		 Arrive<Vector2> arrive = new Arrive<Vector2>(Components.STEERABLE.get(entity))
//    			 .setTarget((Location<Vector2>)Components.STEERABLE.get(resource).getBody().getPosition())
//    			 .setTimeToTarget(0.01f)
//    			 .setArrivalTolerance(0.002f)
//    			 .setDecelerationRadius(2f);
//    		 
//    		 SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
//    		 behavior.setBehavior(arrive);
   	 }
   	 
		public void update(Entity entity){
//      		 SteerableComponent steerable = Components.STEERABLE.get(entity);
//      		 SteeringBehaviorComponent behavior = Components.STEERING_BEHAVIOR.get(entity);
//
//      		 Arrive<Vector2> arrive = (Arrive<Vector2>) behavior.getBehavior();
//      		 Vector2 targetPosition = arrive.getTarget().getPosition();
//
//      		 if(steerable.getPosition().dst2(targetPosition) < 1.5f){
//      			 if(Components.HEALTH.has(unit.target)){
//      				 HealthComponent health = Components.HEALTH.get(unit.target);
//         			 health.value -= 1;
//         			 if(health.value <= 0){
//         				 unit.target = null;
//         			 }
//      			 }
//      			
//      		 }
//   		 }
//   		 
//   		 
//   	 }
	
    }
	},
    
	COMBAT_ACTIVE(){
		@Override
		public void enter (Entity entity) {
			super.enter(entity);
		}
		
		@Override
		public void update (Entity entity) {
			super.update(entity);
			SquadMemberComponent squadMemberComp = Components.SQUAD_MEMBER.get(entity);
			TargetComponent squadTargetComp = Components.TARGET.get(squadMemberComp.squad);
			if(squadTargetComp.target != null){
				SquadComponent enemySquadComp = Components.SQUAD.get(squadTargetComp.target);
				TargetComponent targetComp = Components.TARGET.get(entity);
				targetComp.target = enemySquadComp.members.first();
			}
		}
		
		
	},
    
    ATTACK_TARGET(){
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
   		}
   		
   	}
   	 
   	 @Override
   	public void update (Entity entity) {
   		super.update(entity);
   		WeaponComponent weaponComp = Components.WEAPON.get(entity);
   		TargetComponent targetComp = Components.TARGET.get(entity);
   		if(targetComp.target != null){
   		

	   		if(weaponComp.cooldown <= 0){
	      		SteerableComponent steerable = Components.STEERABLE.get(entity);
	      		
	      		float angle = steerable.getOrientation();
	      		float unitDiameter = Constants.unitRadius * 2;
	      		
	      		Vector2 position = new Vector2();
	      		position.set(steerable.getPosition());
	      		position.add(unitDiameter * MathUtils.cos(angle), unitDiameter * MathUtils.sin(angle));
	      		
	      		Vector2 velocity = new Vector2(20, 0).setAngle(angle * MathUtils.radDeg);
	      		
	      		EntityFactory.createProjectile(position, velocity, Components.FACTION.get(entity).faction, 20);
	      		weaponComp.cooldown += weaponComp.attackSpeed;
	   		}
   		}
   		
   		weaponComp.cooldown -= Gdx.graphics.getDeltaTime();
   	}
    };



    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }


    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
//   	 
//        switch (telegram.message){
//            case Messages.REGROUP_ORDER:
//                if(!Components.STATE_MACHINE.get(entity).stateMachine.isInState(REGROUP))
//                    Components.STATE_MACHINE.get(entity).stateMachine.changeState(REGROUP);
//                break;
//            case Messages.RESOUCES_ORDER:
//                Components.STATE_MACHINE.get(entity).stateMachine.changeState(COLLECT_RESOURCES);
//        }

        return false;
    }
}
