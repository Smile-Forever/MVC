package com.zxb.mvcapp.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxb.mvcapp.dao.CriteriaCustomer;
import com.zxb.mvcapp.dao.CustomerDAO;
import com.zxb.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.zxb.mvcapp.domain.Customer;



public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//����ӿ�ʵ��
	//private CustomerDAO customerDAO = new CustomerDAOXMLImpl();
	private CustomerDAO customerDAO = new CustomerDAOJdbcImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String method = request.getParameter("method");
//		
//		switch(method) {
//		case "add": add(request,response);
//			break;
//		case "query": query(request,response);
//			break;
//		case "delete": delete(request,response);
//			break;
//		case "update": update(request,response);
//			break;
//		}
//	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//1.��ȡservletPath��/edit.do �� /addCustomer.do
		String servletPath = request.getServletPath();
		//2.ȥ��/��.do  �õ�������edit addCustomer �������ַ���
		String methodName = servletPath.substring(1);
		methodName = methodName.substring(0,methodName.length() - 3);
		
			try {
				//3.���÷����ȡ methodName��Ӧ�ķ���
				Method method = getClass().getDeclaredMethod(methodName, 
						HttpServletRequest.class,
						HttpServletResponse.class);
				//4.���÷�����ö�Ӧ�ķ���
				method.invoke(this,request, response);
			} catch (Exception e) {
				//e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		
		
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String forwordPath = "/error.jsp";
		//1.��ȡ�������  id
		String idStr = request.getParameter("id");
		int id = -1;
		try {
			Customer customer = customerDAO.get(Integer.parseInt(idStr));
			if(forwordPath != null) {
				forwordPath = "/updatecustomer.jsp";
				request.setAttribute("customer", customer);
			}
		} catch (NumberFormatException e) {
			
		}
		
		request.getRequestDispatcher(forwordPath).forward(request, response);
		
	}
	
	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//1.��ȡ�������
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String oldName = request.getParameter("oldName");
				//2.����name�Ƿ�ռ��
				//2.1 �Ƚ�name��oldName�Ƿ���ͬ�� ����ͬ��˵��������
				if(!oldName.equalsIgnoreCase(name)) {
					//2.2 ������ͬ ������CustomerDAO��  getCountWithName(String name) ��ȡ��name�Ƿ������ݿ��д���
					//2.����CustomerDAO ��customerDAO.get(id)����
					long count = customerDAO.getCountWithName(name);
					//������ֵ����0 ����Ӧ updatecustomer.jspҳ��
					if(count > 0) {
						// Ҫ����newcustomer.jspҳ������ʾ������Ϣ��
						
						//2.2.2 newwcustomer.jsp�ı�ֵ�ɻ���
						//���Է�ʽ��value="<%= request.getAttribute("name") == null ? "" : request.getAttribute("name")  %>>
							
						
						//2.2.3 �������� return
						request.setAttribute("message", "�û���"+ name+"�Ѿ���ռ��,������ѡ��!");
						request.getRequestDispatcher("updatecustomer.jsp").forward(request, response);
						
						return;
					}
				}
				
				//��ͨ��
				//3.����������װ��һ��Customer ���� customer
						Customer customer = new Customer(name, address, phone);
						customer.setId(Integer.parseInt(id));
				//4.����customerDAO�� save(Customer customer)����
				customerDAO.update(customer);
				//5.�ض���query.doҳ��
				response.sendRedirect("query.do");
		
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idStr = request.getParameter("id");
		int id = 0;
		/*
		 * try-catch�����ã���ֹidStr������ת����int����
		 *  ������ת�� id=0 �޷������κε�ɾ������
		 */
		try {
		    id = Integer.parseInt(idStr);
			customerDAO.delete(id);
		} catch (Exception e) {
			
		}
		response.sendRedirect("query.do");
	}


	private void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//��ȡģ����ѯ���������
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		//�����������װ��CriteriaCustomer����
		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);
		
		//1.����CustomerDAO ��getAll()���� �õ�List����
		List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(cc);
		//��customer���Ϸ���request��
		request.setAttribute("customers", customers);
		//ת��ҳ�浽 index.jspҳ��
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}


	private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//1.��ȡ��������name address phone
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		//2.������ô�Ƿ�ս��
		//2.1����CustomerDAO��  getCountWithName(String name) ��ȡ��name�Ƿ������ݿ��д���
		long count = customerDAO.getCountWithName(name);
		//2.2 ������ֵ����0 ����Ӧ newcustomer.jspҳ��
		if(count > 0) {
			//2.2.1 Ҫ����newcustomer.jspҳ������ʾ������Ϣ��
			request.setAttribute("Message", "�û���"+name+"�Ѿ���ռ��,������ѡ��!");
			request.getRequestDispatcher("newcustomer.jsp").forward(request, response);
		
			return;
		}
		
		
		//2.2.2 newwcustomer.jsp�ı�ֵ�ɻ���
		//���Է�ʽ��value="<%= request.getAttribute("name") == null ? "" : request.getAttribute("name")  %>>
		
		//2.2.3 �������� return
		//3.����������װ��һ��Customer ���� customer
		Customer customer = new Customer(name ,address, phone);
		
		//4.����customerDAO�� save(Customer customer)����
		customerDAO.save(customer);
		//5.�ض���success.jspҳ��
		
		response.sendRedirect("success.jsp");
	}
}
