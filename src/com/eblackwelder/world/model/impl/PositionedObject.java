package com.eblackwelder.world.model.impl;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Positionable;

public class PositionedObject implements Renderable, Positionable {

	private final int layer;
	private Vector2D position;
	protected Renderable renderable;

	public PositionedObject(Renderable renderable, Point2D position) {
		this(renderable, position, Renderable.DEFAULT_LAYER);
	}
	
	public PositionedObject(Renderable renderable, Point2D position, int layerNumber) {
		this.renderable = renderable;
		this.position = new Vector2D(position);
		this.layer = layerNumber;
	}
	
	@Override
	public Rectangle2D getBounds() {
		return renderable.getBounds(getPosition());
	}

	@Override
	public Rectangle2D getBounds(Point2D position) {
		return renderable.getBounds(position);
	}
	
	@Override
	public void render(Graphics2D g2) {
		g2.translate(position.getX(), position.getY());
		renderPositioned(g2);
		g2.translate(-position.getX(), -position.getY());
	}

	protected void renderPositioned(Graphics2D g2) {
		renderable.render(g2);
	}
	
	@Override
	public Vector2D getPosition() {
		return position;
	}

	@Override
	public void setPosition(Point2D position) {
		this.position = new Vector2D(position);
	}

	@Override
	public int getLayer() {
		return layer;
	}
}