package vrecservice;

import vrec.Configuration;

public abstract class AbstractService 
{
	public AbstractService()
	{
		Configuration.initStorage();
	}
}
