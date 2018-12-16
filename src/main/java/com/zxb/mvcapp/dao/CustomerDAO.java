package com.zxb.mvcapp.dao;

import java.util.List;

import com.zxb.mvcapp.domain.Customer;

public interface CustomerDAO {
	
	public List<Customer> getAll();
	
	public void save(Customer customer);
	
	public Customer get(Integer id);
	
	public void delete(Integer id);
	
	public void update(Customer customer);
	
	/**
	 * 返回和 name 相等的记录数
	 * @param name
	 * @return
	 */
	public long getCountWithName(String name);
	
	
	/**
	 *  返回符合条件的List
	 * @param cc 封装了查询条件
	 * @return
	 */
	List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc);
	
}
