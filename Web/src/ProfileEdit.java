

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dutluk.*;
import Dutluk.User.Gender;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/ProfileEdit")
public class ProfileEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProfileEdit() {
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
		request.getSession().setAttribute("name", user.getName());
		request.getSession().setAttribute("mail", user.getEmail());
		request.getSession().setAttribute("xp", user.getExperiencePoint());
		request.getSession().setAttribute("level", user.getLevel());
		request.getSession().setAttribute("gender", user.getGender().toString());


		if(user.getBirthdate()==null)
			request.getSession().setAttribute("birthdate", "");
		else
			request.getSession().setAttribute("birthdate", user.getBirthdate());

		if(user.getPhone()==null)
			request.getSession().setAttribute("phone", "");
		else
			request.getSession().setAttribute("phone", user.getPhone());

		if(user.getBio()==null)
			request.getSession().setAttribute("bio", " ");
		else
			request.getSession().setAttribute("bio", user.getBio());



		request.getRequestDispatcher("profile_edit.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		String action = request.getParameter("func");

		HttpSession session = request.getSession();
		DatabaseService db = new DatabaseService();
		String email = session.getAttribute("email").toString();
		User user = db.findUserByEmail(email);


		//This part is written for a quick test and
		//it revelead update method returns false.
		//even register method returns false here.
		user.setName("xx");
		boolean x = user.Update();
		if(x==true)	request.setAttribute("success", "true");
		else if(x==false) request.setAttribute("error","true");
		request.getRequestDispatcher("profile_edit.jsp").forward(request,response);
		
		//This is the proposed method to edit.
		/*
			String newGender_in = request.getParameter("editGender");
			Gender newGender;
			if(newGender_in == "male") newGender=Gender.Male;
			else newGender=Gender.Female;

			String newBirthdate = request.getParameter("editBirthdate");
			String newName = request.getParameter("editName");
			String newPhone = request.getParameter("editPhone");

			if(newBirthdate!=""){
				try {
					user.setBirthdate(newBirthdate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			user.setGender(newGender);
			user.setName(newName);
			user.setPhone(newPhone);

			Calendar cal = Calendar.getInstance();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
			user.setUpdatedOn(timestamp);

			Boolean result = user.Update();

			if(result)
			{
				request.setAttribute("success", "true");
				request.getRequestDispatcher("profile_edit.jsp").forward(request,response);
			}else
			{
				request.setAttribute("error", "true");
				request.getRequestDispatcher("profile_edit.jsp").forward(request, response);
			}*/


	}
}
