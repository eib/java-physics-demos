/**
 * 
 */
package com.eblackwelder.physics.model.impl;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.model.Charge;

/**
 * @author Ethan
 *
 */
public class ChargedParticle extends KineticObject implements Charge {

	private final double charge;
	
	public ChargedParticle(double mass, double charge) {
		this(mass, charge, new Position());
	}

	public ChargedParticle(double mass, double charge, Position position) {
		this(mass, charge, position, new Velocity());
	}

	public ChargedParticle(double mass, double charge, Position position, Velocity velocity) {
		super(mass, position, velocity);
		this.charge = charge;
	}

	@Override
	public double getCharge() {
		return charge;
	}

}
