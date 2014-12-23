

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
@WebServlet("/updateProfile")
public class updateProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public updateProfile() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setName(request.getParameter("name"));
		//user.setBirthdate(request.getParameter("birthdate"));
		user.setGender(request.getParameter("gender"));
		user.setPhone(request.getParameter("phone"));
		user.setBio(request.getParameter("bio"));
		user.setEmail(request.getParameter("email"));
		Boolean result = user.updateProfile(request.getParameter("email"));
		updateProfileResult registerResult  = new updateProfileResult();
		response.reset();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if(result){
			registerResult.setMessage("profile successfully editted");
		}else
			registerResult.setMessage("editing profile halted");
		registerResult.setResult(result);
		Gson gson = new Gson();
		PrintWriter pw = response.getWriter();
		pw.print(gson.toJson(registerResult));
		pw.flush();
		pw.close();
	}

}
