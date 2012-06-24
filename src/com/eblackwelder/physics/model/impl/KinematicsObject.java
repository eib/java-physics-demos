package com.eblackwelder.physics.model.impl;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.model.Acceleratable;
import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.world.World;
import com.eblackwelder.world.model.impl.MovingObject;

/**
 * @author Ethan
 *
 */
public class KinematicsObject extends MovingObject implements Acceleratable {
	
	private Acceleration acceleration; //acceleration "vector", in position change per second
	
	public KinematicsObject() {
		this(new Position());
	}
	public KinematicsObject(Position position) {
		this(position, new Velocity());
	}
	public KinematicsObject(Position position, Velocity velocity) {
		this(position, velocity, new Acceleration());
	}
	public KinematicsObject(Position position, Velocity velocity, Acceleration acceleration) {
		super(position, velocity);
		this.acceleration = acceleration;
	}

	@Override
	public void update(long millisElapsed, World world) {
		double deltaSeconds = millisElapsed / 1000.0;

		Velocity deltaV = getAcceleration().getDeltaVelocity(deltaSeconds);
		Velocity newV = getVelocity().add(deltaV);
		setVelocity(newV);
		
		super.update(millisElapsed, world);
	}
	
	@Override
	public Acceleration getAcceleration() {
		return acceleration;
	}
	
	@Override
	public void setAcceleration(Acceleration acceleration) {
		this.acceleration = acceleration;
	}
}
