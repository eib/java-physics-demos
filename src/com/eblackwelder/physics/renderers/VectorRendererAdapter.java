package com.eblackwelder.physics.renderers;

import java.awt.Color;
import java.awt.Graphics2D;

import com.eblackwelder.math.MathUtils;
import com.eblackwelder.math.Position;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public abstract class VectorRendererAdapter implements Renderer {

	protected Renderer delegate;
	private Color overlayColor;
	private int width;
	private double magnitudeScale;

	public VectorRendererAdapter(Renderer delegate, Color overlayColor, int width, double scale) {
		this.delegate = delegate;
		this.overlayColor = overlayColor;
		this.width = width;
		this.magnitudeScale = scale;
	}

	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		if (delegate != null) {
			delegate.render(object, context);
		}
	}

	protected void renderVector(Vector2D v, Position p, IGraphicsContext context) {
		double magnitude = magnitudeScale * v.getMagnitude();
		double theta = v.getTheta();
		if (MathUtils.almostZero(magnitude, 0.001)) {
			return;
		}
		Graphics2D gOverlay = context.getOverlayLayer();
		
		gOverlay.translate(p.x, p.y);
		gOverlay.rotate(theta);
		
		int height = (int) Math.floor(magnitude);
		int[] xPoints = { 0, height, height, height + 6, height, height, 0 };
		int[] yPoints = { width, width, width + 3, 0, -width - 3, -width, -width };
		
		gOverlay.setColor(overlayColor);
		gOverlay.fillPolygon(xPoints, yPoints, xPoints.length);
	
		gOverlay.rotate(-theta);
		gOverlay.translate(-p.x, -p.y);
	}

}