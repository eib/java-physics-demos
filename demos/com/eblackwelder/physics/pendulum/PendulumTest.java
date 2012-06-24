package com.eblackwelder.physics.pendulum;

import junit.framework.Assert;

import org.junit.Test;

import com.eblackwelder.math.Position;


public class PendulumTest {

	@Test
	public void testGetTheta_StartingThetaWorks() {
		PendulumBuilder builder = new PendulumBuilder();
		builder
			.setMass(5)
			.setArmLength(10)
			.setPivot(0, 0)
			.setGravity(0, 10);
		double[] startingDegrees = { 0, 30, 45, 60, 90, 135, 180 };
		for (int ii = 0; ii < startingDegrees.length; ii++) {
			double degrees = startingDegrees[ii];
			
			builder.setStartingDegrees(degrees);
			Assert.assertEquals("Starting degrees " + degrees + ".",
					degrees, Math.toDegrees(builder.build().getTheta()), 0.0001);
		}
	}

	@Test
	public void testConstructor_ThatStartingPositionWorks() {
		PendulumBuilder builder = new PendulumBuilder();
		builder
			.setPivot(0, 0)
			.setArmLength(0)
			.setStartingDegrees(90);
		Assert.assertEquals(new Position(0, 0), builder.build().getPosition());

		builder.setArmLength(10);
		Assert.assertEquals(new Position(10, 0), builder.build().getPosition());

		builder.setArmLength(-10);
		Assert.assertEquals(new Position(-10, 0), builder.build().getPosition());

		builder.setArmLength(10).setStartingDegrees(0);
		Assert.assertEquals(new Position(0, 10), builder.build().getPosition());

		builder.setArmLength(10.0 * Math.sqrt(2.0)).setStartingDegrees(45);
		Assert.assertEquals(new Position(10, 10), builder.build().getPosition());

		builder.setArmLength(10.0 * Math.sqrt(2.0)).setStartingDegrees(45);
		Assert.assertEquals(new Position(10, 10), builder.build().getPosition());
		
		builder.setArmLength(5).setStartingDegrees(53.1301024); //3-4-5 right triangle
		Assert.assertEquals(new Position(4, 3), builder.build().getPosition());
	}

	@Test
	public void testConstructor_ThatStartingPositionWorksWithPivotPosition() {
		PendulumBuilder builder = new PendulumBuilder();
		builder
			.setPivot(100, -100) //non-zero
			.setArmLength(0)
			.setStartingDegrees(90);
		Assert.assertEquals(new Position(100, -100), builder.build().getPosition());

		builder.setArmLength(10);
		Assert.assertEquals(new Position(110, -100), builder.build().getPosition());

		builder.setArmLength(-10);
		Assert.assertEquals(new Position(90, -100), builder.build().getPosition());

		builder.setArmLength(10).setStartingDegrees(0);
		Assert.assertEquals(new Position(100, -90), builder.build().getPosition());

		builder.setArmLength(10.0 * Math.sqrt(2.0)).setStartingDegrees(45);
		Assert.assertEquals(new Position(110, -90), builder.build().getPosition());

		builder.setArmLength(10.0 * Math.sqrt(2.0)).setStartingDegrees(45);
		Assert.assertEquals(new Position(110, -90), builder.build().getPosition());
		
		builder.setArmLength(5).setStartingDegrees(53.1301024); //3-4-5 right triangle
		Assert.assertEquals(new Position(104, -97), builder.build().getPosition());
	}
}
