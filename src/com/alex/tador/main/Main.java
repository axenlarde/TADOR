package com.alex.tador.main;

import org.apache.log4j.Level;

import com.alex.tador.action.LetsParty;
import com.alex.tador.utils.InitLogging;
import com.alex.tador.utils.UsefulMethod;
import com.alex.tador.utils.Variables;


/**
 * CAMITO main class
 *
 * @author Alexandre RATEL
 */
public class Main
	{
	
	public Main()
		{
		//Set the software name
		Variables.setSoftwareName("TADOR");
		//Set the software version
		Variables.setSoftwareVersion("1.0");
		
		/****************
		 * Initialization of the logging
		 */
		Variables.setLogFileName(Variables.getSoftwareName()+"_LogFile.txt");
		Variables.setLogger(InitLogging.init(Variables.getLogFileName()));
		Variables.getLogger().info("\r\n");
		Variables.getLogger().info("#### Entering application");
		Variables.getLogger().info("## Welcome to : "+Variables.getSoftwareName()+" version "+Variables.getSoftwareVersion());
		Variables.getLogger().info("## Author : RATEL Alexandre : 2020");
		/*******/
		
		/******
		 * Initialization of the variables
		 */
		new Variables();
		/************/
		
		/**********
		 * We check if the java version is compatible
		 */
		UsefulMethod.checkJavaVersion();
		/***************/
		
		/**********************
		 * Reading of the configuration files
		 */
		try
			{
			/**
			 * Config file reading
			 */
			Variables.setTabConfig(UsefulMethod.readMainConfigFile(Variables.getConfigFileName()));
			
			/**
			 * Database file reading
			 */
			UsefulMethod.initDatabase();
			
			/***
			 * Server connection initialization
			 * 
			 * We do it now to avoid latency during real request
			 */
			UsefulMethod.initCUCM();
			}
		catch(Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/********************/
		
		/*****************
		 * Setting of the inside variables from what we read in the configuration file
		 */
		try
			{
			UsefulMethod.initInternalVariables();
			}
		catch(Exception exc)
			{
			Variables.getLogger().error(exc.getMessage());
			Variables.getLogger().setLevel(Level.INFO);
			}
		/*********************/
		
		/*******************
		 * Start main user interface
		 */
		try
			{
			Variables.getLogger().info("Launching Main Class");
			new LetsParty();//main file
			}
		catch (Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/******************/
		
		//End of the main class
		}
	
	
	

	public static void main(String[] args)
		{
		new Main();
		}
	/*2020*//*RATEL Alexandre 8)*/
	}
