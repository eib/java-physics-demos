/**
 * 
 */
package com.eblackwelder.physx.renderable;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

/**
 * @author Ethan
 *
 */
public class Ball extends AbstractShape {

	public Ball(double radius, Color c) {
		super(new Ellipse2D.Double(-radius, -radius, 2.0 * radius, 2.0 * radius), c);
	}
}
