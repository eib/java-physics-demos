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
public class ClosedWorldMode implements WorldMode {

	@Override
	public void handleOutOfBounds(Mass m, World w) {
		if (!w.getBounds().intersects(m.getBounds())) {
			w.remove(m);
		}
	}

}
