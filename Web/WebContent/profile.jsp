<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Login</title>

</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
		<%@ page import = "Dutluk.*" %>
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>Profile</h2>
					<form id="profileInfo" method="get"
						class="profileInfo form-horizontal">
						<div class="form-group">
						Name: <label><%= request.getSession().getAttribute("name")%></label>
						</div>
						<div class="form-group">
						Birthdate: <label><%= request.getSession().getAttribute("birthdate")%></label>
						</div>
						<div class="form-group">
						Gender: <label name="gender"><%= request.getSession().getAttribute("gender")%></label>
						</div>
						<div class="form-group">
						Mail: <label name="mail"><%= request.getSession().getAttribute("mail")%></label></div>
						<div class="form-group">
						Phone: <label name="phone"><%= request.getSession().getAttribute("phone")%></label></div>
						<div class="form-group">
						XP: <label name="xp"><%= request.getSession().getAttribute("xp")%></label></div>
						<div class="form-group">
						Level: <label name="level"><%= request.getSession().getAttribute("level")%></label></div>
						<div class="form-group">
						Bio: <label name="bio"><%= request.getSession().getAttribute("bio")%></label></div>
						<a href='profile_edit.jsp'>Edit Profile</a>
						</div>	
					</form>
				</div>
			</div>
			
			
		</div>
	</div>
	

</body>
</html>