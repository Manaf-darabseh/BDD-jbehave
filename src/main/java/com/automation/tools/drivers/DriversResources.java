package com.automation.tools.drivers;

import com.automation.utils.OsUtil;
import com.automation.utils.UtilProperties;

public class DriversResources {
	public static String Browser;

	/**
	 * 
	 * @return
	 */
	public static String getDriverFilename()

	{
		Browser = UtilProperties.getInstance().getProperty("RunBrowsers");
		if (Browser.equals("chrome")) {
			return OsUtil.getCrossPlatformFileName("chromedriver");

		} else {
			return "";

		}

	}
}
