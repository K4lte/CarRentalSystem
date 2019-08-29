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
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.LocationDAO;
import com.main.auto.model.Location;
import com.main.auto.service.ReservationCart;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet(urlPatterns = {"/location", 
							"/location_delete", 
							"/location_edit", 
							"/location_save",
							"/location_select"})
public class LocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LocationDAO locationDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getLocationDAO();
	private Gson gson = new Gson();
	
    public LocationServlet() {
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
		Location entity = getEntity(request);
		switch (action) {
		case "/location_save":
			locationDAO.add(entity);
			break;
		case "/location_edit":
			locationDAO.edit(entity);
			break;
		case "/location_delete":
			locationDAO.delete(entity.getId());
			break;	
		case "/location_select":
			select(request, response);
			break;
		default:
			showList(request, response);
			break;
		}
	} // chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Location> list = locationDAO.getAll();
        // Convert Java object to JSON format and returned as JSON formatted String
		
        response.setContentType("application/json");
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()

	private void select(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int selectedCityId = Integer.parseInt(request.getParameter("pickUpLocation"));
		List<Location> list = locationDAO.getByCityId(selectedCityId);
        // Convert Java object to JSON format and returned as JSON formatted String
		
        response.setContentType("application/json");
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	}
	
	// Method to convert JSON back to Java object
	private Location getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		Location entity = gson.fromJson(json, Location.class) ;
		return entity;
	}	// getEntity()
}
