package com.automation.utils;

public class OsUtil {
	static String Browser = UtilProperties.getInstance().getProperty("RunBrowsers");
	private final static String RESOURCES_FOLDER = "/webdrivers";
	private static String myOsFolderName; // to support cross platform, we put
											// platform specific files in a
											// folder names win,nx,or mac. This
											// would hold teh folder name for
											// us, and is initiallized at
											// statrtup
	private static String myExecExtension;

	// this would be called at initial time
	// the code bellow would initialize our os-specific values
	static {
		String OS = System.getProperty("os.name");
		if (OS == null) {

			myOsFolderName = "error";
			myExecExtension = "";

		}

		if (OS.startsWith("Mac")) {
			myOsFolderName = "mac";
			myExecExtension = "";

		} else if (OS.startsWith("Win")) {
			myOsFolderName = "win";
			myExecExtension = ".exe";

		} else {
			// we assume our code would only run on win, mac or linux ONLY
			myOsFolderName = "nx";
		}
	}

	/**
	 * we have a convention that all cross platform files are put in a folder
	 * called resources, and a platform-specific version of each file is put in
	 * each filder
	 * 
	 * @param shortFilename
	 */
	public static String getCrossPlatformFileName(String shortFilename) {
		StringBuilder builder = new StringBuilder();
		builder.append(RESOURCES_FOLDER);
		builder.append("/");
		builder.append(Browser);
		builder.append("/");
		builder.append(myOsFolderName);
		builder.append("/");
		builder.append(shortFilename);
		builder.append(myExecExtension); // myExecExtension should include the .
		return builder.toString();
	}

}
