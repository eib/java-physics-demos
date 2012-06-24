package com.eblackwelder.physics.renderers;

import java.awt.Color;

import com.eblackwelder.math.Position;
import com.eblackwelder.physics.model.Mass;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;


public class NetForceRendererAdapter extends VectorRendererAdapter {

	public NetForceRendererAdapter(int width, double scale) {
		this(null, width, scale);
	}
	
	public NetForceRendererAdapter(Renderer delegate, int width, double scale) {
		super(delegate, Color.ORANGE, width, scale);
	}
	
	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		super.render(object, context);
		
		if (object instanceof Mass) {
			Mass obj = (Mass)object;

			Force netForce = obj.getNetForce();
			Position p = obj.getPosition();
			super.renderVector(netForce, p, context);
		}
	}

}
