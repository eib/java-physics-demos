package com.eblackwelder.physx.object.impl;

import java.awt.geom.Point2D;

import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.object.Acceleratable;
import com.eblackwelder.physx.renderable.Renderable;

/**
 * @author Ethan
 *
 */
public class AcceleratableObject extends MovableObject implements Acceleratable {
	
	private Vector2D acceleration = new Vector2D(0, 0); //acceleration "vector", in position change per second?
	
	public AcceleratableObject(Renderable renderable) {
		super(renderable);
	}
	public AcceleratableObject(Renderable renderable, Point2D position) {
		super(renderable, position);
	}
	public AcceleratableObject(Renderable renderable, Point2D position, Point2D velocity) {
		super(renderable, position, velocity);
	}
	public AcceleratableObject(Renderable renderable, Point2D position, Point2D velocity, Point2D acceleration) {
		super(renderable, position, velocity);
		setAcceleration(acceleration);
	}

	@Override
	public void update(long millisElapsed) {
		double deltaSeconds = millisElapsed / 1000.0;
		Vector2D delta = Vector2D.scale(getAcceleration(), deltaSeconds);
		setVelocity(getVelocity().add(delta));
		super.update(millisElapsed);
	}
	
	@Override
	public Vector2D getAcceleration() {
		return acceleration;
	}
	
	@Override
	public void setAcceleration(Point2D acceleration) {
		this.acceleration = new Vector2D(acceleration);
	}
}
