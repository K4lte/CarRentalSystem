package com.main.auto.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.main.auto.service.ConfigurationManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.ClientDAO;
import com.main.auto.dao.daoInterfaces.EmployeeUserDAO;
import com.main.auto.model.Client;
import com.main.auto.model.EmployeeUser;
import com.main.auto.service.ReservationCart;
import com.main.auto.service.Util;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = {"/login",
							"/isLoggedUser",
							"/getLoggedInCient"})

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
	private ClientDAO clientDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO();
	private EmployeeUserDAO adminDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getEmployeeUserDAO();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();   
       
    public LoginServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			chooseAction(action, request, response);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Exception: ", e);
		}
	}

	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{		
		switch (action) {
		case "/login":
			checkLogin(request, response);
			break;
		case "/isLoggedUser":
			isLoggedUser(request, response);
			break;
		case "/getLoggedInCient":
			getLoggedInCient(request, response);
			break;
		}
	} // chooseAction()
 
	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String role = request.getParameter("role");	
		String errMsg = (String) request.getAttribute("errMsg");
		
		HttpSession session = request.getSession(false);
		if (session == null) {
		    session = request.getSession();
		} 		
		// Check if user has entered login and password
		if(errMsg == null) {
			switch (role) {
			case "admin":
				EmployeeUser admin = adminDAO.login(login, password);
				if (admin.getPermission() != null) {					
					session.setAttribute("isAuthorized", true);
					session.setAttribute("isAdmin", true);
					response.getWriter().write("true");
				} else {
					invalidLogPass(request, response);
				}
				break;			
			case "client":
				Client user = clientDAO.login(login, password);
				if (user.getPermission() != null) {
					session.setAttribute("isAuthorized", true);
		            session.setAttribute("isAdmin", false);
		            ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		            if (cart == null) {
		            	cart = new ReservationCart();
		            	session.setAttribute("cart", cart);
		            }
		            cart.setClient(user);
		            response.getWriter().write("true");
				} else {
					invalidLogPass(request, response);
				}
				break;
			}		
		} else {
			response.getWriter().write(errMsg);
		}
	} // checkLogin()

	private void isLoggedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String user = null;
		HttpSession session = request.getSession(false);
		if (session == null) {
		    session = request.getSession();
		    user = "none";
		} else {
		    // Already created			
			Boolean isAuthorized = (Boolean) session.getAttribute("isAuthorized");
			Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
			if (isAuthorized != null && isAdmin != null) {
				if (isAuthorized && !isAdmin) {
					user = "client";
				} else {
					if (isAuthorized && isAdmin) {
						user = "admin";
				}
					
				} 
			} else {
				user = "none";
			}
		}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(user);
	} // isLoggedUser()
	
	private void getLoggedInCient(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String client = gson.toJson(cart.getClient());
		response.getWriter().write(client);
	} //getLoggedInCient()

	private void invalidLogPass(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String locale = Util.getLocale(request);
		String property = locale.equals("ru") ? "errMsg_locale_ru" : "errMsg_locale_en";
		response.getWriter().write(ConfigurationManager.getProperty(property, "loginForm"));
	}

} // LoginServlet
