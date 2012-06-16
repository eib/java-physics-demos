/**
 * 
 */
package com.eblackwelder.physx.renderable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

/**
 * @author Ethan
 *
 */
public class Block extends AbstractShape {

	public Block(int width, int height, Color c) {
		this(new Dimension(width, height), c);
	}
	
	public Block(Dimension size, Color c) {
		super(new Rectangle2D.Double((double) size.width / -2.0, (double) size.height / -2.0, size.width, size.height), c);
	}
}