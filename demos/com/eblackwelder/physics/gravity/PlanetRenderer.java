package com.eblackwelder.physics.gravity;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.eblackwelder.math.Position;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class PlanetRenderer implements Renderer {

	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		OrbitingObject planet = (OrbitingObject) object;
		int radius = (int) planet.getRadius();
		Position p = planet.getPosition();
		Dimension size = context.getCanvasSize();
		
		Graphics2D mainLayer = context.getMainLayer();
		
		mainLayer.translate(size.width / 2, size.height / 2);
		mainLayer.scale(1, -1);
		mainLayer.translate(p.x, p.y);
		mainLayer.setColor(planet.getColor());
		mainLayer.fillOval(-radius, -radius, radius * 2, radius * 2);
		mainLayer.translate(-p.x, -p.y);
		mainLayer.scale(1, -1);
		mainLayer.translate(-size.width / 2, -size.height / 2);
	}
}
