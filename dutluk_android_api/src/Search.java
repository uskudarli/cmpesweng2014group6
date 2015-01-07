

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
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
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
		String searchedTerm = request.getParameter("term");
		String func = request.getParameter("func");
		String isSem = request.getParameter("isSemantic");
		boolean isSemantic = false;
		if(isSem != null && isSem.equals("true")) {
			isSemantic = true;
		}else {
			isSemantic = false;
		}
		DatabaseService db = new DatabaseService();
		ArrayList<String> similars;
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		if(func.equals("story")) {
			ArrayList<Story> stories = db.searchStory(searchedTerm);
			if(stories.isEmpty() && isSemantic){
				similars = db.getSimilar(searchedTerm);
				for(int i = 0; i < similars.size(); i++) {
					stories.addAll(db.searchStory(similars.get(i)));
				}
			}
			int i = stories.size();
			GetStoryResult [] result = new GetStoryResult[i];
			for(int j = 0; j < i; j++ ) {
				result[j] = new GetStoryResult();
			}
			for(int j = 0; j < i; j++) {
				User tmpUser = db.findUserById(stories.get(j).getUserId());
				result[j].setStoryId(stories.get(j).getStoryId());
				result[j].setContent(stories.get(j).getContent());
				result[j].setThemeId(stories.get(j).getThemeId());
				result[j].setReportCount(stories.get(j).getReportCount());
				result[j].setAvgRate(stories.get(j).getAvgRate());
				result[j].setCreatedOn(stories.get(j).getCreatedOn());
				result[j].setUpdatedOn(stories.get(j).getUpdatedOn());
				result[j].setAbsoluteDate(stories.get(j).getAbsoluteDate());
				result[j].setApproximateDate(stories.get(j).getApproximateDate());
				result[j].setMail(tmpUser.getEmail());
				result[j].setPlaceName(stories.get(j).getPlaceName());
				result[j].setPlaceId(stories.get(j).getPlaceId());
			}
			pw.print(gson.toJson(result));
		}
		if(func.equals("place")) {
			ArrayList<Place> places = db.searchPlace(searchedTerm);
			if(places.isEmpty() && isSemantic) {
				similars = db.getSimilar(searchedTerm);
				for(int i = 0; i < similars.size(); i++) {
					places.addAll(db.searchPlace(similars.get(i)));
				}
			}
			pw.print(gson.toJson(places));
		}
		if(func.equals("user")) {
			ArrayList<User> users = db.searchUser(searchedTerm);
			if(users.isEmpty() && isSemantic) {
				similars = db.getSimilar(searchedTerm);
				for(int i = 0; i < similars.size(); i++) {
					users.addAll(db.searchUser(similars.get(i)));
				}
			}
			pw.print(gson.toJson(users));
		}
		pw.flush();
		pw.close();
		
	}

}
