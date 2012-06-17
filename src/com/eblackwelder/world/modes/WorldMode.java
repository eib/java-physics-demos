/**
 * 
 */
package com.eblackwelder.world.modes;

import com.eblackwelder.world.World;
import com.eblackwelder.world.model.Mass;

/**
 * @author Ethan
 *
 */
public interface WorldMode {
	
	public void handleOutOfBounds(Mass m, World w);
}
