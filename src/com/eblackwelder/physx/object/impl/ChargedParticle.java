/**
 * 
 */
package com.eblackwelder.physx.object.impl;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.object.Charge;
import com.eblackwelder.physx.renderable.Renderable;

/**
 * @author Ethan
 *
 */
public class ChargedParticle extends MassObject implements Charge {

	private final double charge;
	
	public ChargedParticle(Renderable renderable, double mass, double charge) {
		this(renderable, mass, charge, Vector2D.zeroVector());
	}

	public ChargedParticle(Renderable renderable, double mass, double charge, Point2D position) {
		this(renderable, mass, charge, position, Vector2D.zeroVector());
	}

	public ChargedParticle(Renderable renderable, double mass, double charge, Point2D position, Point2D velocity) {
		super(renderable, mass, position, velocity);
		this.charge = charge;
	}

	@Override
	public double getCharge() {
		return charge;
	}

	@Override
	public Rectangle2D getBounds() {
		return renderable.getBounds(getPosition());
	}
	
}
