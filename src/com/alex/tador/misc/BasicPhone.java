package com.alex.tador.misc;

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
	ip;
	
	private PhoneStatus firstStatus;
	private PhoneStatus status;
	
	public BasicPhone(String name, String description, String model, String ip, PhoneStatus status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.firstStatus = status;
		}

	public BasicPhone(String name, String description, String model, String ip, String status)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
		this.ip = ip;
		this.firstStatus = PhoneStatus.valueOf(status);
		}

	public BasicPhone(String name, String description, String model)
		{
		super();
		this.name = name;
		this.description = description;
		this.model = model;
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
	
	/*2020*//*RATEL Alexandre 8)*/
	}
