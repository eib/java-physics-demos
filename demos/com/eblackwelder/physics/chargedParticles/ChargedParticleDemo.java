package com.eblackwelder.physics.chargedParticles;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.eblackwelder.math.MathUtils;
import com.eblackwelder.math.Position;
import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.IntegerRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.options.PercentOption;
import com.eblackwelder.physics.options.RandomizableOption;
import com.eblackwelder.world.WorldObject;

public abstract class ChargedParticleDemo extends AbstractDemo {

	private final Option<Integer> numParticles = new IntegerRangeOption("Number of particles", 2, 100, 25, 5);
	private final Option<Integer> percentPositives = new RandomizableOption<Integer>(new PercentOption("Positive particles", 50, 5), false);
	protected final Option<Double> maxCharge = new DoubleRangeOption("Max. particle charge", 1, 100, 14, 1);
	protected final Option<Double> minCharge = new DoubleRangeOption("Min. particle charge", 1, 100, 5, 1);
	private final Option<Integer> maxMass = new IntegerRangeOption("Max. particle mass", 10, 300, 100, 10);
	private final Option<Integer> minMass = new IntegerRangeOption("Min. particle mass", 10, 300, 30, 10);

	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(numParticles);
		options.add(percentPositives);
		options.add(maxCharge);
		options.add(minCharge);
		options.add(maxMass);
		options.add(minMass);
		return options;
	}

	protected Collection<WorldObject> createParticles() {
		final int NUM_PARTICLES    = numParticles.getValue();
		final int PERCENT_POSITIVE = this.percentPositives.getValue();
		final int NUM_POSITIVES    = (int) Math.floor((double) (NUM_PARTICLES * PERCENT_POSITIVE) / 100.0);
		final double MAX_CHARGE    = Math.max(minCharge.getValue(), maxCharge.getValue());
		final double MIN_CHARGE    = Math.min(minCharge.getValue(), maxCharge.getValue());
		final int MAX_MASS         = Math.max(minMass.getValue(), maxMass.getValue());;
		final int MIN_MASS         = Math.min(minMass.getValue(), maxMass.getValue());
	
		Collection<WorldObject> particles = new ArrayList<WorldObject>();
		Dimension size = this.getSize();
		int MIN_X = (int) (size.getWidth() * 0.2);
		int MAX_X = (int) (size.getWidth() * 0.8);
		for (int ii = 0; ii < NUM_PARTICLES; ii++) {
			double charge = MathUtils.random(MIN_CHARGE, MAX_CHARGE);
			boolean positive = ii >= NUM_POSITIVES;
			
			if (positive) {
				charge = -charge;
			}
			int x = MathUtils.random(MIN_X, MAX_X);
			int y = MathUtils.random(0, size.height);
			Position position = new Position(x, y);
			
			double mass = MathUtils.random(MIN_MASS, MAX_MASS);
			particles.add(new Particle(mass, charge, position));
		}
		return particles;
	}

}