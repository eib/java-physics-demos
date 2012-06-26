package com.eblackwelder.physics.collisions;

import java.awt.Graphics2D;

import com.eblackwelder.math.Position;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class CollidableRenderer implements Renderer {

	@Override
	public void render(WorldObject obj, IGraphicsContext context) {
		CollidableObject c = (CollidableObject) obj;
		Graphics2D mainLayer = context.getMainLayer();
		
		Position p = c.getPosition();
		
		mainLayer.setColor(c.getColor());
		mainLayer.translate(p.x, p.y);
		mainLayer.fill(c.getShape());
		mainLayer.translate(-p.x, -p.y);
	}

}
