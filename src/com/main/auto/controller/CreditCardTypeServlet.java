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
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.CreditCardTypeDAO;
import com.main.auto.model.CreditCardType;

@WebServlet(urlPatterns = {"/credit_card_type", 
							"/credit_card_type_delete", 
							"/credit_card_type_edit", 
							"/credit_card_type_save" })
public class CreditCardTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CreditCardTypeDAO creditCardTypeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getCreditCardTypeDAO();
	private Gson gson = new GsonBuilder().create();    
	
    public CreditCardTypeServlet() {
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
		CreditCardType entity = getEntity(request);
		switch (action) {
		case "/credit_card_type_save":
			creditCardTypeDAO.add(entity);
			break;
		case "/credit_card_type_edit":
			creditCardTypeDAO.edit(entity);
			break;
		case "/credit_card_type_delete":
			creditCardTypeDAO.delete(entity.getId());
			break;	
		default:
			showList(request, response);
			break;
		}
	} // chooseAction()
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<CreditCardType> list = creditCardTypeDAO.getAll();
        // Convert Java object to JSON format and returned as JSON formatted String
		
		String json = gson.toJson(list);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } // showList()
	
	// Method to convert JSON back to Java object
	private CreditCardType getEntity(HttpServletRequest request) throws IOException {
		String json = "";
		try (BufferedReader reader = request.getReader()){
			if (reader != null) {
				json = reader.readLine();
			}
		}
		CreditCardType entity = gson.fromJson(json, CreditCardType.class) ;
		return entity;
	} // getEntity()
} // PaymentTypeServlet
