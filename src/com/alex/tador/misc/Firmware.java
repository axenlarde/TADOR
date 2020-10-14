package com.alex.tador.misc;

import com.alex.tador.utils.Variables.Protocol;

/**
 * @author Alexandre RATEL
 *
 * Used to store device firmware parameters
 */
public class Firmware
	{
	/**
	 * Variables
	 */
	
	
	private String name,firmware;
	private Protocol protocol;

	public Firmware(String name, Protocol protocol, String firmware)
		{
		super();
		this.name = name;
		this.protocol = protocol;
		this.firmware = firmware;
		}

	public String getName()
		{
		return name;
		}

	public String getFirmware()
		{
		return firmware;
		}

	public Protocol getProtocol()
		{
		return protocol;
		}
	
	
	
	
	/*2020*//*Alexandre RATEL 8)*/
	}
