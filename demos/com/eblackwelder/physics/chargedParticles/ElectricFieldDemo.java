/**
 * 
 */
package com.eblackwelder.physics.chargedParticles;

import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.forces.ElectricField;
import com.eblackwelder.physics.options.BooleanOption;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.renderers.ElectricFieldRenderer;
import com.eblackwelder.physics.updatables.ForceResetter;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.ui.UIWorldDriver;

/**
 * @author Ethan
 *
 */
public class ElectricFieldDemo extends ChargedParticleDemo {
	
	final Option<Boolean> fieldDirection   = new BooleanOption("Field is positive", true);
	final Option<Double>  fieldStrength    = new DoubleRangeOption("Field Strength (X)", 10, 400, 150, 25);
	
	@Override
	public String getTitle() {
		return "Electric Field";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo illustrates the effects of an electric field on",
			"charged particles.",
			" ",
			"See how the force exerted on a charged particle by an electric field ",
			"is affected by the following properties of the particle:",
			" 1) polarity (red vs. blue)",
			" 2) charge strength (the brightness of the particle's color)",
			" 3) mass (proportional to the particle's size)",
			" "
		};
	}
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(fieldDirection);
		options.add(fieldStrength);
		return options;
	}

	@Override
	public WorldDriverBase createDriver(JFrame parent) {
		Collection<WorldObject> particles = createParticles();
		WorldObject field = createElectricField();
		
		UIWorldDriver driver = new UIWorldDriver("Electric Field");
		World world = driver.getWorld();
		world.addObject(new ForceResetter());
		world.addObject(field);
		world.addObjects(particles);
		
		driver.addRenderer(new ElectricFieldRenderer());
		double MAX_CHARGE = Math.max(minCharge.getValue(), maxCharge.getValue());
		driver.setRendererForType(new ParticleRenderer(4, MAX_CHARGE), Particle.particleType);
		super.configureWorld(driver);
		return driver;
	}

	private ElectricField createElectricField() {
		double ex = fieldStrength.getValue();
		boolean isPositive = fieldDirection.getValue();
		Vector2D E = new Vector2D(ex, 0);
		return new ElectricField(E, isPositive);
	}

	public static void main(String[] args) {
		PhysicsMain.runDemo(ElectricFieldDemo.class.getName());
	}
}
