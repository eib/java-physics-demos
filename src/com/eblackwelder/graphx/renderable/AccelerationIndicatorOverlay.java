/**
 * 
 */
package com.eblackwelder.graphx.renderable;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.world.model.Acceleratable;

/**
 * @author Ethan
 *
 */
public class AccelerationIndicatorOverlay extends AbstractVectorOverlay {

	private final Acceleratable acceleratable;

	public AccelerationIndicatorOverlay(Acceleratable acceleratable) {
		this(acceleratable, 1.0);
	}
	
	public AccelerationIndicatorOverlay(Acceleratable acceleratable, double scale) {
		super(scale, Color.BLUE);
		this.acceleratable = acceleratable;
	}

	public AccelerationIndicatorOverlay(Acceleratable acceleratable, double scale, Color color, Point2D offset) {
		super(scale, color, offset);
		this.acceleratable = acceleratable;
	}

	@Override
	public Rectangle2D getBounds(Point2D position) {
		return acceleratable.getBounds();
	}

	@Override
	protected Point2D getIndicatorProperty() {
		return acceleratable.getAcceleration();
	}

	@Override
	protected Point2D getPosition() {
		return acceleratable.getPosition();
	}

}
