package com.eblackwelder.physics.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.eblackwelder.math.MathUtils;
import com.eblackwelder.math.Position;
import com.eblackwelder.physics.model.Charge;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class ChargeRenderer implements Renderer {

	private final double massToRadius;
	
	public ChargeRenderer(double massToRadiusRatio) {
		this.massToRadius = massToRadiusRatio;
	}

	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		if (object instanceof Charge) {
			render((Charge) object, context.getMainLayer());
		}
	}
	
	protected void render(Charge c, Graphics2D mainLayer) {
		Color color = getColor(c);
		mainLayer.setColor(color);
		
		Position p = c.getPosition();
		double radius = c.getMass() / massToRadius;
		
		AffineTransform transform = mainLayer.getTransform(); 
		mainLayer.translate(p.x, p.y);
		mainLayer.scale(radius, radius);
		mainLayer.fillOval(-1, -1, 2, 2);
		mainLayer.setTransform(transform);
	}

	protected Color getColor(Charge c) {
		Color color;
		if (MathUtils.almostZero(c.getCharge(), 0.001)) {
			color = Color.LIGHT_GRAY;
		} else {
			color = c.getCharge() > 0 ? Color.BLUE : Color.RED;
		}
		return color;
	}

}
