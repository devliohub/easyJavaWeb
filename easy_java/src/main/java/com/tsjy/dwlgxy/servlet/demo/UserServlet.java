package com.tsjy.dwlgxy.servlet.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tsjy.dwlgxy.bean.*;

@WebServlet(urlPatterns = "/demo/user")
public class UserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Menu school = new Menu("No.1 Middle School", "101 North Street");
//		UserAdmin user = new UserAdmin(123, "Bob", school);
//		req.setAttribute("user", user);
		req.getRequestDispatcher("/WEB-INF/demo/user.jsp").forward(req, resp);
	}
}
