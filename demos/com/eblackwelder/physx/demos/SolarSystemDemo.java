package com.eblackwelder.physx.demos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.effects.Viscosity;
import com.eblackwelder.physx.object.Positionable;
import com.eblackwelder.physx.object.Updatable;
import com.eblackwelder.physx.object.impl.MassObject;
import com.eblackwelder.physx.object.impl.PositionedObject;
import com.eblackwelder.physx.renderable.Ball;
import com.eblackwelder.physx.renderable.Field;
import com.eblackwelder.physx.renderable.Renderable;
import com.eblackwelder.physx.worldMode.OpenWorldMode;
import com.eblackwelder.physx.worldMode.WorldMode;

public class SolarSystemDemo extends AbstractDemo {

	private final Option<Double> yearsPerSecond = new DoubleRangeOption("Earth years per second", 0, 10, 0.3, .1);
	private final Option<Double> mercurySizePixels = new DoubleRangeOption("Mercury size (pixels)", 0.1, 10.0, 1.0, .1);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(yearsPerSecond);
		options.add(mercurySizePixels);
		return options;
	}

	@Override
	public String getTitle() {
		return "Solar System";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo shows off our very own solar system.",
			" ",
			"1. Only the 8 \"official\" planets are rendered.",
			"2. Planet sizes are proportional to one another, minimum 1 pixel each.",
			"   (The sun is always a few white pixels in the center of the system.)",
			"3. The distances between planets are proportional to one another.",
			"4. Orbit periods are proportional (you control how many Earth years elapse per second).",
			" ",
		};
	}

	@Override
	protected WorldMode getWorldMode() {
		return new OpenWorldMode();
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D bounds) {
		List<Object> world = super.getWorldObjects(size, bounds);
		
		//all the planets:
		final Map<Planet, Positionable> planetObjects = new HashMap<Planet, Positionable>();
		double width = Math.max(size.getWidth(), size.getHeight());
		double radiusScale = mercurySizePixels.getValue() / Planet.Mercury.getRadius();
		final double distanceKmScale = width / 2.0 / (Planet.Neptune.getDistanceFromSunKm() + radiusScale * Planet.Neptune.getRadiusKm());
		for (Planet p : Planet.values()) {
			double radius = Math.max(1.0, p.getRadius() * radiusScale);
			double mass = p.getMass();
			double x = p.getDistanceFromSunKm() * distanceKmScale;
			double y = 0;
			Renderable r = new Ball(radius, p.getColor());
			Positionable obj = new MassObject(r, mass, new Point.Double(x, y));
			world.add(obj);
			planetObjects.put(p, obj);
			System.out.println(p.getName() + " at " + x + ", " + y);
		}
		
		//orbiting effect (cheating... cuz it's not really gravity):
		final double yearsPerMilli = yearsPerSecond.getValue() / 1000.0;
		world.add(new Updatable() {
			private double year = 0.0;
			@Override public void update(long millisElapsed) {
				year += yearsPerMilli * millisElapsed;
				
				for (Planet p : Planet.values()) {
					double degrees = year / p.getPeriod() * 360.0; //years / (years/rev) * (degrees/rev) == degrees
					double theta = Math.toRadians(degrees);
					double r = p.getDistanceFromSunKm() * distanceKmScale;
					
					double x = Math.sin(theta) * r;
					double y = Math.cos(theta) * r;
					Positionable obj = planetObjects.get(p);
					obj.setPosition(new Point.Double(x, y));
				}
			}
		});

		//space (the final frontier):
		world.add(new Field(bounds, Color.BLACK));
		
		//the sun:
		world.add(new PositionedObject(new Ball(2, Color.WHITE), new Point(0, 0)));
		return world;
	}

	public static void main(String[] args) {
		Main.runDemo(SolarSystemDemo.class.getName());
	}
}

/*
 * Earth's distance from Sun: 149597890 km
 * Earth's radius: 6378.1 km
 * Earth's mass: 5.9742×10^24 kg
 */
enum Planet {
	Mercury(0.38709893, 0.3825, 0.055, 0.2408467, new Color(231, 176, 73)), //distance (from sun), radius, mass, Color
	Venus(0.72333199, 0.9488, 0.815, 0.61519726, new Color(231, 178, 110)),
	Earth(1.00000011, 1, 1, 1.0000174, new Color(104, 156, 240)),
	Mars(1.52366231, 0.53226, 0.107, 1.8808476, new Color(217, 184, 130)),
	Jupiter(5.20336301, 11.209, 318, 11.862615, new Color(217, 184, 130)),
	Saturn(9.53707032, 9.449, 95, 29.447498, new Color(190, 174, 158)),
	Uranus(19.19126393, 4.007, 14, 84.016846, new Color(190, 228, 229)),
	Neptune(30.06896348, 3.883, 17, 164.79132, new Color(92, 143, 235));

	public static final double EARTH_DISTANCE_FROM_SUN_KM = 149597890.0;
	public static final double EARTH_RADIUS_KM = 6378.1;
	public static final double EARTH_MASS_KG = 5.9742 * Math.pow(10, 24);
	
	private double distanceFromSun; //Astronomical Unit (AU)
	private double radius; //:E (ratio to Earth's radius)
	private double mass; //:E (ratio to Earth's mass)
	private double period; //years
	private Color color;
	
	private Planet(double distanceFromSun, double radius, double mass, double period, Color color) {
		this.distanceFromSun = distanceFromSun;
		this.radius = radius;
		this.mass = mass;
		this.period = period;
		this.color = color;
	}

	public String getName() {
		return this.name();
	}

	public Color getColor() {
		return color;
	}

	public double getDistanceFromSun() {
		return distanceFromSun;
	}

	public double getDistanceFromSunKm() {
		return distanceFromSun * EARTH_DISTANCE_FROM_SUN_KM;
	}
	
	public double getPeriod() {
		return period;
	}

	public double getRadius() {
		return radius;
	}
	
	public double getRadiusKm() {
		return radius * EARTH_RADIUS_KM;
	}

	public double getMass() {
		return mass;
	}
	
	public double getMassKg() {
		return mass * EARTH_MASS_KG;
	}
}
