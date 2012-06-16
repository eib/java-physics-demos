/**
 * 
 */
package com.eblackwelder.physx.worldMode;

import com.eblackwelder.physx.World;
import com.eblackwelder.physx.object.Mass;

/**
 * @author Ethan
 *
 */
public interface WorldMode {
	
	public void handleOutOfBounds(Mass m, World w);
}
