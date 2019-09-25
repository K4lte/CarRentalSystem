package com.main.auto.controller.filter;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.main.auto.service.ConfigurationManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.main.auto.service.Util;

/**
 * Servlet Filter implementation class ValidationFilter
 */
@WebFilter(urlPatterns = {"/login",
							"/search",
							"/client_details",
							"/booking"})
public class ValidationFilter implements Filter {
	private static final Logger logger = Logger.getLogger(ValidationFilter.class.getName());
	private static String locale;
	private static String property;

    /**
     * Default constructor. 
     */
	
    public ValidationFilter() {
        
    }
  
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
/*		locale = Util.getLocale(httpRequest);	
		property = locale.equals("ru") ? "errMsg_locale_ru" : "errMsg_locale_en";
*/
		String path = httpRequest.getServletPath();
		
		httpResponse.setCharacterEncoding("UTF-8");

		switch (path) {
		case "/login":
			validateLoginForm(httpRequest, httpResponse);
			break;
		case "/search":
			validateSearchForm(httpRequest, httpResponse);
			break;
		case "/client_details":
			validateClientForm(httpRequest, httpResponse);
			break;
		case "/booking":
			validatePaymentForm(httpRequest, httpResponse);
			break;	
		}
		chain.doFilter(request, response);
	}

	private void validateLoginForm(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
		String login = httpRequest.getParameter("login");
		String password = httpRequest.getParameter("password");
		String role = httpRequest.getParameter("role");
		
        if (login == null || "".equals(login)
                || password == null || "".equals(password)) {
        	Util.setErrorMessage(httpRequest, "loginFormError", "loginForm.empty");
        } else {
        	if (!"admin".equals(role) && !"client".equals(role)) {
        		Util.setErrorMessage(httpRequest, "roleError", "role.empty");
        	}
        } 
	}

	private void validateSearchForm(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		int selectedPickUpLocationId = Integer.valueOf(httpRequest.getParameter("pickUpLocation"));
		int selectedDropOffLocationId = Integer.valueOf(httpRequest.getParameter("dropOffLocation"));
		String selectedPickUpDate = httpRequest.getParameter("pickUpDate");
		String selectedDropOffDate = httpRequest.getParameter("dropOffDate");
		if (selectedPickUpLocationId == 0 || selectedDropOffLocationId == 0) {
			Util.setErrorMessage(httpRequest, "errorMsg", "location.empty");
		} else {
			if (selectedPickUpDate != null && !"".equals(selectedPickUpDate)
	                && selectedDropOffDate != null && !"".equals(selectedDropOffDate)) {			
				switch (Date.valueOf(selectedPickUpDate).compareTo(Date.valueOf(selectedDropOffDate))) {
				    case 0:   
				    	Util.setErrorMessage(httpRequest, "errorMsg", "date.sameDates");
				    	break;
				    case 1:   
				    	Util.setErrorMessage(httpRequest, "errorMsg", "date.date2BeforeDate1");
				    	break;
			}
			} else {
				Util.setErrorMessage(httpRequest, "errorMsg", "date.empty");
			}
		}  
	}
	
	private void validateClientForm(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		String firstName = httpRequest.getParameter("FirstName");
		String lastName = httpRequest.getParameter("LastName");
		String passportNumber = httpRequest.getParameter("PassportNumber");
		String driverLicenseNumber = httpRequest.getParameter("DriverLicenseNumber");
		String birthDate = httpRequest.getParameter("BirthDate");
		String phoneNumber = httpRequest.getParameter("PhoneNumber");
		String email = httpRequest.getParameter("Email");
		String regexName = "^[A-Z][a-z]+";
		String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}";	
		String regexPhone = "\\d{3}-\\d{3}-\\d{4}";

		if (firstName != null && !"".equals(firstName)) {
			if (!firstName.matches(regexName)) {
				Util.setErrorMessage(httpRequest, "firstNameError", "firstName");
			} else {
		        if (lastName != null && !"".equals(lastName)) {
					if (!lastName.matches(regexName)) {
					    Util.setErrorMessage(httpRequest, "lastNameError", "lastName");
		            } else {
		                if (passportNumber == null || "".equals(passportNumber)) {
		                    Util.setErrorMessage(httpRequest, "passportError", "passport");
		                } else {
		                    if(driverLicenseNumber == null || "".equals(driverLicenseNumber)) {
		                        Util.setErrorMessage(httpRequest, "driverLicenseError", "license");
		                    } else {
		                        if(birthDate != null && !"".equals(birthDate)) {
		                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		                            dateFormat.setLenient(false);
		                            try {
			                            java.util.Date d1 = dateFormat.parse(birthDate);
			                            if(phoneNumber != null && !"".equals(phoneNumber)) {
			                                if (!phoneNumber.matches(regexPhone)) {
			                                    Util.setErrorMessage(httpRequest, "phoneError", "phone");
			                                } else {
			                                    if(email != null && !"".equals(email)) {
			                                        if (!email.matches(regexEmail)) {
			                                            Util.setErrorMessage(httpRequest, "emailError", "email");
			                                        } 
			                                    } else {
			                                        Util.setErrorMessage(httpRequest, "emailError", "email.empty");
			                                    }
			                                }
			                            } else {
			                                Util.setErrorMessage(httpRequest, "phoneError", "phone.empty");
			                            }
		                            } catch (Exception e) {
		                                	logger.log(Level.ERROR, "Exception: ", e);
		                                	Util.setErrorMessage(httpRequest, "birthdayError", "birthday");
		                            }
		                        } else {
		                        	Util.setErrorMessage(httpRequest, "birthdayError", "birthday.empty");
		                        }
		                    }
		                }
		            } 
		        } else { // if last name
		        	Util.setErrorMessage(httpRequest, "lastNameError", "lastName.empty");
		        }
			}
		} else {  // if first name
			Util.setErrorMessage(httpRequest, "firstNameError", "firstName.empty");
		}
		
	} //validateClientForm()

	
	private void validatePaymentForm(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		int creditCardTypeId = Integer.valueOf(httpRequest.getParameter("creditCardType"));
		String creditCardNumber = httpRequest.getParameter("creditCardNumber");
		String regexNumber = "\\d{12}";
		if (creditCardTypeId == 0) {
			Util.setErrorMessage(httpRequest, "cardTypeError", "ccType");
		} else {
			if (creditCardNumber != null && !"".equals(creditCardNumber)) {
				if (!creditCardNumber.matches(regexNumber)) {
					Util.setErrorMessage(httpRequest, "cardNumberError", "ccNumber");
				}
			} else {
				Util.setErrorMessage(httpRequest, "cardNumberError", "ccNumber.empty");
			}
		} 
	} // validatePaymentForm()
	

}