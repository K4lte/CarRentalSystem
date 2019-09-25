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

@WebFilter(urlPatterns = {"/clientReservationsPage.html",
					        "/reservation_client_monitoring",
					        "/reservation_client_warning",
					        "/damage_not_paid"})
public class ClientFilter implements Filter{

        public ClientFilter() {

        }

        public void destroy() {

        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession(false);

            boolean isAuthorized = (boolean) session.getAttribute("isAuthorized");
            boolean isAdmin = (boolean) session.getAttribute("isAdmin");

            if (isAuthorized && !isAdmin) {
                chain.doFilter(request, response); // Just continue chain if it's login.html or LoginServlet
            } else {
            	httpResponse.sendRedirect("carSearchPage.html");
            }

        } // doFilter()


}
