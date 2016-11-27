package hurrycaneblurryname.ryde.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.edmodo.rangebar.RangeBar;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Predicate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hurrycaneblurryname.ryde.ElasticSearchRequestController;
import hurrycaneblurryname.ryde.Model.Request.Request;
import hurrycaneblurryname.ryde.Model.Request.RequestHolder;
import hurrycaneblurryname.ryde.R;

/**
 * Search for Open requests
 * Author: Chen, Cho
 * Storyboard by: Blaz
 * Version: 2
 */

public class SearchRequestsActivity extends AppCompatActivity {
    private Button searchButton;
    private Button searchNearbyButton;

    private RadioGroup searchGroup;
    private EditText searchEditText;
    private EditText searchEditText2;

    private ListView searchView;
    private ArrayAdapter<Request> searchViewAdapter;

    private ArrayList<Request> searchResult;
    private Location mLastLocation;

    private ToggleButton distanceToggle;
    private ToggleButton priceToggle;
    private TextView minValueText;
    private TextView maxValueText;
    private boolean priceFilterApplied;
    private boolean distanceFilterApplied;

    private int min = 0;
    private int max = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_requests);
        setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        searchBlock = (RelativeLayout) findViewById(R.id.searchRequestBlock);
        searchResult = new ArrayList<>();

        searchEditText = (EditText) findViewById(R.id.SearchEditText);
        searchEditText2 = (EditText) findViewById(R.id.SearchEditText2);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchNearbyButton = (Button)findViewById(R.id.searchNearbyButton);

        searchGroup = (RadioGroup)findViewById(R.id.searchRadioGroup);
        searchView = (ListView) findViewById(R.id.SearchResultListView);

        distanceToggle = (ToggleButton) findViewById(R.id.toggleDistanceFilter);
        priceToggle = (ToggleButton) findViewById(R.id.togglePriceFilter);
        priceFilterApplied = false;


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mLastLocation = extras.getParcelable("currLocation");
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {

                  int selectedId = searchGroup.getCheckedRadioButtonId();

                  searchResult.clear();
                  distanceToggle.setChecked(false);
                  priceToggle.setChecked(false);
                  priceFilterApplied = distanceFilterApplied = false;

                  String[] searchText = new String[2];

                  switch(selectedId) {
                      case (R.id.radio_location):
                          searchText[0] = searchEditText.getText().toString();
                          searchByLocation(searchText);
                          break;
                      case R.id.radio_keyword:

                          searchText[0] = searchEditText.getText().toString();
                          searchByKeyword(searchText);
                          break;
                      case R.id.radio_geo:
                          searchText[0] = searchEditText.getText().toString();
                          searchText[1] = searchEditText2.getText().toString();
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
                    searchByGeo(lat, lon);
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

        distanceToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TOGGLE", "CLICKED");
                if(priceFilterApplied) {
                    priceFilterApplied=false;
                    priceToggle.setChecked(false);
                }

                Log.i("FilterTOGGLE", String.valueOf(priceToggle.isChecked()));
                Log.i("FilterBOOL", String.valueOf(priceFilterApplied));


                if (!distanceFilterApplied && distanceToggle.isChecked()) {

                    filterPricePerKMParamsDialog();
                    distanceToggle.setChecked(true);
                    distanceToggle.setSelected(true);
                    ArrayList<Request> filtered = new ArrayList<Request>();

                    // Set predicate for filtering
                    // http://guidogarcia.net/blog/2011/10/29/java-different-ways-filter-collection/
                    // Accessed: November 26
                    // Post by Guido Garcia

                    Predicate<Request> pred = new Predicate<Request>() {
                        public boolean apply(Request r) {
                            // do the filtering
                            return (min*1000 < r.getDistance() && r.getDistance() < max*1000) ;
//                            return true; //TODO implement distance filtering
                        }
                    };

                    Comparator<Request> comp = new Comparator<Request>() {
                        @Override
                        public int compare(Request r1, Request r2) {
                            Double ppk1 = r1.getEstimate()/r1.getDistance();
                            Double ppk2 = r2.getEstimate()/r2.getDistance();

                            return (ppk1).compareTo(ppk2);

                        }
                    };

                    filtered =  filterAndSortList(pred, comp);

                    searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, filtered);
                    searchView.setAdapter(searchViewAdapter);

                } else if (distanceFilterApplied && !distanceToggle.isChecked()) {
                    distanceToggle.setChecked(false);
                    distanceFilterApplied = false;

                    // The toggle is disabled
                    // Set the view back to al the results
                    searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, searchResult);
                    searchView.setAdapter(searchViewAdapter);
                } else {
                    distanceFilterApplied = false;
                    distanceToggle.setChecked(false);
                }
            }
        });

        priceToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(distanceFilterApplied) {
                    distanceFilterApplied=false;
                    distanceToggle.setChecked(false);
                }

                if (!priceFilterApplied && priceToggle.isChecked()) {

                    filterPriceParamsDialog();
                    priceToggle.setChecked(true);
                    priceToggle.setSelected(true);
                    ArrayList<Request> filtered = new ArrayList<Request>(searchResult);

                    // Set predicate for filtering
                    // http://guidogarcia.net/blog/2011/10/29/java-different-ways-filter-collection/
                    // Accessed: November 26
                    // Post by Guido Garcia

                    Predicate<Request> pred = new Predicate<Request>() {
                        public boolean apply(Request r) {
                            // do the filtering
                            return (min<r.getEstimate() && r.getEstimate()<max) ;
                        }
                    };

                    Comparator<Request> comp = new Comparator<Request>() {
                        @Override
                        public int compare(Request r1, Request r2) {
                            return r1.getEstimate().compareTo(r2.getEstimate());
                        }
                    };

                   filtered =  filterAndSortList(pred, comp);

                    searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, filtered);
                    searchView.setAdapter(searchViewAdapter);

                } else if (priceFilterApplied && !priceToggle.isChecked()) {
                    priceToggle.setChecked(false);
                    priceFilterApplied = false;

                    // The toggle is disabled
                    // Set the view back to al the results
                    searchViewAdapter = new ArrayAdapter<Request>(SearchRequestsActivity.this, R.layout.list_item, searchResult);
                    searchView.setAdapter(searchViewAdapter);
                } else {
                    priceFilterApplied = false;
                    priceToggle.setChecked(false);
                }

            }
        });
    }


    /**
     * Change of search mode when a radio button is clicked
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Set layout programmatically. Move the layout to be attached relatively to another
        // http://stackoverflow.com/questions/3277196/can-i-set-androidlayout-below-at-runtime-programmatically
        // Accessed November 24, 2016
        // Author: Qberticus

        searchEditText.getText().clear();
        searchResult.clear();
        distanceToggle.setChecked(false);
        priceToggle.setChecked(false);
        priceFilterApplied = distanceFilterApplied = false;

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


        // Check which radio button was clicked
        // Readjust layout based on search mode
        switch(view.getId()) {
            case R.id.radio_location:
                if (checked) {
                    p.addRule(RelativeLayout.BELOW, R.id.SearchEditText);
                    findViewById(R.id.searchRadioGroup).setLayoutParams(p);

                    searchEditText2.setVisibility(View.INVISIBLE);
                    searchEditText.setHint(R.string.searchLocation);
                }
                break;
            case R.id.radio_keyword:
                if (checked) {
                    p.addRule(RelativeLayout.BELOW, R.id.SearchEditText);
                    findViewById(R.id.searchRadioGroup).setLayoutParams(p);

                    searchEditText2.setVisibility(View.INVISIBLE);
                    searchEditText.setHint(R.string.searchKeyword);
                }
                break;
            case R.id.radio_geo:
                if (checked) {
                    p.addRule(RelativeLayout.BELOW, R.id.SearchEditText2);
                    findViewById(R.id.searchRadioGroup).setLayoutParams(p);

                    searchEditText2.setVisibility(View.VISIBLE);
                    searchEditText.setHint(R.string.latitude);
                    searchEditText2.setHint(R.string.longitude);
                }
                break;

        }
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
     * Filter and sort based on predicate and comparator
     * @param predicate filter criteria
     * @param comparator sort criteria
     * @return filtered and sorted list
     */

    private ArrayList<Request> filterAndSortList (Predicate<Request> predicate, Comparator<Request> comparator) {
        // Filter the search results
        ArrayList<Request> filterList = new ArrayList<>();
        for (Request r : searchResult) {
            if (predicate.apply(r)) {
                filterList.add(r);
            }
        }
        Collections.sort(filterList, comparator);
        return filterList;
    }

    /**
     * Creates a dialog for getting filter price parameters for user
     * Filter is only applied when user clicks "filter"
     */
    private void filterPriceParamsDialog() {
        final Dialog filterDialog = new Dialog(SearchRequestsActivity.this);
        LayoutInflater inflater = (LayoutInflater)SearchRequestsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.filter_dialog, (ViewGroup)findViewById(R.id.filter_dialog_root_element));
        filterDialog.setTitle("Set price filter:");
        filterDialog.setContentView(layout);


        min = 0; max = 1000;
        final Resources res = getResources();
        String minText = String.format(res.getString(R.string.priceFormat), min);
        String maxText = String.format(res.getString(R.string.priceFormat), max);

        minValueText = (TextView) filterDialog.findViewById(R.id.minFilterText);
        minValueText.setText(minText);
        maxValueText = (TextView) filterDialog.findViewById(R.id.maxFilterText);
        maxValueText.setText(maxText);

        Button dialogButton = (Button)layout.findViewById(R.id.filter_dialog_button);
        RangeBar dialogSeekBar = (RangeBar)layout.findViewById(R.id.filter_dialog_rangebar);

        filterDialog.show();

        filterDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                priceFilterApplied=false;
                priceToggle.setChecked(false);
                filterDialog.dismiss();
            }
        });

        dialogSeekBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                String minText = String.format(res.getString(R.string.priceFormat), leftThumbIndex+1);
                String maxText = String.format(res.getString(R.string.priceFormat), rightThumbIndex+1);
                minValueText.setText(minText);
                maxValueText.setText(maxText);
                min = leftThumbIndex;
                max = rightThumbIndex;

            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceFilterApplied = true;
                filterDialog.dismiss();
            }
        });
    }


    /**
     * Creates a dialog for getting filter price parameters for user.
     * Filter is not applied until user clicks "filter"
     */
    private void filterPricePerKMParamsDialog() {
        final Dialog filterDialog = new Dialog(SearchRequestsActivity.this);
        LayoutInflater inflater = (LayoutInflater)SearchRequestsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.filter_dialog, (ViewGroup)findViewById(R.id.filter_dialog_root_element));
        filterDialog.setTitle("Set price/km filter:");
        filterDialog.setContentView(layout);

        min = 0; max = 100;
        final Resources res = getResources();
        String minText = String.format(res.getString(R.string.distanceFormat), min);
        String maxText = String.format(res.getString(R.string.distanceFormat), max);
        minValueText = (TextView) filterDialog.findViewById(R.id.minFilterText);
        minValueText.setText(minText);
        maxValueText = (TextView) filterDialog.findViewById(R.id.maxFilterText);
        maxValueText.setText(maxText);

        Button dialogButton = (Button)layout.findViewById(R.id.filter_dialog_button);
        RangeBar dialogSeekBar = (RangeBar)layout.findViewById(R.id.filter_dialog_rangebar);
        dialogSeekBar.setTickCount(100);

        filterDialog.show();

        filterDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                distanceFilterApplied=false;
                distanceToggle.setChecked(false);
                filterDialog.dismiss();
            }
        });

        dialogSeekBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                Log.i("LeftIndex", Integer.toString(leftThumbIndex));
                String minText = String.format(res.getString(R.string.priceFormat), leftThumbIndex+1);
                String maxText = String.format(res.getString(R.string.priceFormat), rightThumbIndex+1);
                minValueText.setText(minText);
                maxValueText.setText(maxText);
                min = leftThumbIndex;
                max = rightThumbIndex;
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceFilterApplied = true;
                filterDialog.dismiss();
            }
        });
    }

    /**
     * Wrapper that helps to find requests based on searchParam geolocation
     *
     * @param searchParam string array can contain [lat, lon]
     */
    private void searchByGeo(String... searchParam) {
        Log.i("SEARCH", Arrays.toString(searchParam));
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

    /**
     * Wrapper for searching through request descriptions with searchParam[0]
     * @param searchParam
     */
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
     * Wrapper for searching location using geocoding (addresses, landmarks, etc.)
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


            search[1] = String.valueOf(address.getLongitude());
            search[0] = String.valueOf(address.getLatitude());
            searchByGeo(search);
        }


    }


}
