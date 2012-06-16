package com.eblackwelder.physx.demos.options;

import javax.swing.JComponent;

/**
 * @author Ethan
 *
 */
public interface Option<E> {

	public String getText();
	public E getValue();
	public E getRandomValue();
	public JComponent getComponent();
}
