package com.eblackwelder.physics.collisions;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.model.impl.KineticObject;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Colorable;

public class CollidableObject extends KineticObject implements WorldObject, Colorable {

	public static final Type collisionType = new Type("Collision Object");

	private final double width;
	private final double elasticity;
	private final Color color;
	private final Shape shape;

	public CollidableObject(double mass, double elasticity, Color color, double width) {
		this(mass, elasticity, color, width, new Position());
	}
	
	public CollidableObject(double mass, double elasticity, Color color, double width, Position position) {
		this(mass, elasticity, color, width, position, new Velocity());
	}
	
	public CollidableObject(double mass, double elasticity, Color color, double width, Position position, Velocity velocity) {
		super(mass, position, velocity);
		this.width = width;
		this.elasticity = elasticity;
		this.color = color;

		double borderRadius = width * elasticity; //border-radius is the distance b/w the Object's midpoint and the border
		this.shape = new RoundRectangle2D.Double(-width / 2.0, -width / 2.0, width, width,
				borderRadius, borderRadius);
	}

	public Shape getShape() {
		return shape;
	}

	public double getWidth() {
		return width;
	}

	public double getElasticity() {
		return elasticity;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Type getType() {
		return collisionType;
	}
	
}
