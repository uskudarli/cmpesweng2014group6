import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dutluk.*;

@WebServlet("/AddStory")
public class AddStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		
		
		HttpSession session = request.getSession();
		String email = session.getAttribute("email").toString();
		DatabaseService db = new DatabaseService();
		User user = db.findUserByEmail(email);
		
		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
		
		
		Story story = new Story();
		
		story.setUserId(user.getUserID());
		story.setContent(request.getParameter("editStory").toString());
		story.setThemeId(Integer.parseInt(request.getParameter("theme")));
		story.setIsDeleted(0);
		story.setReportCount(0);
		story.setAvgRate(0);
		story.setCreatedOn(timestamp);
		story.setUpdatedOn(timestamp);
		String storyTime = request.getParameter("editStime");
		System.out.print(storyTime);
		if(storyTime!=null){
			try {
				story.setAbsoluteDate(storyTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		story.setApproximateDate(request.getParameter("editStime").toString());
		
		
		
		
		if(story.addStory())
		{
			
			request.setAttribute("error", "true");
			request.setAttribute("message", "Story is added.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		else
		{
		
			request.setAttribute("error", "true");
			request.setAttribute("message", "Sorry, Something went wrong.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		

	}
}
