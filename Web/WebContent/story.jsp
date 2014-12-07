<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dutluk</title>
<style>
#nav {
	line-height: 30px;
	height: 300px;
	width: 100px;
	float: right;
	padding-top: 25px;
}

#story {
	width: 350px;
	height: 100px;
	float: left;
	padding: 10px;
}

#storyend {
	width: 350px;
	float: left;
	padding: 10px;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />
	<%@ page import="Dutluk.*"%>
	<%@ page import="java.sql.*, Dutluk.DatabaseService"%>
	<%
		ResultSet rs = null;
		String author = null;
		DatabaseService db = new DatabaseService();
		String storyId = request.getParameter("storyId");
		try{
			Connection connection = db.getConnection();
	        Statement statement = connection.createStatement() ;
	        rs =statement.executeQuery("SELECT * FROM Stories WHERE StoryID = '"+storyId+"'");
	        while(rs.next())
	        {
	        	int userId = rs.getInt(2);
	        	Statement statement3 = connection.createStatement();
    			ResultSet rs3 = statement3.executeQuery("SELECT * FROM Users WHERE UserId = '"+userId+"'");
    			while(rs3.next())
    			{
    				author = rs3.getString(2); 
    			}
        	%>

	<div id="nav">
		recommendation<br> falan<br> filan<br>
	</div>
	<div id="story">
		<center>
			<h2>Story Name</h2>
			<h4>
				Written by <a href='profile.jsp?id=<%= userId %>'> <%= author %>
				</a>
			</h4>
		</center>
		<p>
			<%= rs.getString(3) %>
			<br>
			<br>
			<%
				String email = request.getSession().getAttribute("email").toString();
				statement3 = connection.createStatement();
				rs3 = statement3.executeQuery("SELECT * FROM Users WHERE Mail = '"+email+"'");
				int currentUserId = 0;
				while(rs3.next())
				{
					currentUserId = rs3.getInt(1);
					request.getSession().setAttribute("StoryID", storyId);
					request.getSession().setAttribute("UserID", currentUserId);
				}
				rs3 = statement3.executeQuery("SELECT * FROM Rate WHERE StoryID = '"+storyId+"' AND UserID = '"+currentUserId+"'");
				int rate = 0;
				while(rs3.next())
				{
					%>
			Your current rate is:
			<%
					rate = rs3.getInt(3);
					out.print(rate);
				}
				
				if(rate == 0)
				{
				%>
			Rate Story:
		<form method="post" action="RateStory">
			<select name="rate">
				<option value="1">Terrible</option>
				<option value="2">Not good</option>
				<option value="3">It's OK</option>
				<option value="4">Good</option>
				<option value="5">Great!</option>
			</select> <input type="submit" value="Rate">
		</form>

	</div>

	<%
				}
				%><br>Average Rate for this story is:<%= rs.getInt(7)%>
	<%
	        }
	        
		}
		catch(Exception e){
			out.print(e);
		}
	%>
</body>
</html>