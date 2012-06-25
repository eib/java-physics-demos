package com.eblackwelder.physics.gravity;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.world.World;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.ui.UIWorldDriver;

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
	public WorldDriverBase createDriver(JFrame parent) {
		UIWorldDriver driver = new UIWorldDriver(getTitle());
		super.configureWorld(driver);
		driver.setRendererForType(new PlanetRenderer(), OrbitingObject.planetType);
		
		//space (the final frontier):
		driver.setBackgroundColor(Color.BLACK);
		
		//the planets
		World world = driver.getWorld();
		world.addObjects(createPlanets());

		//the sun
		world.addObject(new OrbitingObject(Color.YELLOW, 0, 2.0, 1, 0));
		return driver;
	}

	protected List<OrbitingObject> createPlanets() {
		Dimension size = super.getSize();
		double yearsPerMilli = yearsPerSecond.getValue() / 1000.0;
		double width = Math.max(size.getWidth(), size.getHeight());
		double radiusScale = mercurySizePixels.getValue() / Planet.Mercury.getRadius();
		final double distanceKmScale = width / 2.0 / (Planet.Neptune.getDistanceFromSunKm() + radiusScale * Planet.Neptune.getRadiusKm());
		
		List<OrbitingObject> planetObjects = new ArrayList<OrbitingObject>();
		for (Planet p : Planet.values()) {
			OrbitingObject orbit = new OrbitingObject(p, yearsPerMilli, radiusScale, distanceKmScale);
			planetObjects.add(orbit);
		}
		return planetObjects;
	}
	
	public static void main(String[] args) {
		PhysicsMain.runDemo(SolarSystemDemo.class.getName());
	}
}
