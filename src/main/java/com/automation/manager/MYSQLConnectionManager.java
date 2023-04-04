package com.automation.manager;

import com.automation.utils.StringUtils;
import com.automation.utils.UtilProperties;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MYSQLConnectionManager {
	DataSource ds = null;
	private static Map<String, MYSQLConnectionManager> instances = new HashMap<String, MYSQLConnectionManager>();

	public static MYSQLConnectionManager getInstance(String country) {
		MYSQLConnectionManager instance = null;
		if (instances.containsKey(country)) 
			instance = instances.get(country);
		else {
			instance = new MYSQLConnectionManager(country);
			instances.put(country, instance);
		}
		return instance;
	}

	private MYSQLConnectionManager(String country) {
		ds = getMySQLDataSource(country);
	}

	private DataSource getMySQLDataSource(String Country) {
		MysqlDataSource mysqlDS = new MysqlDataSource();
		mysqlDS.setURL(UtilProperties.getInstance().getProperty(Country + "_MYSQL_DB_URL"));
		mysqlDS.setUser(UtilProperties.getInstance().getProperty(Country + "_MYSQL_DB_USERNAME"));
		mysqlDS.setPassword(UtilProperties.getInstance().getProperty(Country + "_MYSQL_DB_PASSWORD"));
		return mysqlDS;
	}

	public List<String> getListData(String query, String Column) {
		List<String> Result = new ArrayList<String>();
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			if(UtilProperties.getInstance().getProperty(query) == null) {
			rs = stmt.executeQuery(query);
			}else {
			rs = stmt.executeQuery(UtilProperties.getInstance().getProperty(query));
			}

			while (rs.next()) {
				Result.add(StringUtils.trimValue(rs.getString(Column)));
			}

			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public JsonObject GetDBData(String Query) {
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			String Json = "{";
			con = ds.getConnection();
			stmt = con.createStatement();
			if (UtilProperties.getInstance().getProperty(Query) == null) {
				rs = stmt.executeQuery(Query);
			} else {
				rs = stmt.executeQuery(UtilProperties.getInstance().getProperty(Query));
			}
			while (rs.next()) {
				for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
					Json += "\"" + rs.getMetaData().getColumnName(i) + "\":\"" + rs.getString(i) + "\",";
				}
				Json = Json.substring(0, Json.length() - 1) + ",";
			}
			con.close();
			Json = Json.substring(0, Json.length() - 1) + "}";
			JsonParser parser = new JsonParser();
			JsonObject JsonOBJ = (JsonObject) parser.parse(Json);
			return JsonOBJ;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
