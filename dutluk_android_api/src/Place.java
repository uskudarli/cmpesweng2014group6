import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class Place {
        private int PlaceID;
        public String Name;
        private double Longtitude;
        private double Latitude;
        private Date CreatedOn;
        private Date UpdatedOn;
        DatabaseService db;
        
        
        public Place(){
                
        }
        
        public int addPlace() {
        	int id = 0;
        	db = new DatabaseService();
        	Calendar cal = Calendar.getInstance();
            String sql = "INSERT INTO Places (PlaceID, Name, Longtitude, Latitude, CreationDate, LastUpdate) VALUES(";
            sql += "'" + PlaceID + "',";
            sql += "'" + Name + "',";
            sql += "'" + Longtitude + "',";  
            sql += "'" + Latitude + "',";
            sql += "'" + new java.sql.Timestamp(cal.getTimeInMillis()) + "',";
            sql += "'" + new java.sql.Timestamp(cal.getTimeInMillis()) + "')";
            boolean result = db.executeSql(sql);
            ResultSet rs = null;
            try {
                    Connection con = db.getConnection();
                    Statement statement = con.createStatement() ;
                    rs =statement.executeQuery("SELECT * FROM Places ORDER BY PlaceID DESC Limit 1") ;
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
        
        public String getName() {
                return Name;
        }
        public void setName(String name) {
                Name = name;
        }
        public double getLongtitude() {
                return Longtitude;
        }
        public void setLongtitude(double longtitude) {
                Longtitude = longtitude;
        }
        public double getLatitude() {
                return Latitude;
        }
        public void setLatitude(double latitude) {
                Latitude = latitude;
        }
        public Date getCreatedOn() {
                return CreatedOn;
        }
        public void setCreatedOn(Date createdOn) {
                CreatedOn = createdOn;
        }
        public Date getUpdatedOn() {
                return UpdatedOn;
        }
        public void setUpdatedOn(Date updatedOn) {
                UpdatedOn = updatedOn;
        }
        public int getPlaceID() {
                return PlaceID;
        }
        public void setPlaceID(int id) {
                PlaceID = id;
        }
}