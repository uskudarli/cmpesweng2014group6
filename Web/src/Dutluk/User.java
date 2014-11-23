package Dutluk;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class User {
	private int UserID;
	private String Name;
	private String Email;
	private Date Birthdate;
	public Gender gender = Gender.Male;
	public enum Gender {
		Male, Female;
	}
	private String Phone;
	private int ExperiencePoint;
	private int Level;
	private int IsDeleted;
	private int PicID;
	private String Bio;
	private String Password;
	private Date CreatedOn;
	private Date UpdatedOn;
	private DatabaseService db;
	public User()
	{

	}

	public Boolean Register()
	{
		db = new DatabaseService();
		User temp = db.findUserByEmail(Email);
		if(temp.getEmail() != null)
			return false;
		else
		{
			String sql = "INSERT INTO Users (Name,Mail,IsDeleted,Password, CreationDate, LastUpdate) VALUES(";
			sql += "'" + Name + "',";
			sql += "'" + Email + "',";
			sql += "'" + IsDeleted + "',";
			sql += "'" + Password + "',";
			sql += "'" + CreatedOn + "',";
			sql += "'" + UpdatedOn + "')";
			db.executeSql(sql);
			return true;
		}
	}

	//This is the update profile, except profile picture
	public Boolean UpdateProfile() throws SQLException, ClassNotFoundException 
	{

		
		db = new DatabaseService();
		Connection conn = db.getConnection();
		
		String sql = "UPDATE Users SET Name = ? , LastUpdate = NOW() WHERE UserID= ? ;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, this.Name);
		//pstmt.setInt(2,this.getGenderIndex());
		pstmt.setInt(2,this.UserID);
		pstmt.executeUpdate();

		if(this.Birthdate!=null){
			sql= "UPDATE Users SET Birthdate = ?, LastUpdate = NOW() WHERE UserID= ? ;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, new java.sql.Date(this.Birthdate.getTime()));
			pstmt.setInt(2,this.UserID);
			pstmt.executeUpdate();
		}
		
		if(this.Phone!=null){
			sql= "UPDATE Users SET Phone = ?, LastUpdate = NOW() WHERE UserID= ? ;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.Phone);
			pstmt.setInt(2,this.UserID);
			pstmt.executeUpdate();
		}
		
		if(this.Bio!=null){
			sql= "UPDATE Users SET Bio = ?, LastUpdate = NOW() WHERE UserID= ? ;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.Bio);
			pstmt.setInt(2,this.UserID);
			pstmt.executeUpdate();
		}
		
		return true;
	
		
	}

	public Boolean ChangePassword(String mail, String pass)
	{
		Calendar cal = Calendar.getInstance();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
		db = new DatabaseService();
		db.executeSql("UPDATE Users"
				+ " SET Password = '"+pass+"',"
				+ " LastUpdate = '"+timestamp+"'"
				+ " WHERE Mail = '"+mail+"'"
				);



		return true;
	}

	public Boolean Login()
	{
		db = new DatabaseService();
		User temp = db.findUserByEmail(Email);
		
		String givenmail = this.getEmail();
		String givenpass = this.getPassword();
		User dbuser = db.findUserByEmail(givenmail);
		String dbpass = dbuser.getPassword();
		
		if(givenpass.equals(dbpass))
			return true;
		else return false;
		
		/*if(temp.getEmail() != "")
		{
			if(temp.getPassword().equals(Password))
				return true;
			else
				return false;
		}else
			return false;*/
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public void setGender(Gender g) {
		this.gender = g;
	}

	public void setGender(String s) {
		if(s=="male" || s=="Male" || s=="MALE"){
			this.gender = Gender.Male;
		}else{
			this.gender = Gender.Female;
		}
	}

	public Gender getGender() {
		return gender;
	}
	
	public int getGenderIndex(){
		if(this.getGender().toString().contains("fF"))
			return 2;
		else
			return 1;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Date getBirthdate() {
		return Birthdate;
	}

	public void setBirthdate(Date birthdate) {
		Birthdate = birthdate;
	}

	public void setBirthdate(String s) throws ParseException{
		Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(s);
		this.setBirthdate(date);
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public int getExperiencePoint() {
		return ExperiencePoint;
	}

	public void setExperiencePoint(int experiencePoint) {
		ExperiencePoint = experiencePoint;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public int getIsDeleted() {
		return IsDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		IsDeleted = isDeleted;
	}

	public int getPicID() {
		return PicID;
	}

	public void setPicID(int picID) {
		PicID = picID;
	}

	public String getBio() {
		return Bio;
	}

	public void setBio(String bio) {
		Bio = bio;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
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

	public DatabaseService getDb() {
		return db;
	}

	public void setDb(DatabaseService db) {
		this.db = db;
	}

}
