

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dutluk.DatabaseService;
import Dutluk.User;

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

		PreparedStatement statement = null;
		String sql = "INSERT INTO Comments (StoryID, UserID, Comment, IsDeleted, CreationDate, LastUpdate) VALUES (?, ?, ?, 0, NOW(), NOW())";
		
        try {
                Connection con = db.getConnection();
                statement = con.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(storyId));
                statement.setInt(2, Integer.parseInt(userId));
                statement.setString(3, comment);

                statement.execute();

        
		} catch (ClassNotFoundException e1) {
	        e1.printStackTrace();
		} catch (SQLException e1) {
	        e1.printStackTrace();
	}
        
        request.getRequestDispatcher("story.jsp?storyId="+storyId).forward(request, response);

	}
	
}
