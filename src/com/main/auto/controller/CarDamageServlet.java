package com.main.auto.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.CarDAO;
import com.main.auto.dao.daoInterfaces.CarDamageDAO;
import com.main.auto.dao.daoInterfaces.DamageTypeDAO;
import com.main.auto.model.CarDamage;
import com.main.auto.model.Reservation;
import com.main.auto.service.ReservationCart;

@WebServlet(urlPatterns = {"/damage_save",
							"/damage_not_paid"})
public class CarDamageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CarDamageServlet.class.getName());
	private CarDamageDAO carDamageDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDamageDAO();
	private Gson gson = new Gson();
       
    public CarDamageServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			chooseAction(action, request, response);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Exception: ", e);
		}
	}

	// Method for selecting action depending on urlPatterns
	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{		
		switch (action) {
		case "/damage_save":
			saveEntity(request, response);
			break;	
		case "/damage_not_paid":
			getDamageNotPaid(request, response);
			break;	
		}
	} //chooseAction()
	
	// Method to save data to the database
	private void saveEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		String carNumber = request.getParameter("vin");
		int damageTypeId = Integer.valueOf(request.getParameter("typeId"));
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
	
	
	private void getDamageNotPaid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int reservationId = Integer.valueOf(request.getParameter("reservationId"));
		CarDamage damage = carDamageDAO.getDamagedWithoutPayment(reservationId);
		Reservation reservation = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO().getById(reservationId);
		
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		cart.setDamage(damage);
		cart.setReservation(reservation);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write("Success");
	}
} // CarDamageServlet
