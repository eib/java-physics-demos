/**
 * 
 */
package com.eblackwelder.physx.demos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.eblackwelder.graphx.renderable.AbstractColorable;
import com.eblackwelder.graphx.renderable.AbstractTextMessage;
import com.eblackwelder.graphx.renderable.Ball;
import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Utils;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.world.model.Mass;
import com.eblackwelder.world.model.Updatable;
import com.eblackwelder.world.model.impl.MassObject;
import com.eblackwelder.world.modes.OpenWorldMode;
import com.eblackwelder.world.modes.WorldMode;

/**
 * @author Ethan
 *
 */
public class PendulumDemo extends AbstractDemo {
	
	private final Color[] colors = {
			Color.YELLOW, Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY
	};
	private final String[] names = {
			"YELLOW", "RED", "GREEN", "ORANGE", "BLUE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY"
	};
	private final Option<Color>   colorOption           = new RandomizableOption<Color>(new ChoicesOption<Color>("Color", names, colors, 2), false);
	
	private final Option<Double>  massOption            = new DoubleRangeOption("Object mass", 10, 1000, 100, 10);
	private final Option<Double>  gravityYOption        = new DoubleRangeOption("Gravity", -300, 300, -160, 20);
	private final Option<Double>  startingAngleOption   = new RandomizableOption<Double>(new DoubleRangeOption("Starting angle (degrees)", -90, 90, 60, 5), false);
	private final Option<Integer> armLengthOption       = new IntegerRangeOption("Arm length (px)", 100, 400, 250, 5);
	
	@Override
	public String getTitle() {
		return "Pendulum";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Demonstrates tension and gravity."
		};
	}
	
	@Override
	protected WorldMode getWorldMode() {
		return new OpenWorldMode();
	}

	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(startingAngleOption);
		options.add(armLengthOption);
		options.add(gravityYOption);
		options.add(colorOption);
		options.add(massOption);
		return options;
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D worldBounds) {
		List<Object> objects = super.getWorldObjects(size, worldBounds);
		
		double mass = massOption.getValue();
		double radius = Math.sqrt(mass * mass) / Math.PI;
		
		//attachment point
		final Vector2D attachment = new Vector2D(0, 0);// worldBounds.getMaxY());

		//starting position
		double minSide = Math.min((double) size.getWidth() / 2.0, (double) size.getHeight() / 2.0);
		final double armLength = Math.min(minSide, armLengthOption.getValue()); 
		
		double startingRadians = Math.toRadians(startingAngleOption.getValue());
		double px = Math.sin(startingRadians) * armLength + attachment.getX();
		double py = -Math.cos(startingRadians) * armLength + attachment.getY();
		Vector2D position = new Vector2D(px, py);

		//The swinging base of the pendulum
		Renderable renderable = new Ball((int) radius, colorOption.getValue());
		final Mass m = new MassObject(renderable, mass, position);
		objects.add(m);
		
		objects.add(new AbstractTextMessage() {
			protected String[] getMessages() {
				double velocity = m.getVelocity().getMagnitude();
				double kinetic = 0.5 * m.getMass() * velocity * velocity;
				double potential = m.getMass() * gravityYOption.getValue() * -m.getPosition().y;
				return new String[] {
						"Energy",
						String.format("  Total     : %.2f", kinetic + potential),
						String.format("  Kinetic   : %.2f", kinetic),
						String.format("  Potential : %.2f", potential)
				};
			}
		});
		//Draws the string back to the attachment
		objects.add(new AbstractColorable(Renderable.BACKGROUND_LAYER, Color.GRAY) {
			@Override
			public void renderColored(Graphics2D g2) {
				Vector2D point = m.getPosition();
				g2.setStroke(new BasicStroke(8.0f));
				g2.drawLine((int) point.getX(), (int) point.getY(), (int) attachment.getX(), (int) attachment.getY());
			}
			@Override
			public Rectangle2D getBounds(Point2D position) {
				return null;
			}
		});

		Vector2D gravity = new Vector2D(0, gravityYOption.getValue());
		final double mg = mass * -gravity.getY();
//		objects.add(new Gravity(gravity));
		objects.add(new Updatable() {
			@Override public void update(long millisElapsed) {
				doForces(m, attachment, mg, armLength);
			}
		});
		return objects;
	}
	
	private void doForces(Mass m, Point2D attachment, double mg, double armLength) {
		//The y-components of gravity cancel out.
		//The resultant is in the ratio as the direction of the attachment
		Vector2D position = m.getPosition();
		double dx = position.getX() - attachment.getX();
		double dy = position.getY() - attachment.getY();
		
		double length = Math.sqrt(dx * dx + dy * dy);
		double theta = -Math.atan(dx / dy);
		double sinTheta = Math.sin(theta);
		double cosTheta = Math.cos(theta);

		double t = mg * cosTheta;
		double tx = -t * sinTheta;
		double ty = t * cosTheta;

		double net0 = m.getNetForce().getMagnitude();
		if (!Utils.almostZero(net0, 0.001)) {
//			System.out.println("!!! Net force: " + net0);
		}
		
		Vector2D gravity = new Vector2D(0, -mg);
		m.addForce(gravity);
		
		Vector2D tension = new Vector2D(tx, ty);
		m.addForce(tension);
		
		double expectedNet = Math.abs(mg * sinTheta);
		
		Vector2D netForce = m.getNetForce();
		double net = netForce.getMagnitude();
		
//		System.out.println(String.format("Net=%.3f, degrees=%.3f, arm=%.0f", net, Math.toDegrees(theta), length));
		
		if (!Utils.almostZero(net - expectedNet, 0.001)) {
//			System.out.println("!!! Net force: " + net + ", expected=" + expectedNet);
		}
		
		if (!Utils.almostZero(theta, 0.001)) {
			double weight = -tension.getMagnitude() / cosTheta;
			if (!Utils.almostZero(weight + mg, 0.001)) {
//				System.out.println("!!! Weight: " + weight);
			}
		}
//		if (!Utils.almostZero(length - armLength, 0.1)) {
//			System.out.println(String.format("Theta: %.1f", Math.toDegrees(theta)));
//			System.out.println("!!! Arm length: " + length);
//		}
	}

	public static void main(String[] args) {
		Main.runDemo(PendulumDemo.class.getName());
	}
}
