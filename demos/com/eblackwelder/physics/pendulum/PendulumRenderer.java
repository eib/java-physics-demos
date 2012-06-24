package com.eblackwelder.physics.pendulum;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import com.eblackwelder.math.Position;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class PendulumRenderer implements Renderer {

	private final Color gold1 = new Color(255, 215, 0);
	private final Color gold2 = new Color(238, 201, 0);
	private final Color gold3 = new Color(205, 173, 0);
	private final Color DARK_BROWN = new Color(107, 66, 38);
	
	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		if (object instanceof Pendulum) {
			Graphics2D mainLayer = context.getMainLayer();
			renderPendulum((Pendulum) object, mainLayer);
		}
	}
	
	public void renderPendulum(Pendulum pendulum, Graphics2D mainLayer) {
		Position pivot = pendulum.getPivotPoint();
		Position p = pendulum.getPosition();
		double theta = Math.PI / 2.0 - pendulum.getTheta(); //convert back to Swing coordinates

		mainLayer.setStroke(new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
		mainLayer.setColor(DARK_BROWN);
		
		//Draw the arm:
		int[] xPoints = { 0, 1, 1, 0 }; //x == magnitude
		int[] yPoints = { 1, 1, -1, -1 }; //y == width
		AffineTransform transform = mainLayer.getTransform();
		mainLayer.translate(pivot.x, pivot.y);
		mainLayer.rotate(theta);
		mainLayer.fillOval(-10, -10, 20, 20);
		mainLayer.scale(pendulum.getArmLength(), 8.0);
		mainLayer.fillPolygon(xPoints, yPoints, xPoints.length);
		mainLayer.setTransform(transform);
		
		//Draw the ball:
		int radius = (int) (pendulum.getMass() / 2.0); //proportional is good enough for me
		RadialGradientPaint paint = createGradientPaint(p, radius);
		mainLayer.setPaint(paint);
		mainLayer.fillOval((int)p.x - radius, (int)p.y - radius, radius * 2, radius * 2);
	}
	
	private RadialGradientPaint createGradientPaint(Position p, int radius) {
		Point2D center = new Point2D.Double(p.x, p.y);
		Point2D focus = new Point2D.Double(p.x - radius * .4, p.y - radius * .6);
		float[] fractions = { 0.0f, 0.4f, 0.9f, 1.0f };
		Color[] colors = { Color.WHITE, gold1, gold3, gold2 };
		RadialGradientPaint paint = new RadialGradientPaint(center, radius, focus,
				fractions, colors, CycleMethod.NO_CYCLE);
		return paint;
	}

}
