package com.alex.tador.misc;

import com.alex.tador.utils.Variables.Protocol;

/**
 * Used to represent a phone
 *
 * @author Alexandre RATEL
 */
public class BasicPhone
	{
	/**
	 * Variables
	 */
	public enum PhoneStatus
		{
		any,
		registered,
		unregistered,
		rejected,
		partiallyregistered,
		unknown
		};
	
	private String name,
	description,
	model,
	ip,
	loadID;
	
	private PhoneStatus firstStatus;
	private PhoneStatus status;
	private Protocol protocol;
	
	public BasicPhone(String name, String description, String model, String ip, Protocol protocol, PhoneStatus status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.protocol = protocol;
		this.firstStatus = status;
		}

	public BasicPhone(String name, String description, String model, String ip, String loadID, String status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.loadID = loadID;
		this.firstStatus = PhoneStatus.valueOf(status);
		}

	public BasicPhone(String name, String description, String model, Protocol protocol)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.protocol = protocol;
		}
	
	public void setStatus(PhoneStatus status)
		{
		if(this.firstStatus == null)this.firstStatus = status;
		else this.status = status;
		}
	
	public boolean isOK()
		{
		if((firstStatus.equals(PhoneStatus.registered)) && (status.equals(PhoneStatus.registered)))
			{
			return true;
			}
		return false;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getDescription()
		{
		return description;
		}

	public void setDescription(String description)
		{
		this.description = description;
		}

	public String getModel()
		{
		return model;
		}

	public void setModel(String model)
		{
		this.model = model;
		}

	public String getIp()
		{
		return ip;
		}

	public void setIp(String ip)
		{
		this.ip = ip;
		}

	public PhoneStatus getFirstStatus()
		{
		return firstStatus;
		}

	public PhoneStatus getStatus()
		{
		return status;
		}

	public Protocol getProtocol()
		{
		return protocol;
		}

	public void setProtocol(Protocol protocol)
		{
		this.protocol = protocol;
		}

	public String getLoadID()
		{
		return loadID;
		}

	public void setLoadID(String loadID)
		{
		this.loadID = loadID;
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}
