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
import com.main.auto.dao.CarDAO;
import com.main.auto.dao.CarDamageDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.DamageTypeDAO;
import com.main.auto.dao.ReservationDAO;
import com.main.auto.dao.ReservationStatusDAO;
import com.main.auto.model.CarDamage;
import com.main.auto.model.Reservation;
import com.main.auto.service.ReservationCart;

@WebServlet(urlPatterns = {"/damage", 
							"/damage_delete", 
							"/damage_edit", 
							"/damage_save",
							"/damage_not_paid"})
public class CarDamageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CarDamageDAO carDamageDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDamageDAO();
	private Gson gson = new Gson();
       
    public CarDamageServlet() {
        super();
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

	// Method for selecting action depending on urlPatterns
	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{		
		switch (action) {
		case "/damage_save":
			saveEntity(request, response);
			break;
		case "/damage_edit":
			editEntity(request, response);
			break;
		case "/damage_delete":
			deleteEntity(request, response);
			break;	
		case "/damage_not_paid":
			getDamageNotPaid(request, response);
			break;	
		default:
			showList(request, response);
			break;
		}
	} //chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<CarDamage> list = carDamageDAO.getAll();
        // Convert Java object to JSON format and returned as JSON formatted String
     	
     	String json = gson.toJson(list);
     	response.setCharacterEncoding("UTF-8");
     	response.getWriter().write(json);
    } //showList()
	
	// Method to convert JSON back to Java object
	private CarDamage getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		CarDamage entity = gson.fromJson(json, CarDamage.class) ;
		return entity;
	} //getEntity()
	
	// Method to save data to the database
	private void saveEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		String carNumber = request.getParameter("vin");
		int damageTypeId = Integer.valueOf(request.getParameter("typeId"));
//		int reservationId = Integer.valueOf(request.getParameter("reservationId"));
		String info = request.getParameter("info");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));
		
		CarDAO carDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDAO();
		DamageTypeDAO typeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getDamageTypeDAO();
		
		CarDamage newEntry = new CarDamage();
		newEntry.setCar(carDAO.getByNumber(carNumber));
		newEntry.setDamageType(typeDAO.getById(damageTypeId));
		newEntry.setInfo(info);
		newEntry.setAmount(amount);
		
		carDamageDAO.add(newEntry);
	} //saveEntity()
	
	// Method to edit data in the database
	private void editEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {		
		CarDamage entity = getEntity(request);
		carDamageDAO.edit(entity);
	} //editEntity()

	// Method to delete data from the database
	private void deleteEntity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		CarDamage entity = getEntity(request);
		carDamageDAO.delete(entity.getId());
	} //deleteEntity()
	
	private void getDamageNotPaid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int reservationId = Integer.valueOf(request.getParameter("reservationId"));		
//		ReservationStatusDAO statusDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();	
		CarDamage damage = carDamageDAO.getDamagedWithoutPayment(reservationId);
		Reservation reservation = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO().getById(reservationId);
		
		HttpSession httpSession = request.getSession(false);
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		cart.setDamage(damage);
		cart.setReservation(reservation);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write("Success");
	}
} // CarDamageServlet
