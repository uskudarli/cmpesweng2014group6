<%@page import="com.google.gson.JsonObject"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>${Name}</title>

</head>
<body>
	
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />
	<center><h2>Timeline of <% out.print(request.getParameter("Name")); %></h2></center>
	<br><br>
	<%@ page import="Dutluk.*"%>
	<%@ page import="java.sql.*, Dutluk.DatabaseService" %>
	<% 
	DatabaseService db = new DatabaseService();
	ResultSet rs = null;
	//ResultSet rs2 = null;
	String lat = request.getParameter("Lat");
	int placeId = 0;
	int storyId = 0;
	String userId = "";
	try{
		Connection connection = db.getConnection();
        Statement statement = connection.createStatement() ;
        rs =statement.executeQuery("SELECT * FROM Places WHERE Latitude = '"+lat+"'");
        while(rs.next())
        	placeId = rs.getInt(1);
        
	}catch(Exception e)
       {
           out.println(e);
       }
	
	 %>
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
		Connection connection = db.getConnection();
        Statement statement = connection.createStatement() ;
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
	        			<td><% out.print(rs2.getString(3)); %></td>
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
	}catch(Exception e)
    {
        out.println(e);
        
    }
      
	
	%>
	
	</table><br><br><br>
</body>
</html>