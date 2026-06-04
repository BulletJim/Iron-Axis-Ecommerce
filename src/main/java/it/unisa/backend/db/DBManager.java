package it.unisa.backend.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;

public final class DBManager {

	private static DataSource ds;
	
	static {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:comp/env");
			ds = (DataSource) envContext.lookup("jdbc/IronAxisDB");
			
		} catch (NamingException e) {
			System.err.println("Failed to load DataSource: " + e.getMessage());
		}
	}
	
	public static Connection getConnection() throws SQLException {
		if(ds == null) {
			throw new SQLException("Datasource not initialized");
		}
		return ds.getConnection();
	}
}
