package com.zxb.mvcapp.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.zxb.mvcapp.utils.JdbcUtils;

public class JdbcUtilsTest {
	@Test
	public void testGetConnection() throws SQLException {
		Connection connection = JdbcUtils.getConn();
		System.out.println(connection);
	}

}
