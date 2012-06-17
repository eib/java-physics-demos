/**
 * 
 */
package com.eblackwelder.world.model.impl;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Charge;

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
