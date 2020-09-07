package com.alex.tador.misc;

import java.util.ArrayList;

/**
 * @author Alexandre RATEL
 *
 * Used to store office parameters
 */
public class Office
	{
	/***
	 * Variables
	 */
	private String name;
	private ArrayList<BasicPhone> phoneList;
	private boolean done;

	public Office(String name)
		{
		super();
		this.name = name;
		done = false;
		}

	public String getName()
		{
		return name;
		}

	public ArrayList<BasicPhone> getPhoneList()
		{
		return phoneList;
		}

	public void setPhoneList(ArrayList<BasicPhone> phoneList)
		{
		this.phoneList = phoneList;
		}

	public boolean isDone()
		{
		return done;
		}

	public void setDone(boolean done)
		{
		this.done = done;
		}
	
	/*2020*//*Alexandre RATEL 8)*/
	}
