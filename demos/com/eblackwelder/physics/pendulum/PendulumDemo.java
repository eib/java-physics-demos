/**
 * 
 */
package com.eblackwelder.physics.pendulum;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.forces.Gravity;
import com.eblackwelder.physics.options.ChoicesOption;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.IntegerRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.options.RandomizableOption;
import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.physics.updatables.ForceResetter;
import com.eblackwelder.world.World;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.ui.UIWorldDriver;

/**
 * @author Ethan
 *
 */
public class PendulumDemo extends AbstractDemo {
	
	private final Color[] colors = {
			Color.YELLOW, Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY
	};
	private final String[] names = {
			"YELLOW", "RED", "GREEN", "ORANGE", "BLUE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY"
	};
	private final Option<Color>   colorOption           = new RandomizableOption<Color>(new ChoicesOption<Color>("Color", names, colors, 2), false);
	
	private final Option<Double>  massOption            = new DoubleRangeOption("Object mass", 10, 1000, 100, 10);
	private final Option<Double>  gravityYOption        = new DoubleRangeOption("Gravity", 100, 1000, 300, 10);
	private final Option<Double>  startingAngleOption   = new RandomizableOption<Double>(new DoubleRangeOption("Starting angle (degrees)", -70, 70, 60, 5), false);
	private final Option<Integer> armLengthOption       = new IntegerRangeOption("Arm length (px)", 100, 400, 250, 5);
	
	@Override
	public String getTitle() {
		return "Pendulum";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"This demo illustrates the interaction between tension ",
				"and gravity.",
				" ",
				"Every frame, the tension and gravity forces are re-applied,",
				"directly affecting the acceleration of the pendulum ball,",
				"and indirectly modifying its velocity and position.",
				" ",
		};
	}

	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(startingAngleOption);
		options.add(armLengthOption);
		options.add(gravityYOption);
		options.add(colorOption);
		options.add(massOption);
		return options;
	}

	@Override
	public WorldDriverBase createDriver(JFrame parent) {
		UIWorldDriver driver = new UIWorldDriver("Pendulum");
		super.configureWorld(driver);
		
		Acceleration g = new Acceleration(0, gravityYOption.getValue());
		PendulumBuilder builder = new PendulumBuilder();
		builder
			.setMass(massOption.getValue())
			.setArmLength(armLengthOption.getValue())
			.setStartingDegrees(startingAngleOption.getValue())
			.setGravity(g)
			.setPivot(super.widthOption.getValue() / 2.0, 20);
		
		Pendulum pendulum = builder.build();

		World world = driver.getWorld();
		driver.setBackgroundColor(Color.WHITE);
		world.addObject(new ForceResetter());
		world.addObject(new Gravity(g));
		world.addObject(pendulum);
		driver.setRendererForType(new PendulumRenderer(), Pendulum.pendulumType);
		return driver;
	}

	public static void main(String[] args) {
		PhysicsMain.runDemo(PendulumDemo.class.getName());
	}
}
