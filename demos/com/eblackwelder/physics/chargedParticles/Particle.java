package com.eblackwelder.physics.chargedParticles;

import com.eblackwelder.math.Position;
import com.eblackwelder.physics.model.impl.ChargedParticle;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;

public class Particle extends ChargedParticle implements WorldObject {
	public static final Type particleType = new Type("Particle");

	public Particle(double mass, double charge, Position position) {
		super(mass, charge, position);
	}

	@Override
	public void update(long millisElapsed, World world) {
		super.update(millisElapsed, world);
	}

	@Override
	public Type getType() {
		return particleType;
	}
	
}
