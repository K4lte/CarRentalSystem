package com.main.auto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.main.auto.dao.CarDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.model.Car;
import com.main.auto.service.ReservationCart;
import com.main.auto.service.Util;

@WebServlet(urlPatterns = {"/car", 
							"/car_delete", 
							"/car_edit", 
							"/car_save",
							"/car_search",
							"/car_select"})
public class CarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CarDAO carDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDAO();
	private Gson gson = new Gson();
	
       
    public CarServlet() {
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
		//Car entity = getEntity(request);
		switch (action) {
		case "/car_save":
		//	carDAO.add(entity);
			saveEntity(request, response);
			break;
		case "/car_edit":
			//carDAO.edit(entity);
			editEntity(request, response);
			break;
		case "/car_delete":
			//carDAO.delete(entity.getId());
			deleteEntity(request, response);
			break;	
		case "/car_search":
			search(request, response);
			break;
		case "/car_select":
			select(request, response);
			break;
		default:
			showList(request, response);
			break;
		}
	} // chooseAction()
	
	private void saveEntity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Car entity = getEntity(request);
		carDAO.add(entity);
	}

	private void editEntity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Car entity = getEntity(request);
		carDAO.edit(entity);
	}

	private void deleteEntity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Car entity = getEntity(request);
		carDAO.delete(entity.getId());
	}

	// Method to get list of cars
	private void search(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		HttpSession httpSession = request.getSession(false);
		
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		//List<Car> list = carDAO.searchByLocationDate(location, pickUpDate, dropOffDate);
		List<Car> list = carDAO.searchByLocationDate(cart.getLocationPickUp().getId(), cart.getDatePickUp(), cart.getDateDropOff());
		
		int period = Util.getDifferenceDays(cart.getDatePickUp(),cart.getDateDropOff());
		cart.setPeriod(period);
		
		for (Car car : list) {
			Util.getTotalPrice(period, car);
		}
		
		String json = gson.toJson(list);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	} // search()
	
	private void select(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer selectedCarId = Integer.parseInt(request.getParameter("hiddenId"));
		
		HttpSession httpSession = request.getSession(false);
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		Car car = carDAO.getById(selectedCarId);
		Util.getTotalPrice(cart.getPeriod(), car);		
		cart.setCar(car);
		cart.setTotalRental(car.getTotalRental());
		response.sendRedirect("3.html");
	}
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Car> list = carDAO.getAll();
        // Convert Java object to JSON format and returned as JSON formatted String
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()
	
	// Method to convert JSON back to Java object
	private Car getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		Car entity = gson.fromJson(json, Car.class) ;
		return entity;
	}	// getEntity
} //CarServlet
