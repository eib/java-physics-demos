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
public interface Mass extends Acceleratable {

	public void addForce(Point2D forceVector);

	public Vector2D getNetForce();
	
	public double getMass();
}
