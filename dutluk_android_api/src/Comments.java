import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;


/**
 * @author asus
 *
 */
public class Comments {
	private int CommentID;
	private int StoryID;
	private int UserID;
	private String Comment;
	private int IsDeleted;
	private Date CreationDate;
	private Date LastUpdate;
	private String UserMail;
	
	DatabaseService db;
	
	public Comments() {
		
	}
		
	public int AddComment() {
    	db = new DatabaseService();
    	int id = 0;
    	Calendar cal = Calendar.getInstance();
    	String sql = "INSERT INTO `Comments`(`CommentID`, `StoryID`, `UserID`, `Comment`, `IsDeleted`, `CreationDate`, `LastUpdate`) VALUES (";
                sql += "'" + CommentID + "',";
                sql += "'" + StoryID + "',";
                sql += "'" + UserID + "',";
                sql += "'" + Comment + "',";
                sql += "'" + IsDeleted + "',";
                sql += "'" + new java.sql.Timestamp(cal.getTimeInMillis()) + "',";
                sql += "'" + new java.sql.Timestamp(cal.getTimeInMillis()) + "')";
                
        boolean result = db.executeSql(sql);
        ResultSet rs = null;
        try {
                Connection con = db.getConnection();
                Statement statement = con.createStatement() ;
                rs =statement.executeQuery("SELECT * FROM Comments ORDER BY PlaceID DESC Limit 1") ;
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
	
	public int getCommentID() {
		return CommentID;
	}
	public void setCommentID(int commentID) {
		CommentID = commentID;
	}
	public int getStoryID() {
		return StoryID;
	}
	public void setStoryID(int storyID) {
		StoryID = storyID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public int getIsDeleted() {
		return IsDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		IsDeleted = isDeleted;
	}
	public Date getCreationDate() {
		return CreationDate;
	}
	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}
	
	public String getUserMail() {
		return UserMail;
	}

	public void setUserMail(String userName) {
		UserMail = userName;
	}

	public Date getLastUpdate() {
		return LastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		LastUpdate = lastUpdate;
	}
	
}
