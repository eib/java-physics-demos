package com.eblackwelder.physx.object;

import java.awt.geom.Point2D;

import com.eblackwelder.physx.Vector2D;

/**
 * @author Ethan
 *
 */
public interface Positionable extends Bounded {

	public void setPosition(Point2D point);
	
	public Vector2D getPosition();
}
