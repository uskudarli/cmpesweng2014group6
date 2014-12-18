package Dutluk;
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
		Male, Female, Unspecified;
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
		if(s.toLowerCase().startsWith("m")){
			this.gender = Gender.Male;
		}else if(s.toLowerCase().startsWith("f")){
			this.gender = Gender.Female;
		}else{
			this.gender = Gender.Unspecified;
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
		//System.out.print(date);
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
