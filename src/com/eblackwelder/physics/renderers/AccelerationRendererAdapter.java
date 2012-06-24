package com.eblackwelder.physics.renderers;

import java.awt.Color;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physics.model.Acceleratable;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;


public class AccelerationRendererAdapter extends VectorRendererAdapter {

	public AccelerationRendererAdapter(int width, double scale) {
		this(null, width, scale);
	}
	
	public AccelerationRendererAdapter(Renderer delegate, int width, double scale) {
		super(delegate, Color.RED, width, scale);
	}
	
	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		super.render(object, context);
		
		if (object instanceof Acceleratable) {
			Acceleratable obj = (Acceleratable)object;
			Vector2D v = obj.getAcceleration();
			Position p = obj.getPosition();
			super.renderVector(v, p, context);
		}
	}

}
