package com.leandroperez.taf.testutils;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import com.leandroperez.taf.sut.mobile.ios.*;
import com.leandroperez.taf.core.mobile.AppiumUtils;


import com.leandroperez.taf.core.Session;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


public class IOSBaseTest extends AppiumUtils{

	public IOSDriver driver;
	public AppiumDriverLocalService service;
	public HomePage homePage;

	@BeforeAll
	public void ConfigureAppium() throws IOException
	{
		Session session = new Session();
		
		Properties prop = new Properties();
		//FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//org//rahulshettyacademy//resources//data.properties");
				
		FileInputStream fis = new FileInputStream("//Users//leandroperez//Desktop/Automação//AppiumFramework2.0//AppiumFrameworkDesign//src//main//java//org//rahulshettyacademy//resources//data.properties");
		prop.load(fis);
		String ipAddress = prop.getProperty("ipAddress");
		String port = prop.getProperty("port");
			
		service = startAppiumServer(ipAddress,Integer.parseInt(port));
			
				XCUITestOptions	 options = new XCUITestOptions();	
				options.setDeviceName("iPhone 14");
				options.setApp("//Users//leandroperez//Desktop/Automação//AppiumFramework2.0//AppiumFrameworkDesign//src//main//java//org//rahulshettyacademy//resources//UIKitCatalog.app");
				//options.setApp("/Users/rahulshetty/Desktop/UIKitCatalog.app");
			//	options.setApp("//Users//rahulshetty//workingcode//Appium//src//test//java//resources//TestApp 3.app");
				options.setPlatformVersion("16.0");
				//Appium- Webdriver Agent -> IOS Apps.
				options.setWdaLaunchTimeout(Duration.ofSeconds(20));
				
			 driver = new IOSDriver(service.getUrl(), options);
			 driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			 homePage = new HomePage(driver);
			 
	}
	
	
	

	
	
	@AfterAll
	public void tearDown()
	{
		driver.quit();
        service.stop();
		}
	
}
