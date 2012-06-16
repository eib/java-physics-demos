/**
 * 
 */
package com.eblackwelder.physx.effects;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.object.Mass;
import com.eblackwelder.physx.object.Charge;
import com.eblackwelder.physx.renderable.Field;

/**
 * @author Ethan
 *
 */
public class ElectricField extends Field implements Effect {

	private final Vector2D field;

	public ElectricField(double fieldX, double fieldY, Rectangle2D bounds) {
		this(new Vector2D(fieldX, fieldY), bounds, new Color(0xFF, 0xDD, 0xDD));
	}

	public ElectricField(double fieldX, double fieldY, Rectangle2D bounds, Color color) {
		this(new Vector2D(fieldX, fieldY), bounds, color);
	}
	
	public ElectricField(Vector2D field, Rectangle2D bounds, Color color) {
		super(bounds, color);
		this.field = field;
	}

	@Override
	public void apply(Mass a, Rectangle2D shape) {
		Charge c = (Charge) a;
		double charge = c.getCharge();
		double fx = field.x * charge;
		double fy = field.y * charge;
		c.addForce(new Vector2D(fx, fy));
	}

	public boolean isAffected(Mass a, Rectangle2D shape) {
		return Charge.class.isInstance(a) && bounds.intersects(a.getBounds());
	}
}
