<!-- merhaba ben serkan -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd" >
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<HTML>
<HEAD>
	<TITLE>Welcome to Living History Project !</TITLE>
</HEAD>
<BODY bgcolor="#ffffcc">
	<font size="+3" color="green">Welcome to Living History Project!</font>
	<FORM action="main.jsp" method="get">
		<TABLE style="background-color: #ECE5B6;" WIDTH="30%">
			<TR>
				<TH width="50%">Name</TH>
				<TD width="50%"><INPUT TYPE="text" NAME="name"></TD>
			</tr>
			<TR>
				<TH></TH>
				<TD width="50%"><INPUT TYPE="submit" VALUE="Submit"></TD>
			</tr>
		</TABLE>
	<%
    String name = request.getParameter("name");
    //String connectionURL = "jdbc:mysql://localhost:3306/assign";
    String connectionURL = "jdbc:mysql://titan.cmpe.boun.edu.tr:3306/database6";         
    Connection connection = null;       
    PreparedStatement pstatement = null;       
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    int updateQuery = 0;
    if(name!=null ){
        if(name!="") {
        	try {
            	connection = DriverManager.getConnection(connectionURL, "project6", ">>passwordhere<<");
              	String queryString = "INSERT INTO people(Name) VALUES (?)";      
                pstatement = connection.prepareStatement(queryString);
                pstatement.setString(1, name);                  
                updateQuery = pstatement.executeUpdate();
                if (updateQuery != 0) {  
                	out.println("Added to database successfully.");
              	}
            } 
            catch (Exception ex) {
            	out.println("Unable to connect to database.");
            }
            finally {
                pstatement.close();
                connection.close();
            }
          }
        }
	%>
	</FORM>
	<FORM method="post" name="form">
		<table border="1">
			<tr>
				<th>Name</th>
			</tr>
			<%
			Connection con = null;
			String url = "jdbc:mysql://titan.cmpe.boun.edu.tr:3306/database6";
			String driver = "com.mysql.jdbc.Driver";
			
			
			String userName = "project6";
			String password = ">>passwordhere<<";
			int sumcount=0;
			Statement st;
			try{
				Class.forName(driver).newInstance();
				con = DriverManager.getConnection(connectionURL, "project6", ">>passwordhere<<");
				String query = "select * from people";
				st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				while(rs.next()){
				%>
				<tr>
					<td><%=rs.getString(1)%></td>
				</tr>
				<%
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			%>
		</table>
	</FORM>
</BODY>
</HTML>