<%@page import="Dutluk.*"%>
<%@page import="Dutluk.DatabaseService"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Search Results</title>
</head>
<body>
	<%
	String text = request.getParameter("text");
	

	if(text == null || text.equals(""))
		response.sendRedirect("index.jsp");
	DatabaseService db = new DatabaseService();
	ArrayList<Story> stories = db.searchStory(text);
	ArrayList<Place> places = db.searchPlace(text);
	ArrayList<User> users = db.searchUser(text);
	%>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />
	<span>Search results for <%out.print(text); %>:</span>
	<div class="container">
		<div class="row">
			<div class="col-sm-4">
				<span>Stories</span><br><br>
				<%
				for(Story story:stories)
				{
					if(story.getContent().length() > 31)
						out.print("Story Content: "+ story.getContent().substring(0, 30) + "...<br>");
					else
						out.print("Story Content: "+ story.getContent() + "<br>");
					out.print("Created by: " + db.findUserByUserId(story.getUserId()).getName() + "<br>");
					out.print("Created on: " + story.getCreatedOn() + "<br>");
					String storypage = "story.jsp?storyId=" + story.getStoryId();
					%>
					<a class="btn btn-default" href=<%out.print(storypage); %>>Click to view details</a><br><br>
				<%}
				%>
			</div>
			<div class="col-sm-4">
				<span>Places</span><br><br>
				<%
				for(Place place:places)
				{
					out.print("Place Name: "+ place.getName() + "<br>");
					out.print("Created on: " + place.getCreatedOn() + "<br>");
					String timeline = "timeline.jsp?Id=" + place.getPlaceID();
					%>
					<a class="btn btn-default" href=<%out.print(timeline); %>>Click to view details</a><br><br>
				<%}
				%>
			</div>
			<div class="col-sm-4">
				<span>Users</span><br><br>
				<%
				for(User u:users)
				{
					out.print("User Name: "+ u.getName() + "<br>");
					String profile = "profile.jsp?id=" + u.getUserID();
					%>
					<a class="btn btn-default" href=<%out.print(profile); %>>Click to view details</a><br><br>
				<%}
				%>
			</div>
		</div>
	</div>
</body>
</html>