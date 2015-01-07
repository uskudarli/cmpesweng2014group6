

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.ant.FindLeaksTask;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetStory
 */
@WebServlet("/GetStory")
public class GetStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetStory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Story story = new Story();
		User user = new User();
		String mail = request.getParameter("email");
		int type = Integer.parseInt(request.getParameter("type"));
		DatabaseService db = new DatabaseService();
		user = db.findUserByEmail(mail);
		Story [] stories = story.getStories(user.getUserId(), type);
		int i;
		for(i=1;i<100;i++) {
			if(stories[i] == null) {
				break;
			}
		}
		i--;
		Story [] stories2 = new Story[i];
		for(int j = 0; j<i; j++) {
			stories2[j] = stories[j+1];
		}
		GetStoryResult [] result = new GetStoryResult[i];
		for(int j = 0; j < i; j++ ) {
			result[j] = new GetStoryResult();
		}
		for(int j = 0; j < i; j++) {
			User tmpUser = db.findUserById(stories2[j].getUserId());
			result[j].setStoryId(stories2[j].getStoryId());
			result[j].setContent(stories2[j].getContent());
			result[j].setThemeId(stories2[j].getThemeId());
			result[j].setReportCount(stories2[j].getReportCount());
			result[j].setAvgRate(stories2[j].getAvgRate());
			result[j].setCreatedOn(stories2[j].getCreatedOn());
			result[j].setUpdatedOn(stories2[j].getUpdatedOn());
			result[j].setAbsoluteDate(stories2[j].getAbsoluteDate());
			result[j].setPlaceName(stories2[j].getPlaceName());
			result[j].setPlaceId(stories2[j].getPlaceId());
			result[j].setApproximateDate(stories2[j].getApproximateDate());
			result[j].setMail(tmpUser.getEmail());
			result[j].setRememberNumber(db.getRememberedNumber(stories2[j].getStoryId()));
		}
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(result));
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
