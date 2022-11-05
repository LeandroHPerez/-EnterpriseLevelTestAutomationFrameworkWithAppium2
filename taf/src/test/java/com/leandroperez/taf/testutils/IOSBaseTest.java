package com.leandroperez.taf.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import com.leandroperez.taf.pageobjects.mobile.ios.*;
import com.leandroperez.taf.core.mobile.AppiumUtils;


import com.google.common.collect.ImmutableMap;
import com.leandroperez.taf.core.Session;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import org.junit.*;


public class IOSBaseTest extends AppiumUtils{

	public IOSDriver driver;
	public AppiumDriverLocalService service;
	public HomePage homePage;
	
	@BeforeClass
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
	
	
	

	
	
	@AfterClass
	public void tearDown()
	{
		driver.quit();
        service.stop();
		}
	
}
