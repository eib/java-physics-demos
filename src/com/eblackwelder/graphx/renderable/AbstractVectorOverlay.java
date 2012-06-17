package com.eblackwelder.graphx.renderable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.eblackwelder.math.Vector2D;

public abstract class AbstractVectorOverlay extends AbstractRenderable {

	private final double scale;
	private final Point offset;
	private final int overlayWidth  = 3;
	private final int overlayHeight = 3;
	private Color color;
	
	public AbstractVectorOverlay(double scale, Color color) {
		this(scale, color, Vector2D.zeroVector());
	}
	
	public AbstractVectorOverlay(double scale, Color color, Point2D offset) {
		super(Renderable.OVERLAY_LAYER);
		this.scale = scale;
		this.color = color;
		this.offset = new Point((int) offset.getX(), (int) offset.getY());
	}

	@Override
	public void render(Graphics2D g2) {
		Color previousColor = g2.getColor();
		g2.setColor(color);
		Point2D p = getPosition();
		int px = (int) p.getX() + offset.x;
		int py = (int) p.getY() + offset.y;
		Point2D v = getIndicatorProperty();
		
		int lengthX = (int) Math.abs(v.getX() * scale);
		int offsetX = (v.getX() > 0) ? -lengthX : 0;
		g2.fillRect(px + offsetX, py, lengthX, overlayHeight);
		
		int lengthY = (int) Math.abs(v.getY() * scale);
		int offsetY = (v.getY() > 0) ? -lengthY : 0;
		g2.fillRect(px, py + offsetY, overlayWidth, lengthY);

		g2.setColor(previousColor);
	}

	protected abstract Point2D getPosition();
	
	protected abstract Point2D getIndicatorProperty();
}