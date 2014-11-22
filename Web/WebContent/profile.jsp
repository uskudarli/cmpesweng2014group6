<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Dutluk</title>

</head>
<body>
	<%@ page import="Dutluk.*" %>
	<%
		HttpSession newSession = request.getSession();
		if(newSession == null)
		{
			request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
		}else if(newSession.getAttribute("email") == null
				|| newSession.getAttribute("name") == null
				|| newSession.getAttribute("birthdate") == null
				|| newSession.getAttribute("genderChar") == null
				|| newSession.getAttribute("mail") == null
				|| newSession.getAttribute("phone") == null
				|| newSession.getAttribute("xp") == null
				|| newSession.getAttribute("level") == null
				|| newSession.getAttribute("bio") == null
				)	
		{
			request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
		}	
	%>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
		<%@ page import = "Dutluk.*" %>
			<div class="col-xs-6">
				<div style="padding: 10px">
					<br><br><h2 style="display:inline;"><%= request.getSession().getAttribute("name")%></h2>
					level <%= request.getSession().getAttribute("level")%> writer, <%= request.getSession().getAttribute("xp")%> points<br>
					<h4>"<%= request.getSession().getAttribute("bio")%>"</h4>

						Born on <%= request.getSession().getAttribute("birthdate")%>, <%= request.getSession().getAttribute("genderChar")%><br>
						Mail <%= request.getSession().getAttribute("mail")%><br>
						Phone <%= request.getSession().getAttribute("phone")%><br>
						<br>
						<a href='profile_edit.jsp'>This is you! Click here to edit your info.</a>
						</div>	
					</form>
				</div>
			</div>
			
			
		</div>
	</div>
	

</body>
</html>