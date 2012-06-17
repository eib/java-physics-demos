package com.eblackwelder.world.model;

import java.awt.geom.Point2D;

import com.eblackwelder.math.Vector2D;

public interface Acceleratable extends Movable {
	
	public Vector2D getAcceleration();

	public void setAcceleration(Point2D acceleration);

}