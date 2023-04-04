package com.automation.mobile;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import com.automation.manager.JsonMap;
import com.automation.utils.UtilProperties;

public class MobileDriverProviderCreator{
	@SuppressWarnings("rawtypes")
	private static  Hashtable<String, AppiumDriver> drivers = new Hashtable<String, AppiumDriver>();
	private static  Hashtable<String, serverInfo> servers = new Hashtable<String, serverInfo>();
	public  String ResetApp = "true";
	public static  Hashtable<String, String> sessions = new Hashtable<String, String>();
	public static  ArrayList<String> appiumPortsList = new ArrayList<String>();
	public static  ArrayList<String> udid = new ArrayList<String>();
	private  DesiredCapabilities androidCapabilities = new DesiredCapabilities(JsonMap.GetCaps());


	public enum platform {
		ANDROID, IOS
	}
	
	/**
	 * Added to prevent constructing new objects.
	 */
	
	public static class serverInfo {
		public int serverPort;
		public String deviceUUID;
	}

	public static String jobName = null;

	public static platform getPlatform() {
		boolean android = true;
		return android ? platform.ANDROID : platform.IOS;
	}
	
	public void initializePortsAndUUIDs() {
		initializePorts();
		initializeUdids();
	}

	public static void initializePorts() {
		Integer initialPort = 4733;
		int threads = 0;
		try {
			threads = Integer.parseInt(UtilProperties.getInstance().getProperty("threads"));
		} catch (Exception ex) {
			threads = 1;
		}
		for (int i = 0; i < threads; i++) {
			appiumPortsList.add(initialPort.toString());
			initialPort += 10;
		}
	}

	public static void initializeUdids() {

		String udids = UtilProperties.getInstance().getProperty("udid");
		if (udids.contains(",")) {
			udid.addAll(asList(udids.split(",")));
		} else {
			udid.add(udids);
		}

	}

	@SuppressWarnings("rawtypes")
	public AppiumDriver getCurrentDriver() {
		String threadName = Thread.currentThread().getName();
		if (!drivers.containsKey(threadName) || (drivers.get(threadName) == null)) {
		}

		try {
			String sessionId = drivers.get(threadName).getSessionId().toString();
			if (sessionId.isEmpty()) {

				SetupDriver(threadName);
			}
			sessions.put(threadName, sessionId);
		} catch (Exception e) {
			try {
				SetupDriver(threadName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String sessionId = drivers.get(threadName).getSessionId().toString();
			sessions.put(threadName, sessionId);
		}
		return drivers.get(threadName);
	}

	public static serverInfo getCurrentServerInfo(String threadName) {
		serverInfo currentServer = new serverInfo();
		if (servers.get(threadName) != null) {
			currentServer = servers.get(threadName);
		} else {
			serverInfo server = new serverInfo();

			servers.put(threadName, server);
			currentServer = server;
			// servers.ad
		}
		return currentServer;
	}

	@SuppressWarnings({ "rawtypes"})
	public void SetupDriver(String threadName) throws IOException {
		serverInfo currentServer = getCurrentServerInfo(threadName);
		AppiumDriver driver = null;
		String userDir = System.getProperty("user.dir");
		// Setup capabilities

		DesiredCapabilities capabilities;

		if (getPlatform() == platform.ANDROID) {
//			capabilities = DesiredCapabilities.android();
//			capabilities.setCapability("platformVersion", "6.0");
//			capabilities.setCapability("deviceName", "Nexus_6_API_23");
//			capabilities.setCapability("app", userDir+"/app/DriverPal_UNO_1.9.5_STG.apk");
//			capabilities.setCapability("appPackage", "com.qexpress.driverpal");
//			capabilities.setCapability("waitForAppScript", "$.delay(8000); $.acceptAlert();");
//			capabilities.setCapability("autoAcceptAlerts", "true");
//			capabilities.setCapability("autoGrantPermissions", "true");
//			capabilities.setCapability("noResetValue","false");
			capabilities = androidCapabilities;
			capabilities.setCapability("udid",currentServer.deviceUUID);
		} else {
			capabilities = DesiredCapabilities.iphone();

		}
		
		URL serverAddress = new URL("http://0.0.0.0:4723/wd/hub");

		if (getPlatform() == platform.ANDROID) {
			driver = new AndroidDriver(capabilities);
		} else {
			driver = new IOSDriver(serverAddress, capabilities);
		}


		drivers.put(threadName, driver);
		System.out.println("Driver thread name:-----" + threadName + "---- and session id---"
				+ driver.getSessionId().toString() + ", UUID:" + currentServer.deviceUUID);
	}

	@SuppressWarnings("rawtypes")
	public AppiumDriver AndroidDriver(URL serverAddress ,DesiredCapabilities capabilities) {
		return new AndroidDriver(serverAddress ,capabilities);
	}

	@SuppressWarnings("rawtypes")
	public AppiumDriver IOSDriver(URL serverAddress, DesiredCapabilities capabilities) {
		return new IOSDriver(serverAddress, capabilities);
	}

	@SuppressWarnings("rawtypes")
	public void closeDrivers() {
		for (AppiumDriver driver : drivers.values()) {
			if (driver != null) {
				driver.quit();
				driver = null;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void closeCurrentDriver() throws IOException {
		// get the thread name
		try {
			String ThreadName = Thread.currentThread().getName();
			// get the driver name
			AppiumDriver driver = drivers.get(ThreadName);
			if (driver != null) {
				// driver.resetApp();

				if (UtilProperties.getInstance().getProperty("closeDriver").contains("true")) {
					try {
						driver.quit();
						System.out.println("closing: " + driver.getRemoteAddress().getPort() + ":"
								+ driver.getCapabilities().getCapability("udid"));

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					drivers.put(ThreadName, driver);
				} else {
					driver.resetApp();
					System.out.println("reset: " + driver.getRemoteAddress().getPort() + ":"
							+ driver.getCapabilities().getCapability("udid"));

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	
}
