<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/bootstrap/css/bootstrapValidator.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/css/custom.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/datepicker/css/datepicker.css">

<div class="header">

	<div id="textbox">
	<p class="alignleft"><a href="index.jsp">Dutluk</a> <!-- previously "home" -->
	

	<!-- Hide rest if logged in,show only login-register -->
	<%
		if(request.getSession() != null && request.getSession().getAttribute("loggedIn")=="true")
		{
			//out.println("<a href='javascript:void(0);' onclick='goToEdit()'>Profile</a>"); ->moved to profile
			out.println("- <a href='Profile'>Me</a>");
			//out.println("- <a href='addStory.jsp'>Write</a>");
			out.println("</p><p class='alignright'><a href='javascript:void(0);' onclick='logOut()'>Log Out</a></p>");
			//out.println("<a href='changePassword.jsp'>Change Password</a>"); ->moved to edit profile
		}else{
			out.println("<p class='alignright'><a href='loginRegister.jsp'>Login/Register</a></p>");
		}
			
	%>
	<form id="searchForm" class="navbar-form navbar-right" role="search" action="Search">
	    <div class="form-group">
	        <input type="text" class="form-control" placeholder="Search">
	    </div>
    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span></button>
</form>
</div>
</div>


<script src="${pageContext.request.contextPath}/Resources/js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/Resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/jquery.bootstrapvalidator/0.5.2/js/bootstrapValidator.min.js"></script>
<script src="${pageContext.request.contextPath}/Resources/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/Resources/datepicker/js/bootstrap-datepicker.js" type="text/javascript"></script>