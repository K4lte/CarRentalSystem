package com.main.auto.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.main.auto.dao.DAOFactory;
import com.main.auto.dao.DBType;
import com.main.auto.dao.daoInterfaces.OfficeDAO;
import com.main.auto.model.Office;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet(urlPatterns = {"/office"})
public class OfficeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(OfficeServlet.class.getName());
	private OfficeDAO officeDAO = DAOFactory.getDAOFactory(DBType.MYSQL).getOfficeDAO();
	private Gson gson = new Gson();
       
    public OfficeServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			showList(request, response);
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Exception: ", e);
		}
	}
	
	// Method to list the entire contents of a table from the database
	private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Office> list = officeDAO.getAll();
        
		// Convert Java object to JSON format and returned as JSON formatted String		
		String json = gson.toJson(list);
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
    } //showList()

} // OfficeServlet
