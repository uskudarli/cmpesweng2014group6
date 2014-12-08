<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Dutluk</title>

</head>
<body>
	<%
			
	DatabaseService db = new DatabaseService();
	User originalUser = db.findUserByEmail(request.getSession().getAttribute("email").toString());
	String userId = request.getParameter("id");
	
	if(userId == null || Integer.parseInt(userId) == originalUser.getUserID())   //to see own profile
	{
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
	<%@ page import="Dutluk.*"%>
	
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
			<%@ page import="java.sql.*, Dutluk.DatabaseService"%>
			<div class="col-xs-6">
				<div style="padding: 10px">
					<br> <br>
					<h2 style="display: inline;"><%= request.getSession().getAttribute("name")%></h2>
					 <br>level
					<%= request.getSession().getAttribute("level")%>
					writer,
					<%= request.getSession().getAttribute("xp")%>
					points <br> 
					
					
					<% if(originalUser.getPicID() == 0) { %>
					<img src="http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg"
						width=215 height=215 />
						<%} else {
							String path = db.pathByPicId(originalUser.getPicID());
							
							String url = "http://titan.cmpe.boun.edu.tr:8085/image/profile/"+path;
							%>
							<img src=<%out.print(url);%>
						width=215 height=215 />
						<%}
						%>


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
		<br>
		<br>
		<br>
		<center>
			<h2>
				Stories of
				<%= request.getSession().getAttribute("name")%></h2>
		</center>
		<br>
		<br>
		<% 
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
		<table style="width: 100%" border="1">
			<col style="width: 1%">
			<col style="width: 5%">
			<col style="width: 1%">
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
				<td>
					<% if(rs.getString(10)==null)
								out.print(rs.getString(11)); 
							else out.print(rs.getString(10)); %>
				</td>
				<td>
					<% out.print(rs.getString(3)); %>
				</td>
				<td>
					<% out.print(rs.getString(8)); %>
				</td>
			</tr>
			<%	
					}
					%>

		</table>
		<br>
		<br>
		<br>
	</div>

	<%} 
	else   //to see someone else's profile
	{
	%>
	<%@ page import="Dutluk.*"%>
	<%@ page import="java.sql.*, Dutluk.DatabaseService"%>
	<%
	try{
		User user = null;
		Connection connection = db.getConnection();
        Statement statement = connection.createStatement() ;
        ResultSet rs =statement.executeQuery("SELECT * FROM Users WHERE UserID = '"+Integer.parseInt(userId)+"'");
		while(rs.next())
		{
			String email = rs.getString(5);
			user = db.findUserByEmail(email);
		}
		
		%>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
			<%@ page import="java.sql.*, Dutluk.DatabaseService"%>
			<div class="col-xs-6">
				<div style="padding: 10px">
					<br> <br>
					<h2 style="display: inline;"><%= user.getName()%></h2>
					<form id="subscribeForm" method="post" action="Subscribe"
						class="form-horizontal">
						<input type="hidden" name="userId" value=<%=userId %> />
						<%
							
							Boolean isFollowing = db.isFollowing(originalUser.getUserID(), Integer.parseInt(userId));
							if(isFollowing)
							{
								%>
									<input type="hidden" name="func" value="unsubscribe"/> 
									<button type="submit" class="btn btn-default">Unsubscribe</button>
								<%
							}
							else
							{
								%>
								<input type="hidden" name="func" value="subscribe"/> 
								<button type="submit" class="btn btn-default">Subscribe</button>
								<%
							}
						%>
						
					</form>
					<br>level
					<%= user.getLevel()%>
					writer,
					<%= user.getExperiencePoint()%>
					points <br> 

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


					<h4>
						"<%= user.getBio()%>"
					</h4>

					Gender:
					<%= user.getGender()%><br> Birthdate:
					<%= user.getBirthdate()%><br> Mail:
					<%= user.getEmail()%><br> Phone:
					<%= user.getPhone()%><br>

				</div>
			</div>
		</div>
		<br>
		<br>
		<br>
		<center>
			<h2>
				Stories of
				<%= user.getName()%></h2>
		</center>
		<br>
		<br>
		<% 
						db = new DatabaseService();
						ResultSet rs2 = null;
						try{
							connection = db.getConnection();
					        Statement statement2 = connection.createStatement() ;
					        rs2 =statement2.executeQuery("SELECT * FROM Stories WHERE UserID = '"+Integer.parseInt(userId)+"' ORDER BY  Stories.StoryDateAbsolute DESC") ;
						}catch(Exception e)
				        {
				            out.println(e);
				        }
						%>
		<table style="width: 100%" border="1">
			<col style="width: 1%">
			<col style="width: 5%">
			<col style="width: 1%">
			<tr>
				<th>When did it happen?</th>
				<th>Story</th>
				<th>Creation Date</th>
			</tr>

			<%
						while(rs2.next())
						{
						%>
			<tr>
				<td>
					<% if(rs2.getString(10)==null)
									out.print(rs2.getString(11)); 
								else out.print(rs2.getString(10)); %>
				</td>
				<td>
					<% out.print(rs2.getString(3)); %>
				</td>
				<td>
					<% out.print(rs2.getString(8)); %>
				</td>
			</tr>
			<%	
						}
						%>

		</table>
		<br>
		<br>
		<br>
	</div>

	<%
		
		}
		catch(Exception e)
	    {
	        out.println(e);
	        
	    }
        
    %>
	<%	
	}
	%>
</body>
</html>