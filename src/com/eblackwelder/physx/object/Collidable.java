/**
 * 
 */
package com.eblackwelder.physx.object;



/**
 * @author Ethan
 */
public interface Collidable extends Mass {

	public double getElasticity();
	public double getFriction();
}
