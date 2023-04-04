package com.automation.manager;

import com.automation.mobile.MobileDriverProviderCreator;
import com.automation.tools.drivers.DriversResources;
import com.automation.utils.OsUtil;
import com.automation.utils.UtilProperties;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.jbehave.web.selenium.DelegatingWebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.HashMap;
import java.util.Map;

public class DriverProvider extends DelegatingWebDriverProvider {

	private static String browser;
	private DesiredCapabilities androidCapabilities = new DesiredCapabilities(JsonMap.GetCaps());
	private static Map<String, MobileDriverProviderCreator> mobileDrivers = new HashMap<String, MobileDriverProviderCreator>();

	public void initialize() {
		try {
			browser = UtilProperties.getInstance().getProperty("RunBrowsers");
		} catch (Exception e) {
			e.printStackTrace();
		}
		delegate.set(createDriver());
	}

	private WebDriver createDriver() {
		if (browser.equals("chrome")) {
			return createChromeDriver();
		}
		if (browser.equals("mobile")) {
			// return createMobileDriver();
			MobileDriverProviderCreator mobileDriver = new MobileDriverProviderCreator();
			mobileDriver.initializePortsAndUUIDs();
			// mobileDrivers.put(Thread.currentThread().getName(),
			// mobileDriver);
			return mobileDriver.getCurrentDriver();
		} else {

			return createFirefoxDriver();
		}
	}

	protected ChromeDriver createChromeDriver() {

		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + DriversResources.getDriverFilename());

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("test-type");
		options.addArguments("--ignore-ssl-errors=yes");
		options.addArguments("--ignore-certificate-errors");
		capabilities.setCapability("chrome.binary",
				System.getProperty("user.dir") + OsUtil.getCrossPlatformFileName(browser));
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
				
		
		return new ChromeDriver(capabilities);

	}

	public FirefoxDriver createFirefoxDriver() {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + OsUtil.getCrossPlatformFileName(browser));
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		capabilities.setCapability("webdriver.gecko.driver", System.getProperty("user.dir") + OsUtil.getCrossPlatformFileName(browser));
		return new FirefoxDriver(capabilities);
	}

	@SuppressWarnings({ "rawtypes" })
	private AppiumDriver createMobileDriver() {
		System.out.println(androidCapabilities);
		return new AndroidDriver(androidCapabilities);
	}

	public static MobileDriverProviderCreator getMbileDriver(String threadName) {
		return mobileDrivers.get(threadName);
	}

}