/**
 * 
 */
package com.eblackwelder.world.model.impl;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Collidable;

/**
 * @author Ethan
 *
 */
public class KinematicsObject extends MassObject implements Collidable {

	private final double elasticity;
	private final double friction;

	public KinematicsObject(Renderable renderable, double mass) {
		this(renderable, mass, 1);
	}

	public KinematicsObject(Renderable renderable, double mass, double elasticity) {
		this(renderable, mass, elasticity, 0);
	}
	
	public KinematicsObject(Renderable renderable, double mass, double elasticity, double friction) {
		super(renderable, mass);
		this.elasticity = elasticity;
		this.friction = friction;
	}
	
	public KinematicsObject(Renderable renderable, double mass, double elasticity, Point2D position) {
		this(renderable, mass, elasticity, position, Vector2D.zeroVector());
	}

	public KinematicsObject(Renderable renderable, double mass, double elasticity, Point2D position, Point2D velocity) {
		super(renderable, mass, position, velocity);
		this.elasticity = elasticity;
		this.friction = 0;
	}

	@Override
	public double getElasticity() {
		return this.elasticity;
	}
	
	@Override
	public Rectangle2D getBounds() {
		return super.renderable.getBounds(super.getPosition());
	}
	
	@Override
	public double getFriction() {
		return friction;
	}

}
