package com.main.auto.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.main.auto.service.ReservationCart;

@WebServlet(urlPatterns = {"/payment_price"})
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;  
	
    public PaymentServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getPaymentPrice(request, response);
	}
	
	private void getPaymentPrice(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");		
		BigDecimal price = cart.getPrice();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(price.toString());		
	} // getPaymentPrice()
	
} // PaymentServlet
