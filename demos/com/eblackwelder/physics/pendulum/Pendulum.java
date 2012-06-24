package com.eblackwelder.physics.pendulum;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physics.model.impl.KineticObject;
import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;

public class Pendulum extends KineticObject implements WorldObject {

	public static final Type pendulumType = new Type("Pendulum");
	
	private Position pivotPoint;
	private double armLength;
	private Force mg;

	/**
	 * @param mass
	 * @param armLength
	 * @param pivotPoint
	 * @param startingTheta The starting angle (between the vertical axis and the pendulum arm)
	 * @param g Acceleration due to gravity
	 */
	public Pendulum(double mass, double armLength, Position pivotPoint, double startingTheta, Acceleration g) {
		super(mass);
		this.pivotPoint = pivotPoint;
		this.armLength = armLength;
		this.mg = new Force(g, g.getMagnitude() * mass);
		
		startingTheta = Math.PI / 2.0 - startingTheta; //convert to the angle between the arm and the horizontal (x) axis
		double px = Math.cos(startingTheta) * armLength;
		double py = Math.sin(startingTheta) * armLength;
		setPosition(new Position(pivotPoint.x + px, pivotPoint.y + py));
	}
	
	@Override
	public void update(long millisElapsed, World world) {
		super.addForce(calculateCentripetalForce());
		super.update(millisElapsed, world);
	}
	
	/**
	 * <pre>T = mv^2/length + mg*cos(theta)</pre>
	 * @return The Tension at the current moment.
	 * @see http://www.youtube.com/watch?v=ZkDF3pmaIOo
	 */
	public Force calculateCentripetalForce() {
		double theta = getTheta();
		double m = getMass();
		double v = getVelocity().getMagnitude();
		double len = getArmLength();
		double magnitude = m * v*v / len + mg.getMagnitude() * Math.cos(theta); 
		return new Force(pivotPoint.minus(getPosition()), magnitude);
	}
	
	/** @return The angle (in radians) the ball makes with the vertical axis. */
	public double getTheta() {
		Position p = super.getPosition();
		Vector2D v = p.minus(pivotPoint);
		return Math.PI / 2.0 - v.getTheta();
	}

	public Position getPivotPoint() {
		return pivotPoint;
	}

	public double getArmLength() {
		return armLength;
	}

	@Override
	public Type getType() {
		return pendulumType;
	}

}
