package techinvogue.net.googlemapaddon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {
    List<Address> addressList0 = null; //Address (latitude and longitude) of the source
    List<Address> addressList1 = null;//Address (latitude and longitude) of the destination
    List<Address> addressList2 = null;//Address (latitude and longitude) of the stop-off location
    LatLng srclatlng, stopofflatlng, destnlatlng;
    String url1, url11, url111;
    int j, k, s;
    int key;
    ArrayList<LatLng> markerPoints;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onShowTraffic(View v) {
        mMap.setTrafficEnabled(true);// this will display the traffic conditions on the map
    }


    public void onRouting(View v) throws IOException
    {
        mMap.clear();
        String nostopoff = null;
        EditText txtsrc = (EditText) findViewById(R.id.txtSrc);
        String strsrc = txtsrc.getText().toString();              //parsing the edit text into string for source location
        EditText txtdestn = (EditText) findViewById(R.id.txtDestn);
        String strdestn = txtdestn.getText().toString();          //parsing the edit text into string for destination
        EditText txtstopoff = (EditText) findViewById(R.id.txtStopOff);

        String strstopoff = txtstopoff.getText().toString();      //parsing the edit text into string for stop - off location
        if (strstopoff.equals("")) {
            nostopoff = "no"; // stop off location isn't entered
        } else {
            nostopoff = "yes"; // stop off location is entered
        }

        if (txtsrc.getText().toString().trim().equals(""))//an error message will be displayed if a source is not entered
            txtsrc.setError(" Enter the source");
        if (txtdestn.getText().toString().trim().equals(""))//an error message will be displayed if a destination is not entered
            txtdestn.setError(" Enter the destination");


        else {
            if (strsrc != null || !strsrc.equals("") || strdestn != null || !strdestn.equals(""))

            //checks if source and destination are entered as inputs
            {
                Geocoder geocoder = new Geocoder(this);
                try {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//Receives current location
                    ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

                    //When source string is current location in any case we accept the GPS co-ordinates of the place the person is currently using mobile device from
                    if (strsrc.equalsIgnoreCase("Current Location")) {

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("GPS Not Found");  // GPS not found
                            builder.setMessage("Want to Enable?"); // Want to enable?
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            });
                            builder.setNegativeButton("No", null);
                            builder.create().show();
                            return;
                        } else if (cm.getActiveNetworkInfo() == null) {
                            // There are no active networks.
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("No internet connection");  // Network not found
                            builder.setMessage("Want to Enable?"); // Want to enable?
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            });
                            builder.setNegativeButton("No", null);
                            builder.create().show();
                            return;
                        } else {
                            Criteria criteria = new Criteria();
                            String provider = locationManager.getBestProvider(criteria, false);
                            if (provider != null && !provider.equals("")) {
                                if (mMap.getMyLocation() != null) {
                                    List<Address> aLc = null; //Address (latitude and longitude) of the current Location
                                    aLc = geocoder.getFromLocation(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude(), 1);
                                    Address current = aLc.get(0);
                                    srclatlng = new LatLng(current.getLatitude(), current.getLongitude());//gets the latitude and longitude of the current location
                                    mMap.addMarker(new MarkerOptions().position(srclatlng).title("Source")); //adds marker to the source of current location
                                }
                            }
                        }
                    }

                        //When user enters a string for his/her source location
                        else
                        {
                            addressList0 = geocoder.getFromLocationName(strsrc, 1);//
                            Address address0 = addressList0.get(0);
                            srclatlng = new LatLng(address0.getLatitude(), address0.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(srclatlng).title("Source")); //adds marker to the source of current location
                        }

                        addressList1 = geocoder.getFromLocationName(strdestn, 1);
                        if (nostopoff.equalsIgnoreCase("yes"))
                            addressList2 = geocoder.getFromLocationName(strstopoff, 1);


                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                    Address address1 = addressList1.get(0);

                    if (nostopoff.equalsIgnoreCase("yes")) // if the stop off location is entered, it will enter the route
                    {
                        Address address2 = addressList2.get(0);
                        stopofflatlng = new LatLng(address2.getLatitude(), address2.getLongitude());//stopofflatlng will store the latitude and longitude of the stop-off location
                        mMap.addMarker(new MarkerOptions().position(stopofflatlng).title("Stop - Off Location"));//Will add a marker to the stop - off location
                    }

                    destnlatlng = new LatLng(address1.getLatitude(), address1.getLongitude()); //destnlatlng will store the latitude and longitude of the destination

                    mMap.addMarker(new MarkerOptions().position(destnlatlng).title("Destination")); //Will add a marker to the destination

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(destnlatlng));// Zoom in the camera to the destination place

                    if (nostopoff.equalsIgnoreCase("yes")) // if stop off location is entered
                    { // function to calculate the minimum distance from source to stop off
                        float[] distance2 = new float[3];
                        Location.distanceBetween(srclatlng.latitude, srclatlng.longitude, stopofflatlng.latitude, stopofflatlng.longitude, distance2);
                        float min = distance2[0];
                        for (int i = 0; i < distance2.length - 1; i++) {
                            if (min > distance2[i + 1]) {
                                min = distance2[i + 1];
                                j = i + 1;
                            }
                        }
                        // function to calculate the minimum distance from  stop off to destn
                        float[] distance1 = new float[3];
                        Location.distanceBetween(stopofflatlng.latitude, stopofflatlng.longitude, destnlatlng.latitude, destnlatlng.longitude, distance1);
                        float min1 = distance1[0];
                        for (int i = 0; i < distance1.length - 1; i++) {
                            if (min1 > distance1[i + 1]) {
                                min1 = distance1[i + 1];
                                k = i + 1;
                            }
                        }

                        url1 = makeURL(srclatlng.latitude, srclatlng.longitude, stopofflatlng.latitude, stopofflatlng.longitude);
                        new connectAsyncTask(url1).execute();
                        url11 = makeURL(stopofflatlng.latitude, stopofflatlng.longitude, destnlatlng.latitude, destnlatlng.longitude);
                        new connectAsyncTask(url11).execute();
                    } else { // function to calculate the minimum distance from source to destn if the stop off location isnt entered
                        float[] distance = new float[3];
                        Location.distanceBetween(srclatlng.latitude, srclatlng.longitude, destnlatlng.latitude, destnlatlng.longitude, distance);
                        float min5 = distance[0];
                        for (int i = 0; i < distance.length - 1; i++) {
                            if (min5 > distance[i + 1]) {
                                min5 = distance[i + 1];
                                s = i + 1;
                            }
                        }

                        url111 = makeURL(srclatlng.latitude, srclatlng.longitude, destnlatlng.latitude, destnlatlng.longitude);
                        new connectAsyncTask(url111).execute();
                    }

                }
            }
        }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) { // function generates the url for the route from src and destn entered as function parameters, the parameters could either be the source or the stop off location or the destination
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json"); //calls on the SSL JSON Parser
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyAe0b1_VwGUPjqm82YkLCVYlGirPmQwJkE");
        return urlString.toString();
    }

    public void drawPath(String result) { //function to draw the routes between the src and the destination
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes;
            if (key == 0) { // if the key is 0 then the route between source and stop off
                routes = routeArray.getJSONObject(j);
            } else if (key == 1) {// if the key is 1 then the route between destination and stop off
                routes = routeArray.getJSONObject(k);
            } else {// if the key is 0 then the route between source and destination, just in case the stop off isnt entered.
                routes = routeArray.getJSONObject(s);
            }

            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
                            .geodesic(true)
            );
        } catch (JSONException e) {
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public void changeView(View v) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)//if the map type is normal, change to satellite view or vice versa
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void onZoom(View v) {
        // Zoom in function using + button on the UI
        if (v.getId() == R.id.btnZoomIn) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        // Zoom out function using - button on the UI
        if (v.getId() == R.id.btnZoomOut) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void onmylocation(View v) {
        //Click of the current location button
        if (v.getId() == R.id.btn_mylocation) {
            EditText source = (EditText) findViewById(R.id.txtSrc);
            source.setText("Current Location");
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));//Default location at lat and longitude 0,0 named as Marker
        mMap.setMyLocationEnabled(true);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        String url2;
        private ProgressDialog progressDialog;

        // an asynchronous class for the process to run in background and not let the user feel that the app has hung up
        connectAsyncTask(String urli) {
            url2 = urli;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // displays while it fetches the route
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        public String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url2);

            if (url2.equalsIgnoreCase(url1)) {
                key = 0;

            } else if (url2.equalsIgnoreCase(url11)) {
                key = 1;

            } else if (url2.equalsIgnoreCase(url111)) {
                key = 2;
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            } else {
                progressDialog.setMessage("Waste");
            }
        }
    }
}
