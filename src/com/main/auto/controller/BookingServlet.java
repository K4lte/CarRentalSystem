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
import com.main.auto.dao.CarDamageDAO;
import com.main.auto.dao.CityDAO;
import com.main.auto.dao.ClientDAO;
import com.main.auto.dao.ReservationStatusDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.OfficeDAO;
import com.main.auto.dao.PaymentDAO;
import com.main.auto.dao.PermissionDAO;
import com.main.auto.dao.CreditCardTypeDAO;
import com.main.auto.dao.ReservationDAO;
import com.main.auto.model.Payment;
import com.main.auto.model.Reservation;
import com.main.auto.service.ReservationCart;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet(urlPatterns = {"/search",
							"/info",
							"/booking"})
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BookingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			chooseAction(action, request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
			response.sendRedirect("1.html");
			break;
		}
	} // chooseAction()
	
	private void searchCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// location = office
		int selectedPickUpLocationId = Integer.valueOf(request.getParameter("pickUpLocation"));
		int selectedDropOffLocationId = Integer.valueOf(request.getParameter("dropOffLocation"));
		Date selectedPickUpDate = Date.valueOf(request.getParameter("pickUpDate"));
		Date selectedDropOffDate = Date.valueOf(request.getParameter("dropOffDate"));
		
		HttpSession httpSession = request.getSession();
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
        if (cart == null) {
        	cart = new ReservationCart();
    		httpSession.setAttribute("cart", cart);
        }
		OfficeDAO officeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO();
		cart.setLocationPickUp(officeDAO.getById(selectedPickUpLocationId));
		cart.setLocationDropOff(officeDAO.getById(selectedDropOffLocationId));
		cart.setDatePickUp(selectedPickUpDate);
		cart.setDateDropOff(selectedDropOffDate);		
		response.sendRedirect("2.html");
	}
	
	
	private void getInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession httpSession = request.getSession();
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
        // Convert Java object to JSON format and returned as JSON formatted String
		Gson gson = new Gson();
		String json = gson.toJson(cart);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);	
	}	
	
	private void selectPaymentType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession httpSession = request.getSession();
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		
		if (cart.getDamage()!=null && cart.getReservation()!=null) {    	
	    	payDamage(request, response);
	    } else {
	    	if (cart.getCar() != null) {    	
	    		bookCar(request, response);
	    	}
	    }		    
	}
	
	private void payDamage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession httpSession = request.getSession();
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		
		Integer creditCardTypeId = Integer.valueOf(request.getParameter("creditCardType"));
		String creditCardNumber = request.getParameter("creditCardNumber");

		PaymentDAO paymentDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPaymentDAO();
		CreditCardTypeDAO cardTypeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCreditCardTypeDAO();		
		CarDamageDAO damageDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDamageDAO();
			
		// Add new payment to Data Base
		Payment payment = new Payment();	
		payment.setCreditCardType(cardTypeDAO.getById(creditCardTypeId));
		payment.setCreditCardNumber(creditCardNumber);		
		payment.setTotalAmount(cart.getDamage().getAmount());
		payment.setReservation(cart.getReservation());
		payment.setId(paymentDAO.add(payment));
		cart.getDamage().setPayment(payment);
		damageDAO.edit(cart.getDamage());
		httpSession.removeAttribute("cart");
		response.sendRedirect("7.html");	
		
	}

	
	private void bookCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer creditCardTypeId = Integer.valueOf(request.getParameter("creditCardType"));
		String creditCardNumber = request.getParameter("creditCardNumber");

		HttpSession httpSession = request.getSession();

		ClientDAO clientDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO();
		ReservationDAO reservationDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO();
		PaymentDAO paymentDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPaymentDAO();
		CreditCardTypeDAO cardTypeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCreditCardTypeDAO();
		ReservationStatusDAO statusDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();
		PermissionDAO permissionDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getPermissionDAO();
		
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		if (cart.getClient().getPermission().getRole().equals("guest")) {
			// change permission to 3 (user)
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
		
		httpSession.removeAttribute("cart");
		response.sendRedirect("5.html");		
	}
	
}
