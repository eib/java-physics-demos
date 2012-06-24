package com.eblackwelder.physics.options;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public abstract class AbstractRangeOption<E> extends AbstractOption<E> {

	protected final JSpinner spinner;

	public AbstractRangeOption(String title, SpinnerModel model) {
		super(title);
		this.spinner = new JSpinner(model);
	}

	@Override
	public JComponent getComponent() {
		return spinner;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getValue() {
		return (E) spinner.getValue();
	}

}