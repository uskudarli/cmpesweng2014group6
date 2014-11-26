package Dutluk;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		return conn;
	}
	
	/*public Boolean Update(User user)
	{
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("UPDATE Users SET Name=?, Birthdate=?, Gender=?, Phone=?, Bio=?, LastUpdate=? WHERE Mail = ?");
			pstmt.setString(1, user.getName());
			java.sql.Date sqlDate = new java.sql.Date(user.getBirthdate().getTime());
			pstmt.setDate(2, sqlDate);
			pstmt.setString(3, user.getGender().toString());
			pstmt.setString(4, user.getPhone());
			pstmt.setString(5, user.getBio());
			java.util.Date today = new java.util.Date();
			pstmt.setTimestamp(6, new java.sql.Timestamp(today.getTime()));
			pstmt.setString(7, user.getEmail());
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
		
	}*/
	
	public void executeStmt(PreparedStatement stmt)
	{
		
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
				pstmt.setString(4, null);
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
}
