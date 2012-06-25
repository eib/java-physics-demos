package com.eblackwelder.physics.gravity;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.math.MathUtils;
import com.eblackwelder.math.Position;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.collisions.CollidableObject;
import com.eblackwelder.physics.collisions.CollidableRenderer;
import com.eblackwelder.physics.forces.Gravity;
import com.eblackwelder.physics.options.BooleanOption;
import com.eblackwelder.physics.options.ChoicesOption;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.IntegerRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.options.PercentOption;
import com.eblackwelder.physics.options.RandomizableOption;
import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.physics.updatables.ForceResetter;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.ui.UIWorldDriver;

public class GravityDemo extends AbstractDemo {

	private final Color[] colors = {
			Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.YELLOW
	};
	private final String[] names = {
			"RED", "GREEN", "ORANGE", "BLUE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY", "YELLOW"
	};
	
	private final Option<Integer> numBallsOption        = new IntegerRangeOption("Number of balls", 1, 100, 15);
	private final Option<Integer> frictionOption        = new PercentOption("Energy loss due to friction", 5, 5);
	
	private final Option<Double>  gravityXOption        = new DoubleRangeOption("Gravity X-component", -300, 300, 0, 20);
	private final Option<Double>  gravityYOption        = new DoubleRangeOption("Gravity Y-component", -300, 300, -160, 20);
	
	private final Option<Integer> minElasticityOption   = new PercentOption("Min. ball elasticity", 25, 5);
	private final Option<Integer> maxElasticityOption   = new PercentOption("Max. ball elasticity", 95, 5);
	
	private final Option<Integer> minRadiusOption       = new IntegerRangeOption("Min. ball radius", 3, 30, 30, 1);
	private final Option<Integer> maxRadiusOption       = new IntegerRangeOption("Max. ball radius", 5, 75, 30, 1);

	private final Option<Boolean> velocityXOption       = new BooleanOption("Initial X-Velocity", true);
	private final Option<Boolean> velocityYOption       = new BooleanOption("Initial Y-Velocity", false);
	private final Option<Integer> maxVelocityOption     = new IntegerRangeOption("Max. velocity", 10, 300, 100, 20);
	private final Option<Color>   colorOption           = new RandomizableOption<Color>(new ChoicesOption<Color>("Color", names, colors), true);

	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(numBallsOption);
		options.add(frictionOption);
		options.add(gravityXOption);
		options.add(gravityYOption);
		options.add(maxElasticityOption);
		options.add(minElasticityOption);
		options.add(maxRadiusOption);
		options.add(minRadiusOption);
		options.add(velocityXOption);
		options.add(velocityYOption);
		options.add(maxVelocityOption);
		options.add(colorOption);
		return options;
	}

	@Override
	public String getTitle() {
		return "Gravity";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo illustrates the relationship between acceleration and",
			"velocity.",
			" ",
			"Show the velocity and acceleration vectors and notice how one is",
			"constantly changing while the other is constant.",
			" ",
			"Optional forces include: simplified friction and elastic forces.",
			"See how they affect how much energy is lost between bounces. The",
			"more round an object is, the more \"elastic\" it is.",
			" "
		};
	}

	@Override
	public WorldDriverBase createDriver(JFrame parent) {
		UIWorldDriver driver = new UIWorldDriver(getTitle());
		super.configureWorld(driver);
		driver.setRendererForType(new CollidableRenderer(), CollidableObject.collisionType);
		
		World world = driver.getWorld();
		final double gx = gravityXOption.getValue();
		final double gy = -gravityYOption.getValue(); //into "cartesian" coordinates
		
		Acceleration g = new Acceleration(gx, gy);
		
		world.addObject(new ForceResetter());
		world.addObject(new Gravity(g));
		// world.add(new Drag(0.1, bounds)); //TODO: Implement drag (as an option)
		world.addObjects(createObjects());		
		return driver;
	}

	private Collection<WorldObject> createObjects() {
		Collection<WorldObject> objects = new ArrayList<WorldObject>();
		
		Dimension size = super.getSize();
		final int NUM_BALLS  = numBallsOption.getValue();
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.3 );
		for (int ii = 0; ii < NUM_BALLS; ii++) {
			Color c = colorOption.getValue();
			
			int minSide = Math.min(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int maxSide = Math.max(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int width = MathUtils.random(minSide, maxSide);
			int height = MathUtils.random(minSide, maxSide);
			
			int maxY = MAX_Y - 2 * height;
			
			int x = (int) MathUtils.random(0, size.getWidth());
			int y = MathUtils.random(0, maxY);
			Position position = new Position(x, y);

			int maxV = maxVelocityOption.getValue();
			int vx = velocityXOption.getValue() ? MathUtils.random(-maxV, maxV) : 0;
			int vy = velocityYOption.getValue() ? MathUtils.random(-maxV, maxV) : 0;
			Velocity velocity = new Velocity(vx, vy);
			
			double mass = width * 2.0;
			
			double minElasticity = Math.min(minElasticityOption.getValue(), maxElasticityOption.getValue()) / 100.0;
			double maxElasticity = Math.max(minElasticityOption.getValue(), maxElasticityOption.getValue()) / 100.0;
			double elasticity = MathUtils.random(minElasticity, maxElasticity); //1 => no energy loss when hitting a wall, 0 => a sticky wall
			
			WorldObject object = new CollidableObject(mass, elasticity, c, width, position, velocity);
			objects.add(object);
		}
		return objects;
	}

	public static void main(String[] args) {
		PhysicsMain.runDemo(GravityDemo.class.getName());
	}
}
