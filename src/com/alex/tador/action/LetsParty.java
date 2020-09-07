package com.alex.tador.action;


import com.alex.tador.axl.AXLTools;
import com.alex.tador.misc.BasicPhone;
import com.alex.tador.misc.Office;
import com.alex.tador.misc.OfficeTools;
import com.alex.tador.utils.UsefulMethod;
import com.alex.tador.utils.Variables;

/**
 * Class aiming to start everything needed
 *
 * @author Alexandre RATEL
 */
public class LetsParty extends Thread
	{
	/**
	 * Variables
	 */
	
	
	/**
	 * Constructor
	 */
	public LetsParty()
		{
		start();
		}
	
	public void run()
		{
		try
			{
			Variables.getLogger().debug("###Start processing###");
			/**
			 * 1st : Initial phone survey
			 */
			//First we get the device pool phone list
			Variables.getLogger().debug("1st : Fetch the device list");
			for(Office o : Variables.getOfficeList())
				{
				o.setPhoneList(OfficeTools.getDevicePoolPhoneList(o.getName(), Variables.getSrccucm()));
				}
			Variables.getLogger().debug("1st : Done !");
			
			//Second we trigger a phone survey
			OfficeTools.phoneSurvey(Variables.getOfficeList(), Variables.getSrccucm());
			
			/**
			 * 2nd : Configure the new load name
			 */
			Variables.getLogger().debug("2nd : Configuring the new phone load");
			for(Office o : Variables.getOfficeList())
				{
				for(BasicPhone bp : o.getPhoneList())
					{
					try
						{
						AXLTools.updatePhoneLoad(bp.getName(), UsefulMethod.getFirmware(bp.getModel()), Variables.getSrccucm());
						}
					catch (Exception e)
						{
						Variables.getLogger().error("ERROR while updating phone load for the following device : "+bp.getName()+" : "+bp.getModel()+" : "+e.getMessage());
						}
					}
				}
			Variables.getLogger().debug("2nd : Done !");
			
			/**
			 * 3rd : Reset device pool per device pool
			 * 
			 * Here we do as follow :
			 * - If the device pool is too big, we split it and reset device per device
			 * - If the device pool is under the max phone allowed, we reset the whole device pool
			 */
			Variables.getLogger().debug("3rd : resetting devices");
			
			int count = 0;
			int maxPhone = Integer.parseInt(UsefulMethod.getTargetOption("maxphonereset"));
			int timeBetweenreset = Integer.parseInt(UsefulMethod.getTargetOption("timebetweenreset"));
			
			for(Office o : Variables.getOfficeList())
				{
				if(o.getPhoneList().size() > maxPhone)
					{
					Variables.getLogger().debug("3rd : The device pool is too big so we reset the device "+maxPhone+" by "+maxPhone+" : "+o.getName()+" "+o.getPhoneList().size());
					int phoneCount = 0;
					for(BasicPhone bp : o.getPhoneList())
						{
						if(phoneCount < maxPhone)
							{
							phoneCount = 0;
							Variables.getLogger().debug("3rd : Waiting a bit");
							this.sleep(timeBetweenreset*1000);
							}
						phoneCount++;
						try
							{
							AXLTools.resetDevice(bp.getName(), Variables.getSrccucm());
							}
						catch (Exception e)
							{
							Variables.getLogger().error("ERROR while reseting the following device : "+bp.getName()+" : "+bp.getModel()+" : "+e.getMessage());
							}
						}
					Variables.getLogger().debug("3rd : finished to reset the device pool's device : "+o.getName());
					o.setDone(true);
					}
				else
					{
					count += o.getPhoneList().size();
					if(count > maxPhone)
						{
						count = 0;
						Variables.getLogger().debug("3rd : We reach the max phone limit : Waiting a bit");
						this.sleep(timeBetweenreset*1000);
						}
					try
						{
						AXLTools.resetDevicePool(o.getName(), Variables.getSrccucm());
						o.setDone(true);
						Variables.getLogger().debug("3rd : finished to reset the device pool : "+o.getName());
						}
					catch (Exception e)
						{
						Variables.getLogger().error("ERROR while reseting the following device pool : "+o.getName()+" : "+e.getMessage());
						}
					}
				}
			Variables.getLogger().debug("3rd : Done !");
			
			/**
			 * 4th : Survey for X minutes and then write down the report
			 */
			Variables.getLogger().debug("4th : Waiting a few minutes");
			int timeToWait = Integer.parseInt(UsefulMethod.getTargetOption("timetowait"));
			timeToWait = timeToWait*60*1000;
			//this.sleep(timeToWait);
			this.sleep(5000);
			Variables.getLogger().debug("4th : Writing phone survey");
			OfficeTools.writePhoneSurveyToCSV(Variables.getOfficeList());
			Variables.getLogger().debug("4th : Writing final survey");
			OfficeTools.writeOverallResultToCSV(Variables.getOfficeList());
			Variables.getLogger().debug("4th : Done !");
			
			Variables.getLogger().debug("###End processing###");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(),e);
			}
		}
	
	
	/*2020*//*RATEL Alexandre 8)*/
	}
