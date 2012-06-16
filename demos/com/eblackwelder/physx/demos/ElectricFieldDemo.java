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
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.PercentOption;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.physx.effects.ElectricField;
import com.eblackwelder.physx.object.impl.ChargedParticle;
import com.eblackwelder.physx.physics.CoulombsLaw2;
import com.eblackwelder.physx.renderable.Ball;
import com.eblackwelder.physx.renderable.Renderable;
import com.eblackwelder.physx.worldMode.ClosedWorldMode;
import com.eblackwelder.physx.worldMode.WorldMode;

/**
 * @author Ethan
 *
 */
public class ElectricFieldDemo extends AbstractDemo {
	
	private final Option<Integer> numParticles     = new IntegerRangeOption("Number of particles", 1, 100, 25, 5);
	private final Option<Integer> percentPositives = new RandomizableOption<Integer>(new PercentOption("Positive particles", 50, 5), false);
	private final Option<Double>  maxCharge        = new DoubleRangeOption("Max. particle charge", 1, 100, 14, 1);
	private final Option<Double>  minCharge        = new DoubleRangeOption("Min. particle charge", 1, 100, 5, 1);
	private final Option<Integer> maxMass          = new IntegerRangeOption("Max. particle mass", 10, 300, 100, 10);
	private final Option<Integer> minMass          = new IntegerRangeOption("Min. particle mass", 10, 300, 10, 10);
	private final Option<Boolean> fieldDirection   = new BooleanOption("Field Direction L-R", true);
	private final Option<Double>  fieldStrength    = new DoubleRangeOption("Field Strength (X)", 10, 400, 150, 25);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(numParticles);
		options.add(percentPositives);
		options.add(maxCharge);
		options.add(minCharge);
		options.add(maxMass);
		options.add(minMass);
		options.add(fieldDirection);
		options.add(fieldStrength);
		return options;
	}

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
			"is affected by the following properties:",
			" 1) polarity (red vs. blue)",
			" 2) charge strength (the brightness of the particle's color)",
			" 3) mass (proportional to the particle's size)",
			" "
		};
	}
	
	@Override
	protected WorldMode getWorldMode() {
		return new ClosedWorldMode();
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
		
		CoulombsLaw2.RenderableCreator renderableCreator = new CoulombsLaw2.RenderableCreator() {
			@Override public Renderable create(double mass, double charge) {
				int radius = (int) (Math.sqrt(mass) / Math.PI * 10.0);
				int red, blue, green;
				
				//positive
				if (charge > 0) {
					blue = 0xFF;
					green = red  = (int) ((MAX_CHARGE - charge) / MAX_CHARGE * 200.0);
					
				 //negative
				} else if (charge < 0) {
					red = 0xFF;
					green = blue = (int) ((MAX_CHARGE + charge) / MAX_CHARGE * 200.0);

				//neutral
				} else {
					red = green = blue = 0xCC;
				}
				Color c = new Color(red, green, blue);
				return new Ball(radius, c);
			}
		};
		
		//Add some forces:
		boolean isLeft = fieldDirection.getValue();
		Color fieldColor = isLeft ? new Color(0xFF, 0xDD, 0xDD) : new Color(0xDD, 0xDD, 0xFF);
		double field = isLeft ? fieldStrength.getValue() : -fieldStrength.getValue();
		world.add(new ElectricField(field, 0, bounds, fieldColor));
		
		//Create some point-charges:
		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.2 );
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.2 );
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

			Renderable renderable = renderableCreator.create(mass, charge);
			world.add(new ChargedParticle(renderable, mass, charge, position));
		}
		return world;
	}

	public static void main(String[] args) {
		Main.runDemo(ElectricFieldDemo.class.getName());
	}
}
