

import java.io.IOException;
import java.util.Calendar;

import javax.mail.*;
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		DatabaseService db = new DatabaseService();
		String email = request.getParameter("email");
		User user = db.findUserByEmail(email);
		if(user.getName() == null)
		{
			request.setAttribute("error", "true");
			request.setAttribute("message", "User not found!");
			request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
		}
		else
		{
			//send a new random password with an e-mail to user
			
		}
		
		//response.sendRedirect("index.jsp");
	}

}
