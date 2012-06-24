package com.eblackwelder.physics.forces;

import com.eblackwelder.physics.model.Mass;
import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Updatable;

public class Gravity implements WorldObject, Updatable {

	public static Type gravityType = new Type("Gravity");
	
	private final Acceleration g;
	private final double magnitude;
	
	public Gravity(Acceleration g) {
		this.g = g;
		this.magnitude = g.getMagnitude();
	}
	
	@Override
	public void update(long millisElapsed, World world) {
		for (WorldObject obj : world.getObjects()) {
			if (obj instanceof Mass) {
				Mass m = (Mass) obj;
				double mg = this.magnitude * m.getMass();
				m.addForce(new Force(g, mg));
			}
		}
	}

	@Override
	public Type getType() {
		return gravityType;
	}

}
