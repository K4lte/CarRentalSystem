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
import com.main.auto.dao.OfficeDAO;
import com.main.auto.model.Office;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet(urlPatterns = {"/office", 
							"/office_delete", 
							"/office_edit",
							"/office_save" })
public class OfficeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OfficeDAO officeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO();
	private Gson gson = new Gson();
       
    public OfficeServlet() {
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
		Office entity = getEntity(request);
		switch (action) {
		case "/office_save":
			officeDAO.add(entity);
			break;
		case "/office_edit":
			officeDAO.edit(entity);
			break;
		case "/office_delete":
			officeDAO.delete(entity.getId());
			break;	
		default:
			showList(request, response);
			break;
		}
	} // chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Office> list = officeDAO.getAll();
		// Convert Java object to JSON format and returned as JSON formatted String
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()
	
	// Method to convert JSON back to Java object
	private Office getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		Office entity = gson.fromJson(json, Office.class) ;
		return entity;
	}	// getEntity()
} // OfficeServlet
