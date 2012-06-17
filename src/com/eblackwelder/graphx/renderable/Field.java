package com.eblackwelder.graphx.renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Field implements Colorable {

	protected final Rectangle2D bounds;
	protected final int layer;
	private Color color;

	public Field(Rectangle2D bounds) {
		this(bounds, null); //indicates no color
	}

	public Field(Rectangle2D bounds, Color c) {
		this(bounds, c, Renderable.BACKGROUND_LAYER);
	}
	
	public Field(Rectangle2D bounds, Color c, int layerNumber) {
		this.bounds = bounds;
		this.color = c;
		this.layer = layerNumber;
	}

	@Override
	public void render(Graphics2D g2) {
		if (color != null) {
			Color previousColor = g2.getColor();
			g2.setColor(color);
			renderColored(g2);
			g2.setColor(previousColor);
		}
	}

	protected void renderColored(Graphics2D g2) {
		g2.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
	}

	@Override
	public Rectangle2D getBounds(Point2D position) {
		return bounds;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public void setColor(Color c) {
		this.color = c;
	}
}