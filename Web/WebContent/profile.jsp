<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Dutluk</title>

</head>
<body>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.File"%>
	<%
	HttpSession newSession = request.getSession();
	if(newSession == null)
	{
		request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
	}else if(newSession.getAttribute("email") == null
			)	
	{
		request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
	}		
	DatabaseService db = new DatabaseService();
	User originalUser = db.findUserByEmail(request.getSession().getAttribute("email").toString());
	String userId = request.getParameter("id");
	
	if(userId == null || Integer.parseInt(userId) == originalUser.getUserID())   //to see own profile
	{
		
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
					<h2 style="display: inline;"><%= originalUser.getName()%></h2>
					 <br>level
					<%= originalUser.getLevel()%>
					writer,
					<%= originalUser.getExperiencePoint()%>
					points <br> 
					
					
					<% if(originalUser.getPicID() == 0) { %>
					<img src="http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg"
						width=215 height=215 />
						<%} else {
							String path = db.pathByPicId(originalUser.getPicID());
							
							String url = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+path;
							%>
							<img src=<%out.print(url);%>
						width=215 height=215 />
						<%}
						%>


					<h4>
						"<%if(originalUser.getBio() != null) {
						out.print(originalUser.getBio());
						}%>"
					</h4>

					Gender:
					<%= originalUser.getGender()%><br>
					Birthdate:
					<%if(originalUser.getBirthdate()!=null){
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						String bd = sdf.format(originalUser.getBirthdate().getTime());
						out.print(bd);
					}%><br> 
					Mail:
					<%= originalUser.getEmail()%><br> 
					Phone:
					<% if(originalUser.getPhone()!= null) out.print(originalUser.getPhone());%><br> 
					<a href='ProfileEdit'>Edit your details</a> <br>

				</div>
			</div>
		</div>
		<br>
		<br>
		<br>
		<center>
			<h2>
				Stories of
				<%= originalUser.getName()%></h2>
		</center>
		<br>
		<br>
		<%
			ArrayList<Story> stories = db.getStoriesOfUser(originalUser.getUserID());
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
					for(Story s:stories)
					{
					%>
			<tr>
				<td>
					<% if(s.getAbsoluteDate()==null)
								out.print(s.getApproximateDate()); 
							else out.print(s.getAbsoluteDate()); %>
				</td>
				<td>
						<a href='story.jsp?storyId=<%=s.getStoryId() %>'><%= s.getContent() %></a>
	        			
				</td>
				<td><% out.print(s.getCreatedOn()); %></td >
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
		User user = db.findUserByUserId(Integer.parseInt(userId));
		
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
							
							String url = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+path;
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
			ArrayList<Story> stories = db.getStoriesOfUser(user.getUserID());
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
						for(Story s:stories)
						{
						%>
			<tr>
				<td>
					<% if(s.getAbsoluteDate()==null)
									out.print(s.getApproximateDate()); 
								else out.print(s.getAbsoluteDate()); %>
				</td>
				<td>
					<a href='story.jsp?storyId=<%=s.getStoryId() %>'><%= s.getContent() %></a>
				</td>
				<td>
					<% out.print(s.getCreatedOn()); %>
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