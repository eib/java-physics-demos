/**
 * 
 */
package com.eblackwelder.physics.options;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import com.eblackwelder.math.MathUtils;

/**
 * @author Ethan
 *
 */
public class BooleanOption extends AbstractOption<Boolean> {

	private final JCheckBox option;
	
	public BooleanOption(String title) {
		this(title, false);
	}

	public BooleanOption(String title, boolean initialValue) {
		super(title + "?");
		option = new JCheckBox();
		option.setSelected(initialValue);
	}

	@Override
	public JComponent getComponent() {
		return option;
	}

	@Override
	public Boolean getRandomValue() {
		return MathUtils.randomBoolean();
	}

	@Override
	public Boolean getValue() {
		return option.isSelected();
	}

}
