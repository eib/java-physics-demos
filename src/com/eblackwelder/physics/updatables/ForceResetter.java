package com.eblackwelder.physics.updatables;

import com.eblackwelder.physics.model.Mass;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Updatable;
import com.eblackwelder.world.model.impl.AbstractWorldObject;

public class ForceResetter extends AbstractWorldObject implements Updatable {
	
	public ForceResetter() {
		super("Force Resetter");
	}

	@Override
	public void update(long millisElapsed, World world) {
		for (WorldObject obj : world.getObjects()) {
			if (obj instanceof Mass) {
				Mass m = (Mass) obj;
				m.clearForces();
			}
		}
	}
}
