

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
		// TODO Auto-generated method stub
        String action = request.getParameter("func");
        String userMail = request.getParameter("email");
        DatabaseService db = new DatabaseService();
        User user = db.findUserByEmail(userMail);
    	RegisterResult registerResult  = new RegisterResult();
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if(action.equals("subscribe"))
		{
		        String otherUserId = request.getParameter("userId");
		        db.subscribe(user.getUserId(), Integer.parseInt(otherUserId));
				registerResult.setMessage("user is followed successfully");
				registerResult.setResult(true);
		}
		else if(action.equals("unsubscribe"))
		{
		        String otherUserId = request.getParameter("userId");
		        db.unsubscribe(user.getUserId(), Integer.parseInt(otherUserId));
				registerResult.setMessage("user is unfollowed successfully");
				registerResult.setResult(true);
		}else if(action.equals("subscribePlace"))
		{
		        String placeId = request.getParameter("placeId");
		        db.subscribePlace(user.getUserId(), Integer.parseInt(placeId));
				registerResult.setMessage("place is followed successfully");
				registerResult.setResult(true);
		}else
		{
		        String placeId = request.getParameter("placeId");
		        db.unsubscribePlace(user.getUserId(), Integer.parseInt(placeId));
				registerResult.setMessage("place is unfollowed successfully");
				registerResult.setResult(true);
		}
        
		
        Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(registerResult));
		pw.flush();
		pw.close();
	}

}
