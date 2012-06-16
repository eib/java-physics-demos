package com.eblackwelder.physx;

import java.awt.geom.Point2D;

@SuppressWarnings("serial")
public class Vector2D extends java.awt.geom.Point2D.Double {

	public static Vector2D zeroVector() {
		return new Vector2D(0, 0);
	}
	
	public Vector2D(double x, double y) {
		super(x, y);
	}
	
	public Vector2D(Point2D p) {
		this(p.getX(), p.getY());
	}

	public Vector2D add(Point2D addend) {
		this.x += addend.getX();
		this.y += addend.getY();
		return this;
	}

	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2D scale(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	public Vector2D reverse() {
		this.x = -x;
		this.y = -y;
		return this;
	}
	
	public Vector2D zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}

	public Vector2D unit() {
		double length = Math.sqrt(x * x + y * y);
		this.x /= length;
		this.y /= length;
		return this;
	}
	
	public double getMagnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public static Vector2D scale(Point2D v, double scalar) {
		return new Vector2D(v.getX() * scalar, v.getY() * scalar);
	}
	
	public static Vector2D add(Point2D a, Point2D b) {
		return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY());
	}

	public static Vector2D midPoint(Point2D a, Point2D b) {
		return new Vector2D(a.getX() + (b.getX() - a.getX()) / 2.0, a.getY() + (b.getY() - a.getY()) / 2.0);
	}
	
	/**
	 * @param a
	 * @param b
	 * @return The vector A - B
	 */
	public static Vector2D subtract(Point2D a, Point2D b) {
		return new Vector2D(a.getX() - b.getX(), a.getY() - b.getY());
	}
}
