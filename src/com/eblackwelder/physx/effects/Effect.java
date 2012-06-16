/**
 * 
 */
package com.eblackwelder.physx.effects;

import java.awt.geom.Rectangle2D;

import com.eblackwelder.physx.object.Mass;

/**
 * @author Ethan
 *
 */
public interface Effect {

	public boolean isAffected(Mass a, Rectangle2D bounds);
	public void apply(Mass a, Rectangle2D bounds);
}
