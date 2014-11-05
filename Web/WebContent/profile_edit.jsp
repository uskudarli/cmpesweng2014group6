<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Profile edit</title>

</head>
<body>
	<%@ page import="Dutluk.*" %>
	<%
		HttpSession newSession = request.getSession();
		if(newSession == null)
		{
			System.out.println("aa");
			request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
		}else if(newSession.getAttribute("email") == null)
			request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
		else
		{
			DatabaseService db = new DatabaseService();
			User user = db.findUserByEmail(newSession.getAttribute("email").toString());
			out.write(user.getPassword());
		}
		
	%>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
		
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>Profile</h2>
					<form id="editForm" method="post"
						class="editForm form-horizontal">
						<div class="form-group">
							Name: <input class="form-control" id="editUname" type="text" value=""
								name="name" placeholder="Name"></input>
							Birthdate: <input class="form-control" id="editBirthdate" type=text value=""
								name="name" placeholder="Birthdate"></input>
							Gender: <input class="form-control" id="editGender" type="text" value=""
								name="name" placeholder="Gender"></input>
							Phone: <input class="form-control" id="editPhone" type="text" value=""
								name="name" placeholder="Phone"></input>
							Bio: <input class="form-control" id="editBio" type="text" value=""
								name="name" placeholder="Bio"></input>	
								
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-default">Submit</button>
						</div>
					</form>
				</div>
			</div>
			
			
		</div>
	</div>


	
	<div class="modal fade bs-example-modal-sm" id="editSuccess" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				
				<div class="modal-body">
					Edited successfully!
				</div>
				<div class="modal-footer">
					<form id="redirectProfile" method="post" class="form-horizontal" action="Profile">
						<input type="button" name="profileRedirect" value="Ok" id="submit"/>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade bs-example-modal-sm" id="editError" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Error</h4>
				</div>
				<div class="modal-body">
					Some error occurred.
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>