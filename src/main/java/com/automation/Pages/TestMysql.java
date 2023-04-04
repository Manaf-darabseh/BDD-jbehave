package com.automation.Pages;

import java.util.List;

import com.automation.manager.MYSQLConnectionManager;
import com.automation.utils.UtilProperties;
import com.google.gson.JsonObject;


public class TestMysql {

	public static void main(String[] args) {
			testDBCPDataSource();
	}

	private static void testDBCPDataSource() {
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData("EG_SHIPMENT_REQUEST", "TRACKING_CODE");
		System.out.println(r);
		 r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData("AE_SHIPMENT_REQUEST", "TRACKING_CODE");
		System.out.println(r);
		JsonObject x = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).GetDBData("EG_SHIPMENT_REQUEST");
		System.out.println(x.get("TRACKING_CODE"));
	}

}
