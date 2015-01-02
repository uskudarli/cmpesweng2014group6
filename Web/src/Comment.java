

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dutluk.DatabaseService;
import Dutluk.Story;

/**
 * Servlet implementation class Comment
 */
@WebServlet("/Comment")
public class Comment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Comment() {
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
		
		response.setContentType("text/html");
		String storyId = request.getSession().getAttribute("StoryID").toString();
		String userId =  request.getSession().getAttribute("UserID").toString();
		String comment = request.getParameter("comment").toString();
		
		DatabaseService db = new DatabaseService();
		Story story = db.findStorybyStoryId(Integer.parseInt(storyId));
		
		boolean result = db.insertComment(Integer.parseInt(storyId), Integer.parseInt(userId), comment);
		if(result){
			request.setAttribute("success", "true");
			if(story.getUserId() != Integer.parseInt(userId))
				//gamification
				//Adding new comment = +1 points to commenter, +2 points to story owner
				db.gamification(Integer.parseInt(userId), 1, story.getUserId(), 2);
		}
		else 
			request.setAttribute("error","true");
	
        
        request.getRequestDispatcher("story.jsp?storyId="+storyId).forward(request, response);

	}
	
}
