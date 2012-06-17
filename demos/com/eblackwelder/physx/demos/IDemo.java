package com.eblackwelder.physx.demos;

import java.util.List;

import com.eblackwelder.physx.demos.options.Option;
import com.eblackwelder.world.World;

public interface IDemo {
	
	public String getTitle();
	public String[] getDescription();
	public World createWorld();
	public List<Option<?>> getConfigurationOptions();
	public int getRepaintTimeoutMillis();
	
}
