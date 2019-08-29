package com.main.auto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.ReservationDAO;
import com.main.auto.dao.ReservationStatusDAO;
import com.main.auto.model.Reservation;
import com.main.auto.service.ReservationCart;

@WebServlet(urlPatterns = {"/reservation", 
							"/reservation_save",
							"/reservation_edit",
							"/reservation_delete",
							"/reservation_open",
							"/reservation_status_change",
							"/reservation_monitoring",
							"/reservation_client_monitoring",
							"/reservation_client_warning"})
//@MultipartConfig
public class ReservationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReservationDAO reservationDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationDAO();
//	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:MM").create();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); 
       
    public ReservationServlet() {
        super();
    }

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
		case "/reservation_save":
			saveEntity(request, response);			
			break;	
		case "/reservation_edit":
			editEntity(request, response);
			break;
		case "/reservation_delete":
			deleteEntity(request, response);
			break;
		case "/reservation_open":
			showStatusOpen(request, response);
			break;
		case "/reservation_status_change":
			statusChange(request, response);
			break;
		case "/reservation_monitoring":
			monitoring(request, response);
			break;
		case "/reservation_client_monitoring":
			monitoringByClient(request, response);
			break;
		case "/reservation_client_warning":
			monitoringByClient(request, response);
			break;
			
		default:
			showList(request, response);
			break;
		}
	} // chooseAction()
	

	private void saveEntity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Reservation entity = getEntity(request);
		reservationDAO.add(entity);
	}

	private void editEntity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Reservation entity = getEntity(request);
		reservationDAO.edit(entity);
	}

	private void deleteEntity(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Reservation entity = getEntity(request);
		reservationDAO.delete(entity.getId());
	}

	private void showStatusOpen(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String status = "open";
		List<Reservation> list = reservationDAO.getByStatus(status);
        // Convert Java object to JSON format and returned as JSON formatted String 
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
		
	}

	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Reservation> list = reservationDAO.getAll();
        // Convert Java object to JSON format and returned as JSON formatted String 
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()
	
	// Method to convert JSON back to Java object
	private Reservation getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		Reservation entity = gson.fromJson(json, Reservation.class) ;
		return entity;
	} // getEntity()
	
	private void statusChange(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		HttpSession httpSession = request.getSession(false);
		
		int reservationId = Integer.valueOf(request.getParameter("id"));
		int statusId = Integer.valueOf(request.getParameter("statusId"));
		String note = request.getParameter("note");
		Reservation reservation = reservationDAO.getById(reservationId);
		ReservationStatusDAO statusDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getReservationStatusDAO();
		reservation.setStatus(statusDAO.getById(statusId));
		reservation.setNote(note);
		reservationDAO.edit(reservation);
	//		response.sendRedirect("6.html");

	}
	
	private void monitoring(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int statusId = Integer.valueOf(request.getParameter("statusId"));
		
		List<Reservation> list = reservationDAO.getByStatusId(statusId);
        // Convert Java object to JSON format and returned as JSON formatted String 
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
		
	}
	
	private void monitoringByClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession httpSession = request.getSession(false);
		
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		
		List<Reservation> list = new ArrayList<>();
		String action = request.getServletPath();
		switch(action) {
		case "/reservation_client_warning":
			list = reservationDAO.getByClientIdStatus(cart.getClient().getId(), "damaged");
			break;
		case "/reservation_client_monitoring":
			list = reservationDAO.getByClientId(cart.getClient().getId());
			break;		
		}
		
		//List<Reservation> list = reservationDAO.getByClientId(client.getId());
        // Convert Java object to JSON format and returned as JSON formatted String 
		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
		
	}
}


