package com.main.auto.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logout(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	// Method to logout
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		HttpSession session = request.getSession(false);
		// Checks whether the requested session ID is still valid
		if (request.isRequestedSessionIdValid() && session != null) {
			session.removeAttribute("isAuthorized");
			session.invalidate();
		}
		// Edit the cookie information and make session empty to avoid same cookie ID each time a person logs in
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0); // Changing the maximum age to 0 seconds 
			cookie.setValue(null);
			cookie.setPath("/");
			response.addCookie(cookie); // Adding cookie in the response 
		}
	    response.sendRedirect("login.html");
	} // logout()
} // logoutServlet