/**
 * 
 */
package com.eblackwelder.physx;

import java.util.Random;


/**
 * @author Ethan
 *
 */
public abstract class Utils {
	
	private static Random random = new Random();
	
	static {
		random.setSeed(System.currentTimeMillis());
		for (int ii = 0; ii < 40; ii++) {
			random.nextInt();
		}
	}

	public static double random(double lower, double upper) {
		if (lower == upper) {
			return lower;
		}
		return random.nextDouble() * (upper - lower) + lower;
	}
	
	public static int random(int lower, int upper) {
		if (lower == upper) {
			return lower;
		}
		return random.nextInt(upper - lower) + lower;
	}
	
	public static int random(int upper) {
		return random.nextInt(upper);
	}
	
	public static <T> T random(T[] objects) {
		return objects[random(objects.length)];
	}
	
	public static boolean randomBoolean() {
		return random.nextBoolean();
	}

	public static boolean almostZero(double value, double epsilon) {
		return Math.abs(value) < epsilon;
	}
	
}
