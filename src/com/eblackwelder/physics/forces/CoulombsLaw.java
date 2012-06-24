package com.eblackwelder.physics.forces;

import java.util.ArrayList;
import java.util.List;

import com.eblackwelder.math.Vector2D;
import com.eblackwelder.physics.model.Charge;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Updatable;
import com.eblackwelder.world.model.impl.AbstractWorldObject;

public class CoulombsLaw extends AbstractWorldObject implements Updatable {

	public final double kE;
	
	public CoulombsLaw(double coulombConstant) {
		super("Coulombs Law");
		this.kE = coulombConstant;
	}

	@Override
	public void update(long millisElapsed, World world) {
		List<WorldObject> objects = new ArrayList<WorldObject>(world.getObjects());
		
		WorldObject o1, o2;
		Charge c1, c2;
		for (int ii = 0; ii < objects.size(); ii++) {
			o1 = objects.get(ii);
			if (o1 instanceof Charge) {
				c1 = (Charge)o1;
			} else {
				continue;
			}
			for (int jj = ii + 1; jj < objects.size(); jj++) {
				o2 = objects.get(jj);
				if (o2 instanceof Charge) {
					c2 = (Charge)o2;
				} else {
					continue;
				}
				applyCoulombsLaw(c1, c2);
			}
		}
	}
	
	private void applyCoulombsLaw(Charge c1, Charge c2) {
		Vector2D v = c1.getPosition().minus(c2.getPosition());
		double m1 = c1.getCharge();
		double m2 = c2.getCharge();
		double r = v.getMagnitude();
		double F = kE * m1 * m2 / (r*r);
		c1.addForce(new Force(v, F));
		c2.addForce(new Force(v, -F));
	}

}
