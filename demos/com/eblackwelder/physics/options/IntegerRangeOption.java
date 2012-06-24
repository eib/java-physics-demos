/**
 * 
 */
package com.eblackwelder.physics.options;

import javax.swing.SpinnerNumberModel;

import com.eblackwelder.math.MathUtils;

/**
 * @author Ethan
 *
 */
public class IntegerRangeOption extends AbstractRangeOption<Integer> {

	private final int lower;
	private final int upper;
	
	public IntegerRangeOption(String title, int lower, int upper) {
		this(title, lower, upper, lower);
	}
	
	public IntegerRangeOption(String title, int lower, int upper, int initialValue) {
		this(title, lower, upper, initialValue, 1);
	}
	
	public IntegerRangeOption(String title, int lower, int upper, int initialValue, int step) {
		super(title, new SpinnerNumberModel(initialValue, lower, upper, step));
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public Integer getRandomValue() {
		return MathUtils.random(lower, upper); //TODO : random + step
	}
}
