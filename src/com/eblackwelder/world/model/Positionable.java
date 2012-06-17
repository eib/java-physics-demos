package com.eblackwelder.world.model;

import java.awt.geom.Point2D;

import com.eblackwelder.math.Vector2D;

/**
 * @author Ethan
 *
 */
public interface Positionable extends Bounded {

	public void setPosition(Point2D point);
	
	public Vector2D getPosition();
}
