package com.eblackwelder.physx.renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class AbstractShape extends AbstractColorable {

	private final Shape shape;

	public AbstractShape(Shape shape, Color color) {
		this(shape, color, Renderable.DEFAULT_LAYER);
	}

	public AbstractShape(Shape shape, Color color, int layer) {
		super(layer, color);
		this.shape = shape;
	}

	@Override
	public void renderColored(Graphics2D g2) {
		g2.fill(shape);
	}

	@Override
	public Rectangle2D getBounds(Point2D position) {
		Rectangle bounds = shape.getBounds();
		bounds.translate((int) position.getX(), (int) position.getY());
		return bounds;
	}

}