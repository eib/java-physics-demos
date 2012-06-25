package com.eblackwelder.physics.gravity;

import java.awt.Color;

import com.eblackwelder.math.Position;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Updatable;
import com.eblackwelder.world.model.impl.PositionedObject;

public class OrbitingObject extends PositionedObject implements Updatable, WorldObject {

	public static final Type planetType = new Type("Planet");

	private final Color color;
	private final double yearsPerMilli;
	private final double radius;
	private final double period;
	private final double distanceFromSun;
	
	private double year = 0.0;
	
	public OrbitingObject(Color color, double yearsPerMilli, double radius, double period,
			double distanceFromSun) {
		this.color = color;
		this.yearsPerMilli = yearsPerMilli;
		this.radius = radius;
		this.period = period;
		this.distanceFromSun = distanceFromSun;
	}

	public OrbitingObject(Planet p, double yearsPerMilli, double radiusScale, double distanceKmScale) {
		this.color = p.getColor();
		this.yearsPerMilli = yearsPerMilli;
		this.radius = Math.max(1.0, p.getRadius() * radiusScale);
		this.distanceFromSun = p.getDistanceFromSunKm() * distanceKmScale;
		this.period = p.getPeriod();
	}

	public Color getColor() {
		return color;
	}

	public double getYear() {
		return year;
	}
	
	public double getRadius() {
		return radius;
	}

	@Override
	public Type getType() {
		return planetType;
	}

	//orbiting effect (cheating... cuz it's not really gravity):
	@Override
	public void update(long millisElapsed, World world) {
		year += yearsPerMilli * millisElapsed;
		
		double degrees = year / period * 360.0; //years / (years/rev) * (degrees/rev) == degrees
		double theta = Math.toRadians(degrees);
		double r = distanceFromSun;
		
		double x = Math.sin(theta) * r;
		double y = Math.cos(theta) * r;
		super.setPosition(new Position(x, y));
	}
	
}
