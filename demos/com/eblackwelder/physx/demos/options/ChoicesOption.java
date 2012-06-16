/**
 * 
 */
package com.eblackwelder.physx.demos.options;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.eblackwelder.physx.Utils;

/**
 * @author Ethan
 *
 */
public class ChoicesOption<E> extends AbstractOption<E> {

	private final E[] model;
	private final JComboBox choicesBox;

	public ChoicesOption(String title, E[] choices) {
		this(title, choices, 0);
	}

	public ChoicesOption(String title, E[] choices, int selectedIndex) {
		super(title + " (choose one)");
		this.model = choices;
		this.choicesBox = new JComboBox(choices);
		this.choicesBox.setSelectedIndex(selectedIndex);
	}

	public ChoicesOption(String title, String[] names, E[] choices) {
		this(title, names, choices, 0);
	}

	public ChoicesOption(String title, String[] names, E[] choices, int selectedIndex) {
		super(title + " (choose one)");
		this.model = choices;
		if (names.length != choices.length) {
			throw new IllegalArgumentException("The number of choice names doesn't match the number of actual choices.");
		}
		this.choicesBox = new JComboBox(names);
		this.choicesBox.setSelectedIndex(selectedIndex);
	}

	@Override
	public JComponent getComponent() {
		return choicesBox;
	}

	@Override
	public E getRandomValue() {
		return Utils.random(model);
	}

	@Override
	public E getValue() {
		int index = choicesBox.getSelectedIndex();
		return model[index];
	}

}
