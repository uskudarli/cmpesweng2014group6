<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/bootstrap/css/bootstrapValidator.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/css/custom.css">

<div class="header">
	<!-- Hide if logged in -->
	<%
		if(request.getSession() != null)
		{
			if(request.getSession().getAttribute("loggedIn")=="true")
			{
				//out.println("<a href='javascript:void(0);' onclick='goToEdit()'>Profile</a>");
				out.println("<a href='Profile'>Profile</a>");
				out.println("<a href='javascript:void(0);' onclick='logOut()'>LogOut</a>");
				out.println("<a href='changePassword.jsp'>Change Password</a>");
			}else
				out.println("<a href='loginRegister.jsp'>Login/Register</a>");
		}
		else
			out.println("<a href='loginRegister.jsp'>Login/Register</a>");
			
	%>
	<a href="index.jsp">Home</a>
	<!-- TODO session check, show only if logged in -->
</div>


<script src="${pageContext.request.contextPath}/Resources/js/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/Resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/jquery.bootstrapvalidator/0.5.2/js/bootstrapValidator.min.js"></script>
<script src="${pageContext.request.contextPath}/Resources/js/custom.js" type="text/javascript"></script>