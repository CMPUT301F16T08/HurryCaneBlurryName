package hurrycaneblurryname.ryde.View;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.Model.User;
import hurrycaneblurryname.ryde.R;

import static android.R.attr.checked;

/**
 * The type Search Requests activity.
 * Author: Chen
 * Storyboard by: Blaz
 */

public class SearchRequestsActivity extends AppCompatActivity {
    private Button searchButton;
    private Button searchNearbyButton;

    private RadioGroup searchGroup;
    private RadioButton locationRadio;
    private RadioButton geoRadio;
    private RadioButton keywordRadio;
    private EditText searchEditText;

    private ListView searchView;
    private ArrayAdapter<Request> searchViewAdapter;

    private ArrayList<Request> searchResult;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_requests);
        setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchResult = new ArrayList<>();

        searchEditText = (EditText) findViewById(R.id.SearchEditText);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchNearbyButton = (Button)findViewById(R.id.searchNearbyButton);

        searchGroup = (RadioGroup)findViewById(R.id.searchGroup);

        searchView = (ListView) findViewById(R.id.SearchResultListView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mLastLocation = extras.getParcelable("currLocation");
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {

                  int selectedId = searchGroup.getCheckedRadioButtonId();

                  searchResult.clear();
                  String[] searchText =  searchEditText.getText().toString().split(",");

                  switch(selectedId) {
                      case (R.id.radio_location):
//                          Toast.makeText(SearchRequestsActivity.this, "Search by location", Toast.LENGTH_SHORT).show();
                          searchByLocation(searchText);
                          break;
                      case R.id.radio_keyword:
//                          Toast.makeText(SearchRequestsActivity.this, "Search by keyword", Toast.LENGTH_SHORT).show();
                          searchByKeyword(searchText);
                          break;
                      case R.id.radio_geo:
//                          Toast.makeText(SearchRequestsActivity.this, "Search by geo", Toast.LENGTH_SHORT).show();
                          searchByGeo(searchText);
                          break;

                  }
              }
         });

        searchNearbyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchResult.clear();
                if (mLastLocation != null) {
                    String lon = String.valueOf(mLastLocation.getLongitude());
                    String lat = String.valueOf(mLastLocation.getLatitude());
                    searchRequests(lat, lon);
                } else {
                    Toast.makeText(SearchRequestsActivity.this, "Current location not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // save selected request
                Request requestSelected = searchResult.get(position);
                RequestHolder.getInstance().setRequest(requestSelected);
                Intent info = new Intent(SearchRequestsActivity.this, RideInfoFromSearch.class);
                startActivity(info);
            }
        });
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
    /**
     * Execute search for open requests
     * @param searchParam
     */
    private void searchRequests(String... searchParam) {

        String[] searchText =  searchEditText.getText().toString().split(",");
    }

    /**
     * Wrapper that helps to find requests based on searchParam.
     *
     * @param searchParam can contain [lon, lat] or can contain description
     */
    private void searchByGeo(String... searchParam) {
        ElasticSearchRequestController.GetOpenRequestsGeoTask getRequestsTask = new ElasticSearchRequestController.GetOpenRequestsGeoTask();
        getRequestsTask.execute(searchParam);
        ArrayList<Request> newResults;
        try {
            newResults = getRequestsTask.get();

            if (newResults.isEmpty()) {
                Toast.makeText(SearchRequestsActivity.this, "No results!", Toast.LENGTH_SHORT).show();
            }
            searchResult = newResults;
            searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, searchResult);
            searchView.setAdapter(searchViewAdapter);


        } catch (Exception e) {

            Toast.makeText(SearchRequestsActivity.this, "Could not communicate with server", Toast.LENGTH_SHORT).show();
            Log.i("ErrorGetUser", "Something went wrong when looking for requests");
            //e.printStackTrace();
        }
    }

    private void searchByKeyword(String... searchParam) {
        ElasticSearchRequestController.GetOpenRequestsDescTask getRequestsTask = new ElasticSearchRequestController.GetOpenRequestsDescTask();
        getRequestsTask.execute(searchParam);
        ArrayList<Request> newResults;
        try {
            newResults = getRequestsTask.get();

            if (newResults.isEmpty()) {
                Toast.makeText(SearchRequestsActivity.this, "No results!", Toast.LENGTH_SHORT).show();
            }
            searchResult = newResults;
            searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, searchResult);
            searchView.setAdapter(searchViewAdapter);


        } catch (Exception e) {
            Toast.makeText(SearchRequestsActivity.this, "Could not communicate with server", Toast.LENGTH_SHORT).show();
            Log.i("ErrorGetUser", "Something went wrong when looking for requests");
            //e.printStackTrace();
        }
    }

    /**
     * Search google maps location services (addresses, landmarks, etc.)
     * @param searchParam search string
     */
    private void searchByLocation(String... searchParam) {
        String location = searchEditText.getText().toString();
        List<Address> addressList = null;
        String[] search = new String[2];

        if(location == null || location.isEmpty()){
            Toast.makeText(SearchRequestsActivity.this, "No results!", Toast.LENGTH_SHORT).show();
        } else {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.isEmpty()) {
                return;
            }

            //Place Marker
            Address address = addressList.get(0);
            LatLng latlng = new LatLng(address.getLatitude() , address.getLongitude());

            search[0] = String.valueOf(address.getLongitude());
            search[1] = String.valueOf(address.getLatitude());

            searchByGeo(search);
        }


    }


}
