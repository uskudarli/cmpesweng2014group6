<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dutluk</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />
	
	<div class="container">
		<div class="row">
		
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>New Story</h2>
					<form id="editForm" method="post" action="AddStory"
						class="editForm form-horizontal">
						<div class="form-group">
							Story: <input class="form-control" name="editStory" type="text" style="height: 200px;"/>
							Absolute Date: <input class="form-control" id="editStime" name="editStime" type=text 
								name="name" placeholder="dd/mm/yyyy"></input>
							Approximate Date: <input class="form-control" id="editStime" name="editStimeApp" type=text 
								name="name" placeholder="dd/mm/yyyy"></input>		
						</div>
						<div class="form-group">
							<button id="editProfileButton" type="submit" class="btn btn-default">Add Story</button>
						</div>
						<input type="hidden" name="func" value="edit" />
					</form>
				</div>
			</div>
			
			
		</div>
	</div>

</body>
</html>