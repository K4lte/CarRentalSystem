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
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.DamageTypeDAO;
import com.main.auto.model.DamageType;

@WebServlet(urlPatterns = {"/damage_type", 
							"/damage_type_delete", 
							"/damage_type_edit", 
							"/damage_type_save" })
public class DamageTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DamageTypeDAO damageTypeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getDamageTypeDAO();
	private Gson gson = new Gson();
       
    public DamageTypeServlet() {
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
			throws SQLException, ServletException, IOException	{		
		switch (action) {
		case "/damage_type_save":
			saveEntity(request, response);
			break;
		case "/damage_type_edit":
			editEntity(request, response);
			break;
		case "/damage_type_delete":
			deleteEntity(request, response);
			break;	
		default:
			showList(request, response);
			break;
		}
	} //chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<DamageType> list = damageTypeDAO.getAll();
        // Convert Java object to JSON format and returned as JSON formatted String
		
        String json = gson.toJson(list);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()
	
	// Method to convert JSON back to Java object
	private DamageType getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		DamageType entity = gson.fromJson(json, DamageType.class) ;
		return entity;
	} // getEntity()
	
	// Method to save data to the database
	private void saveEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		DamageType entity = getEntity(request);
		damageTypeDAO.add(entity);
	} //saveEntity()
	
	// Method to edit data in the database
	private void editEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {		
		DamageType entity = getEntity(request);			
		damageTypeDAO.edit(entity);
	} //editEntity()

	// Method to delete data from the database
	private void deleteEntity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		DamageType entity = getEntity(request);
		damageTypeDAO.delete(entity.getId());
	} //deleteEntity()
}
