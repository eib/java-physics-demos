package com.eblackwelder.physics;

import java.util.List;

import javax.swing.JFrame;

import com.eblackwelder.physics.options.Option;
import com.eblackwelder.world.driver.WorldDriverBase;

public interface IDemo {
	
	public String getTitle();
	public String[] getDescription();
	public WorldDriverBase createDriver(JFrame parent);
	public List<Option<?>> getConfigurationOptions();
	
}
