package hurrycaneblurryname.ryde.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hurrycaneblurryname.ryde.DataParser;
import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.LocationAddressConverter;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.Request.RequestUserHolder;
import hurrycaneblurryname.ryde.NetworkUtil;
import hurrycaneblurryname.ryde.R;

import static hurrycaneblurryname.ryde.R.string.from;

/**
 * The type Rider Ride Info activity.
 * Author: Chen
 */

public class RideInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView descTextView;
    private TextView riderTextView;
    private TextView driverTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private TextView statusTextView;
    private TextView feeTextView;
    private TextView driverClickTextView;
    private TextView driversClickTextView;

    private Button completeButton;      //TODO
    private Button cancelButton;

    private Request request;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_info);
        setTitle("Ride Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set up connection to TextView
        descTextView = (TextView)findViewById(R.id.descTexts);
        riderTextView = (TextView)findViewById(R.id.riderUsernameText);
        driverTextView = (TextView)findViewById(R.id.driverUsernameText);
        fromTextView = (TextView)findViewById(R.id.fromTexts);
        toTextView = (TextView)findViewById(R.id.toTexts);
        statusTextView = (TextView)findViewById(R.id.statusTexts);
        feeTextView = (TextView)findViewById(R.id.estTexts);

        driverClickTextView = (TextView)findViewById(R.id.driverClickText);
        driversClickTextView = (TextView)findViewById(R.id.driversClickText);

        completeButton = (Button)findViewById(R.id.completeButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        request = RequestHolder.getInstance().getRequest();
        driverClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (request.getDriver().getUsername().equals(""))
                {
                    Toast.makeText(RideInfoActivity.this, "No driver has accepted this request!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestUserHolder.getInstance().setUser(request.getDriver());
                Intent intent = new Intent(RideInfoActivity.this, ProfileInfoActivity.class);
                String showVehicle = "y";
                intent.putExtra("SHOW_VEHICLE", showVehicle);
                startActivity(intent);
            }
        });

        driversClickTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RideInfoActivity.this, AvailableDriversActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelAlertDialog();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                completeAlertDialog();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    protected void onStart() {
        super.onStart();
        // set up TextView Contents according to Clicked request
        request = RequestHolder.getInstance().getRequest();
        setTextViewContent(request);
    }

    // Back Navigation Handle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTextViewContent(Request request){

        final Resources res = getResources();

        descTextView.setText(request.getDescription());
        riderTextView.setText(request.getRider().getUsername());
        if (!request.getDriver().getUsername().equals(""))
        {
            driverTextView.setText(request.getDriver().getUsername());
            driversClickTextView.setVisibility(View.GONE);
        }
        else{
            driverClickTextView.setVisibility(View.GONE);
        }

        // Display address of locations if possible
        fromTextView.setText(LocationAddressConverter.getLocationAddress(this, request.getFrom()));
        toTextView.setText(LocationAddressConverter.getLocationAddress(this, request.getTo()));


        statusTextView.setText(request.getStatus());
        feeTextView.setText(new DecimalFormat("$#0.00").format(request.getEstimate()));

        // display buttons depending on status
        if(request.getStatus().equals("open"))
        {
            completeButton.setVisibility(View.GONE);
        }

        if (request.getStatus().equals("closed")) {
            completeButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            TextView feeText = (TextView)findViewById(R.id.estTextView);
            feeText.setText("Fee:");
        }


    }

    private void cancelAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel a request");
        alertDialogBuilder.setMessage("Are you sure you want to cancel this request?");
        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });

        alertDialogBuilder.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // delete from server
                ElasticSearchRequestController.DeleteRequestsTask deleteRequestTask = new ElasticSearchRequestController.DeleteRequestsTask();
                deleteRequestTask.execute(request);
                Toast.makeText(RideInfoActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void completeAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Complete a request");
        alertDialogBuilder.setMessage("Fee: " + request.getEstimate().toString()+"\nMark request as Complete?");
        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });

        alertDialogBuilder.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (RequestHolder.getInstance().getRequest().getDriverComplete()) {
                    // Update request using elasticsearch query
                    RequestHolder.getInstance().getRequest().setRiderComplete();
                    RequestHolder.getInstance().getRequest().setStatus("closed");
                    ElasticSearchRequestController.UpdateRequestsTask updateRequestsTask = new ElasticSearchRequestController.UpdateRequestsTask();
                    updateRequestsTask.execute(RequestHolder.getInstance().getRequest());
                    Toast.makeText(RideInfoActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RideInfoActivity.this, "Please wait for Driver to complete!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

        mMap =googleMap;

        //Remove default toolbar
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //Get the lat lng
        double [] requestFrom = request.getFrom();
        double [] requestTo = request.getTo();
        final LatLng from = new LatLng(requestFrom[1], requestFrom[0]);
        final LatLng to = new LatLng(requestTo[1], requestTo[0]);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the start marker
        options.position(from);

        //For the start location, the color of marker is GREEN
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        options.title("Start Location");
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        // Setting the position of the end marker
        options.position(to);

        //For the end location, the color of marker is RED
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        options.title("End Location");
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                //move map camera to show both points
                //Source : http://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers
                //Date Accessed : 11/24/2016
                //Author: andr
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(from);
                builder.include(to);
                LatLngBounds bounds = builder.build();
                int padding = 130; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);
                // Remove listener to prevent position reset on camera move.
                mMap.setOnCameraChangeListener(null);
            }
        });

        if(NetworkUtil.getConnectivityStatusString(RideInfoActivity.this) != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            // Getting URL to the Google Directions API
            String url = getUrl(to, from);
            FetchUrl FetchUrl = new FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        }

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
                    mMap.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }


}
