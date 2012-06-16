/**
 * 
 */
package com.eblackwelder.physx.physics;

import java.awt.geom.Rectangle2D;

import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.World;
import com.eblackwelder.physx.object.Mass;
import com.eblackwelder.physx.object.Charge;
import com.eblackwelder.physx.object.impl.ChargedParticle;
import com.eblackwelder.physx.renderable.Renderable;

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

	@Override
	public void apply(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2) {
		if (a1.getPosition().distance(a2.getPosition()) < minCombiningDistance) {
			Charge c1 = (Charge) a1;
			Charge c2 = (Charge) a2;
			if (c1.getCharge() > 0 != c2.getCharge() > 0) { //combine oppositely charged particles :)
				double charge = c1.getCharge() + c2.getCharge();
				double m1 = c1.getMass();
				double m2 = c2.getMass();
				double mass = m1 + m2;

				Vector2D p1 = c1.getPosition();
				Vector2D p2 = c2.getPosition();
				Vector2D position = Vector2D.midPoint(p1, p2);
				
				Vector2D v1 = c1.getVelocity();
				Vector2D v2 = c2.getVelocity();
				//conservation of momentum:
				//m1v1 + m2v2 = (m1+m2)v'
				//v' = (m1*v1 + m2*v2) / (m1+m2)
				Vector2D velocity = Vector2D.scale(v1, m1).add(Vector2D.scale(v2, m2)).scale(1 / (m1+m2));
				
				Renderable renderable = renderableCreator.create(mass, charge);
				ChargedParticle newParticle = new ChargedParticle(renderable, mass, charge, position, velocity);
				
				World world = World.getSingleton();
				world.remove(a1); //TODO : this doesn't play nicely with Overlay items.
				world.remove(a2);
				world.add(newParticle);
			}
		} else {
			super.apply(a1, a2, b1, b2);
		}
	}

}
