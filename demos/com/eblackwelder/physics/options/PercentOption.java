/**
 * 
 */
package com.eblackwelder.physics.options;

/**
 * @author Ethan
 *
 */
public class PercentOption extends IntegerRangeOption {

	public PercentOption(String title, int initialValue) {
		this(title, initialValue, 1);
	}
	
	public PercentOption(String title, int initialValue, int step) {
		super(title + " (Percent)", 0, 100, initialValue, step);
	}
	
}
