package com.main.auto.controller.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/1*")
public class LoginFilter implements Filter {
	
    public LoginFilter() {

    }

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		String path = httpRequest.getRequestURI();	  
		
		httpResponse.setCharacterEncoding("UTF-8");
		
		boolean isLoggedIn = (session != null && session.getAttribute("isAuthorized") != null);
		if (path.endsWith("/login") || path.endsWith("/login.html")) {
		    chain.doFilter(request, response); // Just continue chain if it's login.html or LoginServlet
		} else {
			 if (isLoggedIn) {
				// "no-cache" - Forces caches to obtain a new copy of the page from the origin server
				// "no-store" - Directs caches not to store the page under any circumstance
				// "must-revalidate" - A cache must not use the response to satisfy subsequent requests for this resource without successful validation on the origin server
				httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
				httpResponse.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
				httpResponse.setDateHeader("Expires", 0);  //Causes the proxy cache to see the page as "stale"
				chain.doFilter(request, response);  			        	
			 } else {
				 httpResponse.sendRedirect("login.html"); 
			 }
		}	            
	    
	} // doFilter()
					
} //LoginFilter
	