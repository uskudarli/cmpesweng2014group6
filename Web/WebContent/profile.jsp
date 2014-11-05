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
		
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>Profile</h2>
					<form id="profileInfo" method="post"
						class="profileInfo form-horizontal">
						<div class="form-group">
						Name: <label name="name">Namehere</label><br>
						Birthdate: <label name="birthdate">Birthdatehere</label><br>
						Gender: <label name="gender">Genderhere</label><br>
						Mail: <label name="mail">Mailhere</label><br>
						Phone: <label name="phone">Phonehere</label><br>
						XP: <label name="xp">XPhere</label><br>
						Level: <label name="level">Levelhere</label><br>
						Bio: <label name="bio">Biohere</label><br>
						<a href='profile_edit.jsp'>Edit Profile</a>
						</div>
					</form>
				</div>
			</div>
			
			
		</div>
	</div>
	

</body>
</html>