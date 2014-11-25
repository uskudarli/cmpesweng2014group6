

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
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
			request.getSession().setAttribute("birthdate", new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate().getTime()));

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


		String newGender_in = request.getParameter("editGender");
		Gender newGender;
		if(newGender_in.toLowerCase().startsWith("f")) newGender=Gender.Female;
		else if(newGender_in.toLowerCase().startsWith("m")) newGender=Gender.Male;
		else newGender=Gender.Unspecified;

		String newBirthdate = request.getParameter("editBirthdate");
		String newName = request.getParameter("editName");
		String newPhone = request.getParameter("editPhone");
		String newBio = request.getParameter("editBio");

		if(newBirthdate!=null){
			try {
				user.setBirthdate(newBirthdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		user.setGender(newGender);
		user.setName(newName);
		user.setPhone(newPhone);
		user.setBio(newBio);

		boolean updated=false;
		try {
			updated = user.UpdateProfile();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if(updated) request.setAttribute("success", "true");
		else request.setAttribute("error","true");
		
		//Resubmit new details to session, 
		//otherwise although updated, old info will be shown
		
		request.getSession().setAttribute("name", newName);
		request.getSession().setAttribute("gender", newGender.toString());


		if(newBirthdate==null)
			request.getSession().setAttribute("birthdate", "");
		else
			request.getSession().setAttribute("birthdate", newBirthdate);

		if(newPhone==null)
			request.getSession().setAttribute("phone", "");
		else
			request.getSession().setAttribute("phone", newPhone);

		if(newBio==null)
			request.getSession().setAttribute("bio", " ");
		else
			request.getSession().setAttribute("bio", newBio);
		
		
		
		
		request.getRequestDispatcher("profile_edit.jsp").forward(request,response);



	}
}
