

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DatabaseService {
    private String JDBC_DRIVER; 
    private String DB_URL;
    private String USER;
    private String PASS;
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	public DatabaseService()
	{
		this.readConfig();
	}
	
    public void readConfig(){
        try{
                //So be careful, if it can't find your config.xml, it will catch an exception
                //with the message "CONFIGNOTFOUND". If you can't even login, seek for this
                //message in your console/catalina output.

                //Also, titan's tomcat returns  "/dutluk"
                //Instead of putting config.xml to that directory, I had change the wd
                //string to /home/project6/tomcat, where I put config.xml file

                String working_directory=new File("dutluk").getAbsolutePath();
                //System.out.println("CONFIG WD>>>\n"+working_directory+"\n<<<");
                //Titan's tomcat returns /. here, so I modify it by hand.
                if(working_directory.equals("/dutluk")) 
                	working_directory="/home/project6/tomcat";   
                XPath xpath = XPathFactory.newInstance().newXPath();
                InputSource inputSource = new InputSource(working_directory+"/config.xml");
                DB_URL = (String) xpath.evaluate("//config//jdbc//url",inputSource);
                JDBC_DRIVER = (String) xpath.evaluate("//config//jdbc//driver",inputSource);
                USER = (String) xpath.evaluate("//config//jdbc//username",inputSource);
                PASS = (String) xpath.evaluate("//config//jdbc//password",inputSource);
        }catch(Exception e){
                System.err.println("CONFIGNOTFOUND: " + e.getMessage());
        }
}
	
	Boolean executeSql(String sql)
	{
		Boolean result = false;
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			result = true;
		}catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
		}catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	   		try{
	   			if(stmt!=null)
	   				stmt.close();
	   		}catch(SQLException se2){
	   		}// nothing we can do
	   		try{
	   			if(conn!=null)
	   				conn.close();
	   		}catch(SQLException se){
	   			se.printStackTrace();
	   		}//end finally try
	    }
		return result;
	}
	
    public Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }
	
	public boolean checkUser(String mail, String password) {
		User user = findUserByEmail(mail);
		boolean b = password.equals(user.getPassword());
		if(b) {
			return true;
		}
		return false;
	}
    
    public User findUserById(int userId) {
		User user = new User();
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ? and IsDeleted = 0");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				user.setName(rs.getString("Name"));
				user.setEmail(rs.getString("Mail"));
				user.setUserId(rs.getInt("UserID"));
				user.setBirthdate(rs.getDate("Birthdate"));
				user.setPhone(rs.getString("Phone"));
				user.setExperiencePoint(rs.getInt("ExperiencePoint"));
				user.setLevel(rs.getInt("Level"));
				user.setIsDeleted(0);
				user.setPicId(rs.getInt("PicID"));
				user.setBio(rs.getString("Bio"));
				user.setPassword(rs.getString("Password"));
				user.setCreatedOn(rs.getDate("CreationDate"));
				user.setUpdatedOn(rs.getDate("LastUpdate"));
			}
			return user;
		}catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
		}catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	   		try{
	   			if(stmt!=null)
	   				stmt.close();
	   		}catch(SQLException se2){
	   		}// nothing we can do
	   		try{
	   			if(conn!=null)
	   				conn.close();
	   		}catch(SQLException se){
	   			se.printStackTrace();
	   		}//end finally try
	    }
		return null;
    }
    
    public Boolean UpdateProfile(User user)
    {
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("UPDATE Users SET Name = ? , Gender = ? ,  LastUpdate = NOW() WHERE UserID= ?");
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getGender().toString());
                    pstmt.setInt(3, user.getUserId());
                    pstmt.executeUpdate();
                    if(user.getBirthdate() != null){
                            pstmt = conn.prepareStatement("UPDATE Users SET Birthdate = ?, LastUpdate = NOW() WHERE UserID= ?");
                            pstmt.setDate(1, new java.sql.Date(user.getBirthdate().getTime()));
                            pstmt.setInt(2, user.getUserId());
                            pstmt.executeUpdate();
                    }
                    if(user.getPhone() != null){
                            pstmt = conn.prepareStatement("UPDATE Users SET Phone = ?, LastUpdate = NOW() WHERE UserID= ?");
                            pstmt.setString(1, user.getPhone());
                            pstmt.setInt(2,user.getUserId());
                            pstmt.executeUpdate();
                    }
                    if(user.getBio() != null){
                            pstmt = conn.prepareStatement("UPDATE Users SET Bio = ?, LastUpdate = NOW() WHERE UserID= ?");
                            pstmt.setString(1, user.getBio());
                            pstmt.setInt(2,user.getUserId());
                            pstmt.executeUpdate();
                    }
                    if(user.getPicId() > 0){
                            pstmt = conn.prepareStatement("UPDATE Users SET PicID = ?, LastUpdate = NOW() WHERE UserID= ?");
                            pstmt.setInt(1, user.getPicId());
                            pstmt.setInt(2, user.getUserId());
                            pstmt.executeUpdate();
                    }
                    return true;
            }catch(SQLException | ClassNotFoundException se){
                    //Handle errors for JDBC
                    se.printStackTrace();
            }catch(Exception e){
                    //Handle errors for Class.forName
                    e.printStackTrace();
            }finally{
                    //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
            }               
            return false;

    }
    
    public Theme findThemeById(int id) {
    	Theme theme = new Theme();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("SELECT * FROM Themes WHERE themeID = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				theme.setName(rs.getString("Name"));
				theme.setThemeID(id);
			}
			return theme;
		}catch(SQLException se){
		     //Handle errors for JDBC
		     se.printStackTrace();
		}catch(Exception e){
		     //Handle errors for Class.forName
		     e.printStackTrace();
		}finally{
		     //finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}
		return null;
    }
    
	User findUserByEmail(String mail)
	{
		User user = new User();
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("SELECT * FROM Users WHERE Mail = ? and IsDeleted = 0");
			pstmt.setString(1, mail);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				user.setName(rs.getString("Name"));
				user.setEmail(mail);
				user.setUserId(rs.getInt("UserID"));
				user.setBirthdate(rs.getDate("Birthdate"));
				user.setPhone(rs.getString("Phone")); 
				if(rs.getString("Gender")!=null&&rs.getString("Gender").equals("Male"))
                    user.setGender("male");
				else if(rs.getString("Gender")!=null&&rs.getString("Gender").equals("Female"))
                    user.setGender("female");
				else
                    user.setGender("unspecified");
				user.setExperiencePoint(rs.getInt("ExperiencePoint"));
				user.setLevel(rs.getInt("Level"));
				user.setIsDeleted(0);
				user.setPicId(rs.getInt("PicID"));
				user.setBio(rs.getString("Bio"));
				user.setPassword(rs.getString("Password"));
				user.setCreatedOn(rs.getDate("CreationDate"));
				user.setUpdatedOn(rs.getDate("LastUpdate"));
				user.setFollowerNumber(getFollowerNumber(rs.getInt("UserID")));
			}
			return user;
		}catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
		}catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	   		try{
	   			if(stmt!=null)
	   				stmt.close();
	   		}catch(SQLException se2){
	   		}// nothing we can do
	   		try{
	   			if(conn!=null)
	   				conn.close();
	   		}catch(SQLException se){
	   			se.printStackTrace();
	   		}//end finally try
	    }
		return null;
	}
	
    public Place findPlacebyPlaceId(int id)
    {
            Place place = new Place();
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM Places WHERE PlaceID = ?");
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    
                    while(rs.next())
                    {
                            place.setCreatedOn(rs.getDate("CreationDate"));
                            place.setUpdatedOn(rs.getDate("LastUpdate"));
                            place.setName(rs.getString("Name"));
                            place.setPlaceID(rs.getInt("PlaceID"));
                            place.setLongtitude(rs.getDouble("Longtitude"));
                            place.setLatitude(rs.getDouble("Latitude"));
                    }
                    return place;
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return place;
    }
    
    public int findPlaceIdByStoryId(int storyId) {
        int placeId = 0;
        try
        {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                pstmt = conn.prepareStatement("SELECT PlaceID FROM StoriesInPlaces WHERE StoryID = ?");
                pstmt.setInt(1, storyId);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                        placeId = rs.getInt("PlaceID");
                }
                return placeId;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
    }finally{
         //finally block used to close resources
                try{
                        if(stmt!=null)
                                stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                        if(conn!=null)
                                conn.close();
                }catch(SQLException se){
                        se.printStackTrace();
                }//end finally try
    }
        return 0;
    }
    
    public ArrayList<Place> findAllPlaces()
    {
            ArrayList<Place> places = new ArrayList<Place>();
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM Places");
                    ResultSet rs = pstmt.executeQuery();
                    
                    while(rs.next())
                    {
                            Place temp = new Place();
                            temp.setCreatedOn(rs.getDate("CreationDate"));
                            temp.setUpdatedOn(rs.getDate("LastUpdate"));
                            temp.setName(rs.getString("Name"));
                            temp.setPlaceID(rs.getInt("PlaceID"));
                            temp.setLongtitude(rs.getDouble("Longtitude"));
                            temp.setLatitude(rs.getDouble("Latitude"));
                            places.add(temp);
                    }
                    return places;
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return places;
    }
	
    
    public ArrayList<Comments> findAllComments(int storyId, int userId)
    {
            ArrayList<Comments> comments = new ArrayList<Comments>();
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String sql = "SELECT * FROM Comments where 1=1";
                    if(storyId != 0) {
                    	sql += " and storyID=" + storyId;
                    }
                    if(userId != 0) {
                    	sql += " and userID=" + userId;
                    }
                    pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery();
                    
                    while(rs.next())
                    {
                            Comments temp = new Comments();
                            temp.setCreationDate(rs.getDate("CreationDate"));
                            temp.setCommentID(rs.getInt("CommentID"));
                            temp.setComment(rs.getString("Comment"));
                            temp.setStoryID(rs.getInt("StoryID"));
                            temp.setUserID(rs.getInt("UserID"));
                            temp.setIsDeleted(rs.getInt("IsDeleted"));
                            temp.setLastUpdate(rs.getDate("LastUpdate"));
                            temp.setUserMail(findUserById(rs.getInt("UserID")).getEmail());
                            comments.add(temp);
                    }
                    return comments;
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return comments;
    }
    
    
    public Story findStorybyStoryId(int id)
    {
            Story story = new Story();
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM Stories WHERE StoryID = ?");
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    
                    while(rs.next())
                    {
                            story.setStoryId(id);
                            story.setCreatedOn(rs.getDate("CreationDate"));
                            story.setUpdatedOn(rs.getDate("LastUpdate"));
                            story.setUserId(rs.getInt("UserID"));
                            story.setThemeId(rs.getInt("ThemeID"));
                            if(rs.getString("StoryDateApproximate") != null)
                                    story.setApproximateDate(rs.getString("StoryDateApproximate"));
                            if(rs.getDate("StoryDateAbsolute") != null)
                                    story.setAbsoluteDate(rs.getDate("StoryDateAbsolute"));
                            story.setReportCount(rs.getInt("ReportCount"));
                            story.setIsDeleted(rs.getInt("IsDeleted"));
                            story.setContent(rs.getString("Content"));
                            story.setAvgRate(rs.getInt("AvgRate"));
    					  	story.setPlaceName(findPlacebyPlaceId(findPlaceIdByStoryId(rs.getInt("StoryID"))).getName());
    					  	story.setPlaceId(findPlaceIdByStoryId(rs.getInt("StoryID")));
                    }
                    return story;
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return story;
    }
    
    public void pictureProfileConnection(int userId, int pictureId) {
        try
        {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                pstmt = conn.prepareStatement("UPDATE Users SET PicID = ?, LastUpdate=NOW() WHERE UserID = ?");
                pstmt.setInt(1, pictureId);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
    }finally{
         //finally block used to close resources
                try{
                        if(stmt!=null)
                                stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                        if(conn!=null)
                                conn.close();
                }catch(SQLException se){
                        se.printStackTrace();
                }//end finally try
    }
    }
    
    public int insertPhoto(String filePath)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("INSERT INTO Pictures (Path, CreationDate, LastUpdate) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, filePath);
                    java.util.Date today = new java.util.Date();
                    pstmt.setTimestamp(2, new java.sql.Timestamp(today.getTime()));
                    pstmt.setTimestamp(3, new java.sql.Timestamp(today.getTime()));
                    pstmt.executeUpdate();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    while(rs.next())
                    {
		                String key = rs.getString(1);
		                return Integer.parseInt(key);  
                    }
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return 0;
    }
    
    
    public int insertComment(String comment, int storyId, int userId)
    {
            try
            {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                pstmt = conn.prepareStatement("INSERT INTO Comments (Comment, StoryID, UserID, CreationDate, LastUpdate) VALUES (?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, comment);
                pstmt.setInt(2, storyId);
                pstmt.setInt(3, userId);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                while(rs.next())
                {
                        
	                String key = rs.getString(1);
	                return Integer.parseInt(key);
                  
                }
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return 0;
    }
    
    public int insertPlace(String placeName, String lon, String lat)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("INSERT INTO Places (Name, Longtitude, Latitude, CreationDate, LastUpdate) VALUES (?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS);
                    pstmt.setString(1, placeName);
                    pstmt.setString(2, lon);
        pstmt.setString(3, lat);
                    pstmt.executeUpdate();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    while(rs.next())
                    {
                            
                String key = rs.getString(1);
                return Integer.parseInt(key);
                      
                    }
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return 0;
    }
    
	public void uploadPhoto() {

    }
    
    public void insertPhotoStoryConnection(int storyID, int pictureID)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("INSERT INTO PicturesInStories (PicID, StoryID) VALUES(?,?)");
                    pstmt.setInt(1, pictureID);
                    pstmt.setInt(2, storyID);
                    pstmt.executeUpdate();
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
    }
    
    public ArrayList<Story> getRecommendedStories(int themeId) {
    	ArrayList<Story> stories = new ArrayList<Story>();
    	String sql;
    	if(themeId == 0) {
    		sql = "SELECT * from Stories order by AvgRate desc limit 5";
    	}else {
    		sql = "SELECT * from Stories WHERE themeID = ? order by AvgRate desc limit 5";    		
    	}
        try
        {
	        Class.forName(JDBC_DRIVER);
	        conn = DriverManager.getConnection(DB_URL, USER, PASS);
	        pstmt = conn.prepareStatement(sql);
	        if(themeId > 0)
	        	pstmt.setInt(1, themeId);
	        ResultSet rs = pstmt.executeQuery();
	        while(rs.next())
	        	stories.add(findStorybyStoryId(rs.getInt("StoryID")));
	        return stories;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
		}finally{
		     //finally block used to close resources
		    try{
		            if(stmt!=null)
		                    stmt.close();
		    }catch(SQLException se2){
		    }// nothing we can do
		    try{
		            if(conn!=null)
		                    conn.close();
		    }catch(SQLException se){
		            se.printStackTrace();
		    }//end finally try
		}
        return null;
    }
    
    public String getProfilePicturePath(int userId) {
    	String path = "";
        try
        {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            pstmt = conn.prepareStatement("SELECT DISTINCT(Path) from Pictures WHERE PicID IN (SELECT PicID FROM Users WHERE UserID = ?)");
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            	path = "http://titan.cmpe.boun.edu.tr:8085/image/" + rs.getString("Path");
            return path;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
        }finally{
             //finally block used to close resources
            try{
                    if(stmt!=null)
                            stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                    if(conn!=null)
                            conn.close();
            }catch(SQLException se){
                    se.printStackTrace();
            }//end finally try
        }    	
    	return null;
    }
    
    public ArrayList<String> getStoryPicturePath(int storyId) {
    	ArrayList<String> paths = new ArrayList<String>();
        try
        {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            pstmt = conn.prepareStatement("SELECT DISTINCT(Path) from Pictures WHERE PicID IN (SELECT PicID FROM PicturesInStories WHERE StoryID = ?)");
            pstmt.setInt(1, storyId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            	paths.add("http://titan.cmpe.boun.edu.tr:8085/image/" + rs.getString("Path"));
            return paths;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
        }finally{
             //finally block used to close resources
            try{
                    if(stmt!=null)
                            stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                    if(conn!=null)
                            conn.close();
            }catch(SQLException se){
                    se.printStackTrace();
            }//end finally try
        }    	
    	return null;
    }
    
    public ArrayList<String> getPicturePathsOfaPlace(int placeID){
        ArrayList<String> paths = new ArrayList<String>();
        try
        {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            pstmt = conn.prepareStatement("SELECT DISTINCT(Path) from Pictures WHERE PicID IN (SELECT PicID FROM PicturesInStories WHERE StoryID IN (SELECT StoryID FROM StoriesInPlaces WHERE PlaceID = ?))");
            pstmt.setInt(1, placeID);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
                    paths.add(rs.getString("Path"));
            return paths;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
        }finally{
             //finally block used to close resources
            try{
                    if(stmt!=null)
                            stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                    if(conn!=null)
                            conn.close();
            }catch(SQLException se){
                    se.printStackTrace();
            }//end finally try
        }
            return null;
    }
    
    public void subscribe(int followerId, int followedId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM SubscriptionsToUsers WHERE FollowerID = ? AND FollowedID = ?");
                    pstmt.setInt(1, followerId);
                    pstmt.setInt(2, followedId);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            int isActive = rs.getInt("IsActive");
                            //user subscribed before then unsubscribed, make isActive 1 to subscribe again
                            if(isActive == 0)
                            {
                                    try
                                    {
                                            pstmt = conn.prepareStatement("UPDATE SubscriptionsToUsers SET IsActive = 1, LastUpdate=? WHERE FollowerID = ? AND FollowedID = ?");
                                            java.util.Date today = new java.util.Date();
                                            pstmt.setTimestamp(1, new java.sql.Timestamp(today.getTime()));
                                            pstmt.setInt(2, followerId);
                                            pstmt.setInt(3, followedId);
                                            pstmt.executeUpdate();
                                    }catch(SQLException se)
                                    {
                                            se.printStackTrace();
                                    }
                            }
                    }else // subscribe for the first time
                    {
                            try
                            {
                                    pstmt = conn.prepareStatement("INSERT INTO SubscriptionsToUsers (FollowerID, FollowedID, CreationDate, LastUpdate, IsActive) VALUES(?,?,?,?,?)");
                                    pstmt.setInt(1, followerId);
                                    pstmt.setInt(2, followedId);
                                    java.util.Date today = new java.util.Date();
                                    pstmt.setTimestamp(3, new java.sql.Timestamp(today.getTime()));
                                    pstmt.setTimestamp(4, new java.sql.Timestamp(today.getTime()));
                                    pstmt.setInt(5, 1);
                                    pstmt.executeUpdate();
                            }catch(SQLException se)
                            {		
                                    se.printStackTrace();
                            }
                    }
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
    }

    public int getFollowerNumber(int userId) {
    	int result = 0;
        try
        {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                pstmt = conn.prepareStatement("SELECT count(1) FROM SubscriptionsToUsers WHERE FollowedID = ? and IsActive = 1");
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                        result = rs.getInt("count(1)");
                }
                return result;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
    }finally{
         //finally block used to close resources
                try{
                        if(stmt!=null)
                                stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                        if(conn!=null)
                                conn.close();
                }catch(SQLException se){
                        se.printStackTrace();
                }//end finally try
    }
        return 0;
    }
    
    public int getRememberedNumber(int storyId) {
    	int result = 0;
        try
        {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                pstmt = conn.prepareStatement("SELECT count(1) FROM IRememberThat WHERE StoryID = ?");
                pstmt.setInt(1, storyId);
                ResultSet rs = pstmt.executeQuery();
                
                while(rs.next())
                {
                        result = rs.getInt("count(1)");
                }
                return result;
                
        }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
        }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
    }finally{
         //finally block used to close resources
                try{
                        if(stmt!=null)
                                stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                        if(conn!=null)
                                conn.close();
                }catch(SQLException se){
                        se.printStackTrace();
                }//end finally try
    }
        return 0;
    }
    
    public boolean isRemembered(int userId, int storyId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM IRememberThat WHERE StoryID = ? AND UserID = ?");
                    pstmt.setInt(1, storyId);
                    pstmt.setInt(2, userId);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            return true;
                    }
                    else
                            return false;
             
            }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
            return false;
            }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
            return false;
            }finally{
            //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
       }
    }
    
    public boolean remember(int userId, int storyId)
    {
    		boolean result = false;
            try
            {
                    
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("INSERT INTO IRememberThat (StoryID, UserID, CreationDate, LastUpdate) VALUES(?,?,NOW(),NOW())");
                    pstmt.setInt(1, storyId);
                    pstmt.setInt(2, userId);
                    pstmt.execute();
                    result = true;
            }catch(SQLException se)
            {
            	result = false;
                se.printStackTrace();
            }finally{
             //finally block used to close resources
                    try{
                        if(stmt!=null)
                            stmt.close();
                    }catch(SQLException se2){
                    	result = false;
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                    		result = false;
                            se.printStackTrace();
                    }//end finally try
        }
            return result;
    }
    
    public boolean dontRemember(int userId, int storyId)
    {
    	boolean result = false;
        try
        {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            pstmt = conn.prepareStatement("DELETE FROM IRememberThat WHERE StoryID = ? AND UserID = ?");
            pstmt.setInt(1, storyId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            result = true;
        }catch(SQLException se)
        {
        	result = false;
            se.printStackTrace();
        }finally{
         //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            	result = false;
            }// nothing we can do
            try{
                if(conn!=null)
                        conn.close();
            }catch(SQLException se){
            	result = false;
                    se.printStackTrace();
            }//end finally try
        }
        return result;
    }
    
    public Boolean isFollowing(int followerId, int followedId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM SubscriptionsToUsers WHERE FollowerID = ? AND FollowedID = ? AND IsActive = ?");
                    pstmt.setInt(1, followerId);
                    pstmt.setInt(2, followedId);
                    pstmt.setInt(3, 1);
                    ResultSet rs = pstmt.executeQuery();
                    //following
                    if(rs.next())
                    {
                            return true;
                    }else // not following
                    {
                            return false;
                    }
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return false;
    }
    
    public Boolean isFollowingPlace(int followerId, int followedId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM SubscriptionsToPlaces WHERE FollowerID = ? AND FollowedID = ? AND IsActive = ?");
                    pstmt.setInt(1, followerId);
                    pstmt.setInt(2, followedId);
                    pstmt.setInt(3, 1);
                    ResultSet rs = pstmt.executeQuery();
                    //following
                    if(rs.next())
                    {
                            return true;
                    }else // not following
                    {
                            return false;
                    }
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return false;
    }
    
    public void unsubscribe(int followerId, int followedId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM SubscriptionsToUsers WHERE FollowerID = ? AND FollowedID = ? AND IsActive = ?");
                    pstmt.setInt(1, followerId);
                    pstmt.setInt(2, followedId);
                    pstmt.setInt(3, 1);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            
                            try
                            {
                                    pstmt = conn.prepareStatement("UPDATE SubscriptionsToUsers SET IsActive = 0, LastUpdate=? WHERE FollowerID = ? AND FollowedID = ?");
                                    java.util.Date today = new java.util.Date();
                                    pstmt.setTimestamp(1, new java.sql.Timestamp(today.getTime()));
                                    pstmt.setInt(2, followerId);
                                    pstmt.setInt(3, followedId);
                                    pstmt.executeUpdate();
                            }catch(SQLException se)
                            {
                                    se.printStackTrace();
                            }
                            
                    }
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
    }
    
    
    public void subscribePlace(int followerId, int followedId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM SubscriptionsToPlaces WHERE FollowerID = ? AND FollowedID = ?");
                    pstmt.setInt(1, followerId);
                    pstmt.setInt(2, followedId);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            int isActive = rs.getInt("IsActive");
                            //user subscribed before then unsubscribed, make isActive 1 to subscribe again
                            if(isActive == 0)
                            {
                                    try
                                    {
                                            pstmt = conn.prepareStatement("UPDATE SubscriptionsToPlaces SET IsActive = 1, LastUpdate=? WHERE FollowerID = ? AND FollowedID = ?");
                                            java.util.Date today = new java.util.Date();
                                            pstmt.setTimestamp(1, new java.sql.Timestamp(today.getTime()));
                                            pstmt.setInt(2, followerId);
                                            pstmt.setInt(3, followedId);
                                            pstmt.executeUpdate();
                                    }catch(SQLException se)
                                    {
                                            se.printStackTrace();
                                    }
                            }
                    }else // subscribe for the first time
                    {
                            try
                            {
                                    pstmt = conn.prepareStatement("INSERT INTO SubscriptionsToPlaces (FollowerID, FollowedID, CreationDate, LastUpdate, IsActive) VALUES(?,?,?,?,?)");
                                    pstmt.setInt(1, followerId);
                                    pstmt.setInt(2, followedId);
                                    java.util.Date today = new java.util.Date();
                                    pstmt.setTimestamp(3, new java.sql.Timestamp(today.getTime()));
                                    pstmt.setTimestamp(4, new java.sql.Timestamp(today.getTime()));
                                    pstmt.setInt(5, 1);
                                    pstmt.executeUpdate();
                            }catch(SQLException se)
                            {
                                    se.printStackTrace();
                            }
                    }
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
    }

    public void unsubscribePlace(int followerId, int followedId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM SubscriptionsToPlaces WHERE FollowerID = ? AND FollowedID = ? AND IsActive = ?");
                    pstmt.setInt(1, followerId);
                    pstmt.setInt(2, followedId);
                    pstmt.setInt(3, 1);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            
                            try
                            {
                                    pstmt = conn.prepareStatement("UPDATE SubscriptionsToPlaces SET IsActive = 0, LastUpdate=? WHERE FollowerID = ? AND FollowedID = ?");
                                    java.util.Date today = new java.util.Date();
                                    pstmt.setTimestamp(1, new java.sql.Timestamp(today.getTime()));
                                    pstmt.setInt(2, followerId);
                                    pstmt.setInt(3, followedId);
                                    pstmt.executeUpdate();
                            }catch(SQLException se)
                            {
                                    se.printStackTrace();
                            }
                            
                    }
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
    }

    public String pathByPicId(int picId)
    {
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM Pictures WHERE PicID = ?");
                    pstmt.setInt(1, picId);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            return rs.getString("Path");
                            
                    }
                    return null;
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return null;
    }

    public void deleteProfilePic(int userId){
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("UPDATE Users SET PicID = ?, LastUpdate=NOW() WHERE UserID = ?");
                    pstmt.setInt(1, 0);
                    pstmt.setInt(2, userId);
                    pstmt.executeUpdate();
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
    }
    
    public String pictureNameGenerator()
    {
            String result = null;
            try
            {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    pstmt = conn.prepareStatement("SELECT * FROM Pictures ORDER BY PicID DESC");

                    ResultSet rs = pstmt.executeQuery();
                    int lastPicId = 0;
                    if(rs.next())
                    {
                            lastPicId = rs.getInt("PicID");
                 
                    }
                    lastPicId++;
                    result = lastPicId + ".jpg";
                    return result;
                    
            }catch(SQLException se){
             //Handle errors for JDBC
             se.printStackTrace();
            }catch(Exception e){
             //Handle errors for Class.forName
             e.printStackTrace();
        }finally{
             //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
        }
            return result;
    }
    
    //format and separate tags from string, write to database and return inserted tag ids
    public ArrayList<String> insertTags(String tagString)
    {
            ArrayList<String> tagIds = new ArrayList<String>();
            tagString = tagString.replaceAll("(\\s)*,(\\s)*", ","); // erase spaces before and after comma
            tagString = tagString.replaceAll("(\\s)+$", ""); // erase spaces at the end of the string
            tagString = tagString.replaceAll("(\\s)+", " "); // replace multiple spaces between words with one space
            String[] tags = tagString.split(",");
            for(String tag: tags)
            {
                    try{
                            conn = getConnection();
                            pstmt = conn.prepareStatement("SELECT * FROM Tags WHERE Name=?");
                            pstmt.setString(1, tag);
                            ResultSet rs = pstmt.executeQuery();
                            if(rs.next())
                            {
                                    tagIds.add(Integer.toString(rs.getInt("TagID")));
                            }else
                            {
                                    PreparedStatement ps = conn.prepareStatement("INSERT INTO Tags (Name, CreationDate, LastUpdate) VALUES(?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS);
                                    ps.setString(1, tag);
                                    ps.executeUpdate();
                                    ResultSet rs2 = ps.getGeneratedKeys();
                                    while(rs2.next())
                                    {
                                            tagIds.add(rs2.getString(1));
                                
                                    }
                            }
                            
                    }catch(SQLException | ClassNotFoundException se)
                    {
                            se.printStackTrace();
                    }
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return tagIds;
    }

    public void insertTagStoryConnection(ArrayList<String> tagIds, int storyId)
    {
            for(String tagId:tagIds)
            {
                    try{
                            conn = getConnection();
                            pstmt = conn.prepareStatement("SELECT * FROM TagsInStories WHERE TagID=? AND StoryID=?");
                            pstmt.setInt(1, Integer.parseInt(tagId));
                            pstmt.setInt(2, storyId);
                            ResultSet rs = pstmt.executeQuery();
                            if(!rs.next())
                            {
                                    pstmt = conn.prepareStatement("INSERT INTO TagsInStories (TagID, StoryID) VALUES(?,?)");
                                    pstmt.setInt(1, Integer.parseInt(tagId));
                                    pstmt.setInt(2, storyId);
                                    pstmt.executeUpdate();
                            }
                            
                    }catch(SQLException | ClassNotFoundException se)
                    {
                            se.printStackTrace();
                    }
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
    }
    
    public void insertTagPlaceConnection(ArrayList<String> tagIds, int placeId)
    {
            for(String tagId:tagIds)
            {
                    try{
                            conn = getConnection();
                            pstmt = conn.prepareStatement("SELECT * FROM TagsInPlaces WHERE TagID=? AND PlaceID=?");
                            pstmt.setInt(1, Integer.parseInt(tagId));
                            pstmt.setInt(2, placeId);
                            ResultSet rs = pstmt.executeQuery();
                            if(!rs.next())
                            {
                                    pstmt = conn.prepareStatement("INSERT INTO TagsInPlaces (TagID, PlaceID) VALUES(?,?)");
                                    pstmt.setInt(1, Integer.parseInt(tagId));
                                    pstmt.setInt(2, placeId);
                                    pstmt.executeUpdate();
                            }
                            
                    }catch(SQLException | ClassNotFoundException se)
                    {
                            se.printStackTrace();
                    }
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
    }

    public ArrayList<String> getSimilar(String query) throws IOException{
        ArrayList<String> sim = new ArrayList<String>();

        String requestURL = "https://www.googleapis.com/freebase/v1/search?lang=en&indent=true&key=AIzaSyDaFirSUUeGlNSg7bxGcyqdf9L8NzHKA10&query=" + query;
        URL freebaseRequest = new URL(requestURL);
        URLConnection connection = freebaseRequest.openConnection();  
        connection.setDoOutput(true);  

        Scanner scanner = new Scanner(freebaseRequest.openStream());
        String response = scanner.useDelimiter("\\Z").next();
        scanner.close();
        JsonElement jelement = new JsonParser().parse(response);
        JsonObject  jobject = jelement.getAsJsonObject();
        JsonArray jsonArray = jobject.getAsJsonArray("result");
        int tmp = 0;
        if(jsonArray.size() > 5)
        	tmp = 5;
        else
        	tmp = jsonArray.size();
        for(int i = 0; i < tmp; i++) {
            jobject = jsonArray.get(i).getAsJsonObject();
            jobject = jobject.getAsJsonObject("notable");
            System.out.println(jobject.get("name").getAsString());
            sim.add(jobject.get("name").getAsString());
        }
        return sim;
}
    
    public ArrayList<String> findTagIds(String text)
    {
            ArrayList<String> tagIds = new ArrayList<String>();

            
                    try{
                            conn = getConnection();
                            pstmt = conn.prepareStatement("SELECT * FROM Tags WHERE Name LIKE ?");
                            pstmt.setString(1, "%" + text + "%");
                            ResultSet rs = pstmt.executeQuery();
                            while(rs.next())
                            {
                                    tagIds.add(Integer.toString(rs.getInt("TagID")));
                            }
                            
                    }catch(SQLException | ClassNotFoundException se)
                    {
                            se.printStackTrace();
                    }
                    if(conn != null) {
                    	try {
        					conn.close();
        				} catch (SQLException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
                    }
            return tagIds;
    }
    
    public ArrayList<Place> findPlacesFromTags(String text)
    {
            ArrayList<Place> places = new ArrayList<Place>();
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("SELECT * FROM Places WHERE PlaceID IN (SELECT PlaceID from TagsInPlaces where TagID IN (SELECT TagID FROM Tags WHERE Name LIKE ?))");
                    pstmt.setString(1,  "%" + text + "%");
                    ResultSet rs = pstmt.executeQuery();
                    while(rs.next())
                    {
                            places.add(findPlacebyPlaceId(rs.getInt("PlaceID")));
                    }
                    return places;
            }catch(SQLException | ClassNotFoundException se)
            {
                    se.printStackTrace();
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return places;
    }
    
    public ArrayList<Story> findStoriesFromTags(String text)
    {
            
            ArrayList<Story> stories = new ArrayList<Story>();
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("SELECT * FROM Stories WHERE StoryID IN (SELECT StoryID from TagsInStories where TagID IN (SELECT TagID FROM Tags WHERE Name LIKE ?)) AND IsDeleted = ?");
                    pstmt.setString(1,  "%" + text + "%");
                    pstmt.setInt(2, 0);
                    ResultSet rs = pstmt.executeQuery();
                    while(rs.next())
                    {
                            stories.add(findStorybyStoryId(rs.getInt("StoryID")));
                    }
                    return stories;
            }catch(SQLException | ClassNotFoundException se)
            {
                    se.printStackTrace();
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return stories;
    }
    
    public ArrayList<User> searchUser(String text)
    {
            if(text == null || text.equals(""))
                    return new ArrayList<User>();
            text = text.toLowerCase();
            ArrayList<User> users = new ArrayList<User>();
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("SELECT DISTINCT(UserID) FROM Users WHERE Name LIKE ?");
                    pstmt.setString(1,  "%" + text + "%");
                    ResultSet rs = pstmt.executeQuery();
                    while(rs.next())
                    {
                            users.add(findUserById(rs.getInt("UserID")));
                    }
                    return users;
            }catch(SQLException | ClassNotFoundException se){
                    //Handle errors for JDBC
                    se.printStackTrace();
            }catch(Exception e){
                    //Handle errors for Class.forName
                    e.printStackTrace();
            }finally{
                    //finally block used to close resources
                    try{
                            if(stmt!=null)
                                    stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                            if(conn!=null)
                                    conn.close();
                    }catch(SQLException se){
                            se.printStackTrace();
                    }//end finally try
            }

            return users;

    }
    
    //search in the content and tags of a story
    public ArrayList<Story> searchStory(String text)
    {
            if(text == null || text.equals(""))
                    return new ArrayList<Story>();
            text = text.toLowerCase();
            ArrayList<Story> stories = new ArrayList<Story>();
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("SELECT DISTINCT(StoryID) FROM Stories WHERE (Content LIKE ? OR StoryID IN (SELECT StoryID from TagsInStories where TagID IN (SELECT TagID FROM Tags WHERE Name LIKE ?))) AND IsDeleted = ?");
                    pstmt.setString(1,  "%" + text + "%");
                    pstmt.setString(2,  "%" + text + "%");
                    pstmt.setInt(3, 0);
                    ResultSet rs = pstmt.executeQuery();
                    while(rs.next())
                    {
                            stories.add(findStorybyStoryId(rs.getInt("StoryID")));
                    }
                    return stories;
            }catch(SQLException | ClassNotFoundException se)
            {
                    se.printStackTrace();
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return stories;
            
    }
    
    //search in the name and tags of a place
    public ArrayList<Place> searchPlace(String text)
    {
            if(text == null || text.equals(""))
                    return new ArrayList<Place>();
            text = text.toLowerCase();
            ArrayList<Place> places = new ArrayList<Place>();
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("SELECT DISTINCT(PlaceID) FROM Places WHERE Name LIKE ? OR PlaceID IN (SELECT PlaceID from TagsInPlaces where TagID IN (SELECT TagID FROM Tags WHERE Name LIKE ?))");
                    pstmt.setString(1,  "%" + text + "%");
                    pstmt.setString(2,  "%" + text + "%");
                    ResultSet rs = pstmt.executeQuery();
                    while(rs.next())
                    {
                            places.add(findPlacebyPlaceId(rs.getInt("PlaceID")));
                    }
                    return places;
            }catch(SQLException | ClassNotFoundException se)
            {
                    se.printStackTrace();
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return places;
            
    }
    
    public int getRate(int userId, int storyId)
    {
            int rate = 0;
            try{
                    conn = getConnection();
                    pstmt = conn.prepareStatement("SELECT * FROM Rate WHERE StoryID = ? AND UserID = ?");
                    pstmt.setInt(1, storyId);
                    pstmt.setInt(2, userId);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next())
                    {
                            rate = rs.getInt("Rate");
                    }
                    return rate;
            }catch(SQLException | ClassNotFoundException se)
            {
                    se.printStackTrace();
            }
            if(conn != null) {
            	try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return rate;
    }
    
    
	public void executeStmt(PreparedStatement stmt)
	{
		
	}
	
	public int findUserIdByStoryId(int storyId) {
		int userId = 0;
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("SELECT UserID FROM Stories WHERE StoryID = ? and IsDeleted = 0");
			pstmt.setInt(1, storyId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				userId = rs.getInt("UserID");
			}
			return userId;
		}catch(SQLException se){
	         //Handle errors for JDBC
	         se.printStackTrace();
		}catch(Exception e){
	         //Handle errors for Class.forName
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
	   		try{
	   			if(stmt!=null)
	   				stmt.close();
	   		}catch(SQLException se2){
	   		}// nothing we can do
	   		try{
	   			if(conn!=null)
	   				conn.close();
	   		}catch(SQLException se){
	   			se.printStackTrace();
	   		}//end finally try
	    }
		return 0;
	}
	
    public int calculateLevel(int xp)
    {
            int level = (int) Math.sqrt(xp);
            level = level-2;
            return level;
    }
    
    public void setXPandLevel(int userid, int point)
    {
            if(userid != -1 && point != 0)
            {
                    try{
                            conn = getConnection();
                            pstmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
                            pstmt.setInt(1, userid);
                            ResultSet rs = pstmt.executeQuery();
                            int xp = 0;
                            if(rs.next())
                            {
                                    xp = rs.getInt("ExperiencePoint");
                            }
                            xp += point;
                            int level = 0;
                            if(xp >= 9)
                            {
                                    level = calculateLevel(xp);
                            }
                            pstmt = conn.prepareStatement("UPDATE Users SET ExperiencePoint = ?, LastUpdate=NOW(), Level = ? WHERE UserID = ?");
                            pstmt.setInt(1, xp);
                            pstmt.setInt(2, level);
                            pstmt.setInt(3, userid);
                            pstmt.executeUpdate();
                    }catch(SQLException | ClassNotFoundException se){
                            //Handle errors for JDBC
                            se.printStackTrace();
                    }catch(Exception e){
                            //Handle errors for Class.forName
                            e.printStackTrace();
                    }finally{
                            //finally block used to close resources
                            try{
                                    if(stmt!=null)
                                            stmt.close();
                            }catch(SQLException se2){
                            }// nothing we can do
                            try{
                                    if(conn!=null)
                                            conn.close();
                            }catch(SQLException se){
                                    se.printStackTrace();
                            }//end finally try
                    }
            }
    }
    
    public void gamification(int userid1, int point1, int userid2, int point2)
    {
            setXPandLevel(userid1, point1);
            setXPandLevel(userid2, point2);
    }
}
