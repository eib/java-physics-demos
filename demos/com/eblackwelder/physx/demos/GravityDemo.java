/**
 * 
 */
package com.eblackwelder.physx.demos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.graphx.renderable.RoundedBlock;
import com.eblackwelder.math.Utils;
import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.PercentOption;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.physx.effects.Gravity;
import com.eblackwelder.physx.effects.Viscosity;
import com.eblackwelder.world.model.impl.KinematicsObject;
import com.eblackwelder.world.modes.BoundedWorldMode;
import com.eblackwelder.world.modes.WorldMode;

/**
 * @author Ethan
 *
 */
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
	protected WorldMode getWorldMode() {
		double worldElasticity = 1.0 - ((double) frictionOption.getValue() / 100.0);
		return new BoundedWorldMode(worldElasticity);
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D bounds) {
		List<Object> world = super.getWorldObjects(size, bounds);
		final int NUM_BALLS  = numBallsOption.getValue();
		final double gx = gravityXOption.getValue();
		final double gy = gravityYOption.getValue();
		
		//Add some forces:
		world.add(new Gravity(gx, gy));
		
		//a tad bit of air resistance:
		world.add(new Viscosity(0.1, bounds));

		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.3 );
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.3 );
		for (int ii = 0; ii < NUM_BALLS; ii++) {
			Color c = colorOption.getValue();
			
			int minSide = Math.min(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int maxSide = Math.max(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int width = Utils.random(minSide, maxSide);
			int height = Utils.random(minSide, maxSide);
			
			int maxX = MAX_X - 2 * width;
			int maxY = MAX_Y - 2 * height;
			
			int x = Utils.random(-maxX, maxX);
			int y = Utils.random(0, maxY);
			Point2D position = new Point(x, y);

			int maxV = maxVelocityOption.getValue();
			int vx = velocityXOption.getValue() ? Utils.random(-maxV, maxV) : 0;
			int vy = velocityYOption.getValue() ? Utils.random(-maxV, maxV) : 0;
			Point2D velocity = new Point(vx, vy);
			
			double mass = width * height;
			
			double minElasticity = Math.min(minElasticityOption.getValue(), maxElasticityOption.getValue()) / 100.0;
			double maxElasticity = Math.max(minElasticityOption.getValue(), maxElasticityOption.getValue()) / 100.0;
			double elasticity = Utils.random(minElasticity, maxElasticity); //1 => no energy loss when hitting a wall, 0 => a sticky wall
			
			//border-radius is the distance b/w the Object's midpoint and the border
			double borderRadius = Math.sqrt(height * height + width * width) * elasticity;
			Renderable renderable = new RoundedBlock(width, height, borderRadius, c);
			
			world.add(new KinematicsObject(renderable, mass, elasticity, position, velocity));
		}
		return world;
	}

	public static void main(String[] args) {
		Main.runDemo(GravityDemo.class.getName());
	}
}
