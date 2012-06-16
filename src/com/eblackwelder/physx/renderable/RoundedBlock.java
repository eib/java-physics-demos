/**
 * 
 */
package com.eblackwelder.physx.renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Ethan
 *
 */
public class RoundedBlock extends AbstractColorable {

	private final double width;
	private final double height;
	private final Shape shape;

	public RoundedBlock(double width, double height, double borderRadius, Color c) {
		this.width = width;
		this.height = height;
		this.color = c;
		this.shape = new RoundRectangle2D.Double(-width / 2.0, -height / 2.0, width, height, borderRadius, borderRadius);
	}
	
	@Override
	public Rectangle2D getBounds(Point2D position) {
		return new Rectangle2D.Double(position.getX() - (width / 2.0), position.getY() - (height / 2.0), width, height);
	}

	@Override
	public void renderColored(Graphics2D g2) {
		g2.fill(shape);
	}
}
