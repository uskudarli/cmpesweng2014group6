

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dutluk.*;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doPost(request, response);
		response.setContentType("text/html"); 
		String action = request.getParameter("func");
		
		HttpSession session = request.getSession();
		DatabaseService db = new DatabaseService();
		String email = session.getAttribute("email").toString();
		User user = db.findUserByEmail(email);
		request.setAttribute("name", user.getName());
		System.out.print(user.getName());
		request.setAttribute("mail", user.getEmail());
		request.setAttribute("XP", user.getExperiencePoint());
		request.setAttribute("level", user.getLevel());
		request.setAttribute("gender", user.getGender());
		if(user.getBirthdate()==null)
			request.setAttribute("birthdate", "not specified yet.");
		else
			request.setAttribute("birthdate", user.getBirthdate());
		
		if(user.getBirthdate()==null)
			request.setAttribute("phone", "not specified yet.");
		else
			request.setAttribute("phone", user.getPhone());
		
		if(user.getBirthdate()==null)
			request.setAttribute("bio", "not specified yet.");
		else
			request.setAttribute("bio", user.getBio());
		
		
		
		request.getRequestDispatcher("profile.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html"); 
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
		user.setIsDeleted(0);
		user.setCreatedOn(timestamp);
		user.setUpdatedOn(timestamp);
		Boolean result = user.Register();
		
			HttpSession session = request.getSession();
			session.setAttribute("loggedIn", true);
			session.setAttribute("email", request.getParameter("email"));
			if(result)
			{
				response.getWriter().write("true");
			}else
				response.getWriter().write("false");

	}
}
