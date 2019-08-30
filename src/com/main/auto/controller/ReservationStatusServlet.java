package com.main.auto.controller;

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
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.ReservationStatusDAO;
import com.main.auto.model.ReservationStatus;

/**
 * Servlet implementation class ReservationStatusServlet
 */
@WebServlet(urlPatterns = {"/reservation_status", 
							"/reservation_status_matched"})
public class ReservationStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new GsonBuilder().create();    
	private ReservationStatusDAO statusDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();
	//private ReservationStatusCombsDAO statusCombsDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusCombsDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			chooseAction(action, request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	// Method for selecting action depending on urlPatterns
	private void chooseAction(String action, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException
	{
		switch (action) {
		case "/reservation_status":
			show(request, response);			
			break;	
		case "/reservation_status_matched":
			showMatchedStatusList(request, response);
			break;
		}
	} // chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void show(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<ReservationStatus> list = statusDAO.getAll();
		
		// Convert Java object to JSON format and returned as JSON formatted String
		
	/*	String json = gson.toJson(list);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);	*/
		sendJson(list, response);
	}
	
	private void showMatchedStatusList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int id = Integer.valueOf(request.getParameter("statusId"));
		List<ReservationStatus> list = statusDAO.getStatusMatchedById(id);
		
		// Convert Java object to JSON format and returned as JSON formatted String
		
		/*String json = gson.toJson(list);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
		*/
		
		sendJson(list, response);
	}	
	
	private <T> void sendJson(T data, HttpServletResponse response) throws IOException {
		String json = gson.toJson(data);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	}
	
}
