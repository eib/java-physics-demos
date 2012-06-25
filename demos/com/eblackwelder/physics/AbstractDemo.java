package com.eblackwelder.physics;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.eblackwelder.physics.options.BooleanOption;
import com.eblackwelder.physics.options.ChoicesOption;
import com.eblackwelder.physics.options.IntegerRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.renderers.AccelerationRendererAdapter;
import com.eblackwelder.physics.renderers.AppliedForcesRendererAdapter;
import com.eblackwelder.physics.renderers.NetForceRendererAdapter;
import com.eblackwelder.physics.renderers.VelocityRendererAdapter;
import com.eblackwelder.world.ui.UIWorldDriver;


/**
 * @author Ethan
 *
 */
public abstract class AbstractDemo implements IDemo {
	
	protected final Option<Double>  demoSpeed        = new ChoicesOption<Double>("Demo speed", new String[] { "1/10x", "1/4x", "1/2x", "1x", "2x", "4x", "10x" }, new Double[] { .1, .25, .5, 1., 2., 4., 10. }, 3);
	protected final Option<Integer> targetFPS        = new ChoicesOption<Integer>("Target FPS", new String[] { "15", "30", "60" }, new Integer[] { 50, 20, 10 }, 1);
	protected final Option<Integer> widthOption      = new IntegerRangeOption("Width (px)", 200, 800, 600, 50);
	protected final Option<Integer> heightOption     = new IntegerRangeOption("Height (px)", 200, 800, 600, 50);
	protected final Option<Boolean> showVelocity     = new BooleanOption("Show velocity (green)", false);
	protected final Option<Boolean> showAcceleration = new BooleanOption("Show acceleration (red)", false);
	protected final Option<Boolean> showForces       = new BooleanOption("Show forces (brown)", false);
	protected final Option<Boolean> showNetForce     = new BooleanOption("Show net force (orange)", false);
	
	public abstract String getTitle();
	public abstract String[] getDescription();

	public void configureWorld(UIWorldDriver driver) {
		if (showForces.getValue()) {
			driver.addRenderer(new AppliedForcesRendererAdapter(2, 0.01));
		}
		if (showNetForce.getValue()) {
			driver.addRenderer(new NetForceRendererAdapter(4, 0.01));
		}
		if (showAcceleration.getValue()) {
			driver.addRenderer(new AccelerationRendererAdapter(1, 1.5));
		}
		if (showVelocity.getValue()) {
			driver.addRenderer(new VelocityRendererAdapter(2, 1.5));
		}
		driver.setPreferredSize(new Dimension(widthOption.getValue(), heightOption.getValue()));
	}
	
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = new ArrayList<Option<?>>();
		options.add(demoSpeed);
		options.add(widthOption);
		options.add(heightOption);
		options.add(targetFPS);
		options.add(showVelocity);
		options.add(showAcceleration);
		options.add(showForces);
		options.add(showNetForce);
		return options;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}

	protected Dimension getSize() {
		return new Dimension(widthOption.getValue(), heightOption.getValue());
	}
	
	protected int getRepaintTimeoutMillis() {
		return targetFPS.getValue();
	}

}
