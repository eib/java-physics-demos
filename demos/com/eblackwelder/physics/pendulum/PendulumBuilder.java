package com.eblackwelder.physics.pendulum;

import com.eblackwelder.math.Position;
import com.eblackwelder.physics.properties.Acceleration;

public class PendulumBuilder {
	double mass = 1;
	double length = 1;
	double px = 0;
	double py = 0;
	double gy = 0;
	double gx = 0;
	double startingTheta = 0;
	
	public PendulumBuilder setMass(double mass) {
		this.mass = mass;
		return this;
	}
	public PendulumBuilder setArmLength(double length) {
		this.length = length;
		return this;
	}
	public PendulumBuilder setPivot(double px, double py) {
		this.px = px;
		this.py = py;
		return this;
	}
	public PendulumBuilder setGravity(double gx, double gy) {
		this.gx = gx;
		this.gy = gy;
		return this;
	}
	public PendulumBuilder setGravity(Acceleration g) {
		this.gx = g.x;
		this.gy = g.y;
		return this;
	}
	public PendulumBuilder setStartingTheta(double startingTheta) {
		this.startingTheta = startingTheta;
		return this;
	}
	public PendulumBuilder setStartingDegrees(double startingDegrees) {
		return setStartingTheta(Math.toRadians(startingDegrees));
	}
	public Pendulum build() {
		return new Pendulum(mass, length, new Position(px, py), startingTheta, new Acceleration(gx, gy));
	}
}