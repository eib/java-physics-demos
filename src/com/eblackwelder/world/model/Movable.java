/**
 * 
 */
package com.eblackwelder.world.model;

import java.awt.geom.Point2D;

import com.eblackwelder.math.Vector2D;

/**
 * @author Ethan
 *
 */
public interface Movable extends Positionable {

	public Vector2D getVelocity();

	public void setVelocity(Point2D velocity);
}
