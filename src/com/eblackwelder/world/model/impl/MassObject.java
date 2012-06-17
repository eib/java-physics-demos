/**
 * 
 */
package com.eblackwelder.world.model.impl;

import java.awt.geom.Point2D;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Mass;

/**
 * @author Ethan
 *
 */
public class MassObject extends AcceleratableObject implements Mass {
	
	private final double mass;
	private Vector2D netForce = Vector2D.zeroVector();
	
	public MassObject(Renderable renderable, double mass) {
		super(renderable);
		this.mass = mass;
	}

	public MassObject(Renderable renderable, double mass, Point2D position) {
		super(renderable, position);
		this.mass = mass;
	}

	public MassObject(Renderable renderable, double mass, Point2D position, Point2D velocity) {
		super(renderable, position, velocity);
		this.mass = mass;
	}

	@Override
	public void update(long millisElapsed) {
		Vector2D acceleration = Vector2D.scale(netForce, 1.0 / getMass());
		setAcceleration(acceleration);
		netForce.x = 0;
		netForce.y = 0;
		super.update(millisElapsed);
	}

	@Override
	public double getMass() {
		return this.mass;
	}

	@Override
	public void addForce(Point2D forceVector) {
		netForce.add(forceVector);
	}

	@Override
	public Vector2D getNetForce() {
		return new Vector2D(netForce);
	}
	
}
