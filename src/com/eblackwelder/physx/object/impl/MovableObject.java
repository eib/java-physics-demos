package com.eblackwelder.physx.object.impl;

import java.awt.Point;
import java.awt.geom.Point2D;

import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.object.Movable;
import com.eblackwelder.physx.object.Updatable;
import com.eblackwelder.physx.renderable.Renderable;

public class MovableObject extends PositionedObject implements Updatable, Movable {

	private Vector2D velocity;

	public MovableObject(Renderable renderable) {
		this(renderable, new Point(0, 0));
	}
	public MovableObject(Renderable renderable, Point2D position) {
		this(renderable, position, new Point(0, 0));
	}
	public MovableObject(Renderable renderable, Point2D position, Point2D velocity) {
		super(renderable, position);
		this.velocity = new Vector2D(velocity);
	}
	
	@Override
	public void update(long millisElapsed) {
		/*
		 * veloc and accel are given in units / second (think: pixels/sec)
		 * given millis (ms) since last update
		 * position (units) + veloc (units/s) * 1s/1000ms * millis (ms) = position' (units) 
		 */
		double deltaSeconds = millisElapsed / 1000.0;
		Vector2D delta = Vector2D.scale(getVelocity(), deltaSeconds);
		setPosition(getPosition().add(delta));
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = new Vector2D(velocity);
	}

}