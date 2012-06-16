package com.eblackwelder.physx.effects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.World;
import com.eblackwelder.physx.object.Mass;
import com.eblackwelder.physx.object.Collidable;

/**
 * @author Ethan
 *
 */
public class Buoyancy extends Viscosity {
	
	private final double waterLine; //TODO : arbitrarily choose the -Y as "down", can it be easily abstracted?
	private final double density;
	
	public Buoyancy(double waterLine, double density, double viscosity) {
		super(viscosity, Color.BLUE, calculateBounds(waterLine));
		this.waterLine = waterLine;
		this.density = density;
	}

	@Override
	public boolean isAffected(Mass a, Rectangle2D shape) {
		return Collidable.class.isInstance(a) && shape.getMinY() < waterLine;
	}

	@Override
	public void apply(Mass a, Rectangle2D shape) {
		Collidable c = (Collidable) a;
		//Simplifying assumption: all objects are rectangles! (TODO : not)
		//F = pressure * area, A = [width * depth], P = [width * depth * density]
		//F = [width * depth * density] * [width * depth]
		double width = shape.getWidth();
		double depth = Math.abs(shape.getMinY() - waterLine);
		double area = depth * width;
		double pressure = width * depth * (density / 100.0);
		double forceY = pressure * area;
		Vector2D force = new Vector2D(0, forceY);
		c.addForce(force);
		
		//Also apply viscosity:
		super.applyViscosity(c, shape);
	}

	public static Rectangle2D calculateBounds(double waterLine) {
		Dimension size = World.getSingleton().getPreferredSize();
		double width = size.getWidth();
		double height = size.getHeight();
		return new Rectangle2D.Double(-width / 2.0, -height / 2.0, width, height / 2.0 + waterLine);
	}
}
