package com.automation.steps;

import java.io.IOException;
import java.util.Set;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriverException;

import com.automation.mobile.MobileDriverProviderCreator;
import com.automation.utils.UtilProperties;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class LifecycleSteps {
	MobileDriverProviderCreator mobiledriverProvider;
	private final WebDriverProvider webDriverProvider;

	
	public LifecycleSteps(WebDriverProvider webDriverProvider, MobileDriverProviderCreator mobiledriverProvider ) {
		this.webDriverProvider = webDriverProvider;
		this.mobiledriverProvider = mobiledriverProvider;
	}

	public static AppiumDriverLocalService Service;

	public void startServer() throws IOException {

		mobiledriverProvider.initializePortsAndUUIDs();

		for (int i = 0; i < MobileDriverProviderCreator.appiumPortsList.size(); i++) {

			String bootStrapPort = String.valueOf(mobiledriverProvider);

			Service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().withIPAddress("127.0.0.1")
					.withArgument(GeneralServerFlag.LOG_LEVEL, "error")
					.withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, bootStrapPort)
					.withArgument(GeneralServerFlag.SESSION_OVERRIDE));

			Service.start();
		}
	}

	@BeforeStories
	public void runBeforeAllStories() throws IOException {
		
	}

	@BeforeStory
	public void runBeforeEachStory() throws IOException {

	}

	@AfterStory
	public void runAfterEachStory() {
		try {
			// do something
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
	}

	@AfterStories
	public void runAfterStories() throws IOException {
		try {
			// driverProvider.closeDrivers();
			Runtime.getRuntime().exec("Taskkill /IM chromedriver.exe /F");
			Runtime.getRuntime().exec("Taskkill /IM firefox.exe /F");
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
	}

	@BeforeScenario
	public void BeforeScenario() {

	}

	@AfterScenario
	public void runAfterEachScenario() throws IOException {
		if (!UtilProperties.getInstance().getProperty("RunBrowsers").equals("mobile")) {
			deleteAllCookies();			
		} else {
			mobiledriverProvider.closeCurrentDriver();

		}
	}

	public void deleteAllCookies() {
		try {
			System.out.println("\nDeleting all the cookies!");
			Set<Cookie> cookies = webDriverProvider.get().manage().getCookies();
			for (Cookie c : cookies) {
				System.out.println("\tCookie: " + c.getName());
			}
			webDriverProvider.get().manage().deleteAllCookies();
		} catch (WebDriverException e) {
			e.printStackTrace();
		}
	}

}
