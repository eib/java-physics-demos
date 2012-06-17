/**
 * 
 */
package com.eblackwelder.physx.effects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Mass;

/**
 * @author Ethan
 *
 */
public class Gravity implements Effect {

	private final Vector2D acceleration;
	
	public Gravity(double x, double y) {
		this.acceleration = new Vector2D(x, y);
	}

	public Gravity(Point2D acceleration) {
		this.acceleration = new Vector2D(acceleration);
	}

	@Override
	public void apply(Mass a, Rectangle2D bounds) {
		a.addForce(Vector2D.scale(acceleration, a.getMass()));
	}

	@Override
	public boolean isAffected(Mass a, Rectangle2D bounds) {
		return true;
	}
	
}
