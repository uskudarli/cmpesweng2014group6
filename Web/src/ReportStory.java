

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dutluk.DatabaseService;
import Dutluk.Story;
import Dutluk.User;

/**
 * Servlet implementation class RememberStory
 */
@WebServlet("/ReportStory")
public class ReportStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportStory() {
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
		String action = request.getParameter("funct");
		String userMail = request.getSession().getAttribute("email").toString();
		DatabaseService db = new DatabaseService();
		User user = db.findUserByEmail(userMail);
		String storyId = request.getSession().getAttribute("StoryID").toString();
		
		if(action.equals("report"))
		{
			//System.out.print(storyId);
			//System.out.print(user.getUserID());
			db.report(user.getUserID(), Integer.parseInt(storyId));

		}
		
		String redirect = "story.jsp?storyId="+storyId;
		response.sendRedirect(redirect);
	}

}
