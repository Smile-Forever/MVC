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
	 * ���غ� name ��ȵļ�¼��
	 * @param name
	 * @return
	 */
	public long getCountWithName(String name);
	
	
	/**
	 *  ���ط���������List
	 * @param cc ��װ�˲�ѯ����
	 * @return
	 */
	List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc);
	
}
