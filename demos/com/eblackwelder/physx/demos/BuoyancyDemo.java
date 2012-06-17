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

import com.eblackwelder.graphx.renderable.Block;
import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Utils;
import com.eblackwelder.physx.Main;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.DoubleRangeOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.physx.demos.options.PercentOption;
import com.eblackwelder.physx.demos.options.RandomizableOption;
import com.eblackwelder.physx.effects.Buoyancy;
import com.eblackwelder.physx.effects.Gravity;
import com.eblackwelder.physx.effects.Viscosity;
import com.eblackwelder.world.model.impl.KinematicsObject;
import com.eblackwelder.world.modes.BoundedWorldMode;
import com.eblackwelder.world.modes.WorldMode;
import com.eblackwelder.world.modes.BoundedWorldMode.WALL;

/**
 * @author Ethan
 *
 */
public class BuoyancyDemo extends AbstractDemo {
	private final Color[] colors = {
			Color.RED, Color.GREEN, Color.ORANGE, Color.BLACK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.YELLOW
	};
	private final String[] names = {
			"RED", "GREEN", "ORANGE", "BLACK", "DARK GRAY", "GRAY", "LIGHT GRAY", "YELLOW"
	};

	private final Option<Integer> numBallsOption  = new IntegerRangeOption("Number of blocks", 0, 50, 10);
	private final Option<Color>   colorOption     = new RandomizableOption<Color>(new ChoicesOption<Color>("Ball Color", names, colors), true);
	private final Option<Integer> amountOfWater   = new PercentOption("Amount of Water", 30);
	private final Option<Integer> gravityOption   = new IntegerRangeOption("Gravity", 20, 1000, 160, 20);
	private final Option<Boolean> viscosityOption = new BooleanOption("Air Viscosity", false);
	private final Option<Double>  airViscosity    = new DoubleRangeOption("Air Viscosity", 0, 100, 10, 5);
	private final ChoicesOption<String> buoyancyOption = new ChoicesOption<String>("Buoyancy", new String[] {
			"Water",
			"Trampoline",
			"Jello"	
	});
	
	@Override
	public String getTitle() {
		return "Buoyancy";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo illustrates the spring-like effect buoyancy has on objects.",
			"Note how thinner objects are able to sink deeper into the water, as ",
			"they have less area on their bottom face/side.",
			" ",
			"Forces in play are: gravity, buoyancy forces, and viscosity (in both ",
			"the air and the water).",
			" ",
			"By modifying the buoyant forces and the viscous properties of the water,",
			"we can simulate a variety of different materials.",
			" "
		};
	}
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(numBallsOption);
		options.add(colorOption);
		options.add(amountOfWater);
		options.add(buoyancyOption);
		options.add(gravityOption);
		options.add(viscosityOption);
		options.add(airViscosity);
		return options;
	}

	@Override
	protected WorldMode getWorldMode() {
		return new BoundedWorldMode(WALL.EAST, WALL.WEST);
	}

	@Override
	protected List<Object> getWorldObjects(Dimension size, Rectangle2D bounds) {
		List<Object> world = super.getWorldObjects(size, bounds);
		final int numBlocks = numBallsOption.getValue();
		final double gravity = -gravityOption.getValue();
		final double percentWater = (double) amountOfWater.getValue() / 100.0;
		final double waterLine = percentWater * size.getHeight() - size.getHeight() / 2.0;
		
		final double viscosity = airViscosity.getValue();
		
		Buoyancy buoyancy;
		String name = buoyancyOption.getValue();
		if ("Jello".equals(name)) {
			buoyancy = new Buoyancy(waterLine, 80.0, 100.0);
		} else if ("Trampoline".equals(name)) {
			buoyancy = new Buoyancy(waterLine, 1000.0, 50.0);
		} else {
			buoyancy = new Buoyancy(waterLine, 5.0, 50.0);
		}
		
		//Add some forces:
		world.add(new Gravity(0, gravity));
		world.add(buoyancy);
		
		//a tad bit of air resistance:
		if (viscosityOption.getValue()) {
			world.add(new Viscosity(viscosity, new Color(0xFF, 0xFC, 0xF0), new Rectangle2D.Double(-size.getWidth() / 2.0, waterLine, size.getWidth(), size.getHeight())));
		}
		int MAX_X = (int) Math.floor( ((double) size.getWidth())  / 2.0 );
		int MAX_Y = (int) Math.floor( ((double) size.getHeight()) / 2.0 );
		for (int ii = 0; ii < numBlocks; ii++) {
			Color c = colorOption.getValue();
			int width = Utils.random(5, 75); //TODO : options
			int height = Utils.random(5, 75);
			Renderable block = new Block(width, height, c);
			
			int maxX = MAX_X - 2 * width;
			int maxY = MAX_Y - 2 * height;
			int x = Utils.random(-maxX, maxX);
			int y = Utils.random(0, maxY);
			Point2D position = new Point(x, y);

			int vx = Utils.random(-150, 150); //TODO : option
			int vy = 0; //Utils.random(-150, 150); //TODO : option
			Point2D velocity = new Point(vx, vy);
			
			double mass = width * height;
			double elasticity = Utils.random(.04, .5); //TODO : option
			
			world.add(new KinematicsObject(block, mass, elasticity, position, velocity));
		}
		return world;
	}
	
	public static void main(String[] args) {
		Main.runDemo(BuoyancyDemo.class.getName());
	}
}
