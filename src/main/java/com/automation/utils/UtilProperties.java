package com.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

public class UtilProperties {
	private Properties defaultProps = new Properties();
	private Properties appProps = null;


	private static Object lock = new Object();
	private static UtilProperties instance = null;

	private UtilProperties() {
	}

	public static UtilProperties getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new UtilProperties();
					try {
						instance.loadProperties();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return (instance);
	}

	private void loadProperties() throws IOException {

		List<String> files = new ArrayList<String>();
		String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator
				+ "resources" + File.separator + "Configurations";
		File directory = new File(path);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile() && FilenameUtils.getExtension(file.getPath()).equals("properties")) {
				files.add(file.getPath());

				// create and load default properties
				FileInputStream in = new FileInputStream(file.getAbsolutePath());
				defaultProps.load(in);
				in.close();

				// create application properties with default
				appProps = new Properties(defaultProps);

				try {
					// user/application properties
					in = new FileInputStream(file.getAbsolutePath());
					appProps.load(in);
					in.close();
				} catch (Throwable th) {
					// TODO: log something
				}
			}
		}
	}

	public String getProperty(String key) {
		String val = null;
		if (key != null) {
			if (appProps != null)
				val = (String) appProps.getProperty(key);
			if (val == null) {
				val = defaultProps.getProperty(key);
			}
		}
		return (val);

	}

	public Integer getInteger(String key) {
		return Integer.parseInt(getProperty(key));
	}

}
