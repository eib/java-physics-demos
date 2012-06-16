/**
 * 
 */
package com.eblackwelder.physx.physics;

import java.awt.geom.Rectangle2D;
import java.util.List;

import com.eblackwelder.physx.object.Mass;


/**
 * @author Ethan
 * An interface for pair-wise physics effects.
 * Examples: collisions, point-charge attraction/repulsion, etc.
 */
public interface Physics {
	
	public void apply(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2);
	
	public boolean affects(Mass a, Rectangle2D bounds);
	
	public void startNext(List<Mass> affected);
}
