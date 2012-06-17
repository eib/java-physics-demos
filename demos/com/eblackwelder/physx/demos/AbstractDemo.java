package com.eblackwelder.physx.demos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.eblackwelder.graphx.renderable.AccelerationIndicatorOverlay;
import com.eblackwelder.graphx.renderable.SpeedIndicatorOverlay;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.ChoicesOption;
import com.eblackwelder.physx.demos.options.IntegerRangeOption;
import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.world.World;
import com.eblackwelder.world.model.Acceleratable;
import com.eblackwelder.world.model.Movable;
import com.eblackwelder.world.modes.BoundedWorldMode;
import com.eblackwelder.world.modes.WorldMode;


/**
 * @author Ethan
 *
 */
public abstract class AbstractDemo implements IDemo {
	
	private final Option<Double>  demoSpeed        = new ChoicesOption<Double>("Demo speed", new String[] { "1/10x", "1/4x", "1/2x", "1x", "2x", "4x", "10x" }, new Double[] { .1, .25, .5, 1., 2., 4., 10. }, 3);
	private final Option<Integer> targetFPS        = new ChoicesOption<Integer>("Target FPS", new String[] { "15", "30", "60" }, new Integer[] { 50, 20, 10 }, 1);
	private final Option<Integer> widthOption      = new IntegerRangeOption("Width (px)", 200, 800, 600, 50);
	private final Option<Integer> heightOption     = new IntegerRangeOption("Height (px)", 200, 800, 600, 50);
	private final Option<Boolean> showVelocity     = new BooleanOption("Show velocity (green)", false);
	private final Option<Boolean> showAcceleration = new BooleanOption("Show acceleration (blue)", false);
	
	public AbstractDemo() {
		//Hack: disabling indicator vectors because CoulombsLaw _might_ delete/add more world Objects.
		boolean indicatorsDisabled = CoulombsLawDemo.class.isInstance(this);
		if (indicatorsDisabled) {
			String toolTip = "The " + getTitle() + " Demo does not support this feature.";
			for (JComponent c : new JComponent[] { showVelocity.getComponent(), showAcceleration.getComponent() }) {
				c.setEnabled(false);
				c.setToolTipText(toolTip);
			}
		}
	}
	
	public abstract String getTitle();
	public abstract String[] getDescription();

	protected WorldMode getWorldMode() {
		return new BoundedWorldMode();
	}
	
	protected List<Object> getWorldObjects(Dimension worldSize, Rectangle2D worldBounds) {
		return new ArrayList<Object>();
	}
	
	public final World createWorld() {
		Dimension size = getSize();
		WorldMode mode = getWorldMode();
		World world = World.newInstance(size, mode, demoSpeed.getValue());
		for (Object o : getWorldObjects(size, world.getBounds())) {
			world.add(o);
			if (Movable.class.isInstance(o) && showVelocity.getValue()) {
				world.add(new SpeedIndicatorOverlay((Movable) o, 0.5, Color.GREEN));
			}
			if (Acceleratable.class.isInstance(o) && showAcceleration.getValue()) {
				world.add(new AccelerationIndicatorOverlay((Acceleratable) o, 0.5, Color.BLUE, new Vector2D(-5, -5)));
			}
		}
		return world;
	}
	
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = new ArrayList<Option<?>>();
		options.add(demoSpeed);
		options.add(widthOption);
		options.add(heightOption);
		options.add(targetFPS);
		options.add(showVelocity);
		options.add(showAcceleration);
		return options;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}

	protected Dimension getSize() {
		return new Dimension(widthOption.getValue(), heightOption.getValue());
	}
	
	public int getRepaintTimeoutMillis() {
		return targetFPS.getValue();
	}

}
