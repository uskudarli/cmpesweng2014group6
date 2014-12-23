

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseService {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
	static final String DB_URL = "jdbc:mysql://titan.cmpe.boun.edu.tr:3306/database6";
	static final String USER = "project6";
	static final String PASS = "xXumhNf4";
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	public DatabaseService()
	{
		
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
}
