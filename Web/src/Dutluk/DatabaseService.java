package Dutluk;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;


public class DatabaseService{
	private String DB_URL,JDBC_DRIVER,USER,PASS;
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;

	public DatabaseService()
	{
		this.readConfig();
	}
	
	public void readConfig(){
		try{

			//In MacOS, it just couldn't find my real working directory.
			//I had to put my config.xml file in 
			//here:	/Applications/eclipse/Eclipse.app/Contents/MacOS

			//So be careful, if it can't find your config.xml, it will catch an exception
			//with the message "CONFIGNOTFOUND". If you can't even login, seek for this
			//message in your console/catalina output.

			//Also, titan's tomcat returns  "/."
			//Instead of putting config.xml to that directory, I had change the wd
			//string to /home/project6/tomcat, where I put config.xml file

			String working_directory=new File(".").getAbsolutePath();
			//System.out.println("CONFIG WD>>>\n"+working_directory+"\n<<<");
			//Titan's tomcat returns /. here, so I modify it by hand.
			if(working_directory.equals("/.")) working_directory="/home/project6/tomcat";	

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

	public ArrayList<Story> getStoriesOfUser(int userId)
	{
		ArrayList<Story> stories = new ArrayList<Story>();
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Stories WHERE UserID = ? AND IsDeleted = 0 ORDER BY  Stories.StoryDateAbsolute DESC");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				stories.add(new Story(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), 0, rs.getInt(6), rs.getInt(7), rs.getDate(8), rs.getDate(9), rs.getDate(10), rs.getString(11)));
			}
			return stories;
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
		return stories;
	}

	public ArrayList<Theme> getAllThemes()
	{
		ArrayList<Theme> themes = new ArrayList<Theme>();
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Themes ORDER BY  Themes.ThemeID ASC");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				themes.add(new Theme(rs.getInt(1), rs.getString(2)));
			}
			return themes;
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
		return themes;
	}

	public Boolean Login(User user)
	{
		try{
			User dbuser = findUserByEmail(user.getEmail());
			String dbpass = dbuser.getPassword();

			if(user.getPassword().equals(dbpass))
				return true;
			else 
				return false;
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}	
		return false;

	}

	public Boolean ChangePassword(String mail, String pass)
	{
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("UPDATE Users SET Password = ?, LastUpdate = NOW() WHERE Mail = ?");
			pstmt.setString(1, pass);
			pstmt.setString(2, mail);
			pstmt.executeUpdate();
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

	public Boolean UpdateProfile(User user)
	{
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("UPDATE Users SET Name = ? , Gender = ? ,  LastUpdate = NOW() WHERE UserID= ?");
			pstmt.setString(1, user.getName());
			pstmt.setString(2,	user.getGender().toString());
			pstmt.setInt(3, user.getUserID());
			pstmt.executeUpdate();
			if(user.getBirthdate() != null){
				pstmt = conn.prepareStatement("UPDATE Users SET Birthdate = ?, LastUpdate = NOW() WHERE UserID= ?");
				pstmt.setDate(1, new java.sql.Date(user.getBirthdate().getTime()));
				pstmt.setInt(2, user.getUserID());
				pstmt.executeUpdate();
			}
			if(user.getPhone() != null){
				pstmt = conn.prepareStatement("UPDATE Users SET Phone = ?, LastUpdate = NOW() WHERE UserID= ?");
				pstmt.setString(1, user.getPhone());
				pstmt.setInt(2,user.getUserID());
				pstmt.executeUpdate();
			}
			if(user.getBio() != null){
				pstmt = conn.prepareStatement("UPDATE Users SET Bio = ?, LastUpdate = NOW() WHERE UserID= ?");
				pstmt.setString(1, user.getBio());
				pstmt.setInt(2,user.getUserID());
				pstmt.executeUpdate();
			}
			if(user.getPicID() > 0){
				pstmt = conn.prepareStatement("UPDATE Users SET PicID = ?, LastUpdate = NOW() WHERE UserID= ?");
				pstmt.setInt(1, user.getPicID());
				pstmt.setInt(2, user.getUserID());
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

	public int addStory(Story story)
	{
		int id = 0;
		try{
			conn = getConnection();
			if(story.getdateisAbsolute())
				pstmt = conn.prepareStatement("INSERT INTO Stories (UserID, Content, ThemeID, IsDeleted, ReportCount, AvgRate, CreationDate, LastUpdate,StoryDateAbsolute) VALUES (?,?,?,0,0,0,NOW(),NOW(),?)", Statement.RETURN_GENERATED_KEYS);
			else
				pstmt = conn.prepareStatement("INSERT INTO Stories (UserID, Content, ThemeID, IsDeleted, ReportCount, AvgRate, CreationDate, LastUpdate,StoryDateApproximate) VALUES (?,?,?,0,0,0,NOW(),NOW(),?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, story.getUserId());
			pstmt.setString(2, story.getContent());
			pstmt.setInt(3, story.getThemeId());
			if(story.getdateisAbsolute())
				pstmt.setDate(4, new java.sql.Date(story.getAbsoluteDate().getTime()));
			else
				pstmt.setString(4, story.getApproximateDate());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next())
			{

				String key = rs.getString(1);
				id = Integer.parseInt(key);    
			}
			return id;
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
		return id;

	}


	public boolean addStoryAndPlace(int storyId, int placeId)
	{

		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("INSERT INTO StoriesInPlaces (StoryID, PlaceID) VALUES (?, ?)");
			pstmt.setInt(1, storyId);
			pstmt.setInt(2, placeId);
			pstmt.executeUpdate();
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

	public void insertRate(int storyId, int userId, int rate)
	{
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("INSERT INTO Rate (StoryID, UserID, Rate, CreationDate, LastUpdate) VALUES (?, ?, ?, NOW(), NOW())");
			pstmt.setInt(1, storyId);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, rate);
			pstmt.executeUpdate();
			int totalRate = 0;
			int rateCount = 0;
			pstmt = conn.prepareStatement("SELECT * FROM Rate WHERE StoryID = ?");
			pstmt.setInt(1, storyId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				totalRate += rs.getInt(3);
				rateCount++;
			}
			int avgRate = totalRate / rateCount;
			pstmt = conn.prepareStatement("UPDATE Stories SET AvgRate = ? WHERE StoryID = ?");
			pstmt.setInt(1, avgRate);
			pstmt.setInt(2, storyId);
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

	public boolean insertComment(int storyId, int userId, String comment)
	{
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("INSERT INTO Comments (StoryID, UserID, Comment, IsDeleted, CreationDate, LastUpdate) VALUES (?, ?, ?, 0, NOW(), NOW())");
			pstmt.setInt(1, storyId);
			pstmt.setInt(2, userId);
			pstmt.setString(3, comment);
			pstmt.executeUpdate();
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

	public int findPlaceByLatLon(String lat, String lon)
	{
		int id = 0;
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Places WHERE Latitude = ? AND Longtitude = ?");
			pstmt.setString(1, lat);
			pstmt.setString(2, lon);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
			{
				id = rs.getInt("PlaceID");
			}
			return id;
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
		return id;
	}

	Boolean executeSql(String sql)
	{
		Boolean result = false;
		try
		{
			conn = getConnection();
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
			conn = getConnection();
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

	public User findUserByUserId(int id)
	{
		User user = new User();
		try
		{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ? and IsDeleted = 0");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				user.setUserID(id);
				user.setName(rs.getString("Name"));
				user.setEmail(rs.getString("Mail"));
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
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Places WHERE PlaceID = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				place.setPlaceID(id);
				place.setCreatedOn(rs.getDate("CreationDate"));
				place.setUpdatedOn(rs.getDate("LastUpdate"));
				place.setName(rs.getString("Name"));
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
	
	public String findPicturePathOfStory(int id)
	{
		String path = null;
		try
		{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT Path FROM Pictures,PicturesInStories WHERE StoryID= ? AND  Pictures.PicID=PicturesInStories.PicID LIMIT 1");	
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				path = rs.getString(1);
			}
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
		return path;
	}
	
	public Place findPlacebyStoryId(int id)
	{
		Place place = new Place();
		try
		{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Places, StoriesInPlaces WHERE Places.PlaceID=StoriesInPlaces.PlaceID AND StoriesInPlaces.StoryID = ?");	
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				place.setPlaceID(id);
				place.setCreatedOn(rs.getDate("CreationDate"));
				place.setUpdatedOn(rs.getDate("LastUpdate"));
				place.setName(rs.getString("Name"));
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

	public Story findStorybyStoryId(int id)
	{
		Story story = new Story();
		try
		{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Stories WHERE StoryID = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())
			{
				story.setStoryId(id);
				story.setCreatedOn(rs.getTimestamp("CreationDate"));
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

	public ArrayList<Place> findAllPlaces()
	{
		ArrayList<Place> places = new ArrayList<Place>();
		try
		{
			conn = getConnection();
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
				conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
	
	public boolean isReported(int userId, int storyId)
	{
		try
		{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Reports WHERE StoryID = ? AND UserID = ?");
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
	
	public void report(int userId, int storyId)
	{
		try
		{

			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("INSERT INTO Reports (StoryID, UserID, CreationDate, LastUpdate) VALUES(?,?,NOW(),NOW())");
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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
			conn = getConnection();
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

		return places;

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
				users.add(findUserByUserId(rs.getInt("UserID")));
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
		return rate;
	}
	
	
	public List<Story> getSubscriptions(int userId)
	{
		List<Story> stories = new ArrayList<Story>();
		try{
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Stories, SubscriptionsToUsers WHERE SubscriptionsToUsers.FollowerID = ? AND Stories.UserID = SubscriptionsToUsers.FollowedID ORDER BY Stories.CreationDate DESC LIMIT 10");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				Story temp = findStorybyStoryId(rs.getInt("StoryID"));
				temp.setSubscription(0);
				stories.add(temp);
			}
			conn = getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM Stories, SubscriptionsToPlaces, StoriesInPlaces WHERE SubscriptionsToPlaces.FollowerID = ? AND Stories.StoryID = StoriesInPlaces.StoryID AND StoriesInPlaces.PlaceID = SubscriptionsToPlaces.FollowedID AND Stories.UserID <> ? ORDER BY Stories.CreationDate DESC LIMIT 10");
			pstmt.setInt(1, userId);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				Story temp = findStorybyStoryId(rs.getInt("StoryID"));
				temp.setSubscription(rs.getInt("FollowedId"));
				stories.add(temp);
			}
			Set<Story> setItems = new LinkedHashSet<Story>(stories);
			stories.clear();
			stories.addAll(setItems);
			Collections.sort(stories, new Comparator<Story>(){
			     public int compare(Story o1, Story o2){
			         if(o1.getStoryId() == o2.getStoryId())
			             return 0;
			         return o1.getStoryId() < o2.getStoryId() ? 1 : -1;
			     }
			});
			return stories;
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
		return stories;
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
