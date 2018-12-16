package com.zxb.mvcapp.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zxb.mvcapp.domain.Customer;
import com.zxb.mvcapp.utils.JdbcUtils;

/**
 * 封装了CRUD的基本方法，以供子类继承使用
 * 当前DAO直接在方法中获取数据库连接
 * 整个DAO采取 DBUtils解决方案
 * @author Smile
 *
 * @param <T>当前DAO处理的实体类的类型是什么
 */
public class DAO<T> {
	
	private QueryRunner queryRunner = new QueryRunner();
	
	private Class<T> clazz;
	
	public DAO() {
		
		Type superClass = getClass().getGenericSuperclass();
		
		if(superClass instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) superClass;
			
			Type[] typeArgs = parameterizedType.getActualTypeArguments();
			if(typeArgs != null && typeArgs.length > 0) {
				if(typeArgs[0] instanceof Class) {
					clazz = (Class<T>) typeArgs[0];
				}
			}
		}
	}
	
	/**
	 * 返回某一字段的值 ，例如返回一条记录customerName，或返回数据表中的多少条记录
	 * @param sql
	 * @param args
	 * @return
	 */
	public <E> E getForValue(String sql , Object ...args) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConn();
			return (E) queryRunner.query(conn, sql, new ScalarHandler(),args);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(conn, stat, rs);
		}
		return null;
	}
	
	/**
	 * 返回 T 所对应的list
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<T> getForList(String sql , Object ...args){
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConn();
			return queryRunner.query(conn, sql, new BeanListHandler<>(clazz),args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(conn, stat, rs);
		}
		return null;
	}
	/**
	 * 返回对应的 T 的一个实例类的对象
	 * @param sql
	 * @param args
	 * @return
	 */
	public T get(String sql , Object ...args) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConn();
			return queryRunner.query(conn, sql, new BeanHandler<>(clazz),args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(conn, stat, rs);
		}
		return null;
	}
	
	/**
	 * 该方法封装了insert,delete,save操作
	 * @param sql sql语句
	 * @param args 填充sql语句的占位符
	 */
	public void upadte(String sql , Object ...args){
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConn();
			queryRunner.update(conn,sql,args);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(conn, stat, rs);
		}
	}

}
