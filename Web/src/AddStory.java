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
        public boolean isValidDate(String dateString) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                try {
                        df.parse(dateString);
                        return true;
                } catch (ParseException e) {
                        return false;
                }
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
        {
                response.setContentType("text/html");


                HttpSession session = request.getSession();
                String email = session.getAttribute("email").toString();
                DatabaseService db = new DatabaseService();
                User user = db.findUserByEmail(email);

                String lat = request.getParameter("lat");
                String lon = request.getParameter("lng");
                lat = request.getSession().getAttribute("lati").toString();
                lon = request.getSession().getAttribute("long").toString();
                int placeId = 0;
                ResultSet rs = null;
                PreparedStatement statement = null;
                String sql = "SELECT * FROM Places WHERE Latitude ='"+lat+"'";
                try {
                        Connection con = db.getConnection();
                        Statement statement3 = con.createStatement() ;
                        rs =statement3.executeQuery(sql);
                } catch (ClassNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                }

                try {
                        while(rs.next())
                                placeId = rs.getInt(1);
                } catch (SQLException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                }
                if(!(placeId > 0))
                {
                        String placeName = request.getParameter("placeName");

                        statement = null;
                        String Sql = "INSERT INTO Places (Name, Longtitude, Latitude, CreationDate, LastUpdate) VALUES (?,?,?,NOW(),NOW())";
                        try {
                                Connection con = db.getConnection();
                                statement = con.prepareStatement(Sql);
                                statement.setString(1, placeName);
                                statement.setString(2, lon);
                                statement.setString(3, lat);

                                statement.execute();
                        } catch (ClassNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                        } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                        }


                        try {
                                Connection con = db.getConnection();
                                Statement statement2 = con.createStatement() ;
                                ResultSet rs3 =statement2.executeQuery("SELECT * FROM Places ORDER BY PlaceID DESC Limit 1") ;
                                while(rs3.next())
                                {
                                        placeId = rs3.getInt(1);
                                }
                        } catch (ClassNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                        } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                        }
                }

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

                
                if((storyTime!=null) && isValidDate(storyTime)){ 
                        story.setdateisAbsolute(true);
                        try {
                                story.setAbsoluteDateString(storyTime);
                        } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        story.setApproximateDate("");
                }else{
                        story.setdateisAbsolute(false);
                        story.setApproximateDate(storyTime);
                }


                int storyId = story.addStory();
                if((storyId != 0) && (placeId != 0))
                {
                        if(story.addStoryAndPlace(storyId, placeId))
                        {
                                request.setAttribute("error", "true");
                                request.setAttribute("message", "Story is added.");
                                request.getRequestDispatcher("index.jsp").forward(request, response);
                        }
                        else
                        {
                                request.setAttribute("error", "true");
                                request.setAttribute("message", "StoriesInPlaces error.");
                                request.getRequestDispatcher("index.jsp").forward(request, response);
                        }
                }
                else
                {

                        request.setAttribute("error", "true");
                        request.setAttribute("message", "Sorry, Something went wrong.");
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                }


        }
}