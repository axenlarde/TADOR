package com.alex.tador.misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alex.tador.axl.AXLTools;
import com.alex.tador.misc.BasicPhone.PhoneStatus;
import com.alex.tador.risport.RisportTools;
import com.alex.tador.utils.UsefulMethod;
import com.alex.tador.utils.Variables;


/**
 * Toolbox of static method about offices
 *
 * @author Alexandre RATEL
 */
public class OfficeTools
	{
	
	/**
	 * Used to get a devicepool associated phones
	 */
	public static ArrayList<BasicPhone> getDevicePoolPhoneList(String DevicePoolName, CUCM cucm)
		{
		Variables.getLogger().debug("Looking for devicepool's phone "+DevicePoolName);
		
		ArrayList<BasicPhone> l = new ArrayList<BasicPhone>();
		
		String request = "select d.name, d.description, tm.name as model from device d, devicepool dp, typemodel tm where dp.pkid=d.fkdevicepool and tm.enum=d.tkmodel and d.tkClass='1' and dp.name='"+DevicePoolName+"'";
		
		try
			{
			List<Object> reply = AXLTools.doSQLQuery(request, cucm);
			
			for(Object o : reply)
				{
				BasicPhone bp = new BasicPhone("TBD", "", "");
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						bp.setName(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("description"))
						{
						bp.setDescription(list.item(i).getTextContent());
						}
					else if(list.item(i).getNodeName().equals("model"))
						{
						bp.setModel(list.item(i).getTextContent());
						}
					}
				Variables.getLogger().debug("Phone found : "+bp.getName());
				l.add(bp);
				}
			Variables.getLogger().debug("Found "+l.size()+" phones for "+DevicePoolName);
			return l;
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the devicepool's phones for "+DevicePoolName+" "+e.getMessage(),e);
			}
		
		return new ArrayList<BasicPhone>();
		}
	
	/**
	 * Return the device pool name of the given phone
	 */
	public static String getDevicePoolFromPhoneName(String phoneName, CUCM cucm)
		{
		Variables.getLogger().debug("Looking for phone's devicePool : "+phoneName);
		
		String request = "select dp.name from device d, devicepool dp where dp.pkid=d.fkdevicepool and d.name='"+phoneName+"'";
		
		try
			{
			List<Object> reply = AXLTools.doSQLQuery(request, cucm);
			
			for(Object o : reply)
				{
				Element rowElement = (Element) o;
				NodeList list = rowElement.getChildNodes();
				
				for(int i = 0; i< list.getLength(); i++)
					{
					if(list.item(i).getNodeName().equals("name"))
						{
						Variables.getLogger().debug("Found devicePool "+list.item(i).getTextContent()+" for phone "+phoneName);
						return list.item(i).getTextContent();
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while trying to get the phone's devicePool for phone "+phoneName+" : "+e.getMessage(),e);
			}
		return null;
		}
	
	/**
	 * Will execute a streamlined office phone survey
	 * We concatenate all the phone in one big request
	 * to lower the total RIS request per minute and the avoid 
	 * to reach the system limit (15 request per minute max)
	 */
	public static void phoneSurvey(ArrayList<Office> officeList, CUCM srccucm)
		{
		int officeCount = 0;
		ArrayList<BasicPhone> phoneList = new ArrayList<BasicPhone>();
		
		for(Office o : officeList)
			{
			phoneList.addAll(o.getPhoneList());
			officeCount++;
			}
		
		Variables.getLogger().debug(srccucm.getInfo()+" : Phone survey starts, sending RISRequest for "+officeCount+" offices and "+phoneList.size()+" phones");
		phoneList = RisportTools.doPhoneSurvey(srccucm, phoneList);
		
		//We now put the result back to each office
		for(Office o : officeList)
			{
			for(BasicPhone bp : o.getPhoneList())
				{
				boolean srcFound = false;
				for(BasicPhone risBP : phoneList)
					{
					if(bp.getName().equals(risBP.getName()))
						{
						srcFound = true;
						break;
						}
					}
				bp.setStatus(srcFound?PhoneStatus.registered:PhoneStatus.unregistered);
				}
			}
		Variables.getLogger().debug("Phone survey ends");
		}
	
	/**
	 * Used to write down the phone survey
	 * Useful to compare the phone status before and after the migration
	 */
	public static void writePhoneSurveyToCSV(ArrayList<Office> officeList)
		{
		try
			{
			Variables.getLogger().debug("Writing phone survey to a file");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String cr = "\r\n";
			SimpleDateFormat time = new SimpleDateFormat("HHmmss");
			Date date = new Date();
			String fileName = Variables.getPhoneSurveyFileName()+"_"+time.format(date);
			BufferedWriter csvBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+fileName+".csv"), false));
			
			//FirstLine
			csvBuffer.write("Device Pool"+splitter+"Device Name"+splitter+"Device Type"+splitter+"Status Before"+splitter+"Status After"+splitter+"Is OK"+cr);
			
			for(Office o : officeList)
				{
				for(BasicPhone bp : o.getPhoneList())
					{
					csvBuffer.write(o.getName()+splitter+bp.getName()+splitter+bp.getModel()+splitter+bp.getFirstStatus()+splitter+bp.getStatus()+splitter+bp.isOK()+cr);
					}
				}
			
			csvBuffer.flush();
			csvBuffer.close();
			Variables.getLogger().debug("Writing phone survey : Done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while writing phone survey to CSV : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Write the overall result to a csv file
	 */
	public static void writeOverallResultToCSV(ArrayList<Office> officeList)
		{
		try
			{
			Variables.getLogger().debug("Writing the overall result to a file");
			String splitter = UsefulMethod.getTargetOption("csvsplitter");
			String cr = "\r\n";
			SimpleDateFormat time = new SimpleDateFormat("HHmmss");
			Date date = new Date();
			String fileName = Variables.getOverallResultFileName()+"_"+time.format(date);
			BufferedWriter csvBuffer = new BufferedWriter(new FileWriter(new File(Variables.getMainDirectory()+"/"+fileName+".csv"), false));
			
			//FirstLine
			csvBuffer.write("Device Pool"+splitter+"Status"+cr);
			
			for(Office o : officeList)
				{
				csvBuffer.write(o.getName()+splitter+o.isDone()+cr);
				}
			
			csvBuffer.flush();
			csvBuffer.close();
			Variables.getLogger().debug("Writing overall result : Done !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while writing overall result to CSV : "+e.getMessage(),e);
			}
		}	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
