/**
 * 
 */
package com.eblackwelder.graphx.renderable;

import java.awt.Color;

/**
 * @author Ethan
 *
 */
public abstract class AbstractOverlay extends AbstractColorable {

	public AbstractOverlay() {
		this(Color.LIGHT_GRAY);
	}
	
	public AbstractOverlay(Color color) {
		this(Renderable.OVERLAY_LAYER, color);
	}

	private AbstractOverlay(int layer, Color color) {
		super(layer, color);
	}
}
