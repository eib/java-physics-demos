package com.eblackwelder.physics.cursor;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.math.Position;
import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.physics.renderers.AccelerationRendererAdapter;
import com.eblackwelder.physics.renderers.AppliedForcesRendererAdapter;
import com.eblackwelder.physics.renderers.NetForceRendererAdapter;
import com.eblackwelder.physics.renderers.VelocityRendererAdapter;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.ui.UIWorldDriver;

public class KineticCursorDemo extends AbstractDemo {

	protected final Option<Double> massOption = new DoubleRangeOption("Cursor mass", 0.1, 5, 0.5, 0.1);
	
	@Override
	public String getTitle() {
		return "Kinetic Cursor";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Apply unit forces to the cursor using the arrow keys.",
				" ",
		};
	}

	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(massOption);
		return options;
	}

	@Override
	public WorldDriverBase createDriver(JFrame parent) {
		double mass = massOption.getValue();
		KineticCursor cursor = new KineticCursor("You", mass);
		double width = super.widthOption.getValue();
		double height = super.heightOption.getValue();
		cursor.setPosition(new Position(width / 2.0, height / 2.0));
		
		UIWorldDriver driver = new KineticCursorDriver(cursor);
		if (super.showVelocity.getValue()) {
			driver.addRenderer(new VelocityRendererAdapter(3, 0.4));
		}
		if (super.showAcceleration.getValue()) {
			driver.addRenderer(new AccelerationRendererAdapter(1, 0.2));
		}
		if (super.showForces.getValue()) {
			driver.addRenderer(new AppliedForcesRendererAdapter(2, 0.25));
		}
		if (super.showNetForce.getValue()) {
			driver.addRenderer(new NetForceRendererAdapter(2, 0.25));
		}
		driver.setPreferredSize(new Dimension((int) width, (int) height));
		return driver;
	}

	public static void main(String[] args) {
		PhysicsMain.runDemo(KineticCursorDemo.class);
	}
}
