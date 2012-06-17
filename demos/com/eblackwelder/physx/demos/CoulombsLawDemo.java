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

import com.eblackwelder.graphx.renderable.Ball;
import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Utils;
import com.eblackwelder.physx.CoulombsLaw2;
import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.PercentOption;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.world.model.impl.ChargedParticle;
import com.eblackwelder.world.modes.BoundedWorldMode;
import com.eblackwelder.world.modes.OpenWorldMode;
import com.eblackwelder.world.modes.WorldMode;

/**
 * @author Ethan
 *
 */
public class CoulombsLawDemo extends AbstractDemo {

	private final Option<Boolean> closedWorld      = new BooleanOption("Closed World", true);
	private final Option<Integer> numParticles     = new IntegerRangeOption("Number of particles", 0, 200, 15, 1);
	private final Option<Integer> percentPositives = new RandomizableOption<Integer>(new PercentOption("Positive particles", 75, 5), false);
	private final Option<Integer> startingMass     = new RandomizableOption<Integer>(new IntegerRangeOption("Particle Starting Mass", 10, 120, 80, 10), true);
	private final Option<Double>  maxCharge        = new DoubleRangeOption("Max. Charge", 1, 100, 14, 1);
	private final Option<Double>  minCharge        = new DoubleRangeOption("Min. Charge", 1, 100, 8, 1);
	private final Option<Integer> maxMass          = new IntegerRangeOption("Max. Mass", 2, 100, 20, 2);
	private final Option<Integer> minMass          = new IntegerRangeOption("Min. Mass", 2, 100, 10, 2);
	private final Option<Boolean> startingVelocity = new BooleanOption("Starting Velocity", false);
	private final Option<Integer> maxVelocity      = new RandomizableOption<Integer>(new IntegerRangeOption("Max. velocity", 0, 200, 30, 5), true);
	private final Option<Integer> joinDistance     = new IntegerRangeOption("Min. merge distance (px)", 0, 50, 20, 5);
	private final Option<Integer> chargeMultiplier = new IntegerRangeOption("Force multiplier", 1, 200, 50, 5);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(closedWorld);
		options.add(numParticles);
		options.add(percentPositives);
		options.add(startingMass);
		options.add(maxCharge);
		options.add(minCharge);
		options.add(chargeMultiplier);
		options.add(maxMass);
		options.add(minMass);
		options.add(startingVelocity);
		options.add(maxVelocity);
		options.add(joinDistance);
		return options;
	}

	@Override
	public String getTitle() {
		return "Coulombs Law";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo illustrates the effects of Coulomb's Law on charged, moving ",
			"particles.",
			" ",
			"See how oppositely charged particles attract and like charges repel ",
			"one another. See also how mass and distance play a role in how strong ",
			"a force is between two particles.",
			" ",
			"We arbitrarily chose to allow oppositely charged particles to \"merge\" ",
			"into a larger one.",
			" "
		};
	}
	
	@Override
	protected WorldMode getWorldMode() {
		return closedWorld.getValue() ? new BoundedWorldMode() : new OpenWorldMode();
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D bounds) {
		List<Object> world = super.getWorldObjects(size, bounds);
	
		final int NUM_PARTICLES    = numParticles.getValue();
		final int PERCENT_POSITIVE = this.percentPositives.getValue();
		final int NUM_POSITIVES    = (int) Math.floor((double) (NUM_PARTICLES * PERCENT_POSITIVE) / 100.0);
		final double MAX_CHARGE    = Math.max(minCharge.getValue(), maxCharge.getValue());
		final double MIN_CHARGE    = Math.min(minCharge.getValue(), maxCharge.getValue());
		final int MAX_MASS         = Math.max(minMass.getValue(), maxMass.getValue());;
		final int MIN_MASS         = Math.min(minMass.getValue(), maxMass.getValue());
		final int MAX_VELOCITY     = maxVelocity.getValue();
		final int JOIN_DISTANCE    = joinDistance.getValue();
		final int FORCE_MULTIPLIER = chargeMultiplier.getValue() * 1000;
		
		CoulombsLaw2.RenderableCreator renderableCreator = new CoulombsLaw2.RenderableCreator() {
			@Override public Renderable create(double mass, double charge) {
				int radius = (int) (Math.sqrt(mass) / Math.PI * 10.0);
				int red, blue, green;
				
				//positive
				if (charge > 0) {
					blue = 0xFF;
					green = red  = (int) Math.max(0, (MAX_CHARGE - charge) / MAX_CHARGE * 200.0);
					
				 //negative
				} else if (charge < 0) {
					red = 0xFF;
					green = blue = (int) Math.max(0, (MAX_CHARGE + charge) / MAX_CHARGE * 200.0);

				//neutral
				} else {
					red = green = blue = 0xCC;
				}
				Color c = new Color(red, green, blue);
				return new Ball(radius, c);
			}
		};
		
		//Add some physics:
		world.add(new CoulombsLaw2(FORCE_MULTIPLIER, JOIN_DISTANCE, renderableCreator));
		
		//Create some point-charges:
		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.1 );
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.1 );
		for (int ii = 0; ii < NUM_PARTICLES; ii++) {
			double charge = Utils.random(MIN_CHARGE, MAX_CHARGE);
			boolean positive = ii >= NUM_POSITIVES;
			
			if (positive) {
				charge = -charge;
			}
			
			int x = Utils.random(-MAX_X, MAX_X);
			int y = Utils.random(-MAX_Y, MAX_Y);
			Point2D position = new Point(x, y);

			double mass = Utils.random(MIN_MASS, MAX_MASS);
			
			boolean isMoving = startingVelocity.getValue();
			int vx = isMoving ? Utils.random(-MAX_VELOCITY, MAX_VELOCITY) : 0;
			int vy = isMoving ? Utils.random(-MAX_VELOCITY, MAX_VELOCITY) : 0;
			Point2D velocity = new Point(vx, vy);
			
			Renderable renderable = renderableCreator.create(mass, charge);
			world.add(new ChargedParticle(renderable, mass, charge, position, velocity));
		}

//		Renderable renderable = renderableCreator.create(50, -300);
//		world.add(new ChargedParticle(renderable, 50, -300, new Vector2D(Utils.random(-300, 300), Utils.random(-300, 300))));
		return world;
	}

	public static void main(String[] args) {
		Main.runDemo(CoulombsLawDemo.class.getName());
	}
}
