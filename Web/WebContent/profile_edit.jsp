<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Profile edit</title>

</head>
<body>
	<%@ page import="Dutluk.*"%>
	<%
		HttpSession newSession = request.getSession();
		if (newSession == null) {
			request.getRequestDispatcher("loginRegister.jsp").forward(
					request, response);
		} else if (newSession.getAttribute("email") == null)
			request.getRequestDispatcher("loginRegister.jsp").forward(
					request, response);
		else if (request.getAttribute("name") == null
				|| request.getAttribute("birthdate") == null
				|| request.getAttribute("phone") == null
				|| request.getAttribute("bio") == null) {
			//This is inappropriate, and forwards at random time.
			//request.getRequestDispatcher("loginRegister.jsp").forward(request,response);	
		}
		DatabaseService db = new DatabaseService();
		User user = db.findUserByEmail(newSession.getAttribute("email").toString());
	%>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
			
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2><%=request.getSession().getAttribute("name")%></h2>

					<form id="editProfileForm" method="post" class="loginForm form-horizontal" enctype="multipart/form-data">
						<div class="form-group">

							<div class="form-group">
								<select id="editGenderSelect" class="form-control"
									name="editGender">
									<option
										value="Female">Female</option>
									<option
										 value="Male">Male</option>
									<option
										 value="Unspecified">Unspecified</option>
								</select>
							</div>

							<div class="form-group">
								
								<input class="form-control" id="editBirthdate"
									name="editBirthdate" type=text
									value="<%=request.getSession().getAttribute("birthdate")%>"
									placeholder="Birthdate"></input>
							</div>

							<input type="hidden" name="genderHidden" value="<%= request.getSession().getAttribute("gender").toString() %>" />

							<div class="form-group">
								<input class="form-control" name="editPhone" type="text"
									value="<%=request.getSession().getAttribute("phone")%>"
									placeholder="Phone Number"></input>
							</div>

							<div class="form-group">
								<input class="form-control" name="editName" type="text"
									value="<%=request.getSession().getAttribute("name")%>"
									placeholder="Name"></input>
							</div>


							<div class="form-group">
								<textarea class="form-control" cols=40 rows=2 name="editBio"
									placeholder="Bio"><%=request.getSession().getAttribute("bio")%></textarea>
							</div>
							
							<div class="form-group">
								Upload a profile picture:
								<input class="btn btn-default" type="file" name="file" size="50" />
							</div>

						</div>
						<div class="form-group">
							<button id="editProfileButton" type="submit"
								class="btn btn-default">Save</button>
						</div>
						<input type="hidden" name="func" value="editProfile" />
					</form>
					<a href='changePassword.jsp'>Change Password</a>
				</div>
			</div>

			<div class="col-xs-6">
				<div id=picContainer>
				<% if(user.getPicID() == 0) { %>
					<img src="http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg"
						width=215 height=215 />
						<%} else {
							String path = db.pathByPicId(user.getPicID());
							
							String url = "http://titan.cmpe.boun.edu.tr:8085/image/profile/"+path;
							%>
							<img src=<%out.print(url);%>
						width=215 height=215 />
						<%}
						%>
				</div>
				<%if(user.getPicID()>0){ %>
				<div>
					<a class="btn btn-default" href=<%out.print("ProfileEdit?func=deletePic"); %> >Delete Profile Picture</a>
				</div>
				<%} %>
			</div>
			
		</div>
	</div>



	<div class="modal fade bs-example-modal-sm" id="editSuccess"
		tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">

				<div class="modal-body">Your information is successfully
					updated!</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Ok</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade bs-example-modal-sm" id="errorPop" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Error</h4>
				</div>
				<div class="modal-body">Some error occurred.</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" name="success"
		value="<%=request.getAttribute("success")%>" />
	<input type="hidden" name="error"
		value="<%=request.getAttribute("error")%>" />
</body>
</html>