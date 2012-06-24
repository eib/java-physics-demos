package com.eblackwelder.physics.properties;

import com.eblackwelder.math.Vector2D;
import com.eblackwelder.math.Velocity;

public class Acceleration extends Vector2D {

	public Acceleration() {
		super();
	}

	public Acceleration(double x, double y) {
		super(x, y);
	}

	public Acceleration(Vector2D other) {
		super(other);
	}

	public Acceleration add(Acceleration other) {
		return new Acceleration(this.x + other.x, this.y + other.y);
	}

	public Velocity getDeltaVelocity(double deltaSeconds) {
		double vx = this.x * deltaSeconds;
		double vy = this.y * deltaSeconds;
		return new Velocity(vx, vy);
	}
}
