/**
 * 
 */
package com.eblackwelder.physx.object;

import java.awt.geom.Point2D;

import com.eblackwelder.physx.Vector2D;

/**
 * @author Ethan
 *
 */
public interface Movable extends Positionable {

	public Vector2D getVelocity();

	public void setVelocity(Point2D velocity);
}
