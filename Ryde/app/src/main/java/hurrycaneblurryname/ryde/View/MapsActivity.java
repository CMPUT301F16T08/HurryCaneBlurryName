package hurrycaneblurryname.ryde.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.base.Predicate;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;

import hurrycaneblurryname.ryde.AddRequestCommand;
import hurrycaneblurryname.ryde.Command;
import hurrycaneblurryname.ryde.CommandManager;
import hurrycaneblurryname.ryde.DataParser;
import hurrycaneblurryname.ryde.DescriptionTooLongException;
import hurrycaneblurryname.ryde.LocationAddressConverter;
import hurrycaneblurryname.ryde.LocationException;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.Model.UserHolder;
import hurrycaneblurryname.ryde.NetworkUtil;
import hurrycaneblurryname.ryde.Notification;
import hurrycaneblurryname.ryde.NotificationManager;
import hurrycaneblurryname.ryde.R;

/**
 * Sources:
 * http://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/
 * http://www.androidtutorialpoint.com/intermediate/android-map-app-showing-current-location-android/
 * Date Accessed: 11/10/2016
 * Author: Navneet
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    private User user;
    public Double distance;
    LatLng MarkerPoints[];
    boolean pointSet[];
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    GoogleMap.OnMapClickListener mapClick;
    Request sendRequest = null;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    private int notif_number = 0;
    private TextView notifText = null;
    private ArrayList<Notification> notificationList;

    Marker startMarker;
    Marker endMarker;
    Polyline routeLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        MarkerPoints = new LatLng[2];
        pointSet = new boolean[]{false,false};
        startMarker = null;
        endMarker = null;
        routeLine = null;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Hide Confirm Request Button Until Two Locations are Chosen
        Button requestButton = (Button) findViewById(R.id.button_map_request);
        requestButton.setVisibility(View.INVISIBLE);


        //------Drawer Menu Stuff--------
        setTitle(R.string.app_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // retrieve login user info
        user = UserHolder.getInstance().getUser();
        if (user == null) {
            finish();
        }
        toggleDriverMenu(user);

        // set username and email
        View header=navigationView.getHeaderView(0);
        TextView riderUsername = (TextView)header.findViewById(R.id.riderUsername);
        TextView riderEmail = (TextView)header.findViewById(R.id.riderEmail);
        riderUsername.setText(user.getUsername());
        riderEmail.setText(user.getEmail());

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = UserHolder.getInstance().getUser();

        // Enable driver menu
        toggleDriverMenu(user);

        // Notifications
        notificationList = NotificationManager.updateNotifs();
        notif_number = notificationList.size();
        updateNotifCount(notif_number);
        if (notif_number > 0) {
            Toast.makeText(this, "You have new notifications", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Enables driver related menu, such as search for requests and accepted requests
     */
    private void toggleDriverMenu(User user) {
        Menu nav_Menu = navigationView.getMenu();
        if (user==null) {
            return;
        }
        if (user.getRole().equals("rider")) {
            nav_Menu.findItem(R.id.nav_search).setEnabled(false);
            nav_Menu.findItem(R.id.nav_pickup).setEnabled(false);
            nav_Menu.findItem(R.id.nav_driversignup).setEnabled(true);

        } else {
            nav_Menu.findItem(R.id.nav_search).setEnabled(true);
            nav_Menu.findItem(R.id.nav_pickup).setEnabled(true);
            nav_Menu.findItem(R.id.nav_driversignup).setEnabled(false);
            nav_Menu.findItem(R.id.nav_driversignup).setVisible(false);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //Remove default toolbar
        mMap.getUiSettings().setMapToolbarEnabled(false);

        hideConfirmButton();

        //Move default GPS button location
        //Source: http://stackoverflow.com/questions/14489880/change-position-of-google-maps-apis-my-location-button
        //Date Accessed: 11/10/2016
        //Author: Sahil Jain
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        View mapView = mapFragment.getView();

        if (mapView != null &&
                mapView.findViewById(1) != null) {

            //Get view
            View view = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    view.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(2, 0, 40, 40);

        }

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(mapClick = new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (pointSet[0] && pointSet[1]) {
                    pointSet[0] = false;
                    pointSet[1] = false;
                    mMap.clear();
                    hideConfirmButton();
                }
                else {

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    // Adding new item to the Array
                    if(!pointSet[0]){
                        MarkerPoints[0] = point;
                        pointSet[0] = true;
                        //For the start location, the color of marker is GREEN
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        options.title("Start Location");
                        // Add new marker to the Google Map Android API V2
                        startMarker = mMap.addMarker(options);
                    }
                    else{
                        MarkerPoints[1] = point;
                        pointSet[1] = true;
                        //for the end location, the color of marker is RED
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        options.title("End Location");
                        // Add new marker to the Google Map Android API V2
                        endMarker = mMap.addMarker(options);
                    }

                    // Checks, whether start and end locations are captured
                    if (pointSet[0] && pointSet[1]) {
                        setRoute();
                    }
                }
            }
        });

    }

    private void setRoute(){
        LatLng origin = MarkerPoints[0];
        LatLng dest = MarkerPoints[1];

        // Getting URL to the Google Directions API
        String url = getUrl(origin, dest);
        Log.d("onMapClick", url.toString());

        if(NetworkUtil.getConnectivityStatusString(MapsActivity.this) != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            FetchUrl FetchUrl = new FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        }
        else{
            distance = getRoughDistance(MarkerPoints[0], MarkerPoints[1]);
            System.out.println("Rough Distance:" + distance);
        }

        //move map camera to show both points
        //Source : http://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers
        //Date Accessed : 11/24/2016
        //Author: andr
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latlng : MarkerPoints) {
            builder.include(latlng);
        }
        LatLngBounds bounds = builder.build();
        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

        showConfirmButton();
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                distance = parser.getDistance(jObject)/1.0;
                System.out.println("Actual Route Distance:" + distance);
                if(distance.equals(0.0)){
                    distance = getRoughDistance(MarkerPoints[0], MarkerPoints[1]);
                    System.out.println("Rough Distance:" + distance);
                }
                //sendRequest.setEstimate(x);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try{
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    routeLine = mMap.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    //Source: https://www.youtube.com/watch?v=dr0zEmuDuIk
    //Date accessed: 11/10/2016
    //Author: TechAcademy
    public void onSearchStart(View view){
        //Don't do anything if offline
        if(NetworkUtil.getConnectivityStatusString(MapsActivity.this) == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
            return;
        }
        // Hide Keyboard
        View keyboard = this.getCurrentFocus();
        if (keyboard != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(keyboard.getWindowToken(), 0);
        }

        EditText location_tf = (EditText)findViewById(R.id.text_map_search_start);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;


        if(location != null || !location.isEmpty()){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.isEmpty()) {
                return;
            }

            //Get position
            Address address = addressList.get(0);
            LatLng latlng = new LatLng(address.getLatitude() , address.getLongitude());

            MarkerPoints[0] = latlng;
            pointSet[0] = true;
            if(!pointSet[1]){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker
            options.position(latlng);

            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            options.title("Start Location");
            // Add new marker to the Google Map Android API V2
            if(startMarker != null){
                startMarker.remove();
                startMarker = null;
            }
            startMarker = mMap.addMarker(options);

            // Checks, whether start and end locations are captured
            if (pointSet[0] && pointSet[1]) {
                if(routeLine != null){
                    routeLine.remove();
                    routeLine = null;
                }
                setRoute();
            }
        }
    }

    public void onSearchEnd(View view){
        //Don't do anything if offline
        if(NetworkUtil.getConnectivityStatusString(MapsActivity.this) == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
            return;
        }
        // Hide Keyboard
        View keyboard = this.getCurrentFocus();
        if (keyboard != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(keyboard.getWindowToken(), 0);
        }

        EditText location_tf = (EditText)findViewById(R.id.text_map_search_end);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;


        if(location != null || !location.isEmpty()){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.isEmpty()) {
                return;
            }

            //Get position
            Address address = addressList.get(0);
            LatLng latlng = new LatLng(address.getLatitude() , address.getLongitude());

            MarkerPoints[1] = latlng;
            pointSet[1] = true;
            if(!pointSet[0]) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker
            options.position(latlng);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options.title("End Location");
            // Add new marker to the Google Map Android API V2
            if(endMarker != null){
                endMarker.remove();
                endMarker = null;
            }
            endMarker = mMap.addMarker(options);



            // Checks, whether start and end locations are captured
            if (pointSet[0] && pointSet[1]) {
                if(routeLine != null){
                    routeLine.remove();
                    routeLine = null;
                }
                setRoute();
            }
        }
    }


    private void showConfirmButton(){

        Button requestButton = (Button) findViewById(R.id.button_map_request);
        requestButton.setVisibility(View.VISIBLE);
        mMap.setPadding(0,0,0,120);

    }

    private void hideConfirmButton(){
        Button requestButton = (Button) findViewById(R.id.button_map_request);
        requestButton.setVisibility(View.INVISIBLE);
        mMap.setPadding(0,0,0,0);

    }

    public void onRequestConfirm(View view){
        User user = UserHolder.getInstance().getUser();
        sendRequest = new Request(user);
        sendRequest.setEstimate(4 + (distance/1000.0));
        sendRequest.setDistance(distance/1000.0);
        try {
            sendRequest.setLocations(MarkerPoints[0], MarkerPoints[1]);
            Log.i("RequestLatLng", Arrays.toString(sendRequest.getFrom()) + " " + Arrays.toString(sendRequest.getTo()));
        } catch (LocationException e) {
            e.printStackTrace();
        }

        //TODO Estimate price of trip

        // set description dialog
        setDescriptionAlertDialog();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            pointSet[0] = false;
            pointSet[1] = false;
            mMap.clear();
            hideConfirmButton();
        }
    }

    // Notifications in action bar
    // http://stackoverflow.com/questions/17696486/actionbar-notification-count-icon-badge-like-google-has
    // Accessed November 25, 2016
    // Posts by: squirrel, AndrewS, pang
    // http://www.devexchanges.info/2015/05/building-actionbar-notifications-count.html
    // Accessed November 25, 2016
    // Post by Hong Thai

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final View menu_notif = menu.findItem(R.id.menu_hotlist).getActionView();
        notifText = (TextView) menu_notif.findViewById(R.id.notiflist_hot);
        updateNotifCount(notif_number);

        menu_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MapsActivity.this, menu_notif);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_popup, popup.getMenu());

                populateNotifItems(popup);
                popup.show();
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void populateNotifItems(final PopupMenu popup) {
        if (notificationList.size() > 0) {
            for (Notification n : notificationList) {
                MenuItem i = popup.getMenu().add(n.getMessage());
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    for (Notification n : notificationList) {
                        if (n.getMessage().equals(item.getTitle())) {
                            NotificationManager.dismissNotification(n); // Remove notification from server
                        }
                    }
                    notificationList.remove(item.getTitle()); //remove notification after clicked (read)
                    updateNotifCount(notificationList.size());

                    Intent offerIntent = new Intent(MapsActivity.this, MyRideRequestsRemake.class);
                    offerIntent.putExtra("tabpage", 0);

                    startActivity(offerIntent);
                    popup.dismiss();

                    return true;
                }
            });
        }
    }


    private void updateNotifCount(final int new_notif_number) {
        notif_number = new_notif_number;
        if (notifText == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_notif_number == 0)
                    notifText.setVisibility(View.INVISIBLE);
                else {
                    notifText.setVisibility(View.VISIBLE);
                    notifText.setText(Integer.toString(new_notif_number));
                }
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            Intent editProfile = new Intent(this, EditUserProfile.class);
            startActivity(editProfile);
        } else if (id == R.id.nav_ride_requests) {
            Intent rideRequests = new Intent(this, MyRideRequestsRemake.class);
            startActivity(rideRequests);
        } else if (id == R.id.nav_search) {
            Intent search = new Intent(this, SearchRequestsActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable("currLocation", mLastLocation);
            search.putExtras(extras);
            startActivity(search);
        } else if (id == R.id.nav_pickup) {
            Intent pickups = new Intent(this, MyPickupActivity.class);
            startActivity(pickups);
        } else if (id == R.id.nav_driversignup) {
            Intent driverSignup = new Intent(this, AddDriverInfo.class);
            startActivity(driverSignup);
        } else if (id == R.id.nav_logout) {
            UserHolder.getInstance().setUser(new User(null));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Source: http://stackoverflow.com/questions/10903754/input-text-dialog-android
    //Date Accessed: 11/12/2016
    //Author: Aaron
    private void setDescriptionAlertDialog(){
        //text dialog to input an optional string description for the ride request
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Description");

        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        alertDialogBuilder.setView(input);

        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generaFdeted catch block
            }
        });
        alertDialogBuilder.setNegativeButton("Confirm",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Please provide a description", Toast.LENGTH_SHORT).show();
                    setDescriptionAlertDialog();
                } else {

                    //TODO Shorten input text if too long
                    try {
                        sendRequest.setDescription(input.getText().toString());
                    } catch (DescriptionTooLongException e) {
                        e.printStackTrace();
                    }
                    setEstimateAlertDialog();
                }

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setEstimateAlertDialog(){
        //text dialog to input an optional string description for the ride request
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Set fee offer for the trip:");
        alertDialogBuilder.setMessage(
                "Estimated distance: "+ new DecimalFormat("#0.0").format(distance/1000) +" km\n"+
                        "Estimated fee: $"+ new DecimalFormat("#0.00").format(sendRequest.getEstimate())+ "\n");

//        final EditText input = new EditText(this);
        // Specify the type of input expected
        final  EditText input = new EditText(this);
        input.setHint(new DecimalFormat("#0.00").format(sendRequest.getEstimate()));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//
        alertDialogBuilder.setView(input);

        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
                //requestConfirmAlertDialog();
            }
        });
        alertDialogBuilder.setNegativeButton("Confirm",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //TODO Shorten input text if too long
                //Context context = this;

                try {
                    double x = Double.valueOf(input.getText().toString());
                    sendRequest.setEstimate(x);
                } catch (NumberFormatException e) {
                    //this throws on empty inputs but works as intended
                    e.printStackTrace();
                }
                requestConfirmAlertDialog();

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void requestConfirmAlertDialog() {
        // dialog show detail of habit when selected
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Request");
        alertDialogBuilder.setMessage(
                "\nFrom: \n"+ LocationAddressConverter.getLocationAddress(this, sendRequest.getFrom())+
                        "\n\nTo: \n"+ LocationAddressConverter.getLocationAddress(this, sendRequest.getTo()) +
                        "\n\nDescription: "+sendRequest.getDescription() +
                        "\nFee offer: " + "$" + new DecimalFormat("#0.00").format(sendRequest.getEstimate()));
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });
        alertDialogBuilder.setNegativeButton("Send",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //elastic search update request
                CommandManager commandManager = CommandManager.getInstance();
                Command command = new AddRequestCommand(sendRequest);
                commandManager.invokeCommand(MapsActivity.this,command);

                ConnectivityManager connectivityManager
                        = (ConnectivityManager) MapsActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
                    Toast.makeText(MapsActivity.this,"Request Sent!",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MapsActivity.this,"Request Stored!",Toast.LENGTH_LONG).show();
                }


            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Get rough distance in meters between two geopoints using Haversine formula
     * Source: http://stackoverflow.com/questions/15736995/how-can-i-quickly-estimate-the-distance-between-two-latitude-longitude-points
     * Date Accessed: 11/27/2016
     * Author: Aaron D
     * @return double
     */
    private double getRoughDistance(LatLng latlng0, LatLng latlng1){
        double newDistance;
        double earthRadius = 6371;
        double lat0 = Math.toRadians(latlng0.latitude);
        double lat1 = Math.toRadians(latlng1.latitude);
        double lon0 = Math.toRadians(latlng0.longitude);
        double lon1 = Math.toRadians(latlng1.longitude);
        //Haversine Formula
        double dlon = lon1 - lon0;
        double dlat = lat1 - lat0;
        double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat0) * Math.cos(lat1) * Math.pow(Math.sin(dlon/2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        newDistance = (earthRadius * c)*1000;
        return newDistance;
    }
}

