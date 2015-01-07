

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class RememberStory
 */
@WebServlet("/RememberStory")
public class RememberStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RememberStory() {
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
        String storyId = request.getParameter("StoryId");
    	RegisterResult registerResult  = new RegisterResult();
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		boolean result = false;
		int tmpId = db.findUserIdByStoryId(Integer.parseInt(storyId));
        if(action.equals("remember"))
        {
                result = db.remember(user.getUserId(), Integer.parseInt(storyId));
                if(result == true) {
                	registerResult.setMessage("user is remembered successfully");
                    //gamification
                    //Remembering story = +1 points for story owner
                    db.gamification(tmpId, 1, -1, 0);
                }else {
                	registerResult.setMessage("error occured");
                }
				registerResult.setResult(result);
        }
        else
        {
                result = db.dontRemember(user.getUserId(), Integer.parseInt(storyId));
                if(result == true) {
                	registerResult.setMessage("user is not remembered successfully");
                    db.gamification(tmpId, -1, -1, 0);
                }else {
                	registerResult.setMessage("error occured");
                }
                registerResult.setResult(result);
        }
        Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(registerResult));
		pw.flush();
		pw.close();
        
        
	}

}
