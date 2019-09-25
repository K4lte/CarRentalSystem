package com.main.auto.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.main.auto.service.ConfigurationManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.CarDAO;
import com.main.auto.model.Car;
import com.main.auto.service.ReservationCart;
import com.main.auto.service.Util;

@WebServlet(urlPatterns = {"/car_search",
							"/car_select"})
public class CarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CarServlet.class.getName());
	private CarDAO carDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCarDAO();
	private Gson gson = new Gson();
	
       
    public CarServlet() {
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
		case "/car_search":
			search(request, response);
			break;
		case "/car_select":
			select(request, response);
			break;
		}
	} // chooseAction()
	

	// Method to get list of cars
	private void search(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession(false);
		
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		List<Car> list = carDAO.searchByLocationDate(cart.getLocationPickUp().getId(), cart.getDatePickUp(), cart.getDateDropOff());
		
		int period = Util.getDifferenceDays(cart.getDatePickUp(),cart.getDateDropOff());
		cart.setPeriod(period);
		
		for (Car car : list) {
			Util.getTotalPrice(period, car);
		}
		String errMsg = null;
		String locale = Util.getLocale(request);	
		String property = locale.equals("ru") ? "errMsg_locale_ru" : "errMsg_locale_en";
		if (list != null && list.isEmpty()) {
			errMsg = ConfigurationManager.getProperty(property, "noCars") + cart.getLocationPickUp().getLocation().getCity().getCityName()
					+ ", " + cart.getLocationPickUp().getLocation().getAddress();
		    } else {
		    	errMsg = "false";
		    }

		JsonObject availableCars = new JsonObject();	
		availableCars.addProperty("cars", gson.toJson(list));
		// Add errMsg to json list of available cars
		JsonElement jsonElement = gson.toJsonTree(availableCars);
    	jsonElement.getAsJsonObject().addProperty("errMsg", errMsg);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(jsonElement));
	} // search()
	
	private void select(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int selectedCarId = Integer.parseInt(request.getParameter("hiddenId"));
		
		HttpSession session = request.getSession(false);
		ReservationCart cart = (ReservationCart) session.getAttribute("cart");
		Car car = carDAO.getById(selectedCarId);
		Util.getTotalPrice(cart.getPeriod(), car);		
		cart.setCar(car);
		cart.setTotalRental(car.getTotalRental());
		response.sendRedirect("travellerDetailsPage.html");
	}

} //CarServlet
