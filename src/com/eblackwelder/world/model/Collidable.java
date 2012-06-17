/**
 * 
 */
package com.eblackwelder.world.model;



/**
 * @author Ethan
 */
public interface Collidable extends Mass {

	public double getElasticity();
	public double getFriction();
}
