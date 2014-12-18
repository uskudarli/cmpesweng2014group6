package Dutluk;
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
        private boolean dateisAbsolute;
        private int placeId;

        public Story()
        {

        }

        public int getStoryId() {
			return storyId;
		}

        public void setStoryId(int id) {
        	storyId = id;
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

        public void setdateisAbsolute(boolean b){
                dateisAbsolute = b;
        }

        public boolean getdateisAbsolute(){
                return dateisAbsolute;
        }

		public int getPlaceId() {
			return placeId;
		}

		public void setPlaceId(int placeId) {
			this.placeId = placeId;
		}

}