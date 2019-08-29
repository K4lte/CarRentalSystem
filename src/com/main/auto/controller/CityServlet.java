package com.main.auto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main.auto.dao.CityDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.model.City;

/**
 * Servlet implementation class CityServlet
 */
@WebServlet(urlPatterns = {"/city", 
							"/city_delete", 
							"/city_edit", 
							"/city_save" })
public class CityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CityDAO cityDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCityDAO();
	private Gson gson = new GsonBuilder().create();    
	
    public CityServlet() {
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
	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException
	{
		City entity = getEntity(request);
		switch (action) {
		case "/city_save":
			cityDAO.add(entity);
			break;
		case "/city_edit":
			cityDAO.edit(entity);
			break;
		case "/city_delete":
			cityDAO.delete(entity.getId());
			break;	
		default:
			showList(request, response);
			break;
		}
	} // chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<City> list = cityDAO.getOfficiesCities();
        // Convert Java object to JSON format and returned as JSON formatted String
       
		String json = gson.toJson(list);
	   
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()
		
	// Method to convert JSON back to Java object
	private City getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		City entity = gson.fromJson(json, City.class);		
		return entity;
	}	// getEntity()
} //CityServlet
