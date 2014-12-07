

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;




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
                // TODO Auto-generated catch block
                e1.printStackTrace();
        } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
        }
        
        request.getRequestDispatcher("story.jsp?storyId="+storyId).forward(request, response);
	}

}
