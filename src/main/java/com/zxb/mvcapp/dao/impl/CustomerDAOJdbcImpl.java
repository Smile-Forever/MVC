package com.zxb.mvcapp.dao.impl;

import java.util.List;

import com.zxb.mvcapp.dao.CriteriaCustomer;
import com.zxb.mvcapp.dao.CustomerDAO;
import com.zxb.mvcapp.dao.DAO;
import com.zxb.mvcapp.domain.Customer;

public class CustomerDAOJdbcImpl extends DAO<Customer> implements CustomerDAO{

	public List<Customer> getAll() {
		String sql = "select * from customers";
		return getForList(sql);
	}

	public void save(Customer customer) {
		String sql = "insert into customers(name,address,phone) values(?,?,?)";
		upadte(sql, customer.getName(),customer.getAddress(),customer.getPhone());
	}

	public Customer get(Integer id) {
		String sql = "select * from customers where id=?";
		return get(sql,id);
	}

	public void delete(Integer id) {
		String sql = "delete from customers where id=?";
		upadte(sql, id);
	}

	public long getCountWithName(String name) {
		String sql = "select count(id) from customers where name=?";
		return getForValue(sql, name);
	}

	@Override
	public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc) {
		String sql = "select * from customers "
				+ "where name like ? and address like ? and phone like ?";
		//Ìî³äÕ¼Î»·û¼¼ÇÉ£º
		return getForList(sql , cc.getName(),cc.getAddress() ,cc.getPhone());
	}

	@Override
	public void update(Customer customer) {
		String sql = "update customers set name = ? , address = ? , phone = ? where id = ?";
		upadte(sql, customer.getName(), customer.getAddress(), customer.getPhone() , customer.getId());
	}
}
