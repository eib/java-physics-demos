/**
 * 
 */
package com.eblackwelder.physics.chargedParticles;

import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.math.MathUtils;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.forces.CoulombsLaw;
import com.eblackwelder.physics.options.BooleanOption;
import com.eblackwelder.physics.options.IntegerRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.options.RandomizableOption;
import com.eblackwelder.physics.updatables.ForceResetter;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.model.Movable;
import com.eblackwelder.world.ui.UIWorldDriver;

/**
 * @author Ethan
 *
 */
public class CoulombsLawDemo extends ChargedParticleDemo {

	private final Option<Boolean> startingVelocity = new BooleanOption("Starting Velocity", false);
	private final Option<Integer> maxVelocity      = new RandomizableOption<Integer>(new IntegerRangeOption("Max. velocity", 0, 200, 30, 5), true);
	private final Option<Integer> joinDistance     = new IntegerRangeOption("Min. merge distance (px)", 0, 50, 20, 5);
	private final Option<Integer> chargeMultiplier = new IntegerRangeOption("Force multiplier", 1, 200, 50, 5);
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(chargeMultiplier);
		options.add(startingVelocity);
		options.add(maxVelocity);
//		options.add(joinDistance);
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
	public WorldDriverBase createDriver(JFrame parent) {
		UIWorldDriver driver = new UIWorldDriver(getTitle());
		driver.setRendererForType(new ParticleRenderer(5, maxCharge.getValue()), Particle.particleType);

		final int FORCE_MULTIPLIER = chargeMultiplier.getValue() * 1000;
		World world = driver.getWorld();
		world.addObject(new ForceResetter());
		world.addObject(new CoulombsLaw(FORCE_MULTIPLIER));
		world.addObjects(createParticles());

//		//TODO: join objects
//		final int JOIN_DISTANCE    = joinDistance.getValue();
		super.configureWorld(driver);
		return driver;
	}

	@Override
	protected Collection<WorldObject> createParticles() {
		Collection<WorldObject> particles = super.createParticles();
		final int MAX_VELOCITY = maxVelocity.getValue();
		if (startingVelocity.getValue()) {
			for (WorldObject object : particles) {
				Movable particle = (Movable) object;
				int vx = MathUtils.random(-MAX_VELOCITY, MAX_VELOCITY);
				int vy = MathUtils.random(-MAX_VELOCITY, MAX_VELOCITY);
				particle.setVelocity(new Velocity(vx, vy));
			}	
		}
		return particles;
	}

	public static void main(String[] args) {
		PhysicsMain.runDemo(CoulombsLawDemo.class);
	}
}
