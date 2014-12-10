<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Home</title>
<script
	src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCWz0T_U1zwxdI3CKepXVdlSS5iHFJste4"></script>
<script>
var map;
var activewindow = null;
var newmarker = null;
function initialize() {
  var mapOptions = {
	scrollwheel: true,
    zoom: 10,
    center: new google.maps.LatLng(41.0136, 28.9550)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
  //click map to get coordinates
  google.maps.event.addListener(map, 'click', function(event) {
	  getCoordinates(event.latLng);
	  });

	function getCoordinates(location) {
		if(newmarker != null)
			newmarker.setMap(null);
		if(activewindow != null)
			activewindow.close();
		newmarker = new google.maps.Marker({
		    position: location,
		    map: map,
		  });
		var lat = location.lat();
		var lon = location.lng();
		fillLatLon(lat, lon);
		var content= "<a href='addStory.jsp?Lat="+lat+"&Lon="+lon+"' id='infowindow'>" + "Add Story Here" + "</a>";
		attachInfoWindow2(newmarker, content);
		infowindow.open(map,newmarker);
		
	}
	function fillLatLon(lat, lon)
	{ 
	     $('#latbox').val(lat);
	     $('#lngbox').val(lon);
	}
	
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
				
				var content = "<a href='timeline.jsp?Id=" + data[i].PlaceID +"' id='infowindow'>" + data[i].Name + "</a><br> <a href='addStory.jsp?Lat=" + data[i].Latitude +"&Lon="+data[i].Longtitude+"&Name="+data[i].Name+"' id='infowindow'>(Add new Story?)</a> or <a href='timeline.jsp?Id=" + data[i].PlaceID +"' id='infowindow'> Show stories of place</a>";
				
				attachInfoWindow(marker, content);
			}
		}
	});
}

function attachInfoWindow2(marker, content)
{
	var infowindow = new google.maps.InfoWindow({
	    content: content
	  });
	google.maps.event.addListener(marker, 'click', function() {
		if(activewindow != null)
			activewindow.close();
	    infowindow.open(marker.get('map'), marker);
	    activewindow = infowindow;
	  });
}

function attachInfoWindow(marker, content)
{
	var infowindow = new google.maps.InfoWindow({
	    content: content
	  });
	google.maps.event.addListener(marker, 'click', function() {
		if(activewindow != null)
			activewindow.close();
		if(newmarker!=null) {
			newmarker.setMap(null);
			newmarker = null;
		}
	    infowindow.open(marker.get('map'), marker);
	    activewindow = infowindow;
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