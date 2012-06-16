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

import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.Utils;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.PercentOption;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.physx.effects.Gravity;
import com.eblackwelder.physx.object.Mass;
import com.eblackwelder.physx.object.impl.KinematicsObject;
import com.eblackwelder.physx.physics.CollisionDetection;
import com.eblackwelder.physx.renderable.Ball;
import com.eblackwelder.physx.renderable.Renderable;
import com.eblackwelder.physx.worldMode.BoundedWorldMode;
import com.eblackwelder.physx.worldMode.WorldMode;


/**
 * @author Ethan
 *
 */
public class RollYourOwnDemo extends AbstractDemo {
	private final Color[] colors = {
			Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.YELLOW
	};
	private final String[] names = {
			"RED", "GREEN", "ORANGE", "BLUE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY", "YELLOW"
	};
	
	private final Option<Integer> numBallsOption        = new IntegerRangeOption("Number of balls", 1, 100, 20);
	private final Option<Integer> frictionOption        = new PercentOption("Energy loss due to friction", 0, 5);
	private final Option<Boolean> gravityOption         = new BooleanOption("Use Gravity", false);
	private final Option<Double>  gravityXOption        = new DoubleRangeOption("Gravity X-component", -300, 300, 0, 20);
	private final Option<Double>  gravityYOption        = new DoubleRangeOption("Gravity Y-component", -300, 300, -160, 20);
	private final Option<Boolean> collisionDetection    = new BooleanOption("Collision Detection", false);
	private final Option<Integer> elasticityOption      = new PercentOption("Elasticity of collisions", 100, 5);
	private final Option<Boolean> velocityOption        = new BooleanOption("Initial Velocity", false);
	private final Option<Integer> velocityXOption       = new RandomizableOption<Integer>(new IntegerRangeOption("Velocity X-component", -300, 300, 0, 20), true);
	private final Option<Integer> velocityYOption       = new RandomizableOption<Integer>(new IntegerRangeOption("Velocity Y-component", -300, 300, 0, 20), true);
	private final Option<Color>   colorOption           = new RandomizableOption<Color>(new ChoicesOption<Color>("Color", names, colors), true);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(numBallsOption);
		options.add(frictionOption);
		options.add(gravityOption);
		options.add(gravityXOption);
		options.add(gravityYOption);
		options.add(collisionDetection);
		options.add(elasticityOption);
		options.add(velocityOption);
		options.add(velocityXOption);
		options.add(velocityYOption);
		options.add(colorOption);
		return options;
	}

	@Override
	public String getTitle() {
		return "Sandbox";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo is a sandbox for kinematics and collisions.",
			" ",
			"Have fun!",
			" ",
		};
	}

	@Override
	protected WorldMode getWorldMode() {
		return new BoundedWorldMode(1.0);
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D bounds) {
		List<Object> world = super.getWorldObjects(size, bounds);
		final int numBalls = numBallsOption.getValue();
		if (gravityOption.getValue()) {
			world.add(new Gravity(gravityXOption.getValue(), gravityYOption.getValue()));
		}
		if (collisionDetection.getValue()) {
			world.add(new CollisionDetection((1.0 - (double) elasticityOption.getValue()) / 100.0));
		}
	
		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.0 );
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.0 );
		for (int ii = 0; ii < numBalls; ii++) {
			Color c = colorOption.getValue();
			int radius = Utils.random(9, 35);
			Renderable ball = new Ball(radius, c);
			int maxX = MAX_X - 2 * radius;
			int maxY = MAX_Y - 2 * radius;
			
			int x = Utils.random(-maxX, maxX);
			int y = Utils.random(-maxY, maxY);
			Point2D position = new Point(x, y);
	
			boolean initialVelocity = velocityOption.getValue();
			int vx = initialVelocity ? velocityXOption.getValue() : 0;
			int vy = initialVelocity ? velocityYOption.getValue() : 0;
			Point2D velocity = new Point(vx, vy);
			
			double mass = Math.PI * radius * radius;
			double elasticity = 1.0 - ((double) frictionOption.getValue() / 100.0); //1 => no energy loss when hitting a wall, 0 => a sticky wall
			
			Mass m = new KinematicsObject(ball, mass, elasticity, position, velocity);
			world.add(m);
		}
		return world;
	}

	public static void main(String[] args) {
		Main.runDemo(RollYourOwnDemo.class.getName());
	}
}
