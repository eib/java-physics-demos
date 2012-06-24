package com.eblackwelder.physics.cursor;

import com.eblackwelder.graphics.cursor.Movement;
import com.eblackwelder.physics.model.impl.KineticObject;
import com.eblackwelder.physics.properties.Force;
import com.eblackwelder.world.Type;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.model.Named;


public class KineticCursor extends KineticObject implements WorldObject, Named {

	public static final Type cursorType = new Type("KineticCursor");
	
	private String name;
	public final Movement movement = new Movement();
	
	public KineticCursor(String name, double mass) {
		super(mass);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return cursorType;
	}
	
	@Override
	public void update(long millisElapsed, World world) {
		double scale = 5.0 * millisElapsed;
		
		clearForces();
		addForce(new Force(movement.getUnitX() * scale, 0));
		addForce(new Force(0, movement.getUnitY() * scale));
		
		super.update(millisElapsed, world);
	}
}
