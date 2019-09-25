package com.main.auto.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.ClientDAO;
import com.main.auto.model.Client;
import com.main.auto.service.ReservationCart;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet(urlPatterns = {"/client_details"})
public class ClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClientDAO clientDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getClientDAO();
	
    public ClientServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		getClientDetails(request, response);
	}

	
	private void getClientDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession httpSession = request.getSession(false);
		String errMsg = (String) request.getAttribute("errMsg");
		if(errMsg == null) {		
			ReservationCart cart = (ReservationCart) httpSession.getAttribute("cart");
			String passportNumber = request.getParameter("PassportNumber");
			String firstName = request.getParameter("FirstName");
			String lastName = request.getParameter("LastName");
			String driverLicenseNumber = request.getParameter("DriverLicenseNumber");
			String birthDate = request.getParameter("BirthDate");
			String phoneNumber = request.getParameter("PhoneNumber");
			String email = request.getParameter("Email");
			
				Client client = cart.getClient();
				if (client == null) {						
						client = clientDAO.getByPassportNumber(passportNumber);
						if (client == null) {
							client = new Client();
							client.setPermission(DAOFactory.getDAOFactory(DBType.MYSQL).getPermissionDAO().getByPermissionRole("guest"));
						}
			}
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setPassportNumber(passportNumber);
				client.setDriverLicenseNumber(driverLicenseNumber);
				client.setBirthDate(Date.valueOf(birthDate));
				client.setPhoneNumber(phoneNumber);
				client.setEmail(email);
				client.setPassword(passportNumber);
			cart.setClient(client);
		} else {
			response.getWriter().write(errMsg);
		}		
	}
	
} //ClientServlet
