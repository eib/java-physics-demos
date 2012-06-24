package com.eblackwelder.physics.renderers;

import java.awt.Color;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Movable;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;


public class VelocityRendererAdapter extends VectorRendererAdapter {

	public VelocityRendererAdapter(int width, double scale) {
		this(null, width, scale);
	}
	
	public VelocityRendererAdapter(Renderer delegate, int width, double scale) {
		super(delegate, Color.GREEN, width, scale);
	}

	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		super.render(object, context);
		
		if (object instanceof Movable) {
			Movable obj = (Movable)object;
			Vector2D v = obj.getVelocity();
			Position p = obj.getPosition();
			super.renderVector(v, p, context);
		}
	}

}
