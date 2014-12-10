

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import Dutluk.User.Gender;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/ProfileEdit")
public class ProfileEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "image";
  	 
    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProfileEdit() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String action = request.getParameter("func");
		HttpSession session = request.getSession();
		DatabaseService db = new DatabaseService();
		String email = session.getAttribute("email").toString();
		User user = db.findUserByEmail(email);
		if(action != null)
		{
			db.deleteProfilePic(user.getUserID());
		}

		request.getRequestDispatcher("profile_edit.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 

		HttpSession session = request.getSession();
		DatabaseService db = new DatabaseService();
		String email = session.getAttribute("email").toString();
		User user = db.findUserByEmail(email);

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
	        
	        String fileName = null;
	        String newGender_in = null;
	        String newBirthdate = null;
	        String newName = null;
			String newPhone = null;
			String newBio = null;
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
	                    	if(fieldname.equals("editGender"))
	                    		newGender_in = fieldvalue;
	                    	else if(fieldname.equals("editBirthdate"))
	                    		newBirthdate = fieldvalue;
	                    	else if(fieldname.equals("editName"))
	                    		newName = fieldvalue;
	                    	else if(fieldname.equals("editPhone"))
	                    		newPhone = fieldvalue;
	                    	else if(fieldname.equals("editBio"))
	                    		newBio = fieldvalue;
	            			
	                    }
	                   
	                }
	            }
	        } catch (Exception ex) {
	            request.setAttribute("message",
	                    "There was an error: " + ex.getMessage());
	        }

			Gender newGender;
			if(newGender_in.toLowerCase().startsWith("f")) newGender=Gender.Female;
			else if(newGender_in.toLowerCase().startsWith("m")) newGender=Gender.Male;
			else newGender=Gender.Unspecified;
			if(newBirthdate!=null){
				try {
					user.setBirthdate(newBirthdate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
	
			user.setGender(newGender);
			user.setName(newName);
			user.setPhone(newPhone);
			user.setBio(newBio);
			int pictureId = user.getPicID();
			if(fileName != null && !fileName.equals(""))
			{
				pictureId = db.insertPhoto(fileName);
			}
			
			user.setPicID(pictureId);
	
			boolean updated=false;
			try {
				updated = user.UpdateProfile();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			if(updated) 
				request.setAttribute("success", "true");
			else 
				request.setAttribute("error","true");
			
			//Resubmit new details to session, 
			//otherwise although updated, old info will be shown
			
		}
		
		
		request.getRequestDispatcher("profile_edit.jsp").forward(request,response);



	}
}
