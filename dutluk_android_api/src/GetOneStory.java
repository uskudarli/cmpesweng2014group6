

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetOneStory
 */
@WebServlet("/GetOneStory")
public class GetOneStory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOneStory() {
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
		String sql = "select * from Stories where StoryID =" + storyId + " limit 1";
		ResultSet rs = null;
	    Connection con;
		try {
			con = db.getConnection();
			Statement statement = con.createStatement() ;
			rs =statement.executeQuery(sql) ;
			int count = 1;
    	  	Story story = new Story();
			while(rs.next()) {
	    	  	story.setContent(rs.getString("Content"));
			  	story.setUserId(rs.getInt("UserID"));
			  	story.setThemeId(rs.getInt("ThemeID"));
			  	story.setStoryId(rs.getInt("StoryID"));
			  	story.setIsDeleted(rs.getInt("isDeleted"));
			  	story.setReportCount(rs.getInt("ReportCount"));
			  	story.setAvgRate(rs.getInt("AvgRate"));
	            count++;
			}
			
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			PrintWriter pw = response.getWriter();
			pw.print(gson.toJson(story));
			pw.flush();
			pw.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			LoginResult loginResult = new LoginResult();
			loginResult.setMessage("error occured");
			loginResult.setResult(false);
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			PrintWriter pw = response.getWriter();
			pw.print(gson.toJson(loginResult));
			pw.flush();
			pw.close();
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
