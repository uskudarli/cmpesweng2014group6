

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

/**
 * Servlet implementation class UploadPicture
 */
@WebServlet("/UploadPicture")
public class UploadPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIRECTORY = "image";
    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadPicture() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String func = request.getParameter("func");
		int storyId = 0;
		int userId = 0;
		boolean isProfile = false;
		if(func.equals("profile")) {
			isProfile = true;
			userId = Integer.parseInt(request.getParameter("userId"));
		}else {
			storyId = Integer.parseInt(request.getParameter("storyId"));
		}
		DatabaseService db = new DatabaseService();
		String fileName = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart) {            
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
                                System.out.println("aa" + uploadPath);
                                // saves the file on disk
                                item.write(storeFile);
                                request.setAttribute("message",
                                    "Upload has been done successfully!");
                            	RegisterResult registerResult  = new RegisterResult();
                        		response.reset();
                        		response.setContentType("application/json");
                        		response.setCharacterEncoding("UTF-8");
                        		registerResult.setMessage("photo is inserted successfully");
                        		registerResult.setResult(true);
                                Gson gson = new Gson();
                        		PrintWriter pw = response.getWriter();
                        		pw.print(gson.toJson(registerResult));
                        		pw.flush();
                        		pw.close();
                                int key = db.insertPhoto(fileName);
                                if(isProfile == false) {
                                    db.insertPhotoStoryConnection(storyId, key);	
                                }else {
                                	db.pictureProfileConnection(userId, key);
                                }
                            }
                        }                       
                    }
                }
            } catch (Exception ex) {
                request.setAttribute("message",
                        "There was an error: " + ex.getMessage());
            	RegisterResult registerResult  = new RegisterResult();
        		response.reset();
        		response.setContentType("application/json");
        		response.setCharacterEncoding("UTF-8");
        		registerResult.setMessage("error occured");
        		registerResult.setResult(false);
                Gson gson = new Gson();
        		PrintWriter pw = response.getWriter();
        		pw.print(gson.toJson(registerResult));
        		pw.flush();
        		pw.close();
            }
		}
	}
}
