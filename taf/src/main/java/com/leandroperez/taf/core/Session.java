package com.leandroperez.taf.core;

import com.leandroperez.taf.core.enumerator.PlatformInTest;
import com.leandroperez.taf.core.enumerator.TestExecutionStrategy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


@Setter
@Slf4j
public class Session {
    private String PROJECT_ROOT_PATH = System.getProperty("user.dir");
    private String UITEST_PROPERTIES_FILE_PATH = PROJECT_ROOT_PATH + "/src/test/java/com/leandroperez/taf/config/uitest.properties";
    Path uiTestPropertiesPath = Paths.get(UITEST_PROPERTIES_FILE_PATH);
    private WebDriver webDriver;
    private AppiumDriver appiumDriver;
    private AndroidDriver androidDriver;
    private IOSDriver iosDriver;
    PlatformInTest currentPlatformInTest = null;
    private HashMap<String, String> customProperties = new HashMap<>();
    protected String prevBuild = "no";
    Reader reader;
    String decodedPath;
    {
        try {
            decodedPath = URLDecoder.decode(uiTestPropertiesPath.toString(), "UTF-8");
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
        loadOnlyCustomPropertiesNotNoneAndNotNull(prop);
    }

    public void loadAllCustomProperties(Properties propLoaded){
        propLoaded.forEach((k, v) -> customProperties.put(k.toString(), v.toString()));
    }

    public void loadOnlyCustomPropertiesNotNoneAndNotNull(Properties propLoaded){
        propLoaded.forEach((k, v) -> {
            if (v != null && !"none".equalsIgnoreCase(v.toString()) && !"null".equalsIgnoreCase(v.toString())) {
                customProperties.put(k.toString(), v.toString());
                System.out.println("key: " + k.toString() + "value: " + v.toString());
            }
        });
    }

    public void startSession(PlatformInTest platformInTest, TestExecutionStrategy testExecutionStrategy) throws Exception {
        if (testExecutionStrategy == null) {
            throw new RuntimeException("Session configuration error, testExecutionStrategy is null");
        }
        if (platformInTest == null) {
            throw new RuntimeException("Session configuration error, platformInTest is null");
        }

        currentPlatformInTest = platformInTest;

        switch (testExecutionStrategy) {
            case LOCAL:
                System.out.println("Starting LOCAL session");
                startLocalSession(platformInTest);
                break;
            case GRID:
                System.out.println("Starting GRID session");
                startGridSession();
                break;
            case REMOTE:
                System.out.println("Starting REMOTE session");
                startRemoteSession(getURLFromConfiguratedLocalMobileHubAddressProperty(), getConfiguratedMobileDesiredCapabilities(), platformInTest);
                break;
        }
    }

    private void startDefaultLocalSession(PlatformInTest platformInTest) {
        try {
            if (platformInTest == PlatformInTest.WEB) {
                //todo
            } else if (platformInTest == PlatformInTest.ANDROID) {
                executeRuntimeCommandTxtFile();
                AppiumDriverLocalService service = getAppiumDriverDefaultLocalService();
                service.start();
                this.androidDriver = new AndroidDriver(service, getConfiguratedMobileDesiredCapabilities());
                this.appiumDriver = this.androidDriver;
            } else if (platformInTest == PlatformInTest.IOS) {
                executeRuntimeCommandTxtFile();
                AppiumDriverLocalService service = getAppiumDriverDefaultLocalService();
                this.iosDriver = new IOSDriver(service, getConfiguratedMobileDesiredCapabilities());
                this.appiumDriver = this.iosDriver;
            } else {
                throw new IllegalArgumentException("Error: platformInTest is invalid");
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getEnclosingMethod().getDeclaringClass().getEnclosingMethod().getName()
                    + "\n" + "Error: " + e);
        }
    }

    private void startLocalSession(PlatformInTest platformInTest) {
        if (platformInTest == null) {
            throw new RuntimeException("Session configuration error, platformInTest is null");
        }
        try {
            StringBuilder urlString = new StringBuilder();
            switch (platformInTest) {
                case WEB:
                    urlString.append(customProperties.get("localWebHubAddress"));
                    break;
                case ANDROID:
                    executeRuntimeCommandTxtFile();
                    AppiumDriverLocalService serviceAndroid = getAppiumDriverDefaultLocalService();
                    serviceAndroid.start();
                    this.androidDriver = new AndroidDriver(serviceAndroid, getConfiguratedMobileDesiredCapabilities());
                    this.appiumDriver = this.androidDriver;
                    break;
                case IOS:
                    executeRuntimeCommandTxtFile();
                    AppiumDriverLocalService serviceIOS = getAppiumDriverDefaultLocalService();
                    serviceIOS.start();
                    this.iosDriver = new IOSDriver(serviceIOS, getConfiguratedMobileDesiredCapabilities());
                    this.appiumDriver = this.iosDriver;
                    break;
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getEnclosingMethod().getDeclaringClass().getEnclosingMethod().getName()
                    + "\n" + "Error: " + e);
        }
    }


    private void startRemoteSession(URL url, final DesiredCapabilities desiredCapabilities, PlatformInTest platformInTest) {
        if (url == null || desiredCapabilities == null || platformInTest == null) {
            throw new RuntimeException("Session configuration error.");
        }
        try {
            StringBuilder urlString = new StringBuilder();
            switch (platformInTest) {
                case WEB:
                    urlString.append(customProperties.get("localWebHubAddress"));
                    RemoteWebDriver remoteWebDriver = new RemoteWebDriver(url, desiredCapabilities);
                    this.webDriver = remoteWebDriver;
                    this.webDriver.manage().window().maximize();
                    break;
                case ANDROID:
                    this.androidDriver = new AndroidDriver(url, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                    break;
                case IOS:
                    this.iosDriver = new IOSDriver(url, desiredCapabilities);
                    this.appiumDriver = this.iosDriver;
                    break;
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getEnclosingMethod().getDeclaringClass().getEnclosingMethod().getName()
                    + "\n" + "Error: " + e);
        }




        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            if (platformInTest == PlatformInTest.ANDROID) {
                if (customProperties.get("useRemoteServiceUrlForAndroid").equals("yes")) {
                    this.androidDriver = new AndroidDriver(url, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                } else {
                    executeRuntimeCommandTxtFile();
                    AppiumDriverLocalService service = getAppiumDriverDefaultLocalService();
                    service.start();

                    this.androidDriver = new AndroidDriver(service, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                }
            } else if (platformInTest == PlatformInTest.IOS) {
                if (customProperties.get("useRemoteServiceUrlForiOS").equals("yes")) {
                    this.iosDriver = new IOSDriver(url, desiredCapabilities);
                    this.appiumDriver = this.iosDriver;
                } else {
                    executeRuntimeCommandTxtFile();
                    AppiumDriverLocalService service = getAppiumDriverDefaultLocalService();


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

    private URL getURLFromConfiguratedLocalMobileHubAddressProperty() {
        StringBuilder urlString = new StringBuilder();
        URL url = null;
        try {
            urlString.append(customProperties.get("localMobileHubAddress"));
            url = new URL(urlString.toString());
        } catch (MalformedURLException mue) {
            System.out.println(this.getClass().getEnclosingMethod().getDeclaringClass().getEnclosingMethod().getName()
                    + "\n" + "Error: " + mue);
        }
        return url;
    }


    private static AppiumDriverLocalService getAppiumDriverDefaultLocalService() {
        AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
        return service;
    }

    private static AppiumDriverLocalService getAppiumDriverDefaultLocalServiceWithLog(File testLogFilePath) {
        if (testLogFilePath == null)
            testLogFilePath = new File("./log/appium_log.txt");
        AppiumDriverLocalService service = new AppiumServiceBuilder().withLogFile(testLogFilePath).build();
        service.start();
        return service;
    }

    private void startGridSession() {
        /* TODO implement startGridSession */
    }

 private boolean isNotNullOrNoneValue(String string){
     return !(string != null && !"none".equalsIgnoreCase(string));
 }



    public DesiredCapabilities getConfiguratedMobileDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (isAndroid()) {
            String platformName = customProperties.get("platformName");
            String automationName = customProperties.get("automationName");
            String platformVersion = customProperties.get("platformVersion");
            String deviceName = customProperties.get("deviceName");
            String androidNoReset = customProperties.get("androidNoReset");
            String appPackage = customProperties.get("appPackage");
            String appActivity = customProperties.get("appActivity");

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("platformName", platformName);
            }

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("appium:automationName", automationName);
            }

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("appium:platformVersion", platformVersion);
            }

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("appium:deviceName", deviceName);
            }

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("appium:noReset", androidNoReset);
            }

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("appium:appPackage", appPackage);
            }

            if (isNotNullOrNoneValue(platformName)){
                desiredCapabilities.setCapability("appium:appActivity", appActivity);
            }



/*
            desiredCapabilities.setCapability("platformName", customProperties.get("platformName"));
            desiredCapabilities.setCapability("appium:automationName", customProperties.get("automationName"));
            desiredCapabilities.setCapability("appium:platformVersion", customProperties.get("platformVersion"));
            desiredCapabilities.setCapability("appium:deviceName", customProperties.get("deviceName"));
            desiredCapabilities.setCapability("appium:noReset", customProperties.get("androidNoReset"));
            desiredCapabilities.setCapability("appium:appPackage", customProperties.get("appPackage"));
            desiredCapabilities.setCapability("appium:appActivity", customProperties.get("appActivity"));

            */

        }
        if (isIOS()) {
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



    public DesiredCapabilities getConfiguratedMobileDesiredCapabilitiesOLD() {
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
            desiredCapabilities.setCapability("app", customProperties.get("cloudAppAndroid"));
            desiredCapabilities.setCapability("appPackage", customProperties.get("appPackage"));
            desiredCapabilities.setCapability("appActivity", customProperties.get("appActivity"));
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
        return (appiumDriver != null || androidDriver != null || iosDriver != null);
    }

    public Boolean isAndroid() {
        return (currentPlatformInTest != null && currentPlatformInTest == PlatformInTest.ANDROID);
    }

    public Boolean isIOS() {
        return (currentPlatformInTest != null && currentPlatformInTest == PlatformInTest.IOS);
    }

    public Boolean isWeb() {
        return (currentPlatformInTest != null && currentPlatformInTest == PlatformInTest.WEB);
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
        return this.webDriver;
    }

    public WebDriver setWebDriverTimeout(long time, TimeUnit timeUnit) {
        if(webDriver != null)
            getWebDriver().manage().timeouts().pageLoadTimeout(time, timeUnit);
        return this.webDriver;
    }



    public void quitWebDriverSession() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    public void quitMobileDriverSession() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            androidDriver = null;
            iosDriver = null;
        }
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

    private void executeRuntimeCommandTxtFile() {
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                Runtime.getRuntime().exec("./command.txt");
            } else if (SystemUtils.IS_OS_MAC) {
                Runtime.getRuntime().exec("chmod -R 777 " + "./command.txt");
            } else {
                Runtime.getRuntime().exec("./command.txt");
            }
        } catch (IOException e) {
            log.error("Error on execute executeCommandTxtFile()");
            e.printStackTrace();
        }
    }


    private AppiumDriverLocalService startAppiumServerOnSupportedOsWithCustomBuilder() {
        AppiumDriverLocalService service = null;
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                //Runtime.getRuntime().exec("./command.txt");
            } else if (SystemUtils.IS_OS_MAC) {
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