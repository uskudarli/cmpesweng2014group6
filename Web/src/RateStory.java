

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dutluk.DatabaseService;
import Dutluk.Story;

/**
 * Servlet implementation class RateStory
 */
@WebServlet("/RateStory")
public class RateStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RateStory() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String storyId = request.getSession().getAttribute("StoryID").toString();
		int userId = (int)request.getSession().getAttribute("UserID");
		int rate = Integer.parseInt(request.getParameter("rate"));
		
		DatabaseService db = new DatabaseService();
		db.insertRate(Integer.parseInt(storyId), userId, rate);
		Story story = db.findStorybyStoryId(Integer.parseInt(storyId));
		//gamification
		//Rating a story = +1 points for story owner
		db.gamification(story.getUserId(), 1, -1, 0);
        request.getRequestDispatcher("story.jsp?storyId="+storyId).forward(request, response);
	}

}
