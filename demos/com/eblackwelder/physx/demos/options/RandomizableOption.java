package com.eblackwelder.physx.demos.options;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class RandomizableOption<E> implements Option<E> {

	private final JCheckBox useRandomCheckBox;
	private final Option<E> delegate;

	public RandomizableOption(Option<E> delegate) {
		this(delegate, false);
	}
	
	public RandomizableOption(Option<E> delegate, boolean initialValue) {
		this.delegate = delegate;
		this.useRandomCheckBox = new JCheckBox("Random?", initialValue);
		useRandomCheckBox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				updateDelegateComponent();
			}
		});
	}
	
	private void updateDelegateComponent() {
		boolean checked = useRandomCheckBox.isSelected();
		RandomizableOption.this.delegate.getComponent().setEnabled(!checked);
	}

	@Override
	public JComponent getComponent() {
		JPanel contentPanel = new JPanel(new BorderLayout(5, 0));
		contentPanel.add(useRandomCheckBox, BorderLayout.EAST);
		contentPanel.add(delegate.getComponent(), BorderLayout.CENTER);
		updateDelegateComponent();
		return contentPanel;
	}

	protected boolean isRandomChosen() {
		return useRandomCheckBox.isSelected();
	}
	
	@Override
	public E getValue() {
		if (useRandomCheckBox.isSelected()) {
			return delegate.getRandomValue();
		}
		return delegate.getValue();
	}

	@Override
	public E getRandomValue() {
		return delegate.getRandomValue();
	}

	@Override
	public String getText() {
		return delegate.getText();
	}
}