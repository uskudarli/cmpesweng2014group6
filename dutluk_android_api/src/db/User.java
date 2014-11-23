package db;
import java.util.*;
public class User {
	private int UserId;
	private String Name;
	private String Email;
	private Date Birthdate;
	private enum Gender {
		Male, Female
	};
	private String Phone;
	private int ExperiencePoint;
	private int Level;
	private int IsDeleted;
	private int PicId;
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
	
	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
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

	public int getPicId() {
		return PicId;
	}

	public void setPicId(int picId) {
		PicId = picId;
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
		Password = password;
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

