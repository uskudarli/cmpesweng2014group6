<%@page import="Dutluk.*"%>
<%@page import="Dutluk.DatabaseService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.File"%>
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
	
	<div class="indexBody">
	<div class="row">
		<span>Search results for <%out.print(text); %>:</span>
	</div>
		<div class="row nopadding">
			<div class="col-md-4 nopadding">
			<div class="searchResults">
				<div class="searchContainer">
				<div class="searchItem clearfix">
				Stories
				</div>
				<%
				if(stories.size() == 0)
					out.print("No results found in stories");
				for(Story story:stories)
				{
					Place place = db.findPlacebyStoryId(story.getStoryId());
					String path = db.findPicturePathOfStory(story.getStoryId());
					String picUrl = "http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg";
					if(path != null) {
						picUrl = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+path;
					}
				%>
					<div class="searchItem clearfix">
						<a href='story.jsp?storyId=<%=story.getStoryId() %>'>
							
								<div class="searchImage">
									<img class="searchImageStyle" src=<%out.print(picUrl); %> />
								</div>
								<div class="searchContent">
									<table>
										<tr>
											<td>Place:</td>
											<td><%=place.getName() %></td>
										</tr>
										<tr>
											<td>Content:</td>
											<td><%
											if(story.getContent().length() > 31)
												out.print(story.getContent().substring(0, 30) + "...");
											else
												out.print(story.getContent());
											%></td>
										</tr>	
									</table>
								</div>
						</a>
					</div>
					
				<%}
				%>
				</div>
				</div>
			</div>
			<div class="col-md-4 nopadding">
			<div class="searchResults">
			<div class="searchContainer">
				<div class="searchItem clearfix">
				Places
				</div>
				<%
				if(places.size() == 0)
					out.print("No results found in places");
				for(Place place:places)
				{
					ArrayList<String> paths = db.getPicturePathsOfaPlace(place.getPlaceID());
					String picUrl = "http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg";
					if(paths.size()>0)
						picUrl = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+paths.get(0);
					
					
				%>
				<div class="searchItem clearfix">
					<a href='timeline.jsp?Id=<%=place.getPlaceID()%>'>
						<div class="searchImage">
							<img class="searchImageStyle" src=<%out.print(picUrl); %> />
						</div>
						<div class="searchContent">
							<table>
								<tr>
									<td>Place:</td>
									<td><%=place.getName() %></td>
								</tr>
								<tr>
									<td>Created on:</td>
									<td><%=place.getCreatedOn() %></td>
								</tr>	
							</table>
						</div>
					</a>
				</div>
				<%} %>
				</div>
				</div>
			</div>
			<div class="col-md-4 nopadding">
			<div class="searchResults">
			<div class="searchContainer">
				<div class="searchItem clearfix">
				Users
				</div>
				<%
				if(users.size() == 0)
					out.print("No results found in users");
				for(User u:users)
				{
					String picUrl = null;
					if(u.getPicID() == 0)
					{
						picUrl = "http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg";
					}else
					{
						picUrl = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+db.pathByPicId(u.getPicID());
					}
					String profile = "profile.jsp?id=" + u.getUserID();
					%>
					<div class="searchItem clearfix">
						<a href=<%out.print(profile); %>>
							<div class="searchImage">
								<img class="searchImageStyle" src=<%out.print(picUrl); %> />
							</div>
							<div class="searchContent">
								<table>
									<tr>
										<td>Name:</td>
										<td><%=u.getName() %></td>
									</tr>
									
								</table>
							</div>
						</a>
					</div>
				<%}
				%>
				</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>