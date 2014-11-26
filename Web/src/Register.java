

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dutluk.DatabaseService;
import Dutluk.User;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		DatabaseService db = new DatabaseService();
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
		user.setIsDeleted(0);
		user.setCreatedOn(timestamp);
		user.setUpdatedOn(timestamp);
		user.setGender(User.Gender.Male);
		Boolean result = db.register(user);
		HttpSession session = request.getSession();
			if(result)
			{
				session.setAttribute("loggedIn", "true");
				session.setAttribute("email", request.getParameter("email"));
				response.getWriter().write("true");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}else
			{
				session.setAttribute("loggedIn", "false");
				request.setAttribute("error", "true");
				request.setAttribute("message", "This email address is already registered!");
				request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
			}

	}
}
