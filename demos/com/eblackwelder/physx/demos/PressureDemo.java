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
import com.eblackwelder.physx.Vector2D;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.physx.object.Mass;
import com.eblackwelder.physx.object.Updatable;
import com.eblackwelder.physx.object.impl.MassObject;
import com.eblackwelder.physx.physics.CollisionDetection;
import com.eblackwelder.physx.renderable.Ball;
import com.eblackwelder.physx.renderable.Block;
import com.eblackwelder.physx.renderable.Renderable;
import com.eblackwelder.physx.worldMode.BoundedWorldMode;
import com.eblackwelder.physx.worldMode.WorldMode;
import com.eblackwelder.physx.worldMode.BoundedWorldMode.WALL;


/**
 * @author Ethan
 *
 */
public class PressureDemo extends AbstractDemo {
	private final Color[] colors = {
			Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.YELLOW
	};
	private final String[] names = {
			"RED", "GREEN", "ORANGE", "BLUE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY", "YELLOW"
	};
	private final Option<Color>   colorOption           = new RandomizableOption<Color>(new ChoicesOption<Color>("Color", names, colors, 3), false);

	private final Option<Boolean> openTopOption         = new BooleanOption("Open on top", true);
	private final Option<Integer> numBallsOption        = new IntegerRangeOption("Number of balls", 0, 1000, 150, 20);
	private final Option<Integer> gravityOption         = new IntegerRangeOption("Gravity", 0, 300, 200, 10);
	private final Option<Integer> massOption            = new IntegerRangeOption("Mass of block", 1000, 500000, 15000, 1000);
	
	private final Option<Integer> minRadiusOption       = new IntegerRangeOption("Min. ball radius", 3, 30, 5, 1);
	private final Option<Integer> maxRadiusOption       = new IntegerRangeOption("Max. ball radius", 3, 30, 5, 1);

	private final Option<Integer> minVelocityOption     = new IntegerRangeOption("Min. velocity", 10, 1000, 50, 10);
	private final Option<Integer> maxVelocityOption     = new IntegerRangeOption("Max. velocity", 10, 1000, 300, 10);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(openTopOption);
		options.add(numBallsOption);
		options.add(gravityOption);
		options.add(massOption);
		options.add(maxRadiusOption);
		options.add(minRadiusOption);
		options.add(maxVelocityOption);
		options.add(minVelocityOption);
		options.add(colorOption);
		return options;
	}

	@Override
	public String getTitle() {
		return "Piston";
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
		WorldMode mode;
		if (openTopOption.getValue()) {
			mode = new BoundedWorldMode(WALL.EAST, WALL.SOUTH, WALL.WEST);
		} else {
			mode = new BoundedWorldMode(); //all faces
		}
		return mode;
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D bounds) {
		List<Object> world = super.getWorldObjects(size, bounds);
		
		final int NUM_BALLS = numBallsOption.getValue();
		
		int BLOCK_THICKNESS = 50;
		
		Renderable r = new Block(size.width - 10, BLOCK_THICKNESS, Color.RED);
		double blockMass = massOption.getValue();
		final int mg = (int) blockMass * -gravityOption.getValue();
		final Mass block = new MassObject(r, blockMass, new Point(0, size.height / 2 - BLOCK_THICKNESS)) {
			@Override public void setPosition(Point2D position) {
				super.setPosition(position);
			}
		};
		world.add(new Updatable() {
			@Override public void update(long millisElapsed) {
				block.addForce(new Point(0, mg));
			}
		});
		world.add(new TargettedCollisionDetection(block));
		world.add(block);

		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.0);
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.0);
		for (int ii = 0; ii < NUM_BALLS; ii++) {
			Color c = colorOption.getValue();
			
			int minSide = Math.min(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int maxSide = Math.max(minRadiusOption.getValue(), maxRadiusOption.getValue());
			int width = Utils.random(minSide, maxSide);
			
			int maxX = MAX_X - 2 * width;
			int maxY = MAX_Y - 2 * width;
			
			int x = Utils.random(-maxX, maxX);
			int y = Utils.random(-maxY, (int) maxY / 2);
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
			
			//border-radius is the distance b/w the Object's midpoint and the border
			Renderable renderable = new Ball(width, c);
				
			world.add(new MassObject(renderable, mass, position, velocity));
		}
		return world;
	}


	public static void main(String[] args) {
		Main.runDemo(PressureDemo.class.getName());
	}
}

class TargettedCollisionDetection extends CollisionDetection implements Updatable {
	private final Mass block;
	private long deltaT = 0;

	public TargettedCollisionDetection(Mass block) {
		this.block = block;
	}

	@Override
	public void update(long millisElapsed) {
		deltaT = millisElapsed;
	}
	
	@Override
	protected boolean isCollision(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2) {
		if (a1 == block) {
			return super.isCollision(a1, a2, b1, b2) && a2.getVelocity().y > 0;
		} else if (a2 == block) {
			return isCollision(a2, a1, b2, b1);
		}
		return super.isCollision(a1, a2, b1, b2);
	}

	@Override protected void collide(Mass o1, Mass o2) {
		if (o2 == block) {
			collide(o2, o1);
		} else if (o1 == block) {
			double m1 = o1.getMass();
			double m2 = o2.getMass();
			
			Vector2D v1 = o1.getVelocity();
			Vector2D v2 = o2.getVelocity();
			
			double v1i = v1.y;
			double v2i = v2.y;
			
			double v1f = -v1i + 2 * (m1 * v1i + m2 * v2i) / (m1 + m2);
			double v2f = -v2i + 2 * (m1 * v1i + m2 * v2i) / (m1 + m2);

			if (deltaT > 0) {
				double a1 = (v1f - v1i) / deltaT;
				double a2 = (v2f - v2i) / deltaT;

				double damping1 = 0.98;
				double damping2 = 1.0;
				double scale = 1000.0;
				double f1 = a1 * m1 * scale * damping1;
				double f2 = a2 * m2 * scale * damping2;
	
				o1.addForce(new Vector2D(0, f1));
				o2.addForce(new Vector2D(0, f2));
				
				//System.out.println(String.format("Forces: %.2f, %.2f\tDelta-v: %.3f, %.3f", f1, f2, (v1f - v1i), (v2f - v2i)));
			}
//		} else {
//			super.collide(o1, o2);
		}
	}

	@Override
	public boolean affects(Mass a, Rectangle2D bounds) {
		return true;
	}
}