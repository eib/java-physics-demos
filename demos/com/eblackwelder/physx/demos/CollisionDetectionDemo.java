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
import com.eblackwelder.physx.CollisionDetection;
import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.PercentOption;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.world.model.impl.KinematicsObject;
import com.eblackwelder.world.modes.BoundedWorldMode;
import com.eblackwelder.world.modes.WorldMode;


/**
 * @author Ethan
 *
 */
public class CollisionDetectionDemo extends AbstractDemo {
	private final Color[] colors = {
			Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.YELLOW
	};
	private final String[] names = {
			"RED", "GREEN", "ORANGE", "BLUE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY", "YELLOW"
	};

	private final Option<Integer> numBallsOption        = new IntegerRangeOption("Number of balls", 1, 150, 15, 1);
	private final Option<Integer> frictionOption        = new PercentOption("Energy loss due to friction", 0, 5);
	private final Option<Integer> elasticityOption      = new RandomizableOption<Integer>(new PercentOption("Elasticity of collisions (per ball)", 100, 5), false);

	private final Option<Integer> minRadiusOption       = new IntegerRangeOption("Min. ball radius", 3, 30, 10, 1);
	private final Option<Integer> maxRadiusOption       = new IntegerRangeOption("Max. ball radius", 5, 75, 35, 1);

	private final Option<Integer> minVelocityOption     = new IntegerRangeOption("Min. velocity", 10, 300, 50, 10);
	private final Option<Integer> maxVelocityOption     = new IntegerRangeOption("Max. velocity", 10, 300, 100, 10);
	private final Option<Color>   colorOption           = new RandomizableOption<Color>(new ChoicesOption<Color>("Color", names, colors), true);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(numBallsOption);
		options.add(maxRadiusOption);
		options.add(minRadiusOption);
		options.add(maxVelocityOption);
		options.add(minVelocityOption);
		options.add(colorOption);
		options.add(frictionOption);
		options.add(elasticityOption);
		return options;
	}

	@Override
	public String getTitle() {
		return "Collision Detection";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"This demo illustrates the effects of collisions (both elastic and ",
				"inelastic) between objects.",
				" ",
				"Forces in play are: idealized frictional forces.",
				" ",
				"Note: These collisions are approximations only. We are still implementing",
				"\"perfect\" Newtonian collisions.",
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
		
		final int NUM_BALLS = numBallsOption.getValue();
		
		double worldElasticity = 1.0 - ((double) frictionOption.getValue() / 100.0);
		world.add(new CollisionDetection(worldElasticity));

		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.0 );
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.0 );
		for (int ii = 0; ii < NUM_BALLS; ii++) {
			Color c = colorOption.getValue();
			
			int minSide = Math.min(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int maxSide = Math.max(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int width = Utils.random(minSide, maxSide);
			
			int maxX = MAX_X - 2 * width;
			int maxY = MAX_Y - 2 * width;
			
			int x = Utils.random(-maxX, maxX);
			int y = Utils.random(-maxY, maxY);
			Point2D position = new Point(x, y);

			int minV = minVelocityOption.getValue();
			int maxV = maxVelocityOption.getValue();
			int vx = Utils.random(minV, maxV);
			int vy = Utils.random(minV, maxV);
			if (Utils.randomBoolean()) {
				vx *= -1;
			}
			if (Utils.randomBoolean()) {
				vy *= -1;
			}
			Point2D velocity = new Point(vx, vy);
		
			double mass = width * width;
			
			double elasticity = (double) elasticityOption.getValue() / 100.0; //1 => no energy loss when hitting a wall, 0 => a sticky wall
			
			//border-radius is the distance b/w the Object's midpoint and the border
			double borderRadius = Math.sqrt(2 * width * width) * elasticity;
			Renderable renderable = new RoundedBlock(width, width, borderRadius, c);
				
			world.add(new KinematicsObject(renderable, mass, elasticity, position, velocity));
		}
		return world;
	}


	public static void main(String[] args) {
		Main.runDemo(CollisionDetectionDemo.class.getName());
	}
}
