/**
 * 
 */
package com.eblackwelder.physx.demos.options;

import javax.swing.SpinnerNumberModel;

import com.eblackwelder.physx.Utils;

/**
 * @author Ethan
 *
 */
public class DoubleRangeOption extends AbstractRangeOption<Double> {

	private final double lower;
	private final double upper;

	public DoubleRangeOption(String title, double lower, double upper, double numSteps) {
		this(title, lower, upper, Utils.random(lower, upper), (upper - lower) / numSteps);
	}
			
	public DoubleRangeOption(String title, double lower, double upper, double initialValue, double step) {
		super(title, new SpinnerNumberModel(initialValue, lower, upper, step));
		this.lower = lower;
		this.upper = upper;
	}

	@Override
	public Double getRandomValue() {
		return Utils.random(lower, upper);
	}
	
}