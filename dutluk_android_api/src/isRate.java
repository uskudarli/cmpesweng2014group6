

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class isRate
 */
@WebServlet("/isRate")
public class isRate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public isRate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int userId = Integer.parseInt(request.getParameter("userId"));
		int storyId = Integer.parseInt(request.getParameter("storyId"));
		DatabaseService db = new DatabaseService();
		int rate = db.getRate(userId, storyId);
		if(rate == 0) {
	    	RegisterResult registerResult  = new RegisterResult();
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			registerResult.setMessage("no rate is found for this story and user");
			registerResult.setResult(false);
	        Gson gson = new Gson();
			PrintWriter pw = response.getWriter();
			pw.print(gson.toJson(registerResult));
			pw.flush();
			pw.close();
		}else {
	    	RegisterResult registerResult  = new RegisterResult();
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			registerResult.setMessage("ok!");
			registerResult.setResult(true);
	        Gson gson = new Gson();
			PrintWriter pw = response.getWriter();
			pw.print(gson.toJson(registerResult));
			pw.flush();
			pw.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
