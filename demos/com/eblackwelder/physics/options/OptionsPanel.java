package com.eblackwelder.physics.options;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.eblackwelder.physics.IDemo;
import com.eblackwelder.physics.PhysicsMain;

public class OptionsPanel extends JPanel {
	private static final long serialVersionUID = 1599202669707545481L;

	public OptionsPanel(final IDemo demo, final JFrame parent) {
		super(new BorderLayout(10, 10));
		
		JPanel optionsPanel = createOptionsPanel(demo);
		JPanel buttonPanel = createButtonPanel(demo, parent);
		
		super.add(optionsPanel, BorderLayout.NORTH);
		super.add(new JPanel(), BorderLayout.CENTER); //for resize magic
		super.add(buttonPanel, BorderLayout.SOUTH);
		super.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	private JPanel createButtonPanel(final IDemo demo, final JFrame parent) {
		JButton showButton = new JButton("Show Demo");
		showButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				PhysicsMain.showDemo(demo, parent);
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
		return buttonPanel;
	}

	private JPanel createOptionsPanel(final IDemo demo) {
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
		return panel;
	}
}
