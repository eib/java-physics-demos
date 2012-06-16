package com.eblackwelder.physx.renderable;

import java.awt.Color;
import java.awt.Graphics2D;


public abstract class AbstractColorable extends AbstractRenderable implements Colorable {

	protected Color color;

	public AbstractColorable() {
		this(Renderable.DEFAULT_LAYER, Color.GRAY);
	}

	public AbstractColorable(int layer) {
		this(layer, Color.GRAY);
	}

	public AbstractColorable(Color color) {
		this(Renderable.DEFAULT_LAYER, color);
	}
	
	public AbstractColorable(int layer, Color color) {
		super(layer);
		this.color = color;
	}
	
	@Override
	public void setColor(Color c) {
		this.color = c;
	}

	@Override
	public void render(Graphics2D g2) {
		Color previousColor = g2.getColor();
		g2.setColor(color);
		g2.setPaint(color);
		renderColored(g2);
		g2.setColor(previousColor);
	}
	
	public abstract void renderColored(Graphics2D g2);
	
}