package com.main.auto.controller.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {
	
    public LoginFilter() {

    }

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// "no-cache" - Forces caches to obtain a new copy of the page from the origin server
		// "no-store" - Directs caches not to store the page under any circumstance
		// "must-revalidate" - A cache must not use the response to satisfy subsequent requests for this resource without successful validation on the origin server
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		httpResponse.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
		httpResponse.setDateHeader("Expires", 0);  //Causes the proxy cache to see the page as "stale"
		//httpResponse.setContentType("text/html; charset=windows-1251");
//		httpResponse.setContentType("text/html");
//		httpResponse.setCharacterEncoding("UTF-8");
		
		chain.doFilter(request, response); 

	    
	} // doFilter()
					
} //LoginFilter
	