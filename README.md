# Google-Maps-Nav-Add-On

A Google Maps Navigation Add-On will allow users to find a convenient location to stop at while in route to their final destination.

The application has 2 main features : Search and Navigate. The user can search for a particular location. For the purpose of navigation, the application takes three inputs i.e. the source, destination and an optional stop-off location and places three markers on each one of them and displays an optimal path connecting the 3 locations. We've also incorporated the zoom in and zoom out features. And the current location can also be selected in place of source using GPS co-ordinates. Also, the traffic conditions can be seen along with the satellite view of the map. Also, an FAQ section has been added in the Action Bar, which can be viewed on clicking the 3 vertical dots.

The application also handles exceptions such as if the internet connection or gps is disabled, an alert dialogue box will be displayed asking user if he/she wants to enable the same. Also, if the user doesn't enter any input, a warning message is displayed asking the user to enter the input.

MainActivity.java: This is the file from where execution starts i.e. the screen that appears when application launches. An application launcher icon has been added. In the first activity, an image has been added along with two buttons namely - Search and Navigate. On click on the Action Bar, an FAQ section can be seen.

SearchActivity.java: On clicking the Search button in main activity, the user gets re-directed to this activity. The user can input the place he/she wishes to search. When the user presses the 'Search' button, a marker is placed on the desired location.There are 2 buttons '+' and '-' which will zoom in or zoom out on the marked locations. On clicking the button for current location on the map, the user can view his/her current location on the map. Theres also a Compass on the map, which points northward, even if the user rotates the map. Also, a "Change View" button is added, which will toggle between normal and satellite view of the map.

MapsActivity.java: On clicking the Navigate button in main activity, the user gets re-directed to this activity. The user needs to enter 3 inputs namely - source, destination and an optional stop-off location. A button for 'current location' is added. On clicking the button, the GPS coordinates of the current location are considered as the source. There are 2 buttons '+' and '-' which will zoom in or zoom out on the marked locations. Also, a "Change View" button is added, which will toggle between normal and satellite view of the map. The same functionality of the map in the SearchActivity is implemented here as well. Also, theres a button called "Draw the route" which will display an optimal route connecting source to destination via an optional stop-off location. The optimal route is based on the distance parameter. There is another button called "Show Traffic" which will display traffic conditions on the map.

JSONParser.java: This file will make a default HTTP client and help generate a URL for the two LatLng of places that are given as input parameters. In order to make the connection successful, the URL is generated over SSL(Secured Socket Layer) - http://maps.googleapis.com/maps/api/directions/json. This will return a JSON object to be passed over to AsyncTask in the MapsActivity.

Color.xml: The html codes of the three colors namely white, light blue and dark blue are mentioned in this file.

buttonstyle.xml: This XML file contains the coding for the gradient effect of buttons in the home screen of the application.

AndroidManifest.xml: This file contains all the permissions listed as below: INTERNET, ACCESS_NETWORK_STATE, WRITE_EXTERNAL_STORAGE, READ_GSERVICES, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION

We've also used Google Maps API Key for the Android Version

The orientation of the application has been set to Potrait view for better visualization.
