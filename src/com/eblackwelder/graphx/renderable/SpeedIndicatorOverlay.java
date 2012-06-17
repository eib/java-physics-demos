/**
 * 
 */
package com.eblackwelder.graphx.renderable;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.world.model.Movable;

/**
 * @author Ethan
 *
 */
public class SpeedIndicatorOverlay extends AbstractVectorOverlay {

	private final Movable movable;

	public SpeedIndicatorOverlay(Movable movable, double scale, Color color) {
		super(scale, color);
		this.movable = movable;
	}

	public SpeedIndicatorOverlay(Movable movable, double scale, Color color, Point2D offset) {
		super(scale, color, offset);
		this.movable = movable;
	}

	@Override
	public Rectangle2D getBounds(Point2D position) {
		return movable.getBounds();
	}

	@Override
	protected Point2D getIndicatorProperty() {
		return movable.getVelocity();
	}

	@Override
	protected Point2D getPosition() {
		return movable.getPosition();
	}

}
