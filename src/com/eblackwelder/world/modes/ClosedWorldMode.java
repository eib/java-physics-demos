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
public class ClosedWorldMode implements WorldMode {

	@Override
	public void handleOutOfBounds(Mass m, World w) {
		if (!w.getBounds().intersects(m.getBounds())) {
			w.remove(m);
		}
	}

}
