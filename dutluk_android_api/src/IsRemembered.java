

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class IsRemembered
 */
@WebServlet("/IsRemembered")
public class IsRemembered extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsRemembered() {
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
		int storyId = Integer.parseInt(request.getParameter("storyId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		DatabaseService db = new DatabaseService();
		boolean result = db.isRemembered(userId, storyId);
		RegisterResult registerResult  = new RegisterResult();
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		registerResult.setMessage("iþte mesaj");
		registerResult.setResult(result);
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(registerResult));
		pw.flush();
		pw.close();
	}

}
