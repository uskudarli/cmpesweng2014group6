package Dutluk;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Story {
	private int storyId;
	private int userId;
	private String content;
	private int themeId;
	private int isDeleted;
	private int reportCount;
	private int avgRate;
	private Date createdOn;
	private Date updatedOn;
	private Date absoluteDate;
	private String approximateDate;
	private DatabaseService db;

	public Story()
	{
		
	}
	
	public int addStory()
	{
		int id = 0;
		db = new DatabaseService();

		String sql = "INSERT INTO Stories (UserID, Content, ThemeID, IsDeleted, ReportCount, AvgRate, CreationDate, LastUpdate, StoryDateAbsolute, StoryDateApproximate) VALUES(";
		sql += "'" + userId + "',";
		sql += "'" + content + "',";
		sql += "'" + themeId + "',";  
		sql += "'" + isDeleted + "',";
		sql += "'" + reportCount + "',";
		sql += "'" + avgRate + "',";
		sql += "'" + createdOn + "',";
		sql += "'" + updatedOn + "',";
		sql += "'" + absoluteDate + "',";
		sql += "'" + approximateDate + "') ";
		boolean a = db.executeSql(sql);
        
		ResultSet rs = null;
		try {
			Connection con = db.getConnection();
			Statement statement = con.createStatement() ;
			rs =statement.executeQuery("SELECT * FROM Stories ORDER BY StoryID DESC Limit 1") ;
			while(rs.next())
	        {
	            id = rs.getInt(1);
	        }
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return id;
		
	}
	
	public boolean addStoryAndPlace(int storyId, int placeId)
	{
		db = new DatabaseService();
		String sql = "INSERT INTO StoriesInPlaces (StoryID, PlaceID) VALUES ('"+storyId+"', '"+placeId+"')";
		if(db.executeSql(sql))
			return true;
		else
			return false;
	}
	
	//setters and getters:
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userID) {
		userId = userID;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String Content) {
		content = Content;
	}
	
	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeID) {
		themeId = themeID;
	}
	
	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int IsDeleted) {
		isDeleted = IsDeleted;
	}
	
	public int getReportCount() {
		return reportCount;
	}

	public void setReportCount(int ReportCount) {
		reportCount = ReportCount;
	}
	
	public int getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(int AvgRate) {
		avgRate = AvgRate;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date CreatedOn) {
		createdOn = CreatedOn;
	}
	
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date UpdatedOn) {
		updatedOn = UpdatedOn;
	}
	
	public Date getAbsoluteDate() {
		return absoluteDate;
	}

	public void setAbsoluteDate(Date AbsoluteDate) {
		absoluteDate = AbsoluteDate;
		if(!(absoluteDate.equals("")))
			absoluteDate = new java.sql.Date(this.absoluteDate.getTime());
	}
	
	public void setAbsoluteDateString(String s) throws ParseException{
		Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(s);
		//System.out.print(date);
		this.setAbsoluteDate(date);
	}

	
	public String getApproximateDate() {
		return approximateDate;
	}

	public void setApproximateDate(String ApproximateDate) {
		approximateDate = ApproximateDate;
	}

}