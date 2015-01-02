import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Dutluk.*;

@WebServlet("/AddStory")
public class AddStory extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private static final String UPLOAD_DIRECTORY = "image";
   	 
        // upload settings
        private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
        private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
        private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
        /**
         * @see HttpServlet#HttpServlet()
         */
        public AddStory() {
                super();
        }

        /**
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }

        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */
        public boolean isValidDate(String dateString) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                try {
                        df.parse(dateString);
                        return true;
                } catch (ParseException e) {
                        return false;
                }
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
        {
                response.setContentType("text/html");


                HttpSession session = request.getSession();
                String email = session.getAttribute("email").toString();
                DatabaseService db = new DatabaseService();
                User user = db.findUserByEmail(email);

                String lat = request.getParameter("lat");
                String lon = request.getParameter("lon");
                lat = request.getSession().getAttribute("lat").toString();
                lon = request.getSession().getAttribute("lon").toString();
                int placeId = 0;
                placeId = db.findPlaceByLatLon(lat, lon);
               
                Calendar cal = Calendar.getInstance();
                java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());



                String fileName = null;
        		String storyTime = null;
        		String placeName = null;
        		String tags = null;
        		Story story = new Story();
        		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        		if(isMultipart)
        		{
        			
        			DiskFileItemFactory factory = new DiskFileItemFactory();
        	        // sets memory threshold - beyond which files are stored in disk
        	        factory.setSizeThreshold(MEMORY_THRESHOLD);
        	        // sets temporary location to store files
        	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        	 
        	        ServletFileUpload upload = new ServletFileUpload(factory);
        	         
        	        // sets maximum size of upload file
        	        upload.setFileSizeMax(MAX_FILE_SIZE);
        	         
        	        // sets maximum size of request (include file + form data)
        	        upload.setSizeMax(MAX_REQUEST_SIZE);
        	 
        	        // constructs the directory path to store upload file
        	        // this path is relative to application's directory
        	        String uploadPath = getServletContext().getRealPath("")
        	                + File.separator +".."+ File.separator+ UPLOAD_DIRECTORY;
        	         
        	        // creates the directory if it does not exist
        	        File uploadDir = new File(uploadPath);
        	        if (!uploadDir.exists()) {
        	            uploadDir.mkdir();
        	        }
        	 
        	        try {
        	            // parses the request's content to extract file data
        	            List<FileItem> formItems = upload.parseRequest(request);
        	 
        	            if (formItems != null && formItems.size() > 0) {
        	                // iterates over form's fields
        	                for (FileItem item : formItems) {
        	                    // processes only fields that are not form fields
        	                    if (!item.isFormField()) {
        	                    	fileName = item.getName();
        	                    	if(fileName != null && !fileName.equals("")) {
	        	                    	fileName = db.pictureNameGenerator();
	        	                        
	        	                        File storeFile = new File(uploadPath, fileName);
	        	 
	        	                        // saves the file on disk
	        	                        item.write(storeFile);
	        	                        request.setAttribute("message",
	        	                            "Upload has been done successfully!");
        	                    	}
        	                    }else
        	                    {
        	                    	String fieldname = item.getFieldName();
        	                    	String fieldvalue = item.getString();
        	                    	if(fieldname.equals("editStory"))
        	                    		story.setContent(fieldvalue);
        	                    	else if(fieldname.equals("theme"))
        	                    		story.setThemeId(Integer.parseInt(fieldvalue));
        	                    	else if(fieldname.equals("editStime"))
        	                    		storyTime = fieldvalue;
        	                    	else if(fieldname.equals("placeName"))
        	                    		placeName = fieldvalue;
        	                    	else if(fieldname.equals("tags"))
        	                    		tags = fieldvalue;
        	            			
        	                    }
        	                   
        	                }
        	            }
        	        } catch (Exception ex) {
        	            request.setAttribute("message",
        	                    "There was an error: " + ex.getMessage());
        	        }
        	        
        	        
        	        
        		}
        		else {
        			
        			story.setContent(request.getParameter("editStory").toString());
        			story.setThemeId(Integer.parseInt(request.getParameter("theme")));
        			
        			storyTime = request.getParameter("editStime");
        		}
        		story.setUserId(user.getUserID());
        		story.setIsDeleted(0);
        		story.setReportCount(0);
        		story.setAvgRate(0);
        		story.setCreatedOn(timestamp);
        		story.setUpdatedOn(timestamp);
        		
        		
        		
        		if((storyTime!=null) && isValidDate(storyTime)){ 
        			story.setdateisAbsolute(true);
        			try {
        				story.setAbsoluteDateString(storyTime);
        			} catch (ParseException e) {
        				e.printStackTrace();
        			}
        			story.setApproximateDate("");
        		}else{
        			story.setdateisAbsolute(false);
        			story.setApproximateDate(storyTime);
        		}

        		if(!(placeId > 0))
                {
        			placeId = db.insertPlace(placeName, lon, lat);
                }
        		//add story to database and get added story id
        		int storyId = db.addStory(story);
        		
        		
        		if((storyId != 0) && (placeId != 0))
        		{
        			//gamification
        			//Adding new story = +10 points
        			db.gamification(user.getUserID(), 10, -1, 0);
        			//add tags to database and get added tags ids
        			if(tags != null && !tags.equals(""))
            		{
            			ArrayList<String> tagIds = db.insertTags(tags);
            			db.insertTagStoryConnection(tagIds, storyId);
            			db.insertTagPlaceConnection(tagIds, placeId);
            		}
        			if(db.addStoryAndPlace(storyId, placeId))
        			{
        				request.setAttribute("error", "true");
        				request.setAttribute("message", "Story is added.");
        				request.getRequestDispatcher("index.jsp").forward(request, response);
        				if(fileName != null && !fileName.equals(""))
        				{
        					int pictureId = db.insertPhoto(fileName);
        					db.insertPhotoStoryConnection(storyId, pictureId);
        				}
        			        				
        			}
        			
        			else
        			{
        				request.setAttribute("error", "true");
        				request.setAttribute("message", "StoriesInPlaces error.");
        				request.getRequestDispatcher("index.jsp").forward(request, response);
        			}
        		}
        		else
        		{

        			request.setAttribute("error", "true");
        			request.setAttribute("message", "Sorry, Something went wrong.");
        			request.getRequestDispatcher("index.jsp").forward(request, response);
        		}


        }
}