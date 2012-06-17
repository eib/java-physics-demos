/**
 * 
 */
package com.eblackwelder.physx.effects;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.graphx.renderable.Field;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Collidable;
import com.eblackwelder.world.model.Mass;

/**
 * @author Ethan
 *
 */
public class Viscosity extends Field implements Effect {
	private final double viscosity;
	
	public Viscosity(double viscosity, Rectangle2D bounds) {
		this(viscosity, null, bounds);
	}
	public Viscosity(double viscosity, Color c, Rectangle2D bounds) {
		super(bounds, c);
		this.viscosity = viscosity;
	}

	@Override
	public boolean isAffected(Mass a, Rectangle2D shape) {
		return Collidable.class.isInstance(a) && bounds.intersects(shape);
	}
	
	@Override
	public void apply(Mass a, Rectangle2D shape) {
		applyViscosity((Collidable) a, shape);
	}

	protected void applyViscosity(Collidable c, Rectangle2D shape) {
		//Drag force = Viscosity x Area (of c) x Speed (of c) -- but we'll be using width/height instead of area
		Vector2D velocity = c.getVelocity();

		//slow in x-direction
		double height = shape.getHeight();
		double vx = velocity.getX();
		double fx = vx * -Math.abs(viscosity * height);
		
		//slow in y-direction
		double width = shape.getWidth();
		double vy = velocity.getY();
		double fy = vy * -Math.abs(viscosity * width);
		
		//vector
		Vector2D force = new Vector2D(fx, fy);
		c.addForce(force);
	}
}
