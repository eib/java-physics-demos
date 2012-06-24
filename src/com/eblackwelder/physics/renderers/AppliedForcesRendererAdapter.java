package com.eblackwelder.physics.renderers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import com.eblackwelder.math.Position;
import com.eblackwelder.physics.model.Mass;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class AppliedForcesRendererAdapter extends VectorRendererAdapter {

	private static final Color BROWN = new Color(222, 184, 135);

	public AppliedForcesRendererAdapter(int width, double scale) {
		this(null, width, scale);
	}
	
	public AppliedForcesRendererAdapter(Renderer delegate, int width, double scale) {
		super(delegate, BROWN, width, scale);
	}

	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		super.render(object, context);
		
		if (object instanceof Mass) {
			Mass obj = (Mass)object;

			Collection<Force> forces = new ArrayList<Force>(obj.getAppliedForces());
			Position p = obj.getPosition();
			for (Force f : forces) {
				super.renderVector(f, p, context);
			}
		}
	}
}
