import java.util.Date;


public class GetStoryResult {
    private int storyId;
    private String mail;
    private String content;
    private int themeId;
    private int isDeleted;
    private int reportCount;
    private int avgRate;
    private Date createdOn;
    private Date updatedOn;
    private Date absoluteDate;
    private String approximateDate;
    
    public GetStoryResult() {
    	
    }
    
	public int getStoryId() {
		return storyId;
	}
	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getThemeId() {
		return themeId;
	}
	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getReportCount() {
		return reportCount;
	}
	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
	public int getAvgRate() {
		return avgRate;
	}
	public void setAvgRate(int avgRate) {
		this.avgRate = avgRate;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Date getAbsoluteDate() {
		return absoluteDate;
	}
	public void setAbsoluteDate(Date absoluteDate) {
		this.absoluteDate = absoluteDate;
	}
	public String getApproximateDate() {
		return approximateDate;
	}
	public void setApproximateDate(String approximateDate) {
		this.approximateDate = approximateDate;
	}
    
    
}
