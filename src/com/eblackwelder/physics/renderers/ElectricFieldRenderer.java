package com.eblackwelder.physics.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import com.eblackwelder.physics.forces.ElectricField;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class ElectricFieldRenderer implements Renderer {

	private final Color LIGHT_BLUE = new Color(202, 225, 255);
	private final Color LIGHT_RED = new Color(255, 215, 202);
	
	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		if (object instanceof ElectricField) {
			renderField((ElectricField) object, context.getBackgroundLayer(), context.getCanvasSize());
		}
	}
	
	public void renderField(ElectricField field, Graphics2D backgroundLayer, Dimension size) {
		Color color = field.isPositive() ? LIGHT_BLUE : LIGHT_RED;
		backgroundLayer.setColor(color);
		backgroundLayer.fillRect(0, 0, size.width, size.height);
	}

}
