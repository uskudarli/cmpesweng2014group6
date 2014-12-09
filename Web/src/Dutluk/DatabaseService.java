package Dutluk;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;



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
	
	public User findUserByEmail(String mail)
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
				user.setUserID(rs.getInt("UserID"));
				Date dateIn = rs.getDate("Birthdate");
				if(dateIn!=null) user.setBirthdate(dateIn);
				user.setPhone(rs.getString("Phone"));
				user.setExperiencePoint(rs.getInt("ExperiencePoint"));
				user.setLevel(rs.getInt("Level"));
				user.setIsDeleted(0); //TODO why?
				user.setPicID(rs.getInt("PicID"));
				user.setBio(rs.getString("Bio"));
				user.setPassword(rs.getString("Password"));
				user.setCreatedOn(rs.getDate("CreationDate"));
				user.setUpdatedOn(rs.getDate("LastUpdate"));
				if(rs.getString("Gender")!=null&&rs.getString("Gender").equals("Male"))
					user.setGender(User.Gender.Male);
				else if(rs.getString("Gender")!=null&&rs.getString("Gender").equals("Female"))
					user.setGender(User.Gender.Female);
				else
					user.setGender(User.Gender.Unspecified);
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
		return user; //THIS was return null, which give 500 nullpointerexception on each call.
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
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		return conn;
	}
	
	
	public Boolean register(User user)
	{
		User temp = findUserByEmail(user.getEmail());
		if(temp.getEmail() != null)
			return false;
		else
		{
			try
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				pstmt = conn.prepareStatement("INSERT INTO Users (Name,Mail,IsDeleted,Gender,Password, CreationDate, LastUpdate) VALUES(?,?,?,?,?,?,?)");
				pstmt.setString(1, user.getName());
				pstmt.setString(2, user.getEmail());
				pstmt.setInt(3, 0);
				pstmt.setString(4, user.getGender().toString());
				pstmt.setString(5, user.getPassword());

				java.util.Date today = new java.util.Date();
				pstmt.setTimestamp(6, new java.sql.Timestamp(today.getTime()));
				pstmt.setTimestamp(7, new java.sql.Timestamp(today.getTime()));
				pstmt.executeUpdate();
				return true;
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
	
	public void remember(int userId, int storyId)
	{
		try
		{
			
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("INSERT INTO IRememberThat (StoryID, UserID, CreationDate, LastUpdate) VALUES(?,?,NOW(),NOW())");
			pstmt.setInt(1, storyId);
			pstmt.setInt(2, userId);
			pstmt.execute();
		}catch(SQLException se)
		{
			se.printStackTrace();
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
	
	public void dontRemember(int userId, int storyId)
	{
		try
		{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("DELETE FROM IRememberThat WHERE StoryID = ? AND UserID = ?");
			pstmt.setInt(1, storyId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		}catch(SQLException se)
		{
			se.printStackTrace();
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
	}
}
