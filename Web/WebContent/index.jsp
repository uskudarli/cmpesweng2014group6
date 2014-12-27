<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Dutluk.*"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-9"
	pageEncoding="ISO-8859-9"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Home</title>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places&key=AIzaSyCWz0T_U1zwxdI3CKepXVdlSS5iHFJste4"></script>
<script>
	var activewindow = null;
	var newmarker = null;
	function initialize() {
	
	  var markers = [];
	  var map = new google.maps.Map(document.getElementById('map-canvas'), {
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	  });
	
	  var defaultBounds = new google.maps.LatLngBounds(
		new google.maps.LatLng(40.5136, 28.8550),
		new google.maps.LatLng(41.3, 29.2550));
	  map.fitBounds(defaultBounds);
	
	  // Create the search box and link it to the UI element.
	  var input = /** @type {HTMLInputElement} */(
	      document.getElementById('pac-input'));
	  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
	  
	  // create new marker when clicked
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
	  var searchBox = new google.maps.places.SearchBox(
	    /** @type {HTMLInputElement} */(input));
	
	  // [START region_getplaces]
	  // Listen for the event fired when the user selects an item from the
	  // pick list. Retrieve the matching places for that item.
	  google.maps.event.addListener(searchBox, 'places_changed', function() {
	    var places = searchBox.getPlaces();
	
	    if (places.length == 0) {
	      return;
	    }
	    for (var i = 0, marker; marker = markers[i]; i++) {
	      marker.setMap(null);
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
	      var marker = new google.maps.Marker({
	        map: map,
	        icon: image,
	        title: place.name,
	        position: place.geometry.location
	      });
	
	      markers.push(marker);
	
	      bounds.extend(place.geometry.location);
	    }
		if(places.length == 1)
		{
			map.setCenter(places[0].geometry.location);
			map.setZoom(10);
		}else {
	   		map.fitBounds(bounds);
		}
	  });
	  // [END region_getplaces]
	
	  // Bias the SearchBox results towards places that are within the bounds of the
	  // current map's viewport.
	  google.maps.event.addListener(map, 'bounds_changed', function() {
	    var bounds = map.getBounds();
	    searchBox.setBounds(bounds);
	  });
	}
	
	google.maps.event.addDomListener(window, 'load', initialize);

	
</script>

		
</head>
  <body>
  	<%
  	HttpSession newSession = request.getSession();
  	Boolean loggedIn = false;
  	List<Story> subs = new ArrayList<Story>();
  	DatabaseService db = new DatabaseService();
	if(newSession != null && newSession.getAttribute("email") != null)
	{
		loggedIn = true;
  		User user = db.findUserByEmail(newSession.getAttribute("email").toString());
  		subs = db.getSubscriptions(user.getUserID());
	}
  	%>
  	<jsp:include page="header.jsp"/>
  	<div class="indexBody">
  	
  		<div class="row">
	
			<div class="col-md-8 nopadding">
				<div id="map-container">
					<div id="pac-container">
					<input id="pac-input" class="controls" type="text" placeholder="Search on map">
					</div>
					<div id="map-canvas"></div>
				</div>
			</div>
			<div class="col-md-4 nopadding">
				<div class="newsFeed">
				
					<%if(loggedIn) {
						%><div id="newsFeedContainer">
						<%
						for(int i=0; i<10 && i<subs.size(); i++)
						{
							Story s = subs.get(i);
							Place place = db.findPlacebyStoryId(s.getStoryId());
							
							%>
							<div class="newsFeedItem clearfix">
							<a href='story.jsp?storyId=<%=s.getStoryId() %>' class="newsFeedClickable">
							<% 
							if(s.getSubscription()==0)
							{
								User subscribed = db.findUserByUserId(s.getUserId());
								String picUrl = null;
								if(subscribed.getPicID() == 0)
								{
									picUrl = "http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg";
								}else
								{
									picUrl = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+db.pathByPicId(subscribed.getPicID());
								}
							%>
								<div class="newsFeedImage">
									<img src=<%out.print(picUrl); %> width=75 height=75/>
								</div>
								<div class="newsFeedInfo">
									<div>
									<span class="nfb">
										<%=subscribed.getName() %>
									</span>
									shared a story in timeline of <%=place.getName() %>.
									</div>
									<div class="nfcreated"><% out.print(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(s.getCreatedOn()).toString()); %></div>
								</div>
							
						
							<%}else{ 
								String path = db.findPicturePathOfStory(s.getStoryId());
								String picUrl = "http://titan.cmpe.boun.edu.tr:8085/pictures/0.jpg";
								if(path != null) {
									picUrl = "http://titan.cmpe.boun.edu.tr:8085/image"+File.separator+path;
								}
								
							%>
									<div class="newsFeedImage">
										<img src=<%out.print(picUrl); %> width=75 height=75/>
									</div>
									<div class="newsFeedInfo">
									<div>
										A story has been shared in timeline of 
										<span class="nfb">
										<%=place.getName() %>
										</span>
									</div>
									<div class="nfcreated"><% out.print(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(s.getCreatedOn()).toString()); %></div>
									</div>
								
							<%}%>
							</a>
							</div>
						<%}
						%></div><%
					} %>
				
				</div>
			</div>
	  </div>
	  <div class="row">
	  		Top stories recommendations...
	  </div>
    </div>
  </body>
</html>