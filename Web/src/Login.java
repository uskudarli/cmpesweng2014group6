

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dutluk.DatabaseService;
import Dutluk.User;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		//String action = request.getParameter("func");
		//if(action.equals("gotoedit"))
			goToEdit(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		String action = request.getParameter("func");
		if(action.equals("logout"))
		{
			logOut(request,response);
		}else if(action.equals("login"))
			logIn(request, response);
		else if(action.equals("edit"))
			try {
				edit(request,response);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	void logOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		if(session != null)
		{
			try
			{
				response.setHeader("Cache-Control","no-cache"); //Forces caches to obtain a new copy of the page from the origin server
		        response.setHeader("Cache-Control","no-store"); //Directs caches not to store the page under any circumstance
		        response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
		        response.setHeader("Pragma","no-cache");
		        session.setAttribute("email", null);
				session.invalidate();
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}catch(Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println(e);
            }
		}
	}
	void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = new User();
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		Boolean result = user.Login();
		HttpSession session = request.getSession();
		if(result)
		{
			session.setAttribute("loggedIn", "true");
			session.setAttribute("email", user.getEmail());
			response.getWriter().write("true");
			response.sendRedirect("index.jsp");
		}else
		{
			session.setAttribute("loggedIn", "false");
			request.setAttribute("error", "true");
			request.setAttribute("message", "Email or password is not correct!");
			request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
		}
	}
	
	void goToEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		if(session == null)
		{
			response.sendRedirect("loginRegister.jsp");
		}else
		{
			if(session.getAttribute("email") == null)
			{
				response.sendRedirect("loginRegister.jsp");
			}else
			{
				DatabaseService db = new DatabaseService();
				User user = db.findUserByEmail(session.getAttribute("email").toString());
				request.setAttribute("name", user.getName());
				if(user.getBirthdate() == null)
					request.setAttribute("birthdate", "");
				else
				{
					String temp = user.getBirthdate().toString();
					String[] array = temp.split("-");
					String birthdate = array[2] + "/" + array[1] + "/" + array[0];
					request.setAttribute("birthdate", birthdate);
				}
				if(user.getPhone()==null)
					request.setAttribute("phone", "");
				else
					request.setAttribute("phone", user.getPhone());
				if(user.getBio()==null)
					request.setAttribute("bio", "");
				else
					request.setAttribute("bio", user.getBio());
				if(user.getGender()!=null)
					request.setAttribute("gender", user.getGender().toString());
				else
					request.setAttribute("gender", "");
				request.getRequestDispatcher("profile_edit.jsp").forward(request, response);
				//response.sendRedirect("profile_edit.jsp");
			}
		}
		
	}
	
	void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException
	{
		HttpSession session = request.getSession();
		if(session == null)
		{
			response.sendRedirect("loginRegister.jsp");
		}else
		{
			if(session.getAttribute("email") == null)
			{
				response.sendRedirect("loginRegister.jsp");
			}else
			{
				DatabaseService db = new DatabaseService();
				User user = db.findUserByEmail(session.getAttribute("email").toString());
				user.setName(request.getParameter("editUname"));
				String birthdate = request.getParameter("editBirthdate");
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
				user.setBirthdate(dateFormat.parse(birthdate));
				String gender = request.getParameter("editGender");
				if(gender.equals("male"))
					user.setGender(User.Gender.Male);
				else
					user.setGender(User.Gender.Female);
				user.setPhone(request.getParameter("editPhone"));
				user.setBio(request.getParameter("editBio"));
				//burda kaldim
				// edit function will be implemented
				Boolean result = db.Update(user);
				if(result)
					request.setAttribute("success", "true");
				else
					request.setAttribute("error", "true");
				goToEdit(request,response);
				
				//request.getRequestDispatcher("profile_edit.jsp").forward(request, response);
				//response.sendRedirect("profile_edit.jsp");
			}
		}
	}
}
