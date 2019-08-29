package com.main.auto.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main.auto.dao.ClientDAO;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.model.Client;
import com.main.auto.service.ReservationCart;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet(urlPatterns = {"/client", 
							"/client_delete",
							"/client_edit",
							"/client_get",
							"/client_save",
							"/client_details"})
public class ClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClientDAO clientDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO();
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); 
	
    public ClientServlet() {
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
		//doGet(request, response);
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
		case "/client_save":
			saveEntity(request, response);
			break;
		case "/client_edit":
			editEntity(request, response);
			break;
		case "/client_delete":
			deleteEntity(request, response);
			break;	
		case "/client_get":
			getEntityByLogin(request, response);
			break;	
		case "/client_details":
			getClientDetails(request, response);
			break;
		default:
			showList(request, response);
			break;
		}
	} //chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		List<Client> list = clientDAO.getAll();
		
		// Convert Java object to JSON format and returned as JSON formatted String
		
		String json = gson.toJson(list);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	} // showList()
	
	// Method to save data to the database
	private void saveEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
	    	Client clientNew = getEntity(request);
			clientDAO.add(clientNew);
	} //saveEntity()
	
	// Method to edit data in the database
	private void editEntity(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {		
			Client client = getEntity(request);			
			clientDAO.edit(client);
	} //editEntity()

	// Method to delete data from the database
	private void deleteEntity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		Client client = getEntity(request);
		clientDAO.delete(client.getId());
	} //deleteEntity()
	
	// Method to convert JSON back to Java object
	private Client getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		Client client = gson.fromJson(json, Client.class) ;
		return client;
	} //getEntity()
	
	private void getClientDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String passportNumber = request.getParameter("passport_number");
		
		Client newClient = clientDAO.getByPassportNumber(passportNumber);
		if (newClient == null) {
			newClient = new Client();
			String firstName = request.getParameter("first_name");
			String lastName = request.getParameter("last_name");
			String driverLicenseNumber = request.getParameter("driver_license_number");
			String birthDate = request.getParameter("birth_date");
			String phoneNumber = request.getParameter("phone_number");
			String email = request.getParameter("email");
			newClient.setFirstName(firstName);
			newClient.setLastName(lastName);
			newClient.setPassportNumber(passportNumber);
			newClient.setDriverLicenseNumber(driverLicenseNumber);
			newClient.setBirthDate(Date.valueOf(birthDate));
			newClient.setPhoneNumber(phoneNumber);
			newClient.setEmailAddress(email);
			newClient.setPassword(passportNumber);
			newClient.setPermission(DAOFactory.getDAOFactory(DBType.MYSQL).getPermissionDAO().getById(4));
		}
		
		HttpSession httpSession = request.getSession();
//		httpSession.setAttribute("client", newClient);
		ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
		cart.setClient(newClient);
		response.sendRedirect("4.html");
		
	}
	
	// Method to get user's data if user is logged in
	private void getEntityByLogin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		int id;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		 for (Cookie cookie : cookies) {
			 // Get user's data from the database using cookies and transfer it to welcom.html
		   if (cookie.getName().equals("userId")) {
			   id = Integer.parseInt(cookie.getValue());
			   Client user = clientDAO.getById(id);
			   String json = gson.toJson(user);
			   response.setContentType("application/json");
			   response.setCharacterEncoding("UTF-8");
			   response.getWriter().write(json);
		    }
		  }
		}		
	} // getEntityByLogin()
	
} //ClientServlet
