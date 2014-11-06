<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Home</title>
<script
	src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCWz0T_U1zwxdI3CKepXVdlSS5iHFJste4"></script>
<script>
var map;
function initialize() {
  var mapOptions = {
    zoom: 8,
    center: new google.maps.LatLng(-34.397, 150.644)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);
</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="map-container">
<div id="map-canvas"></div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>