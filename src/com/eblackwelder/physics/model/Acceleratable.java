package com.eblackwelder.physics.model;

import com.eblackwelder.physics.properties.Acceleration;
import com.eblackwelder.world.model.Movable;

public interface Acceleratable extends Movable {
	
	public Acceleration getAcceleration();

	public void setAcceleration(Acceleration acceleration);

}