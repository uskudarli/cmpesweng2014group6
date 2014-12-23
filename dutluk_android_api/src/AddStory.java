

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class AddStory
 */
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
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Story story =  new Story();
		Place place = new Place();
		User user = new User();
		String email = request.getParameter("mail");
		DatabaseService db = new DatabaseService();
		user = db.findUserByEmail(email);
		String content = request.getParameter("story");
		story.setContent(content);
		Double lat = Double.parseDouble(request.getParameter("lat"));
		Double lng = Double.parseDouble(request.getParameter("lng"));
		String date = request.getParameter("time");
		String placeTag = request.getParameter("placeTag");
		String storyTag = request.getParameter("storyTag");
		story.setApproximateDate(date);
		story.setUserId(user.getUserId());
		int storyId = story.addStory();
		place.setLatitude(lat);
		place.setLongtitude(lng);
		place.setName(placeTag);
		int placeId = place.addPlace();
		story.addStoryAndPlace(storyId, placeId);
		LoginResult loginResult = new LoginResult();
		if(placeTag != "") {
			ArrayList<String> list = db.insertTags(placeTag);
			db.insertTagPlaceConnection(list, placeId);
		}
		if(storyTag != "") {
			ArrayList<String> list = db.insertTags(storyTag);
			db.insertTagStoryConnection(list, storyId);
		}
		if(placeId>0){
			loginResult.setMessage("successful");
			loginResult.setResult(true);
		}
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(loginResult));
		pw.flush();
		pw.close();
	}

}
