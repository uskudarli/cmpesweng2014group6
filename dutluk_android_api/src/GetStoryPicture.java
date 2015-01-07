

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetStoryPicture
 */
@WebServlet("/GetStoryPicture")
public class GetStoryPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetStoryPicture() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int storyId = Integer.parseInt(request.getParameter("storyId"));
		DatabaseService db = new DatabaseService();
		ArrayList<String> paths = db.getStoryPicturePath(storyId);
		RegisterResult registerResult = new RegisterResult();
		if(paths == null) {
			registerResult.setMessage("no image");
			registerResult.setResult(false);			
		}else {
			registerResult.setMessage(paths.get(paths.size()-1));
			registerResult.setResult(true);
		}
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(registerResult));
		pw.flush();
		pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
