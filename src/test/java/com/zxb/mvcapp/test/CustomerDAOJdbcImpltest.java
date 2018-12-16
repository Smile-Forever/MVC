package com.zxb.mvcapp.test;

import java.util.List;

import org.junit.Test;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import com.zxb.mvcapp.dao.CriteriaCustomer;
import com.zxb.mvcapp.dao.CustomerDAO;
import com.zxb.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.zxb.mvcapp.domain.Customer;

public class CustomerDAOJdbcImpltest {
	
	private CustomerDAO customerDAO = new CustomerDAOJdbcImpl();
	
	@Test
	public void testGetForListWithCriteriaCustomer(){
		CriteriaCustomer cc = new CriteriaCustomer("С", null, null);
		List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(cc);
		System.out.println(customers);
	}
	
	@Test
	public void testGetAll() {
		List<Customer> customers = customerDAO.getAll();
		for (Customer customer : customers) {
			System.out.println(customer);
		}
	}

	@Test
	public void testSave() {
		Customer customer = new Customer();
		customer.setId(2);
		customer.setAddress("shanghei");
		customer.setName("Tom");
		customer.setPhone("18536869999");
		
		customerDAO.save(customer);
	}

	@Test
	public void testGetInteger() {
		Customer customer = customerDAO.get(1);
		System.out.println(customer);
	}

	@Test
	public void testDelete() {
		customerDAO.delete(1);
		
	}

	@Test
	public void testGetCountWithName() {
		long count = customerDAO.getCountWithName("Jerry");
		System.out.println(count);
	}

}
