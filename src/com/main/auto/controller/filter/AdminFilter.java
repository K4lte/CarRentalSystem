package com.main.auto.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/6.html",
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
            String path = httpRequest.getRequestURI();

            httpResponse.setCharacterEncoding("UTF-8");

            boolean isAuthorized = (boolean) session.getAttribute("isAuthorized");
            boolean isAdmin = (boolean) session.getAttribute("isAdmin");

            if (isAuthorized && isAdmin) {
                chain.doFilter(request, response); // Just continue chain if it's login.html or LoginServlet
            } else {

         /*           // "no-cache" - Forces caches to obtain a new copy of the page from the origin server
                    // "no-store" - Directs caches not to store the page under any circumstance
                    // "must-revalidate" - A cache must not use the response to satisfy subsequent requests for this resource without successful validation on the origin server
                    httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    httpResponse.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
                    httpResponse.setDateHeader("Expires", 0);  //Causes the proxy cache to see the page as "stale"
                    chain.doFilter(request, response);*/

                    httpResponse.sendRedirect("1.html");
                }


        } // doFilter()


}
