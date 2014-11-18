package Dutluk;
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
	
	public Boolean addStory()
	{
		db = new DatabaseService();

		String sql = "INSERT INTO Stories (UserID, Content," /*ThemeID,*/+" IsDeleted, ReportCount, AvgRate, CreationDate, LastUpdate, StoryDateAbsolute, StoryDateApproximate) VALUES(";
		sql += "'" + userId + "',";
		sql += "'" + content + "',";
		//sql += "'" + themeId + "',";  // it is on foreign key, so do it later.
		sql += "'" + isDeleted + "',";
		sql += "'" + reportCount + "',";
		sql += "'" + avgRate + "',";
		sql += "'" + createdOn + "',";
		sql += "'" + updatedOn + "',";
		sql += "'" + absoluteDate + "',";
		sql += "'" + approximateDate + "')";
		boolean a = db.executeSql(sql);
		if(a)
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
	}
	
	public String getApproximateDate() {
		return approximateDate;
	}

	public void setApproximateDate(String ApproximateDate) {
		approximateDate = ApproximateDate;
	}

}