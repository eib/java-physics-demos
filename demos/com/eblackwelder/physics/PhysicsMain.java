package com.eblackwelder.physics;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.eblackwelder.physics.chargedParticles.CoulombsLawDemo;
import com.eblackwelder.physics.chargedParticles.ElectricFieldDemo;
import com.eblackwelder.physics.gravity.SolarSystemDemo;
import com.eblackwelder.physics.options.OptionsPanel;
import com.eblackwelder.physics.pendulum.PendulumDemo;
import com.eblackwelder.world.driver.WorldDriverBase;

public class PhysicsMain {

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
	}

	private static final IDemo[] demos = {
		new PendulumDemo(),
		new ElectricFieldDemo(),
		new CoulombsLawDemo(),
//		new GravityDemo(),
		new SolarSystemDemo(),
	};
	
	public static void main(String[] args) {
		String className = args.length > 0 ? args[0] : null;
		runDemo(className);
	}
	
	public static void runDemo(Class<? extends IDemo> clazz) {
		runDemo(clazz.getName()); //this is backwards, but whatever.
	}
	
	public static void runDemo(String demoClassName) {
		if (demoClassName == null) {
			demoClassName = demos[0].getClass().getName();
		}
		
		List<IDemo> demoList = new ArrayList<IDemo>(Arrays.asList(demos));
		int index = 0;
		boolean containsDemo = false;
		for (int ii = 0; ii < demoList.size(); ii++) {
			String tempName = demoList.get(ii).getClass().getName();
			if (tempName.equals(demoClassName)) {
				index = ii;
				containsDemo = true;
				break;
			}
		}
		if (!containsDemo) {
			try {
				Class<?> clazz = Class.forName(demoClassName);
				if (IDemo.class.isAssignableFrom(clazz)) {
					Constructor<?> constructor = clazz.getConstructor();
					IDemo demo = (IDemo) constructor.newInstance();
					demoList.add(demo);
					index = demoList.size() - 1;
				}
			} catch (Exception e) {
				System.out.println("Can't instantiate demo with class name " + demoClassName + ": " + e.getMessage());
				e.printStackTrace(System.out);
			}
		}

		JFrame parent = new JFrame("2D Physics Demo");
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		for (IDemo demo : demoList) {
			String title = demo.getTitle();
			JComponent component = new OptionsPanel(demo, parent);
			tabbedPane.add(title, component);
		}
		tabbedPane.setSelectedIndex(index);
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		
		parent.setContentPane(contentPanel);
		parent.setMinimumSize(new Dimension(650, 350));
		parent.pack();
		parent.setLocationRelativeTo(null);
		parent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		parent.setVisible(true);
	}

	public static void showDemo(IDemo demo, JFrame parent) {
		WorldDriverBase driver = demo.createDriver(parent);
		driver.run();
	}
}
