

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
        public String placeName;
        public int placeId;
        public int rememberNumber;

		public Story()
        {
                
        }
        
        public Story[] getStories(int userId, int type) {
			Story [] stories = new Story[100];
			db = new DatabaseService();
			ResultSet rs = null;
			String sql; 
			System.out.println("type" + type);
			if(type == 0) {
				sql = "SELECT * FROM Stories WHERE UserID = " + userId;
			}else if(type == 1) {
				sql = "select s.* from Stories s, SubscriptionsToUsers stu where s.UserID = stu.FollowedID and stu.FollowerID =" + userId;
			}else {
				sql = "select s.* from Stories s, StoriesInPlaces sip where s.StoryID = sip.StoryID and sip.PlaceId = " + type;
			}
			  try {
			      Connection con = db.getConnection();
			      Statement statement = con.createStatement() ;
			      rs =statement.executeQuery(sql) ;
			      int count = 1; 
			      while(rs.next())
			        {
			    	  	Story story = new Story();
			    	  	story.setContent(rs.getString("Content"));
					  	story.setUserId(rs.getInt("UserID"));
					  	story.setThemeId(rs.getInt("ThemeID"));
					  	story.setStoryId(rs.getInt("StoryID"));
					  	story.setIsDeleted(rs.getInt("isDeleted"));
					  	story.setReportCount(rs.getInt("ReportCount"));
					  	story.setAvgRate(rs.getInt("AvgRate"));
					  	story.setPlaceName(db.findPlacebyPlaceId(db.findPlaceIdByStoryId(rs.getInt("StoryID"))).getName());
					  	story.setPlaceId(db.findPlaceIdByStoryId(rs.getInt("StoryID")));
						story.setCreatedOn(rs.getDate("CreationDate"));
						story.setUpdatedOn(rs.getDate("LastUpdate"));
						story.setAbsoluteDate(rs.getDate("StoryDateAbsolute"));
						story.setApproximateDate(rs.getString("StoryDateApproximate"));
			            stories[count] = story;
			            count++;
			        }
			  } catch (ClassNotFoundException e1) {
			          // TODO Auto-generated catch block
			          e1.printStackTrace();
			  } catch (SQLException e1) {
			          // TODO Auto-generated catch block
			          e1.printStackTrace();
			  }
			  return stories;
        }
        
        public int addStory()
        {
                int id = 0;
                db = new DatabaseService();
                Calendar cal = Calendar.getInstance();
                String sql = "INSERT INTO Stories (UserID, Content, ThemeID, IsDeleted, ReportCount, AvgRate, CreationDate, LastUpdate, StoryDateApproximate) VALUES(";
                sql += "'" + userId + "',";
                sql += "'" + content + "',";
                sql += "'" + themeId + "',";  
                sql += "'" + isDeleted + "',";
                sql += "'" + reportCount + "',";
                sql += "'" + avgRate + "',";
                sql += "'" + new java.sql.Timestamp(cal.getTimeInMillis()) + "',";
                sql += "'" + new java.sql.Timestamp(cal.getTimeInMillis()) + "',";
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
        }
        
        public void setAbsoluteDate(String s) throws ParseException{
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
        
        public int getStoryId() {
        	System.out.println(storyId);
        	return storyId;
        }
        
        public void setStoryId(int storyId) {
        	this.storyId = storyId;
        }

		public String getPlaceName() {
			return placeName;
		}

		public void setPlaceName(String placeName) {
			this.placeName = placeName;
		}

		public int getPlaceId() {
			return placeId;
		}

		public void setPlaceId(int placeId) {
			this.placeId = placeId;
		}

		public int getRememberNumber() {
			return rememberNumber;
		}

		public void setRememberNumber(int rememberNumber) {
			this.rememberNumber = rememberNumber;
		}

}
