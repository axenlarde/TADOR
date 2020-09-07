package com.alex.tador.axl;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.alex.tador.misc.CUCM;
import com.alex.tador.utils.Variables;
import com.alex.tador.utils.Variables.CucmVersion;
import com.cisco.axl.api._10.XLoadInformation;
import com.cisco.axlapiservice10.AXLError;

/**
 * @author Alexandre RATEL
 *
 * Used to store static method about AXL
 */
public class AXLTools
	{
	
	/**************
	 * Method aims to return the Version of the CUCM of the asked item
	 *************/
	public static String getCUCMVersion(CUCM cucm) throws Exception
		{
		if(cucm.getVersion().equals(CucmVersion.version105))
			{
			com.cisco.axl.api._10.GetCCMVersionReq req = new com.cisco.axl.api._10.GetCCMVersionReq();
			com.cisco.axl.api._10.GetCCMVersionRes resp = cucm.getAXLConnectionV105().getCCMVersion(req);//We send the request to the CUCM
			
			return resp.getReturn().getComponentVersion().getVersion();
			}
		else
			{
			throw new Exception("AXL unsupported version");
			}
		}
	
	/**
	 * To make a user authenticate by the CUCM 
	 */
	public static boolean doAuthenticate(CUCM cucm, String userID, String password)
		{
		if(cucm.getVersion().equals(CucmVersion.version105))
			{
			try
				{
				com.cisco.axl.api._10.DoAuthenticateUserReq req = new com.cisco.axl.api._10.DoAuthenticateUserReq();
				
				req.setUserid(userID);
				req.setPassword(password);
				
				com.cisco.axl.api._10.DoAuthenticateUserRes resp = cucm.getAXLConnectionV105().doAuthenticateUser(req);
				
				return Boolean.parseBoolean(resp.getReturn().getUserAuthenticated());
				}
			catch (Exception e)
				{
				Variables.getLogger().error(cucm.getInfo()+" : ERROR while authenticating user "+userID+" : "+e.getMessage(),e);
				}
			}
		else
			{
			Variables.getLogger().debug(cucm.getInfo()+" : Unsupported AXL version");
			}
		
		return false;
		}
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static List<Object> doSQLQuery(String request, CUCM cucm) throws Exception
		{
		if(cucm.getVersion().equals(CucmVersion.version105))
			{
			return doSQLQueryV105(request, cucm);
			}
		
		throw new Exception("Unsupported AXL Version");
		}
	
	/**
	 * Method used to reach the method of the good version
	 */
	public static void doSQLUpdate(String request, CUCM cucm) throws Exception
		{
		if(cucm.getVersion().equals(CucmVersion.version105))
			{
			doSQLUpdateV105(request, cucm);
			}
		else
			{
			throw new Exception("Unsupported AXL Version");
			}
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static List<Object> doSQLQueryV105(String request, CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLQueryReq req = new com.cisco.axl.api._10.ExecuteSQLQueryReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLQueryRes resp = cucm.getAXLConnectionV105().executeSQLQuery(req);//We send the request to the CUCM
		
		List<Object> myList = resp.getReturn().getRow();
		
		return myList;
		}
	
	/***
	 * Method used to launch a SQL request to the CUCM and get
	 * a result as an ArrayList<String>
	 * 
	 * each "String" is a list of result
	 */
	private static void doSQLUpdateV105(String request, CUCM cucm) throws Exception
		{
		Variables.getLogger().debug("SQL request sent : "+request);
		
		com.cisco.axl.api._10.ExecuteSQLUpdateReq req = new com.cisco.axl.api._10.ExecuteSQLUpdateReq();
		req.setSql(request);
		com.cisco.axl.api._10.ExecuteSQLUpdateRes resp = cucm.getAXLConnectionV105().executeSQLUpdate(req);//We send the request to the CUCM
		}
	
	/**
	 * Used to update the given device load name
	 * @param deviceName
	 * @param phoneLoad
	 * @throws AXLError 
	 */
	public static void updatePhoneLoad(String deviceName, String phoneLoad, CUCM cucm) throws AXLError
		{
		com.cisco.axl.api._10.UpdatePhoneReq req = new com.cisco.axl.api._10.UpdatePhoneReq();
		req.setName(deviceName);
		
		com.cisco.axl.api._10.XLoadInformation load = new XLoadInformation();
		load.setSpecial("true");
		load.setValue(phoneLoad);
		
		req.setLoadInformation(new JAXBElement(new QName("loadInformation"), com.cisco.axl.api._10.XLoadInformation.class, load));
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().updatePhone(req);
		}
	
	/**
	 * Used to reset the given device pool
	 * @throws AXLError 
	 */
	public static void resetDevicePool(String devicePool, CUCM cucm) throws AXLError
		{
		com.cisco.axl.api._10.NameAndGUIDRequest req = new com.cisco.axl.api._10.NameAndGUIDRequest();
		
		req.setName(devicePool);
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().resetDevicePool(req);
		}
	
	/**
	 * Used to reset the given device
	 * @throws AXLError 
	 */
	public static void resetDevice(String deviceName, CUCM cucm) throws AXLError
		{
		com.cisco.axl.api._10.NameAndGUIDRequest req = new com.cisco.axl.api._10.NameAndGUIDRequest();
		
		req.setName(deviceName);
		
		com.cisco.axl.api._10.StandardResponse resp = cucm.getAXLConnectionV105().resetPhone(req);
		}
	
	/*2020*//*Alexandre RATEL 8)*/
	}
