package com.eblackwelder.physics.properties;

import com.eblackwelder.math.Vector2D;

public class Force extends Vector2D {
	
	public Force() {
	}

	public Force(double x, double y) {
		super(x, y);
	}

	public Force(Vector2D other) {
		super(other);
	}

	/**
	 * @param direction The direction of the new Force (does not have to be a unit Vector).
	 * @param magnitude The magnitude of the new Force.
	 */
	public Force(Vector2D direction, double magnitude) {
		super(direction, magnitude);
	}

	public Force add(Force other) {
		return new Force(this.x + other.x, this.y + other.y);
	}

	public Acceleration applyToMass(double mass) {
		double ax = this.x / mass;
		double ay = this.y / mass;
		return new Acceleration(ax, ay);
	}
	
	/**
	 * @return This force subtracted from a zero vector.
	 */
	public Force negative() {
		return new Force(-x, -y);
	}
}
