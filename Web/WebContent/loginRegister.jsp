<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Login</title>

</head>
<body>
	<jsp:include page="header.jsp" />
	<jsp:include page="footer.jsp" />

	<div class="container">
		<div class="row">
		
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>Login</h2>
					<form id="loginForm" method="post"
						class="loginForm form-horizontal">
						<div class="form-group">
							<input class="form-control" type="text" value="" name="email"
								placeholder="E-mail"></input>
						</div>
						<div class="form-group">
							<input class="form-control" type="password" name="password"
								placeholder="Password"></input>
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-default">Submit</button>
						</div>
						<a href="" data-toggle="modal" data-target="#myModal"> Forgot password? </a>
					</form>
				</div>
			</div>
			
			<div class="col-xs-6">
				<div style="padding: 10px">
					<h2>Register</h2>
					<form id="registerForm" method="post" class="loginForm form-horizontal" action="Register">
						<div class="form-group">
							<input class="form-control" id="regUname" type="text" value=""
								name="name" placeholder="Name"></input>
						</div>
						<div class="form-group">
							<input class="form-control" id="regEmail" type="text"
								name="email" placeholder="E-mail"></input>
						</div>
						<div class="form-group">
							<input class="form-control" id="regPass" type="password"
								name="password" placeholder="Password"></input>
						</div>
						<div class="form-group">
							<button type="submit" class="btn btn-default">Register</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade bs-example-modal-sm" id="myModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Password Recovery</h4>
				</div>
				<div class="modal-body">
					If you give your username, we will send a new password to your
					email address.
					<form method="post" name="passRecovery" id="passRecovery">
						<p>
							<input type="text" class="span3" name="uname" id="uname"
								placeholder="Username">
						</p>
						

					</form>
				</div>
				<div class="modal-footer">

					<input type="button" value="Reset" id="submit" onclick="" />
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade bs-example-modal-sm" id="registerSuccess" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				
				<div class="modal-body">
					Registration is successful!
				</div>
				<div class="modal-footer">
					<form id="redirectHome" method="post" class="form-horizontal" action="Home">
						<input type="button" name="registerRedirect" value="Ok" id="submit"/>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade bs-example-modal-sm" id="registerError" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Error</h4>
				</div>
				<div class="modal-body">
					This email address is already registered!
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>