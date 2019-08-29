package com.main.auto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.PaymentDAO;
import com.main.auto.model.Client;
import com.main.auto.model.Payment;
import com.main.auto.service.ReservationCart;

@WebServlet(urlPatterns = {"/payment", 
							"/payment_save",
							"/payment_edit",
							"/payment_delete",
							"/payment_price"})
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PaymentDAO paymentDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPaymentDAO();
	private Gson gson = new Gson();   
	
    public PaymentServlet() {
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

	// Method for selecting action depending on urlPatterns
	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{		
		switch (action) {
		case "/payment_save":
			saveEntity(request, response);
			break;		
		case "/payment_edit":			
			editEntity(request, response);
			break;
		case "/payment_delete":			
			deleteEntity(request, response);
			break;	
		case "/payment_price":
			getPaymentPrice(request, response);
			break;	
		default:
			showList(request, response);
			break;
		}
	} //chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Payment> list = paymentDAO.getAll();
		// Convert Java object to JSON format and returned as JSON formatted String
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()
	
	
	private void saveEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		Payment entity = getEntity(request);
	    	paymentDAO.add(entity);
	} //saveEntity()
	
	// Method to edit data in the database
	private void editEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {		
		Payment entity = getEntity(request);	
		paymentDAO.edit(entity);
	} //editEntity()

	// Method to delete data from the database
	private void deleteEntity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		Payment entity = getEntity(request);
		paymentDAO.delete(entity.getId());
	} //deleteEntity()
	
	
	// Method to convert JSON back to Java object
	private Payment getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		Payment entity = gson.fromJson(json, Payment.class) ;
		return entity;
	} //getEntity()
	
	private void getPaymentPrice(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		BigDecimal price = cart.getPrice();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(price.toString());		
	} // getPaymentPrice()
	
} // PaymentServlet
