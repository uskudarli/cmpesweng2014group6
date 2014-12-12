<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Home</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/Resources/phototeam/layout/styles/homepage.css">
<script
	src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCWz0T_U1zwxdI3CKepXVdlSS5iHFJste4&ibraries=places&sensor=true"></script>
<script>
var map;
var activewindow = null;
var newmarker = null;
function initialize() {
	var markers = [];
  var mapOptions = {
	scrollwheel: true,
    zoom: 10,
    center: new google.maps.LatLng(41.0136, 28.9550)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
  
  
  var input = /** @type {HTMLInputElement} */(
	      document.getElementById('pac-container'));
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
	
  var searchBox = new google.maps.places.SearchBox(
		    /** @type {HTMLInputElement} */(input));
  
  
  google.maps.event.addListener(searchBox, 'places_changed', function() {
	    var places = searchBox.getPlaces();

	    if (places.length == 0) {
	      return;
	    }
	    for (var i = 0, searchmarker; searchmarker = markers[i]; i++) {
	      searchmarker.setMap(null);
	    }

	    // For each place, get the icon, place name, and location.
	    markers = [];
	    var bounds = new google.maps.LatLngBounds();
	    for (var i = 0, place; place = places[i]; i++) {
	      var image = {
	        url: place.icon,
	        size: new google.maps.Size(71, 71),
	        origin: new google.maps.Point(0, 0),
	        anchor: new google.maps.Point(17, 34),
	        scaledSize: new google.maps.Size(25, 25)
	      };

	      // Create a marker for each place.
	      var searchmarker = new google.maps.Marker({
	        map: map,
	        icon: image,
	        title: place.name,
	        position: place.geometry.location
	      });

	      markers.push(searchmarker);

	      bounds.extend(place.geometry.location);
	    }
  
	    map.fitBounds(bounds);
  });
  
  google.maps.event.addListener(map, 'bounds_changed', function() {
	    var bounds = map.getBounds();
	    searchBox.setBounds(bounds);
	  });
  
	  
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
<div class="indexBody">
	<div class="row">
	
			<div class="col-md-9 nopadding">
				<div id="map-container">
					<div id="pac-container">
					<input id="pac-input" class="controls" type="text" placeholder="Search on map">
					</div>
					<div id="map-canvas"></div>
				</div>
			</div>
			<div class="col-md-3 newsFeed nopadding">
				<div id="newsFeedContainer">newsfeed</div>
			</div>
	  </div>
	  <div class="row">
	  		Top stories recommendations...
	  </div>
</div>


<jsp:include page="footer.jsp"/>
</body>
</html>