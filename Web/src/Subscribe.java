

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dutluk.DatabaseService;
import Dutluk.User;

/**
 * Servlet implementation class Subscribe
 */
@WebServlet("/Subscribe")
public class Subscribe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Subscribe() {
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
		String action = request.getParameter("func");
		String userMail = request.getSession().getAttribute("email").toString();
		DatabaseService db = new DatabaseService();
		User user = db.findUserByEmail(userMail);
		String otherUserId = request.getParameter("userId");
		if(action.equals("subscribe"))
			db.subscribe(user.getUserID(), Integer.parseInt(otherUserId));
		else
			db.unsubscribe(user.getUserID(), Integer.parseInt(otherUserId));
		String redirect = "profile.jsp?id="+otherUserId;
		response.sendRedirect(redirect);
	}
}
