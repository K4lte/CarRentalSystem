package com.main.auto.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/adminPage.html",
							"/damage_save",
					        "/reservation_status_change",
					        "/reservation_monitoring",
					        "/reservation_status", 
							"/reservation_status_matched"})
public class AdminFilter implements Filter{

        public AdminFilter() {

        }

        public void destroy() {

        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession(false);

            boolean isAuthorized = (boolean) session.getAttribute("isAuthorized");
            boolean isAdmin = (boolean) session.getAttribute("isAdmin");

            if (isAuthorized && isAdmin) {
                chain.doFilter(request, response); // Just continue chain 
            } else {
            	httpResponse.sendRedirect("carSearchPage.html");
            }
        } // doFilter()


}
