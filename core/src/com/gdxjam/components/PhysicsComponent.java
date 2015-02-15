package com.gdxjam.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class PhysicsComponent extends Component{
	
	protected Body body;
	
	public Vector2 getSize(){
		Vector2 size = new Vector2(0, 0);
		
		Fixture fixture = body.getFixtureList().get(0);
		Shape shape = fixture.getShape();
		
		switch(shape.getType()){
		case Circle:
			size.set(shape.getRadius(), shape.getRadius());
			break;
		case Polygon:
			PolygonShape poly = (PolygonShape) shape;
			size.set(shape.getRadius(), shape.getRadius());	//XXX Does this return the width of the polygon shape?
			break;
		}
		return size;

	}
	
	public Body getBody(){
		return body;
	}
	
}
