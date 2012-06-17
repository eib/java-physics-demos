/**
 * 
 */
package com.eblackwelder.physx;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.eblackwelder.math.Utils;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Charge;
import com.eblackwelder.world.model.Mass;

/**
 * @author Ethan
 *
 */
public class CoulombsLaw implements Physics {

	private final double scalar;
	
	public CoulombsLaw(double scalar) {
		this.scalar = scalar;
	}
	
	@Override
	public void startNext(List<Mass> affected) {}

	@Override
	public void apply(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2) {		
		Point2D position1 = a1.getPosition();
		Point2D position2 = a2.getPosition();

		//F =  q1 * q2 * R / r^3 / (4*pi*eps0), where R is a vector and r is a scalar
		Vector2D radius21 = Vector2D.subtract(position2, position1);
		Vector2D radius12 = radius21.reverse();
		double distance = Math.abs(position1.distance(position2));
		
		Charge c1 = (Charge) a1;
		Charge c2 = (Charge) a2;
		
		double q1 = c1.getCharge();
		double q2 = c2.getCharge();
		double scale = q1 * q2 * scalar / distance / distance / distance;

		Vector2D force1 = Vector2D.scale(radius12, scale);
		c1.addForce(force1);
		c2.addForce(force1.reverse());
	}

	@Override
	public boolean affects(Mass a, Rectangle2D bounds) {
		return Charge.class.isInstance(a) &&
			!Utils.almostZero(Charge.class.cast(a).getCharge(), 0.01); //Doesn't apply to neutral particles (floating-point round issues?)
	}

}
