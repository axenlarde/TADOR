package com.alex.tador.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.alex.tador.misc.CUCM;
import com.alex.tador.misc.Firmware;
import com.alex.tador.misc.Office;


/**********************************
 * Used to store static variables
 * 
 * @author RATEL Alexandre
 **********************************/
public class Variables
	{
	/**
	 * Variables
	 */
	
		
	/********************************************
	 * cucmVersion :
	 * Is used to define the cucm version used for the injection
	 ***************************************/
	public enum CucmVersion
		{
		version80,
		version85,
		version90,
		version91,
		version100,
		version105,
		version110,
		version115,
		version120,
		version125
		};
		
	public enum ReachableStatus
		{
		reachable,
		unreachable,
		unknown
		};
		
	/********************************************
	 * sqlRequestType :
	 * Query or update
	 ***************************************/
	public enum SQLRequestType
		{
		update,
		query
		};
		
		
	//Misc
	private static String softwareName;
	private static String softwareVersion;
	private static Logger logger;
	private static ArrayList<String[][]> tabConfig;
	private static String mainDirectory;
	private static String configFileName;
	private static String officeListFileName;
	private static String firmwareListFileName;
	private static String phoneSurveyFileName;
	private static String overallResultFileName;
	private static String logFileName;
	private static ArrayList<Office> officeList;
	private static ArrayList<Firmware> firmwareList;
	
	//AXL & RIS
    private static CUCM srccucm;
    
    /**************
     * Constructor
     **************/
	public Variables()
		{
		mainDirectory = ".";
		configFileName = "configFile.xml";
		officeListFileName = "devicePoolList.txt";
		firmwareListFileName = "firmwareList.xml";
		phoneSurveyFileName = "phoneSurvey";
		overallResultFileName = "OverallResult";
		}

	public static String getSoftwareName()
		{
		return softwareName;
		}

	public static void setSoftwareName(String softwareName)
		{
		Variables.softwareName = softwareName;
		}

	public static String getSoftwareVersion()
		{
		return softwareVersion;
		}

	public static void setSoftwareVersion(String softwareVersion)
		{
		Variables.softwareVersion = softwareVersion;
		}

	public static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public static ArrayList<String[][]> getTabConfig()
		{
		return tabConfig;
		}

	public static void setTabConfig(ArrayList<String[][]> tabConfig)
		{
		Variables.tabConfig = tabConfig;
		}

	public static String getMainDirectory()
		{
		return mainDirectory;
		}

	public static void setMainDirectory(String mainDirectory)
		{
		Variables.mainDirectory = mainDirectory;
		}

	public static String getConfigFileName()
		{
		return configFileName;
		}

	public static void setConfigFileName(String configFileName)
		{
		Variables.configFileName = configFileName;
		}

	public static String getOfficeListFileName()
		{
		return officeListFileName;
		}

	public static void setOfficeListFileName(String officeListFileName)
		{
		Variables.officeListFileName = officeListFileName;
		}

	public static String getFirmwareListFileName()
		{
		return firmwareListFileName;
		}

	public static void setFirmwareListFileName(String firmwareListFileName)
		{
		Variables.firmwareListFileName = firmwareListFileName;
		}

	public static String getLogFileName()
		{
		return logFileName;
		}

	public static void setLogFileName(String logFileName)
		{
		Variables.logFileName = logFileName;
		}

	public static CUCM getSrccucm()
		{
		return srccucm;
		}

	public static void setSrccucm(CUCM srccucm)
		{
		Variables.srccucm = srccucm;
		}

	public static ArrayList<Office> getOfficeList()
		{
		return officeList;
		}

	public static void setOfficeList(ArrayList<Office> officeList)
		{
		Variables.officeList = officeList;
		}

	public static ArrayList<Firmware> getFirmwareList()
		{
		return firmwareList;
		}

	public static void setFirmwareList(ArrayList<Firmware> firmwareList)
		{
		Variables.firmwareList = firmwareList;
		}

	public static String getPhoneSurveyFileName()
		{
		return phoneSurveyFileName;
		}

	public static void setPhoneSurveyFileName(String phoneSurveyFileName)
		{
		Variables.phoneSurveyFileName = phoneSurveyFileName;
		}

	public static String getOverallResultFileName()
		{
		return overallResultFileName;
		}

	public static void setOverallResultFileName(String overallResultFileName)
		{
		Variables.overallResultFileName = overallResultFileName;
		}

	
	
	/*2020*//*RATEL Alexandre 8)*/
	}

