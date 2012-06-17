/**
 * 
 */
package com.eblackwelder.graphx.renderable;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.world.World;

/**
 * @author Ethan
 *
 */
public abstract class AbstractTextMessage extends AbstractRenderable {

	private final Point cursorStart;
	private final int yOffset;
	
	public AbstractTextMessage() {
		this(new Point(20, 30), 20);
	}

	public AbstractTextMessage(Point cursorStart, int yOffset) {
		super(Renderable.OVERLAY_LAYER);
		this.cursorStart = cursorStart;
		this.yOffset = yOffset;
	}

	protected abstract String[] getMessages();
	
	
	@Override
	public void render(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		World.getSingleton().reverseTransform(g2);
		int y = cursorStart.y;
		for (String message : getMessages()) {
			g2.drawString(message, cursorStart.x, y);
			y += yOffset;
		}
		g2.setTransform(transform);
	}
	
	@Override
	public Rectangle2D getBounds(Point2D position) {
		return null;
	}	
}
