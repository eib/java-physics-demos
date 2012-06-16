package com.eblackwelder.physx;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.eblackwelder.physx.demos.BuoyancyDemo;
import com.eblackwelder.physx.demos.CollisionDetectionDemo;
import com.eblackwelder.physx.demos.CoulombsLawDemo;
import com.eblackwelder.physx.demos.ElectricFieldDemo;
import com.eblackwelder.physx.demos.GravityDemo;
import com.eblackwelder.physx.demos.IDemo;
import com.eblackwelder.physx.demos.RollYourOwnDemo;
import com.eblackwelder.physx.demos.options.BooleanOption;
import com.eblackwelder.physx.demos.options.Option;

public class Main {

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
	}

	private static final IDemo[] demos = {
		new GravityDemo(),
		new CollisionDetectionDemo(),
		new BuoyancyDemo(),
		new RollYourOwnDemo(),
		new CoulombsLawDemo(),
		new ElectricFieldDemo()
	};
	
	private static JComponent createOptionsPanel(final IDemo demo, final JFrame parent) {
		List<Option<?>> options = demo.getConfigurationOptions();
		JPanel panel = new JPanel(new GridLayout(options.size(), 2, 5, 5));
		for (Option<?> option : options) {
			String labelText = option.getText();
			if (!BooleanOption.class.isInstance(option)) {
				labelText += ":";
			}
			panel.add(new JLabel(labelText));
			panel.add(option.getComponent());
		}
		
		JButton showButton = new JButton("Show Demo");
		showButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				showDemo(demo, parent);
			}
		});
		JButton aboutButton = new JButton("About");
		aboutButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				String[] message = demo.getDescription();
				String title = "About " + demo.getTitle() + " Demo";
				JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
			}
		});
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(showButton, BorderLayout.WEST);
		buttonPanel.add(aboutButton, BorderLayout.EAST);
		
		JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
		contentPanel.add(panel, BorderLayout.NORTH);
		contentPanel.add(new JPanel(), BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return contentPanel;
	}
	
	public static void main(String[] args) {
		String className = args.length > 0 ? args[0] : null;
		runDemo(className);
	}
	
	public static void runDemo(String demoClassName) {
		List<IDemo> demoList = new ArrayList<IDemo>(Arrays.asList(demos));
		if (demoClassName == null) {
			demoClassName = demos[0].getClass().getName();
		}
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
			JComponent component = createOptionsPanel(demo, parent);
			tabbedPane.add(title, component);
		}
		tabbedPane.setSelectedIndex(index);
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		
		parent.setContentPane(contentPanel);
		parent.setMinimumSize(new Dimension(600, 350));
		parent.pack();
		parent.setLocationRelativeTo(null);
		parent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		parent.setVisible(true);
	}

	public static void showDemo(IDemo demo, JFrame parent) {
		World world = demo.createWorld();
		int repaintMillis = demo.getRepaintTimeoutMillis();
		final RenderPanel renderPanel = new RenderPanel(world, repaintMillis);
		final JLabel statusLabel = new JLabel("FPS: ...");
		
		JDialog dialog = new JDialog(parent, "2D Physics Demo :: " + demo.getTitle(), true);
		Container contentPanel = dialog.getContentPane();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(statusLabel, BorderLayout.NORTH);
		contentPanel.add(renderPanel, BorderLayout.CENTER);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		TimerTask task = new TimerTask() {	
			@Override public void run() {
				final double fps = renderPanel.getFramesPerSecond();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						statusLabel.setText(String.format("FPS: %.3f", fps));
					}
				});
			}
		};
		Timer timer = new Timer("Status timer", true);
		timer.schedule(task, 1000, 200);

		dialog.setVisible(true);
		timer.cancel();
		renderPanel.pause();
	}
}
