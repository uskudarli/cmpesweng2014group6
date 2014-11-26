<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Dutluk</title>

</head>
<body>
	<%@ page import="Dutluk.*"%>
	<%
		HttpSession newSession = request.getSession();
		if(newSession == null)
		{
			request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
		}else if(newSession.getAttribute("email") == null
				|| newSession.getAttribute("name") == null
				|| newSession.getAttribute("birthdate") == null
				|| newSession.getAttribute("gender") == null
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
			<%@ page import="java.sql.*, Dutluk.DatabaseService" %>
			<div class="col-xs-6">
				<div style="padding: 10px">
					<br>
					<br>
					<h2 style="display: inline;"><%= request.getSession().getAttribute("name")%></h2>
					<a>subscribe</a> <br>level
					<%= request.getSession().getAttribute("level")%>
					writer,
					<%= request.getSession().getAttribute("xp")%>
					points <br>
					<img
						src="http://titan.cmpe.boun.edu.tr:8085/pictures/<%= request.getSession().getAttribute("picid")%>.jpg"
						width=215 height=215 />


					<h4>
						"<%= request.getSession().getAttribute("bio")%>"
					</h4>

					Gender:
					<%= request.getSession().getAttribute("gender")%><br>
					Birthdate:
					<%= request.getSession().getAttribute("birthdate")%><br> Mail:
					<%= request.getSession().getAttribute("mail")%><br> Phone:
					<%= request.getSession().getAttribute("phone")%><br> <a
						href='ProfileEdit'>Edit your details</a> <br>

				</div>
				</div>
				</div>
				<br><br><br>
				<center>
					<h2>Stories of <%= request.getSession().getAttribute("name")%></h2>
				</center>
				<br><br>
					<% 
					DatabaseService db = new DatabaseService();
					ResultSet rs = null;
					try{
						Connection connection = db.getConnection();
				        Statement statement = connection.createStatement() ;
				        rs =statement.executeQuery("SELECT * FROM Stories WHERE UserID = '"+(int)request.getSession().getAttribute("userid")+"' ORDER BY  Stories.StoryDateAbsolute DESC") ;
					}catch(Exception e)
			        {
			             out.println(e);
			        }
					%>
					<table style="width:100%">
					<tr>
						<th>When did it happen?</th>
						<th>Story</th>
						<th>Creation Date</th>
					</tr>
					
					<%
					while(rs.next())
					{
					%>
						<tr>
							<td><% out.print(rs.getString(10)); %></td>
							<td><% out.print(rs.getString(3)); %></td>
							<td><% out.print(rs.getString(8)); %></td>
						</tr>
					<%	
					}
					%>
					
					</table><br><br><br>
					
			
		


	</div>


</body>
</html>