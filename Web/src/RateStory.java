

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;





import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dutluk.DatabaseService;

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

		
		
		
		PreparedStatement statement = null;
		String sql = "INSERT INTO Rate (StoryID, UserID, Rate, CreationDate, LastUpdate) VALUES (?, ?, ?, NOW(), NOW())";
		
        try {
                Connection con = db.getConnection();
                statement = con.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(storyId));
                statement.setInt(2, userId);
                statement.setInt(3, rate);

                statement.execute();
               
                	int totalRate = 0;
                	int rateCount = 0;
                	Statement statement2 = con.createStatement() ;
        	        ResultSet rs = statement2.executeQuery("SELECT * FROM Rate WHERE StoryID = '"+storyId+"'");
        	        
        	        while(rs.next())
        	        {
        	        	totalRate += rs.getInt(3);
        	        	rateCount++;
        	        }
        	        
        	        int avgRate = totalRate / rateCount;
        	        statement = con.prepareStatement("UPDATE Stories SET AvgRate = ? WHERE StoryID = ?");
                    statement.setInt(1, avgRate);
                    statement.setInt(2, Integer.parseInt(storyId));
                    statement.execute();
        	        
                
        } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
        } catch (SQLException e1) {
                e1.printStackTrace();
        }
        
        request.getRequestDispatcher("story.jsp?storyId="+storyId).forward(request, response);
	}

}
