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
	scrollwheel: false,
    zoom: 10,
    center: new google.maps.LatLng(41.0136, 28.9550)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
  $.ajax({
		type: "get",
		url: "Markers",
		datatype: "json",
		success: function(data){
			for(var i=0; i<data.length; i++)
			{
				var marker = new google.maps.Marker({
		            position: new google.maps.LatLng(data[i].Latitude, data[i].Longtitude),
		            map: map,
		            title: data[i].Name
				});
				marker.setMap(map);
				
				var content = "<a href='profile.jsp' id='infowindow'>" + data[i].Name + "</a>";
				
				attachInfoWindow(marker, content);
			}
		}
	});
}

function attachInfoWindow(marker, content)
{
	var infowindow = new google.maps.InfoWindow({
	    content: content
	  });
	google.maps.event.addListener(marker, 'click', function() {
	    infowindow.open(marker.get('map'), marker);
	  });
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