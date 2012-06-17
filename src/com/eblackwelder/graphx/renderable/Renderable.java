/**
 * 
 */
package com.eblackwelder.graphx.renderable;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Ethan
 *
 */
public interface Renderable {
	public static final int OVERLAY_LAYER = 100;
	public static final int DEFAULT_LAYER = 0;
	public static final int BACKGROUND_LAYER = -100;
	public static final int WORLD_LAYER = -200;
	
	public int getLayer();
	
	public void render(Graphics2D g2);
	public Rectangle2D getBounds(Point2D position);
}
