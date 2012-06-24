package com.eblackwelder.physics.chargedParticles;

import java.awt.Color;

import com.eblackwelder.physics.model.Charge;
import com.eblackwelder.physics.renderers.ChargeRenderer;

/**
 * 
 * @author Ethan
 */
public class ParticleRenderer extends ChargeRenderer {

	private final double maxCharge;
	
	public ParticleRenderer(double massToRadiusRatio, double maxCharge) {
		super(massToRadiusRatio);
		this.maxCharge = maxCharge;
	}

	@Override
	protected Color getColor(Charge c) {
		double charge = c.getCharge();
		int red, blue, green;
		//positive
		if (charge > 0) {
			blue = 0xFF;
			green = red  = (int) ((maxCharge - charge) / maxCharge * 200.0);
			
		 //negative
		} else if (charge < 0) {
			red = 0xFF;
			green = blue = (int) ((maxCharge + charge) / maxCharge * 200.0);

		//neutral
		} else {
			red = green = blue = 0xCC;
		}
		return new Color(red, green, blue);
	}

}
