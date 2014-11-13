

import java.io.IOException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dutluk.DatabaseService;
import Dutluk.User;

/**
 * Servlet implementation class ResetPassword
 */
@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	public static String generatePassword()
	{
		Random rn = new Random();
	    char[] text = new char[8];
	    for (int i = 0; i < 8; i++)
	    {
	        text[i] = "0123456789abcdefghjklmn".charAt(rn.nextInt("0123456789abcdefghjklmn".length()));
	    }
	    return new String(text);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String action = request.getParameter("func");
		if(action.equals("forgotPassword"))
		{
			DatabaseService db = new DatabaseService();
			String email = request.getParameter("email");
			User user = db.findUserByEmail(email);
			String name = user.getName();
			String newPassword = generatePassword();
			if(user.getName() == null)
			{
				request.setAttribute("error", "true");
				request.setAttribute("message", "User not found!");
				request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
			}
			else
			{
				boolean passwordChanged = user.ChangePassword(email, newPassword);
				if(passwordChanged)
				{
					//send a new random password with an e-mail to user
					Properties props = new Properties();
					props.put("mail.smtp.host", "smtp.gmail.com");
					props.put("mail.smtp.socketFactory.port", "465");
					props.put("mail.smtp.socketFactory.class",
							"javax.net.ssl.SSLSocketFactory");
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.port", "465");
			 
					Session session = Session.getDefaultInstance(props,
						new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication("dutluk.group6@gmail.com","cmpesweng2014");
							}
						});
			 
					try {
			 
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress("DUTLUK"));
						message.setRecipients(Message.RecipientType.TO,
								InternetAddress.parse(email));
						message.setSubject("Your new password on Dutluk");
						message.setText("Dear "+name+", "+
								"\n\nYour new password is: "+newPassword+"\nDon't forget to change your password after log in.");
			 
						Transport.send(message);
			 
						request.setAttribute("error", "true");
						request.setAttribute("message", "The new password is sent to your e-mail address.");
						request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
			 
					} catch (MessagingException e) {
						throw new RuntimeException(e);
					}
				}
				else
				{
					request.setAttribute("error", "true");
					request.setAttribute("message", "An error occurred. Please try again");
					request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
				}
			}
			
			//response.sendRedirect("index.jsp");
		}
		else if(action.equals("resetPassword"))
		{
			HttpSession session = request.getSession();
			String email = session.getAttribute("email").toString();
			DatabaseService db = new DatabaseService();
			User user = db.findUserByEmail(email);
			if(user.getPassword().equals(request.getParameter("oldPassword")))
			{
				if(request.getParameter("newPassword").equals(request.getParameter("reNewPassword")) && !request.getParameter("newPassword").equals(""))
				{
					if(user.ChangePassword(email, request.getParameter("newPassword")))
					{
						request.setAttribute("error", "true");
						request.setAttribute("message", "Your password is changed!");
						request.getRequestDispatcher("changePassword.jsp").forward(request, response);
					}
				}
				else
				{
					request.setAttribute("error", "true");
					request.setAttribute("message", "Your new passwords don't match!");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			}
			else
			{
				request.setAttribute("error", "true");
				request.setAttribute("message", "Your current password doesn't match!");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
			
			
			
			
		}
	}

}
