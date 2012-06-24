/**
 * 
 */
package com.eblackwelder.physics.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.model.Mass;
import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.World;

/**
 * An object with mass. {@link Force}s can be applied on it.
 * 
 * After every call to {@link #update(long, World)}, the applied forces
 * are automatically zero-ed out. Thus, forces must be re-applied to the object every update.
 * 
 * @author Ethan
 */
public class KineticObject extends KinematicsObject implements Mass {
	
	private final double mass;
	private Collection<Force> forces = new ArrayList<Force>();
	
	public KineticObject(double mass) {
		this(mass, new Position());
	}

	public KineticObject(double mass, Position position) {
		this(mass, position, new Velocity());
	}

	public KineticObject(double mass, Position position, Velocity velocity) {
		super(position, velocity);
		this.mass = mass;
	}

	@Override
	public void update(long millisElapsed, World world) {
		Acceleration acceleration = getNetForce().applyToMass(getMass());
		setAcceleration(acceleration);
		super.update(millisElapsed, world);
	}

	@Override
	public double getMass() {
		return this.mass;
	}

	@Override
	public void addForce(Force force) {
		this.forces.add(force);
	}

	@Override
	public Collection<Force> getAppliedForces() {
		return this.forces;
	}

	@Override
	public Force getNetForce() {
		double x = 0;
		double y = 0;
		for (Force f : this.forces) {
			x += f.x;
			y += f.y;
		}
		return new Force(x, y);
	}

	@Override
	public void setNetForce(Force netForce) {
		clearForces();
		this.forces.add(netForce);
	}

	@Override
	public void clearForces() {
		this.forces.clear();
	}
}
