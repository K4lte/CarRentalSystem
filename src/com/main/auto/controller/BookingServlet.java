package com.main.auto.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.main.auto.dao.daoInterfaces.CarDamageDAO;
import com.main.auto.dao.daoInterfaces.ClientDAO;
import com.main.auto.dao.daoInterfaces.CreditCardTypeDAO;
import com.main.auto.dao.daoInterfaces.OfficeDAO;
import com.main.auto.dao.daoInterfaces.PaymentDAO;
import com.main.auto.dao.daoInterfaces.PermissionDAO;
import com.main.auto.dao.daoInterfaces.ReservationDAO;
import com.main.auto.dao.daoInterfaces.ReservationStatusDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.model.Payment;
import com.main.auto.model.Reservation;
import com.main.auto.service.ReservationCart;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet(urlPatterns = {"/search",
							"/info",
							"/booking"})
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BookingServlet.class.getName());
       
    public BookingServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		case "/search":
			searchCar(request, response);
			break;
		case "/info":
			getInfo(request, response);
			break;
		case "/booking":
			selectPaymentType(request, response);
			break;
		default:
			response.sendRedirect("carSearchPage.html");
			break;
		}
	} // chooseAction()
	
	private void searchCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String errMsg = (String) request.getAttribute("errMsg");
		if(errMsg == null) {
		// location = office
			int selectedPickUpLocationId = Integer.valueOf(request.getParameter("pickUpLocation"));
			int selectedDropOffLocationId = Integer.valueOf(request.getParameter("dropOffLocation"));
			Date selectedPickUpDate = Date.valueOf(request.getParameter("pickUpDate"));
			Date selectedDropOffDate = Date.valueOf(request.getParameter("dropOffDate"));
			
			HttpSession session = request.getSession(false);
			if (session == null) {
			    session = request.getSession();
			} 		
			ReservationCart cart = (ReservationCart) session.getAttribute("cart");
	        if (cart == null) {
	        	cart = new ReservationCart();
	    		session.setAttribute("cart", cart);
	        }
			OfficeDAO officeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO();
			cart.setLocationPickUp(officeDAO.getById(selectedPickUpLocationId));
			cart.setLocationDropOff(officeDAO.getById(selectedDropOffLocationId));
			cart.setDatePickUp(selectedPickUpDate);
			cart.setDateDropOff(selectedDropOffDate);		
		} else {
			response.getWriter().write(errMsg);
		}
	}
	
	
	private void getInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
        // Convert Java object to JSON format and returned as JSON formatted String
		Gson gson = new Gson();
		String json = gson.toJson(cart);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
		cart.clearPaymentData();
	}	
	
	private void selectPaymentType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String errorMsg = (String) request.getAttribute("errMsg");		
		if(errorMsg == null) {				
			HttpSession session = request.getSession(false);
			ReservationCart cart = (ReservationCart) session.getAttribute("cart");
			
			if (cart.getDamage()!=null && cart.getReservation()!=null) {    	
		    	payDamage(request, response);
		    } else {
		    	if (cart.getCar() != null) {    	
		    		bookCar(request, response);
		    	}
		    }	
		} else {
			response.getWriter().write(errorMsg);
		}
	}
	
	private void payDamage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		
		Integer creditCardTypeId = Integer.valueOf(request.getParameter("creditCardType"));
		String creditCardNumber = request.getParameter("creditCardNumber");

		PaymentDAO paymentDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPaymentDAO();
		CreditCardTypeDAO cardTypeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCreditCardTypeDAO();		
		CarDamageDAO damageDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDamageDAO();
		ReservationDAO reservationDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO();
		ReservationStatusDAO statusDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();
		
		// Add new payment to Data Base
		Payment payment = new Payment();	
		payment.setCreditCardType(cardTypeDAO.getById(creditCardTypeId));
		payment.setCreditCardNumber(creditCardNumber);		
		payment.setTotalAmount(cart.getDamage().getAmount());
		payment.setReservation(cart.getReservation());
		payment.setId(paymentDAO.add(payment));
		cart.getDamage().setPayment(payment);
		damageDAO.edit(cart.getDamage());
		reservationDAO.changeStatus(statusDAO.getByStatus("damage paid").getId(), cart.getReservation().getId());		
	}

	
	private void bookCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int creditCardTypeId = Integer.valueOf(request.getParameter("creditCardType"));
		String creditCardNumber = request.getParameter("creditCardNumber");

		HttpSession httpSession = request.getSession(false);

		ClientDAO clientDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO();
		ReservationDAO reservationDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO();
		PaymentDAO paymentDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPaymentDAO();
		CreditCardTypeDAO cardTypeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCreditCardTypeDAO();
		ReservationStatusDAO statusDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();
		PermissionDAO permissionDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPermissionDAO();
		
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		if (cart.getClient().getPermission().getRole().equals("guest")) {
			// change permission from "guest" to "user"
			cart.getClient().setPermission(permissionDAO.getByPermissionRole("user"));
			int id = clientDAO.add(cart.getClient());
			cart.getClient().setId(id);
		}
		// Add new reservation to DataBase
		Reservation reservation = new Reservation();
		reservation.setUniqueNumber(cart.getClient().getLastName() + 11);
		reservation.setCar(cart.getCar());
		reservation.setClient(cart.getClient());
		reservation.setPickUpDate(cart.getDatePickUp());
		reservation.setReturnDate(cart.getDateDropOff());
		reservation.setPickUpLocation(cart.getLocationPickUp());
		reservation.setReturnLocation(cart.getLocationDropOff());
		reservation.setStatus(statusDAO.getByStatus("open"));
		reservation.setId(reservationDAO.add(reservation));
		
		// Add new payment to Data Base
		Payment payment = new Payment();	
		payment.setCreditCardType(cardTypeDAO.getById(creditCardTypeId));
		payment.setCreditCardNumber(creditCardNumber);		
		payment.setTotalAmount(cart.getTotalRental());
		payment.setReservation(reservation);
		paymentDAO.add(payment);
	
	}
	
}
