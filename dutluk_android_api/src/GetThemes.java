

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetThemes
 */
@WebServlet("/GetThemes")
public class GetThemes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetThemes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatabaseService db = new DatabaseService();
		String sql = "select * from Themes";
		ResultSet rs = null;
		Connection con;
		try {
			con = db.getConnection();
			Statement statement = con.createStatement();
			rs =statement.executeQuery(sql);
			ArrayList<Theme> themes = new ArrayList<Theme>();
			while(rs.next()) {
				Theme theme = new Theme();
				theme.setName(rs.getString("Name"));
				theme.setThemeID(rs.getInt("themeID"));
				themes.add(theme);
			}
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			PrintWriter pw = response.getWriter();
			pw.print(gson.toJson(themes));
			pw.flush();
			pw.close();
			con.close();
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
