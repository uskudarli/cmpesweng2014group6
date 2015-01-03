<%@page import="javax.xml.ws.http.HTTPException"%>
<%@page import="com.google.gson.JsonObject"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>${Name}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/blueimpgallery/css/blueimp-gallery.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/blueimpgallery/css/blueimp-gallery-indicator.css">
<style>
	#blueimpContainer {
	width:800px; 
	height:300px; 
	margin-left:auto; 
	margin-right:auto;
	margin-bottom: 150px;
}
</style>
</head>
<body>
	<%@ page import="Dutluk.*"%>
	<%@ page import="java.sql.*, Dutluk.DatabaseService" %>
	<%@page import="java.util.ArrayList"%>
	<%@page import="java.io.File"%>
	<%
		String placeId = request.getParameter("Id");
		HttpSession newSession = request.getSession();
		
		if(newSession == null)
		{
			request.getRequestDispatcher("loginRegister.jsp").forward(request,response);
		}else if(placeId == null || placeId.equals(""))
			request.getRequestDispatcher("index.jsp").forward(request, response);
		else if(newSession.getAttribute("email") == null)	
		{
			newSession.setAttribute("redirect", "timeline.jsp");
			newSession.setAttribute("var", "Id");
			newSession.setAttribute("Id", placeId);
			request.getRequestDispatcher("loginRegister.jsp").forward(request, response);
		}
	DatabaseService db = new DatabaseService();
	User originalUser = db.findUserByEmail(request.getSession().getAttribute("email").toString());
	Place place = db.findPlacebyPlaceId(Integer.parseInt(placeId));
	ArrayList<String> paths = db.getPicturePathsOfaPlace(Integer.parseInt(placeId));
	%>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />
	<center><h2>Timeline of <% out.print(place.getName()); %></h2></center>
	<br>
	<%
	if(paths != null && paths.size()>0)
	{%>
	<div id="blueimpContainer">
		<div id="blueimp-gallery-carousel" class="blueimp-gallery blueimp-gallery-carousel blueimp-gallery-controls">
		    <div class="slides"></div>
		    <h3 class="title"></h3>
		    <a class="prev"></a>
		    <a class="next"></a>
		    <a class="play-pause"></a>
		    <ol class="indicator"></ol>
		</div>
	</div>
	<%
		String uploadPath = "http://titan.cmpe.boun.edu.tr:8085/image";
		out.print("<div id='links' style='display: none;'>");
		for(String s: paths)
		{
			out.print("<a href='");
			out.print(uploadPath+File.separator + s + "'>");
			out.print("<img src='" + uploadPath+File.separator + s + "' alt='"+s +"'></a>");	
		}
		out.print("</div");
	}
	%>
	
	<br>
	
	 <form id="subscribeForm" method="post" action="Subscribe"
		class="form-horizontal">
		<input type="hidden" name="placeId" value=<%=placeId %> />
		<%
			
		
		//get number of subscribers
		int numofSubscribers=0;
		ResultSet rsx = null;
		Connection connection = db.getConnection();
        Statement statement = connection.createStatement() ;        
        rsx =statement.executeQuery("SELECT COUNT(*) FROM `SubscriptionsToPlaces` WHERE FollowedID= '"+placeId+"' AND IsActive=1;");
        if(rsx.next()) numofSubscribers=rsx.getInt(1);
		
	
			Boolean isFollowing = db.isFollowingPlace(originalUser.getUserID(), place.getPlaceID());
			if(isFollowing)
			{
				%>
					<input type="hidden" name="func" value="unsubscribePlace"/> 
					<button type="submit" class="btn btn-default">Unsubscribe</button>
				<%
			}
			else
			{
				%>
				<input type="hidden" name="func" value="subscribePlace"/> 
				<button type="submit" class="btn btn-default">Subscribe</button>
				<%
			}
		%>
		<%= numofSubscribers %> people subscribed.
		
	</form>
	<%
		String addStoryDirect = "addStory.jsp?Lat=" + place.getLatitude() + "&Lon=" + place.getLongtitude() + "&Name=" + place.getName();
	%>
	<a class="btn btn-default" href=<%out.print(addStoryDirect); %> >Add new Story</a>
	 
		<table style="width:100%" border = "1">
		<col style = "width:2%">
		<col style = "width:5%">
		<col style = "width:1%">
		<col style = "width:1%">
		<tr>
			<th>When did it happen?</th>
			<th>Story</th>
			<th>Written by</th>
			<th>Creation Date</th>
		</tr>
		
		<%
		
	try{
		int storyId = 0;
		String userId = "";
		ResultSet rs = null;
        rs =statement.executeQuery("SELECT * FROM StoriesInPlaces WHERE PlaceID = '"+placeId+"'");
        
        while(rs.next())
        {
        	storyId = rs.getInt(1);
        	Statement statement2 = connection.createStatement() ;
        	ResultSet rs2 = statement2.executeQuery("SELECT * FROM Stories WHERE StoryID ='"+storyId+"'");
        	while(rs2.next())
        	{
        		%>
	        		<tr>
	        			<td><% 
	        			if(rs2.getString(10)==null) out.print(rs2.getString(11));
	        			else out.print(rs2.getString(10)); %></td>
	        			<td><a href='story.jsp?storyId=<%=storyId %>'><%= rs2.getString(3) %></a></td>
	        			<td><a href = 'profile.jsp?id=<%out.print(rs2.getString(2)); %>'>
	        			<%
	        			userId = rs2.getString(2);
	        			Statement statement3 = connection.createStatement();
	        			ResultSet rs3 = statement3.executeQuery("SELECT * FROM Users WHERE UserId = '"+userId+"'");
	        			while(rs3.next())
	        			{
	        				out.print(rs3.getString(2)); 
	        			}
	        			
	        			%></a></td>
	        			<td><% out.print(rs2.getString(8)); %></td>
	        		</tr>
	        	<%
        	}
        	
        }
   
        statement.close();
        connection.close();
	}catch(Exception e)
    {
        out.println(e);
        
    }
      
	
	%>
	
	</table><br><br><br>
	<script src="${pageContext.request.contextPath}/Resources/blueimpgallery/js/blueimp-gallery.min.js"></script>
	<script src="${pageContext.request.contextPath}/Resources/blueimpgallery/js/blueimp-gallery-fullscreen.js"></script>
	<script src="${pageContext.request.contextPath}/Resources/blueimpgallery/js/blueimp-gallery-indicator.js"></script>
	<script src="${pageContext.request.contextPath}/Resources/blueimpgallery/js/blueimp-helper.js"></script>
	<script>
	blueimp.Gallery(
	    document.getElementById('links').getElementsByTagName('a'),
	    {
	        container: '#blueimp-gallery-carousel',
	        carousel: true
	    }
	);
	</script>
</body>
</html>