package com.excilys.cdb.dao;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.persistence.DaoManager;

public class TestDao {
	public static IDatabaseTester databaseTester;
	public static String jdbcDriver;
	public static String jdbcUrl;
	public static String user;
	public static String password;
	private static final String DB_TEST_PROPERTIES = "db-test.properties";
	
	static {
		final Properties properties = new Properties();
		try (final InputStream is = DaoManager.class
				.getClassLoader().getResourceAsStream(DB_TEST_PROPERTIES)) {
			properties.load(is);
			jdbcDriver = properties.getProperty("driver");
			jdbcUrl = properties.getProperty("url");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void cleanlyInsert(IDataSet dataSet) throws Exception {
		databaseTester = new JdbcDatabaseTester(
				jdbcDriver, jdbcUrl, user, password);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}
	
	@BeforeClass
	public static void createSchema() throws Exception {
		RunScript.execute(jdbcUrl, user, password, "src/test/resources/schema.sql", StandardCharsets.UTF_8, false);
	}
	
	@Before
	public void importDataSet() throws Exception {
		IDataSet dataSet = readDataSet();
		cleanlyInsert(dataSet);
	}
	
	private IDataSet readDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new File("src/test/resources/dbTest.xml"));
	}
	
	/**
	 * Execute the sql file.
	 * @param file File to execute
	 * @param connection Connection
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static void executeSqlFile(String file, Connection connection) throws IOException, SQLException {
		InputStream is = DaoManager.class
				.getClassLoader().getResourceAsStream("test.sql");
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str + "\n ");
		}
		try (final Statement stmt = connection.createStatement()) {
			stmt.execute(sb.toString());
		}
	}
	
	@Test
	public void findsAndReadsExistingComputerById() throws Exception {
		fail();
	}

	
	public static Connection getConnection() throws IOException, SQLException {
		final Properties properties = new Properties();
		try (final InputStream is = DaoManager.class
				.getClassLoader().getResourceAsStream(DB_TEST_PROPERTIES)) {
			properties.load(is);
			return DriverManager.getConnection(jdbcUrl, properties);
		}
	}
}
