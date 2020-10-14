package com.alex.tador.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Level;

import com.alex.tador.axl.AXLTools;
import com.alex.tador.misc.CUCM;
import com.alex.tador.misc.Firmware;
import com.alex.tador.misc.Office;
import com.alex.tador.utils.Variables.CucmVersion;
import com.alex.tador.utils.Variables.Protocol;
import com.cisco.schemas.ast.soap.RISService70;
import com.cisco.schemas.ast.soap.RisPortType;


/**********************************
 * Class used to store the useful static methods
 * 
 * @author RATEL Alexandre
 **********************************/
public class UsefulMethod
	{
	
	/*****
	 * Method used to read the main config file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readMainConfigFile(String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			file = xMLReader.fileRead("./"+fileName);
			
			listParams.add("config");
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+fileName+" : "+exc.getMessage());
			}
		
		}
	
	
	/***************************************
	 * Method used to get a specific value
	 * in the user preference XML File
	 ***************************************/
	public synchronized static String getTargetOption(String node) throws Exception
		{
		//We first seek in the configFile.xml
		for(String[] s : Variables.getTabConfig().get(0))
			{
			if(s[0].equals(node))return s[1];
			}
		
		/***********
		 * If this point is reached, the option looked for was not found
		 */
		throw new Exception("Option \""+node+"\" not found"); 
		}
	/*************************/
	
	
	
	/************************
	 * Check if java version
	 * is correct
	 ***********************/
	public static void checkJavaVersion()
		{
		try
			{
			String jVer = new String(System.getProperty("java.version"));
			Variables.getLogger().info("Detected JRE version : "+jVer);
			
			/*Need to use the config file value*/
			
			if(jVer.contains("1.6"))
				{
				/*
				if(Integer.parseInt(jVer.substring(6,8))<16)
					{
					Variables.getLogger().info("JRE version is not compatible. The application will now exit. system.exit(0)");
					System.exit(0);
					}*/
				}
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().info("ERROR : It has not been possible to detect JRE version",exc);
			}
		}
	/***********************/
	
	
	/************
	 * Method used to read a simple configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String> readFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			
			return xMLGear.getResultList(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("ERROR : The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+fileName+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read a configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readFileTab(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the "+param+" file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+param+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read an advanced configuration file
	 * @throws Exception 
	 */
	public static ArrayList<ArrayList<String[][]>> readExtFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTabExt(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+exc.getMessage());
			}
		}
	
	/**
	 * Used to return the file content regarding the data source (xml file or database file)
	 * @throws Exception 
	 */
	public static String getFlatFileContent(String fileName) throws Exception, FileNotFoundException
		{
		return xMLReader.fileRead(Variables.getMainDirectory()+"/"+fileName);
		}
	
	/**
	 * Method used to initialize the database from
	 * a collection file
	 * @throws 
	 */
	public static void initDatabase() throws Exception
		{
		Variables.setFirmwareList(initFirmwareList());
		Variables.setOfficeList(initOfficeList());
		}
	
	/************
	 * Method used to initialize the office list from
	 * the collection file
	 * @throws Exception 
	 */
	public static ArrayList<Office> initOfficeList() throws Exception
		{
		try
			{
			Variables.getLogger().info("Initializing the office list from collection file");
			ArrayList<Office> officeList = new ArrayList<Office>();
			
			FileInputStream file = new FileInputStream(Variables.getMainDirectory()+"/"+Variables.getOfficeListFileName());
			BufferedReader buffer = new BufferedReader(new InputStreamReader(file,"UTF-8"));
			
			while(true)
				{
				String line = buffer.readLine();
				if(line == null)break;
				if(!line.isEmpty())officeList.add(new Office(line));
				}
			
			Variables.getLogger().debug(officeList.size()+ " offices found in the database");
			return officeList;
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR while initializing the office list : "+exc.getMessage(),exc);
			}
		}
	
	/**
	 * Used to initialize CliProfile list
	 * @throws Exception 
	 */
	public static ArrayList<Firmware> initFirmwareList() throws Exception
		{	
		try
			{
			Variables.getLogger().info("Initializing the Firmware list from collection file");
			ArrayList<Firmware> firmwareList = new ArrayList<Firmware>();
			ArrayList<String> listParams = new ArrayList<String>();
			listParams.add("devices");
			listParams.add("device");
			ArrayList<String[][]> result = xMLGear.getResultListTab(UsefulMethod.getFlatFileContent(Variables.getFirmwareListFileName()), listParams);
			ArrayList<ArrayList<String[][]>> extendedList = xMLGear.getResultListTabExt(UsefulMethod.getFlatFileContent(Variables.getFirmwareListFileName()), listParams);
			
			for(int i=0; i<result.size(); i++)
				{
				try
					{
					String[][] tab = result.get(i);
					ArrayList<String[][]> tabE = extendedList.get(i);
					
					/**
					 * First we check for duplicates
					 */
					String fName = UsefulMethod.getItemByName("name", tab);
					Protocol p = Protocol.valueOf(UsefulMethod.getItemByName("protocol", tab));
					
					boolean found = false;
					for(Firmware f : firmwareList)
						{
						if(f.getName().equals(fName))
							{
							if(f.getProtocol().equals(p))
								{
								Variables.getLogger().debug("Duplicate found, do not adding the device : "+fName+" "+p.name());
								found = true;
								break;
								}
							}
						}
					if(found)continue;
					
					firmwareList.add(new Firmware(fName,
							p,
							UsefulMethod.getItemByName("firmwarename", tab)));
					}
				catch (Exception e)
					{
					Variables.getLogger().error("ERROR while initializing a new firmware list : "+e.getMessage(), e);
					}
				}
			
			Variables.getLogger().debug(firmwareList.size()+ " firmware found in the database");
			Variables.getLogger().debug("firmware list initialization done");
			return firmwareList;
			}
		catch(Exception exc)
			{
			throw new Exception("ERROR while initializing the firmware list : "+exc.getMessage(),exc);
			}
		}
	
	
	/******
	 * Method used to initialize the AXL Connection to the CUCM
	 */
	public static void initAXLConnectionToCUCM(CUCM cucm) throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(cucm.getVersion().equals(CucmVersion.version85))
				{
				throw new Exception("AXL unsupported version");
				}
			else if(cucm.getVersion().equals(CucmVersion.version105))
				{
				com.cisco.axlapiservice10.AXLAPIService axlService = new com.cisco.axlapiservice10.AXLAPIService();
				com.cisco.axlapiservice10.AXLPort axlPort = axlService.getAXLPort();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+cucm.getIp()+":"+cucm.getPort()+"/axl/";
				
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, cucm.getUser());
				((BindingProvider) axlPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, cucm.getPassword());
				
				cucm.setAXLConnectionV105(axlPort);
				}
			
			Variables.getLogger().debug("AXL WSDL Initialization done");
			
			/**
			 * We now check if the CUCM is reachable by asking him its version
			 */
			Variables.getLogger().debug("CUCM version : "+AXLTools.getCUCMVersion(cucm));
			cucm.setReachable(true);
			}
		catch (Exception e)
			{
			Variables.getLogger().error(cucm.getIp()+" : Error while initializing AXL CUCM connection : "+e.getMessage(),e);
			cucm.setReachable(false);
			throw e;
			}
		}
	
	/******
	 * Method used to initialize the RIS Connection to the CUCM
	 */
	public static void initRISConnectionToCUCM(CUCM cucm) throws Exception
		{
		try
			{
			UsefulMethod.disableSecurity();//We first turned off security
			
			if(cucm.getVersion().equals(CucmVersion.version85))
				{
				throw new Exception("RIS unsupported version");
				}
			else if(cucm.getVersion().equals(CucmVersion.version105))
				{
				RISService70 ris = new RISService70();
				RisPortType risPort = ris.getRisPort70();
				
				// Set the URL, user, and password on the JAX-WS client
				String validatorUrl = "https://"+cucm.getIp()+":"+cucm.getPort()+"/realtimeservice2/services/RISService70";
				
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, validatorUrl);
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, cucm.getUser());
				((BindingProvider) risPort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, cucm.getPassword());
				
				cucm.setRISConnection(risPort);
				}
			
			Variables.getLogger().debug("RIS WSDL Initialization done");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(cucm.getIp()+" : Error while initializing RIS CUCM connection : "+e.getMessage(),e);
			cucm.setReachable(false);
			throw e;
			}
		}
	
	/**
	 * Method used when the application failed to 
	 * initialize
	 */
	public static void failedToInit(Exception exc)
		{
		Variables.getLogger().error(exc.getMessage(),exc);
		Variables.getLogger().error("Application failed to init : System.exit(0)");
		System.exit(0);
		}
	
	/**
	 * Initialization of the internal variables from
	 * what we read in the configuration file
	 * @throws Exception 
	 */
	public static void initInternalVariables() throws Exception
		{
		/***********
		 * Logger
		 */
		String level = UsefulMethod.getTargetOption("log4j");
		if(level.compareTo("DEBUG")==0)
			{
			Variables.getLogger().setLevel(Level.DEBUG);
			}
		else if (level.compareTo("INFO")==0)
			{
			Variables.getLogger().setLevel(Level.INFO);
			}
		else if (level.compareTo("ERROR")==0)
			{
			Variables.getLogger().setLevel(Level.ERROR);
			}
		else
			{
			//Default level is INFO
			Variables.getLogger().setLevel(Level.INFO);
			}
		Variables.getLogger().info("Log level found in the configuration file : "+Variables.getLogger().getLevel().toString());
		/*************/
		
		/************
		 * Etc...
		 */
		//If needed, just write it here
		/*************/
		}
	
	/**
	 * Method which convert a string into cucmAXLVersion
	 */
	public static CucmVersion convertStringToCUCMAXLVersion(String version)
		{
		if(version.contains("80"))
			{
			return CucmVersion.version80;
			}
		else if(version.contains("85"))
			{
			return CucmVersion.version85;
			}
		else if(version.contains("105"))
			{
			return CucmVersion.version105;
			}
		else if(version.contains("115"))
			{
			return CucmVersion.version115;
			}
		else if(version.contains("125"))
			{
			return CucmVersion.version125;
			}
		else
			{
			//Default : 10.5
			return CucmVersion.version105;
			}
		}
	
	
	/**************
	 * Method aims to get a template item value by giving its name
	 * @throws Exception 
	 *************/
	public static String getItemByName(String name, String[][] itemDetails) throws Exception
		{
		for(int i=0; i<itemDetails.length; i++)
			{
			if(itemDetails[i][0].equals(name))
				{
				return itemDetails[i][1];
				}
			}
		//throw new Exception("Item not found : "+name);
		Variables.getLogger().debug("Item not found : "+name);
		return "";
		}
	
	/**********************************************************
	 * Method used to disable security in order to accept any
	 * certificate without trusting it
	 */
	public static void disableSecurity()
		{
		try
        	{
            X509TrustManager xtm = new HttpsTrustManager();
            TrustManager[] mytm = { xtm };
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, mytm, null);
            SSLSocketFactory sf = ctx.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
            	{
                public boolean verify(String hostname, SSLSession session)
                	{
                    return true;
                	}
            	}
            );
        	}
        catch (Exception e)
        	{
            e.printStackTrace();
        	}
		}
	
	
	/**
	 * Method used to find the file name from a file path
	 * For intance :
	 * C:\JAVAELILEX\YUZA\Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 * gives :
	 * Maquette_CNAF_TA_FichierCollecteDonneesTelephonie_v1.7_mac.xls
	 */
	public static String extractFileName(String fullFilePath)
		{
		String[] tab =  fullFilePath.split("\\\\");
		return tab[tab.length-1];
		}
	
	/***
	 * Method used to get the AXL version from the CUCM
	 * We contact the CUCM using a very basic request and therefore get the version
	 * @throws Exception 
	 */
	public static CucmVersion getAXLVersionFromTheCUCM() throws Exception
		{
		/**
		 * In this method version we just read the version from the configuration file
		 * This has to be improved to match the method description
		 **/
		CucmVersion AXLVersion;
		
		AXLVersion = UsefulMethod.convertStringToCUCMAXLVersion("version"+getTargetOption("axlversion"));
		
		return AXLVersion;
		}
	
	/******
	 * Method used to determine if the fault description means
	 * that the item was not found or something else
	 * If it is not found we return true
	 * For any other reason we return false
	 * @param faultDesc
	 * @return
	 */
	public static boolean itemNotFoundInTheCUCM(String faultDesc)
		{
		ArrayList<String> faultDescList = new ArrayList<String>();
		faultDescList.add("was not found");
		for(String s : faultDescList)
			{
			if(faultDesc.contains(s))return true;
			}
		
		return false;
		}
	
	/**
	 * Used to initialize the CUCM AXL and RIS Connection
	 * @throws Exception
	 */
	public static void initCUCM() throws Exception
		{
		Variables.getLogger().debug("Initializing CUCM");
		/**
		 * Source CUCM
		 */
		CUCM srccucm = new CUCM(UsefulMethod.getTargetOption("cucmip"),
				UsefulMethod.getTargetOption("cucmport"),
				UsefulMethod.getTargetOption("username"),
				UsefulMethod.getTargetOption("password"),
				UsefulMethod.convertStringToCUCMAXLVersion(UsefulMethod.getTargetOption("axlversion")));
		
		Variables.getLogger().debug("Initializing source CUCM : "+srccucm.getInfo());
		
		initAXLConnectionToCUCM(srccucm);
		initRISConnectionToCUCM(srccucm);
		Variables.getLogger().debug("Initializing source CUCM done !");
		Variables.setSrccucm(srccucm);
		}
	
	/**
	 * Used to get the firmware to use according to the
	 * given device type
	 * 
	 * @param deviceType
	 * @throws Exception 
	 */
	public static String getFirmware(String deviceType, Protocol protocol) throws Exception
		{
		for(Firmware f : Variables.getFirmwareList())
			{
			if((f.getName().equals(deviceType)) && (f.getProtocol().equals(protocol)))return f.getFirmware();
			}
		
		throw new Exception("Firmware not found for device : "+deviceType);
		}
	
	/*2020*//*RATEL Alexandre 8)*/
	}

