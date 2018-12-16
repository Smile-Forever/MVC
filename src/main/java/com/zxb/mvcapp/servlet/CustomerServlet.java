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
	//面向接口实现
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
		//1.获取servletPath：/edit.do 或 /addCustomer.do
		String servletPath = request.getServletPath();
		//2.去除/和.do  得到类似与edit addCustomer 这样的字符串
		String methodName = servletPath.substring(1);
		methodName = methodName.substring(0,methodName.length() - 3);
		
			try {
				//3.利用反射获取 methodName对应的方法
				Method method = getClass().getDeclaredMethod(methodName, 
						HttpServletRequest.class,
						HttpServletResponse.class);
				//4.利用反射调用对应的方法
				method.invoke(this,request, response);
			} catch (Exception e) {
				//e.printStackTrace();
				response.sendRedirect("error.jsp");
			}
		
		
	}
	
	private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String forwordPath = "/error.jsp";
		//1.获取请求参数  id
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
		//1.获取请求参数
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String oldName = request.getParameter("oldName");
				//2.检验name是否被占用
				//2.1 比较name和oldName是否相同， 若相同则说明不可用
				if(!oldName.equalsIgnoreCase(name)) {
					//2.2 若不相同 ，调用CustomerDAO的  getCountWithName(String name) 获取的name是否在数据库中存在
					//2.调用CustomerDAO 的customerDAO.get(id)方法
					long count = customerDAO.getCountWithName(name);
					//若返回值大于0 则响应 updatecustomer.jsp页面
					if(count > 0) {
						// 要求在newcustomer.jsp页面上显示错误信息，
						
						//2.2.2 newwcustomer.jsp的表单值可回显
						//回显方式：value="<%= request.getAttribute("name") == null ? "" : request.getAttribute("name")  %>>
							
						
						//2.2.3 结束方法 return
						request.setAttribute("message", "用户名"+ name+"已经被占用,请重新选择!");
						request.getRequestDispatcher("updatecustomer.jsp").forward(request, response);
						
						return;
					}
				}
				
				//若通过
				//3.将表单参数封装成一个Customer 对象 customer
						Customer customer = new Customer(name, address, phone);
						customer.setId(Integer.parseInt(id));
				//4.调用customerDAO的 save(Customer customer)方法
				customerDAO.update(customer);
				//5.重定向到query.do页面
				response.sendRedirect("query.do");
		
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idStr = request.getParameter("id");
		int id = 0;
		/*
		 * try-catch的作用：防止idStr并不能转换成int类型
		 *  若不能转则 id=0 无法进行任何的删除操作
		 */
		try {
		    id = Integer.parseInt(idStr);
			customerDAO.delete(id);
		} catch (Exception e) {
			
		}
		response.sendRedirect("query.do");
	}


	private void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获取模糊查询的请求参数
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		//把请求参数封装成CriteriaCustomer对象
		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);
		
		//1.调用CustomerDAO 的getAll()方法 得到List集合
		List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(cc);
		//把customer集合放入request中
		request.setAttribute("customers", customers);
		//转发页面到 index.jsp页面
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}


	private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//1.获取表单参数：name address phone
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		//2.检验那么是否被战用
		//2.1调用CustomerDAO的  getCountWithName(String name) 获取的name是否在数据库中存在
		long count = customerDAO.getCountWithName(name);
		//2.2 若返回值大于0 则响应 newcustomer.jsp页面
		if(count > 0) {
			//2.2.1 要求在newcustomer.jsp页面上显示错误信息，
			request.setAttribute("Message", "用户名"+name+"已经被占用,请重新选择!");
			request.getRequestDispatcher("newcustomer.jsp").forward(request, response);
		
			return;
		}
		
		
		//2.2.2 newwcustomer.jsp的表单值可回显
		//回显方式：value="<%= request.getAttribute("name") == null ? "" : request.getAttribute("name")  %>>
		
		//2.2.3 结束方法 return
		//3.将表单参数封装成一个Customer 对象 customer
		Customer customer = new Customer(name ,address, phone);
		
		//4.调用customerDAO的 save(Customer customer)方法
		customerDAO.save(customer);
		//5.重定向到success.jsp页面
		
		response.sendRedirect("success.jsp");
	}
}
