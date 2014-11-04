

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		User user = new User();
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		Boolean result = user.Login();
		HttpSession session = request.getSession();
		if(result)
		{
			session.setAttribute("loggedIn", true);
			session.setAttribute("email", user.getEmail());
			response.getWriter().write("true");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}else
		{
			request.setAttribute("error", "true");
			request.setAttribute("message", "Email address or password is not correct!");
			request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
		}
	}

}
