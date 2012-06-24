package com.eblackwelder.physics.forces;

import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physics.model.Charge;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Updatable;

public class ElectricField implements WorldObject, Updatable {

	public static Type electricFieldType = new Type("ElectricField");

	private final boolean isPositive;
	private final Vector2D E; //used for direction
	private final double magnitude;

	public ElectricField(Vector2D E, boolean isPositive) {
		this.E = E;
		this.magnitude = E.getMagnitude() * (isPositive ? 1.0 : -1.0);
		this.isPositive = isPositive;
	}

	@Override
	public void update(long millisElapsed, World world) {
		for (WorldObject obj : world.getObjects()) {
			if (obj instanceof Charge) {
				Charge c = (Charge) obj;
				applyForce(c);
			}
		}
	}

	public boolean isPositive() {
		return isPositive;
	}

	private void applyForce(Charge c) {
		double qE = this.magnitude * c.getCharge();
		c.addForce(new Force(E, qE));
	}

	@Override
	public Type getType() {
		return electricFieldType;
	}

}
