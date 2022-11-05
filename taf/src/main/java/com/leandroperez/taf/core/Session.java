package com.leandroperez.taf.core;

import com.sun.javafx.PlatformUtil;
import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.MobileElement;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.*;


@Setter
@Slf4j
public class Session {
    private WebDriver webDriver;
    private AppiumDriver appiumDriver;
    private AndroidDriver androidDriver;
    private IOSDriver iosDriver;
    private Boolean isMobile;
    private HashMap<String, String> customProperties = new HashMap<>();
    protected String prevBuild = "no";
    Reader reader;
    ClassLoader loader = this.getClass().getClassLoader();
    URL myUrlUITestProperties = loader.getResource("/taf/src/test/java/com/leandroperez/taf/config/uitest.properties");
    String path = myUrlUITestProperties.getPath();
    String decodedPath;

    {
        try {
            decodedPath = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            reader = new FileReader(decodedPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        try {
            prop.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.forEach((k, v) -> customProperties.put(k.toString(), v.toString()));
    }

    public void startSession() throws Exception {
        if (customProperties.get("executionTarget").isEmpty()) {
            throw new RuntimeException("Session configuration error, execution target is empty");
        } else if (customProperties.get("executionTarget").equals("LOCAL")) {
            startLocalSession();
        } else if (customProperties.get("executionTarget").equals("GRID")) {
            startGridSession();
        } else if (customProperties.get("executionTarget").equals("EXPERITEST")) {
            startExperiTestSession();
        }
    }

    private void startLocalSession() throws IOException {
        StringBuilder urlString = new StringBuilder();
        urlString.append(customProperties.get("hubAddress"));
        URL url = new URL(urlString.toString());
        DesiredCapabilities desiredCapabilities = this.getDesiredCapabilities();
        this.startRemoteSession(url, desiredCapabilities);
    }

    private void startRemoteSession(URL url, final DesiredCapabilities desiredCapabilities) throws IOException {
        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
                if (customProperties.get("defaultService").equals("no")) {
                    this.androidDriver = new AndroidDriver(url, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                } else {
                    //Runtime.getRuntime().exec("./command.txt");
                    executeCommandTxtFile();
                    AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
                    service.start();

                    this.androidDriver = new AndroidDriver(service, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                }
            } else if (Boolean.parseBoolean(customProperties.get("isIos"))) {
                if (customProperties.get("iosDefaultService").equals("no")) {
                    this.iosDriver = new IOSDriver(url, desiredCapabilities);
                    this.appiumDriver = this.iosDriver;
                } else {
                    File testLogFile = new File("./log.txt");
                    //Runtime.getRuntime().exec("./command.txt");
                    executeCommandTxtFile();
                    //AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();

                    /*
                    //Build the Appium service
                    AppiumServiceBuilder builder = new AppiumServiceBuilder();
                    builder.withIPAddress("127.0.0.1");
                    //127.0.0.1 is the  localhost normally resolves to the IPv4  127.0.0.1
                    builder.usingPort(4723); //Appium default port
                    builder.withCapabilities(desiredCapabilities);
                    builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
                    builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");

                    //Start the server with the builder
                    AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
                    //service.start();
                    System.out.println("Appium Server Started via Java");



                    //AppiumDriverLocalService service = new AppiumServiceBuilder().withLogFile(testLogFile).build();

                    service = new AppiumServiceBuilder()
                        .usingPort(4723)
                            .
                            .withCapabilities(desiredCapabilities)
                            .build();

                     */

                    AppiumDriverLocalService service = startAppiumServerOnSupportedOs();

                    //service.start();
                    this.iosDriver = new IOSDriver(service, desiredCapabilities);
                    this.appiumDriver = this.iosDriver;
                }
                this.webDriver = this.appiumDriver;
            } else {
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(url, desiredCapabilities);
                this.webDriver = remoteWebDriver;
                this.webDriver.manage().window().maximize();
            }
        }
    }

    private void startGridSession() {
        /* TODO implement startGridSession */
    }

    private void startExperiTestSession() throws IOException {
        StringBuilder urlString = new StringBuilder();
        urlString.append(customProperties.get("seeTestWdHub"));
        URL url = new URL(urlString.toString());
        DesiredCapabilities desiredCapabilities = this.getDesiredCapabilitiesSeeTest();
        this.startRemoteSession(url, desiredCapabilities);
    }

    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
            desiredCapabilities.setCapability("platformName", customProperties.get("platformName"));
            desiredCapabilities.setCapability("platform Version", customProperties.get("platformVersion"));
            desiredCapabilities.setCapability("deviceName", customProperties.get("deviceName"));
            desiredCapabilities.setCapability("noReset", customProperties.get("noReset"));
            desiredCapabilities.setCapability("appPackage", customProperties.get("appPackage"));
            desiredCapabilities.setCapability("appActivity", customProperties.get("appActivity"));
            desiredCapabilities.setCapability("automationName", customProperties.get("automationName"));
            if (prevBuild.equals("yes")) {
                desiredCapabilities.setCapability("app", customProperties.get("appOld"));
            } else {
                desiredCapabilities.setCapability("app", customProperties.get("app"));
            }
        }
        if (Boolean.parseBoolean(customProperties.get("isIos"))) {
            if (customProperties.get("localization").equals("yes")) {
                System.out.println("localization value " + customProperties.get("localization"));
                System.out.println("language value " + customProperties.get("appLanguage"));
                desiredCapabilities.setCapability("platformName", customProperties.get("iosPlatformName"));
                desiredCapabilities.setCapability("platformVersion", customProperties.get("iosPlatformVersion"));
                desiredCapabilities.setCapability("deviceName", customProperties.get("iosDeviceName"));
                desiredCapabilities.setCapability("app", customProperties.get("iosApp"));
                desiredCapabilities.setCapability("noReset", customProperties.get("iosNoReset"));
                desiredCapabilities.setCapability("automationName", customProperties.get("iosAutomationName"));
                desiredCapabilities.setCapability("udid", customProperties.get("iosUdid"));
                desiredCapabilities.setCapability("xcodeOrgId", customProperties.get("iosXcodeOrgId"));
                desiredCapabilities.setCapability("xcodeSigningId", customProperties.get("iosXcodeSigningId"));
                desiredCapabilities.setCapability("language", customProperties.get("appLanguage"));
                desiredCapabilities.setCapability("locale", customProperties.get("appLanguage"));
            } else {
                desiredCapabilities.setCapability("platformName", customProperties.get("iosPlatformName"));
                desiredCapabilities.setCapability("platformVersion", customProperties.get("iosPlatformVersion"));
                desiredCapabilities.setCapability("deviceName", customProperties.get("iosDeviceName"));
                desiredCapabilities.setCapability("app", customProperties.get("iosApp"));
                desiredCapabilities.setCapability("noReset", customProperties.get("iosNoReset"));
                desiredCapabilities.setCapability("automationName", customProperties.get("iosAutomationName"));
                desiredCapabilities.setCapability("udid", customProperties.get("iosUdid"));
                desiredCapabilities.setCapability("xcodeOrgId", customProperties.get("iosXcodeOrgId"));
                desiredCapabilities.setCapability("xcodeSigningId", customProperties.get("iosXcodeSigningId"));
                desiredCapabilities.setCapability("showIOSLog", customProperties.get("showIOSLog"));
            }
        }
        if (customProperties.get("browserName").equals("chrome")) {
            //ChromeOptions chromeOptions = getChromeOptions();
            //desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }
        if (customProperties.get("browserName").equals("firefox")) {
            //FirefoxOptions firefoxOptions = getFireFoxOptions();
            //desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, firefoxOptions);
        }
        return desiredCapabilities;
    }

    public DesiredCapabilities getDesiredCapabilitiesSeeTest() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
            desiredCapabilities.setCapability("accessKey", customProperties.get("accessKey"));
            desiredCapabilities.setCapability("testName", customProperties.get("testNameAndroid"));
            desiredCapabilities.setCapability("deviceQuery", customProperties.get("deviceQueryAndroid"));
            desiredCapabilities.setCapability(MobileCapabilityType.APP, customProperties.get("cloudAppAndroid"));
            desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, customProperties.get("appPackage"));
            desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, customProperties.get("appActivity"));
        }
        if (Boolean.parseBoolean(customProperties.get("isIos"))) {
            desiredCapabilities.setCapability("accessKey", customProperties.get("accessKey"));
            desiredCapabilities.setCapability("testName", customProperties.get("testNameIos"));
            desiredCapabilities.setCapability("deviceQuery", customProperties.get("deviceQueryIos"));
            desiredCapabilities.setCapability("appVersion", customProperties.get("appVersionIos"));
            desiredCapabilities.setCapability("platformName", customProperties.get("iosPlatformName"));
            desiredCapabilities.setCapability("newCommandTimeout", customProperties.get("newCommandTimeout"));
            desiredCapabilities.setCapability("automationName", customProperties.get("iosAutomationName"));
            desiredCapabilities.setCapability("app", customProperties.get("cloudAppIos"));
            desiredCapabilities.setCapability("bundleId", customProperties.get("iosBundleId"));
        }
        return desiredCapabilities;
    }

    public Boolean isMobile() {
        return isMobile;
    }

    private void setMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }

    public AppiumDriver getAppiumDriver() {
        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            return this.appiumDriver;
        }
        throw new IllegalArgumentException("Appium driver requested, but session is not configured to use an Appium driver");
    }

    public AndroidDriver getAndroidDriver() {
        if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
            return this.androidDriver;
        }
        throw new IllegalArgumentException("Android driver requested, but session is not configured to use an Android driver");
    }

    public IOSDriver getIosDriver() {
        if (Boolean.parseBoolean(customProperties.get("isIos"))) {
            return this.iosDriver;
        }
        throw new IllegalArgumentException("IOS driver requested, but session is not configured to use an iOS driver");
    }

    public WebDriver getWebDriver() {
        this.setMobile(Boolean.parseBoolean(customProperties.get("isMobile")));
        if (!isMobile()) {
            getWebDriver().manage().timeouts().pageLoadTimeout(30L, TimeUnit.SECONDS);
        }
        return this.webDriver;
    }
/*
    protected ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        return chromeOptions;
    }

    protected FirefoxOptions getFireFoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        return firefoxOptions;
    }
*/
    public HashMap<String, String> getCustomProperties() {
        return customProperties;
    }

    public void closeSession() {
        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            try {
                if (customProperties.get("isAndroid").equals("true")) {
                    //appiumDriver.resetApp();
                }/* else if (!appiumDriver.removeApp(customProperties.get("iosBundleId"))) {
                    //appiumDriver.resetApp();
                }*/
            } catch (Exception e) {
                log.error("Error closing App");
                e.printStackTrace();
            }

            try {
                appiumDriver.quit();
            } catch (Exception e) {
                log.error("Error closing session");
                e.printStackTrace();
            }
        } else {
            getWebDriver().quit();
        }

        //TODO verificar inclusão de código para service.stop() para fechar a sessão Appium
    }

    private void executeCommandTxtFile() {
        try {
            if (PlatformUtil.isWindows()) {
                Runtime.getRuntime().exec("./command.txt");
            } else if (PlatformUtil.isMac()) {
                Runtime.getRuntime().exec("chmod -R 777 " + "./command.txt");
            } else {
                Runtime.getRuntime().exec("./command.txt");
            }
        } catch (IOException e) {
            log.error("Error on execute executeCommandTxtFile()");
            e.printStackTrace();
        }
    }



    private AppiumDriverLocalService startAppiumServerOnSupportedOs(){
        AppiumDriverLocalService service = null;
        try {
            if (PlatformUtil.isWindows()) {
                //Runtime.getRuntime().exec("./command.txt");
            } else if (PlatformUtil.isMac()) {
                /*
                AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
                serviceBuilder.withIPAddress("http://127.0.0.1"); //Appium_ip_address
                serviceBuilder.usingPort(4723);
                String nodePath = "/usr/local/bin/node";
                String appiumPath = "/opt/homebrew/lib/node_modules/appium/build/lib/main.js";

                File fileNodePath = new File(FilenameUtils.normalize(nodePath));
                File fileAppiumPath = new File(FilenameUtils.normalize(appiumPath));

                //serviceBuilder.usingDriverExecutable(fileNodePath); //node path
                //serviceBuilder.withAppiumJS(fileAppiumPath); //appium path installed globally via npm. check path with npm root -g in macOS terminal

                service = AppiumDriverLocalService.buildService(serviceBuilder);

                service.isRunning();
                //service.start();
                */




                String nodePath = "/usr/local/bin/node";
                String appiumPath = "/opt/homebrew/lib/node_modules/appium/build/lib/main.js";

                File fileNodePath = new File(FilenameUtils.normalize(nodePath));
                File fileAppiumPath = new File(FilenameUtils.normalize(appiumPath));


                AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
                serviceBuilder.withIPAddress("http://127.0.0.1"); //Appium_ip_address
                serviceBuilder.usingPort(4723);

                //serviceBuilder.usingDriverExecutable(fileNodePath); //node path
                //serviceBuilder.withAppiumJS(fileAppiumPath); //appium path installed globally via npm. check path with npm root -g in macOS terminal

                service = new AppiumServiceBuilder()
                        .withAppiumJS(new File("/usr/local/bin/node"))
                        .withIPAddress("127.0.0.1")
                        .usingPort(4723)
                        .build();

                service.isRunning();
            } else {
                //Runtime.getRuntime().exec("./command.txt");
            }
        } catch (Exception e) {
            log.error("Error on execute startAppiumServer()");
            e.printStackTrace();
        }
        return service;
    }

}