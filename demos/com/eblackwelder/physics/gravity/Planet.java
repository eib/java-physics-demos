package com.eblackwelder.physics.gravity;

import java.awt.Color;

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
