/**
 * 
 */
package com.eblackwelder.physics.model;

import java.util.Collection;

import com.eblackwelder.physics.properties.Force;

/**
 * @author Ethan
 *
 */
public interface Mass extends Acceleratable {

	public void addForce(Force force);
	
	public Collection<Force> getAppliedForces();

	public Force getNetForce();
	
	public void setNetForce(Force netForce);
	
	public void clearForces();
	
	public double getMass();
}
