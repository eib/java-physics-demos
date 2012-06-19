/**
 * 
 */
package com.eblackwelder.physx;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.World;
import com.eblackwelder.world.model.Charge;
import com.eblackwelder.world.model.Mass;
import com.eblackwelder.world.model.impl.ChargedParticle;

/**
 * @author Ethan
 *
 */
public class CoulombsLaw2 extends CoulombsLaw {

	public static interface RenderableCreator {
		public Renderable create(double mass, double charge);
	}
	
	private final RenderableCreator renderableCreator;
	private final double minCombiningDistance;
	
	public CoulombsLaw2(double scalar, double minCombiningDistance, RenderableCreator renderableCreator) {
		super(scalar);
		this.minCombiningDistance = minCombiningDistance;
		this.renderableCreator = renderableCreator;
	}

	//TODO: bugs...
	//Overlay items are left lying around (a limitation of the rendering strategy)
	//Doesn't handle collisions between more than 2 particles well.
	@Override
	public void apply(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2) {
		if (a1.getPosition().distance(a2.getPosition()) < minCombiningDistance) {
			Charge c1 = (Charge) a1;
			Charge c2 = (Charge) a2;
			if (c1.getCharge() > 0 != c2.getCharge() > 0) { //combine oppositely charged particles :)
				Charge newParticle = combineParticles(c1, c2);
				
				World world = World.getSingleton();
				world.remove(c1); //TODO : this doesn't play nicely with Overlay items.
				world.remove(c2);
				world.add(newParticle);
			}
		} else {
			super.apply(a1, a2, b1, b2);
		}
	}

	private Charge combineParticles(Charge ...charges) {
		double newCharge = 0;
		double totalMass = 0;
		
		double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

		Vector2D velocity = new Vector2D(0, 0);
		for (Charge c : charges) {
			newCharge += c.getCharge();
			totalMass += c.getMass();

			double x = c.getPosition().x;
			double y = c.getPosition().y;
			minX = Math.min(minX,  x);
			maxX = Math.max(maxX,  x);
			minY = Math.min(minY,  y);
			maxY = Math.max(maxY,  y);
			
			velocity = velocity.add(Vector2D.scale(c.getVelocity(), c.getMass()));
		}
		//positioned at the exact midpoint (should it actually be the center of mass?)
		double x = minX + (maxX - minX) / 2.0;
		double y = minY + (maxY - minY) / 2.0;
		Point2D position = new Point2D.Double(x, y);

		//conservation of momentum:
		//m1v1 + m2v2 = (m1+m2)v'
		//v' = (m1*v1 + m2*v2) / (m1+m2)
		velocity = velocity.scale(1.0 / totalMass);
		
		Renderable renderable = renderableCreator.create(totalMass, newCharge);
		ChargedParticle newParticle = new ChargedParticle(renderable, totalMass, newCharge, position, velocity);
		return newParticle;
	}

}
