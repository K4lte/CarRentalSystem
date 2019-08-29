package com.main.auto.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.main.auto.dao.ClientDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.EmployeeUserDAO;
import com.main.auto.model.Client;
import com.main.auto.model.EmployeeUser;
import com.main.auto.service.ReservationCart;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = {"/login"})

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClientDAO clientDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO();
	private EmployeeUserDAO adminDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getEmployeeUserDAO();
	private Gson gson = new Gson();   
       
    public LoginServlet() {
        super();

    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			chooseAction(action, request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{
		
		switch (action) {
		case "/login":
			checkLogin(request, response);
			break;
		case "/is_authorized":
			isAuthorized(request, response);
			break;
		}
	} // chooseAction()
 
	private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get login and password from login.html
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		
		PrintWriter out=response.getWriter();
		
		HttpSession session = request.getSession();
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// "no-cache" - Forces caches to obtain a new copy of the page from the origin server
        // "no-store" - Directs caches not to store the page under any circumstance
        // "must-revalidate" - A cache must not use the response to satisfy subsequent requests for this resource without successful validation on the origin server
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        httpResponse.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
        httpResponse.setDateHeader("Expires", 0);  //Causes the proxy cache to see the page as "stale"
        
		// Check if user has entered login and password
		if(login!="" && password!="") {
			switch (role) {
			case "admin":
				EmployeeUser admin = adminDAO.login(login, password);
				if (admin != null) {					
					session.setAttribute("isAuthorized", true);
					session.setAttribute("isAdmin", true);
					response.getWriter().write("true");
				}
				break;			
			case "client":
				Client user = clientDAO.login(login, password);
				if (user != null) {
					session.setAttribute("isAuthorized", true);
		            session.setAttribute("isAdmin", false);
		            ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		            if (cart == null) {
		            	cart = new ReservationCart();
		            	session.setAttribute("cart", cart);
		            }
		            cart.setClient(user);
		            response.getWriter().write("true");
				} 
				break;
			default:
				response.getWriter().write("Either login or password is wrong");
			}		
		} else {
			response.getWriter().write("Please enter login and password");
		}
	} // checkLogin()

	private void isAuthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		boolean isAuthorized = (boolean) session.getAttribute("isAuthorized");
		boolean isAdmin = (boolean) session.getAttribute("isAdmin");
		
		if (isAuthorized && !isAdmin) {
			ReservationCart cart = (ReservationCart) session.getAttribute("cart");		
		    			
			JsonElement jsonElement = gson.toJsonTree(cart.getClient());		    
		    if (cart.getDamage()!=null && cart.getReservation()!=null) {    	
		    	jsonElement.getAsJsonObject().addProperty("carDamage", gson.toJson(cart.getDamage()));
		    } else {
		    	if (cart.getCar() != null) {    	
			    	jsonElement.getAsJsonObject().addProperty("car", gson.toJson(cart.getCar()));
		    	}
		    }		    
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(gson.toJson(jsonElement));
		}
		
	} // isAuthorized()
	
} // LoginServlet
