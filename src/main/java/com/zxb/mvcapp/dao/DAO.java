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
 * ��װ��CRUD�Ļ����������Թ�����̳�ʹ��
 * ��ǰDAOֱ���ڷ����л�ȡ���ݿ�����
 * ����DAO��ȡ DBUtils�������
 * @author Smile
 *
 * @param <T>��ǰDAO�����ʵ�����������ʲô
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
	 * ����ĳһ�ֶε�ֵ �����緵��һ����¼customerName���򷵻����ݱ��еĶ�������¼
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
	 * ���� T ����Ӧ��list
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
	 * ���ض�Ӧ�� T ��һ��ʵ����Ķ���
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
	 * �÷�����װ��insert,delete,save����
	 * @param sql sql���
	 * @param args ���sql����ռλ��
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
